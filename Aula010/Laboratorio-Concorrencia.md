# Aula 010 · Laboratório de Concorrência

Sebesta, Cap. 13 (p. 541-594). As saídas de concorrência mudam a cada execução.

| Prática | Linguagem | O que mostra | Quem impede a corrida |
|---------|-----------|--------------|-----------------------|
| 1 | Java | Condição de corrida em `contador++` | Ninguém: há corrida |
| 2 | Python | As 20 ordens da Figura 13.1 dão 4 resultados | — |
| 3 | Java | `synchronized` e `AtomicInteger` corrigem, com custo | O programador, com sincronização |
| 4 | Java | Impasse (deadlock) e a correção pela ordem dos recursos | — |
| 5 | Go | Canais: passagem de mensagens | O projeto: não há memória compartilhada |
| 6 | Rust | O compilador recusa a corrida | O compilador |
| 7 | JavaScript | Laço de eventos com uma thread só | Só existe uma thread |

---

## 0 · Preparação

**0.1 Cooperação × competição**

- **Cooperação:** uma tarefa precisa **esperar** que outra termine algo antes de continuar. Exemplo: o garçom só leva o prato depois que o cozinheiro termina de preparar.
- **Competição:** duas tarefas querem o **mesmo recurso**, que não pode ser usado pelas duas ao mesmo tempo. Exemplo: duas pessoas querendo usar a única impressora do escritório; uma tem que esperar a outra.

Na cooperação a ordem importa (A depois de B); na competição só importa que não sejam as duas ao mesmo tempo.

---

## 1 · A condição de corrida (Java)

**Previsão:** 2.000.000, já que cada thread soma 1 um milhão de vezes.

| Execução | n = 1.000.000 | n = 1000 |
|----------|---------------|----------|
| 1ª | 1.627.829 | 2.000 |
| 2ª | 1.037.958 | 2.000 |
| 3ª | 1.101.176 | — |

```
esperado: 2000000
obtido:   1627829
esperado: 2000000
obtido:   1037958
esperado: 2000000
obtido:   1101176
```

**1.1** Não bateram, e cada execução deu um valor diferente. As duas threads mexem na mesma variável sem nenhuma proteção, e somas se perdem. Quantas se perdem depende de como o **escalonador** intercalou as threads naquela execução, e isso muda toda vez.

**1.2 Como uma atualização se perde** (`contador` vale 10):

1. Thread A **lê** 10 para o seu registrador.
2. Thread B **lê** 10 também (A ainda não gravou).
3. A **soma**: 11. B **soma**: 11.
4. A **grava** 11. B **grava** 11.

Foram duas somas, mas o contador foi de 10 para 11. A soma de A foi sobrescrita pela de B.

**1.3** Com `n = 1000` deu 2000 nas duas execuções. A thread termina o laço tão rápido que quase nunca as duas estão no meio de um `contador++` ao mesmo tempo. Isso é o que torna o erro **perigoso**: nos testes, com pouca carga, tudo passa; em produção, com muitos acessos simultâneos, os valores começam a sair errados de vez em quando, e o erro é difícil de reproduzir.

---

## 2 · Todas as respostas possíveis (Figura 13.1)

Ordens testadas no simulador:

```
AAABBB → 8
BBBAAA → 7
ABABAB → 6
AABBBA → 4
```

Saída do programa em Python:

```
AAABBB -> 8
BBBAAA -> 7
ABABAB -> 6
Ordens possíveis: 20
TOTAL = 4:  9 ordens, por exemplo AABBBA
TOTAL = 6:  9 ordens, por exemplo AABABB
TOTAL = 7:  1 ordens, por exemplo BBBAAA
TOTAL = 8:  1 ordens, por exemplo AAABBB
```

**2.1** São **20** ordens possíveis (C(6,3): escolher onde ficam os 3 passos de A entre os 6) e **4** resultados diferentes: 4, 6, 7 e 8. Só **1** ordem dá o resultado "correto" 8: AAABBB. Ou seja, 19 de 20 ordens dão um resultado diferente do esperado.

**2.2 Ordem AABBBA → 4**

| Passo | Quem | Ação | Registrador | TOTAL |
|-------|------|------|-------------|-------|
| 1 | A | lê TOTAL | A = 3 | 3 |
| 2 | A | calcula +1 | A = 4 | 3 |
| 3 | B | lê TOTAL | B = 3 | 3 |
| 4 | B | calcula ×2 | B = 6 | 3 |
| 5 | B | grava | — | 6 |
| 6 | A | grava | — | **4** |

A leu TOTAL antes de B gravar, então trabalhou com o valor velho (3). Quando A grava por último, apaga o 6 de B. A operação de B **se perdeu**, como se nunca tivesse executado.

---

## 3 · Corrigindo a corrida (Java)

**Previsão:** a versão sem sincronização é a mais rápida e a única incorreta.

```
esperado:          2000000
sem sincronizacao: 1191125 em 4 ms
synchronized:      2000000 em 125 ms
AtomicInteger:     2000000 em 23 ms

esperado:          2000000
sem sincronizacao: 1927127 em 6 ms
synchronized:      2000000 em 130 ms
AtomicInteger:     2000000 em 23 ms
```

**3.1** `synchronized` e `AtomicInteger` deram 2.000.000; a versão sem cuidado errou. As corretas são mais lentas porque garantir exclusão mútua tem custo: com `synchronized`, cada incremento adquire e libera o monitor, e as threads ficam esperando uma pela outra (na prática a soma vira sequencial). O `AtomicInteger` usa uma instrução atômica do processador (compare-and-swap), mais barata que um monitor, mas ainda força as threads a disputar a mesma posição de memória. Aqui ficou ~5x mais rápido que `synchronized` e ~5x mais lento que a versão errada.

**3.2** Não resolve. Cada saldo seria atômico **sozinho**, mas a transferência são **duas** operações (tirar de A e pôr em B). Entre uma e outra, outra thread pode ler os saldos e ver o dinheiro "sumido", ou duas transferências podem passar pela verificação de saldo ao mesmo tempo. É preciso que a operação inteira seja a seção crítica: um `synchronized`/trava cobrindo as duas contas (pegando as travas sempre na mesma ordem, ver Prática 4), ou uma **transação** no banco de dados.

---

## 4 · Impasse (deadlock)

**Previsão:** nenhuma das duas mensagens aparece.

Saída original:

```
IMPASSE: 2 threads presas
```

Saída com B pegando primeiro `X` e depois `Y`:

```
A terminou
B terminou
sem impasse
```

**4.1** A pegou **X** e dormiu 100 ms; nesse tempo B pegou **Y**. Depois A tenta pegar Y (que está com B) e B tenta pegar X (que está com A). Cada uma segura o que a outra precisa e espera a outra soltar, o que nunca acontece: espera circular.

**4.2** Com a mesma ordem, quem pegar X primeiro também vai conseguir Y, porque a outra thread fica parada esperando X e não chegou a pegar Y. Não existe mais o ciclo "eu tenho o que você quer e você tem o que eu quero". Exemplo real: duas transferências bancárias simultâneas, uma de A para B e outra de B para A, cada uma travando primeiro a conta de origem. A solução é travar sempre na ordem do número da conta.

---

## 5 · Mensagens em vez de memória compartilhada (Go)

**Previsão:** 3 trabalhadores ≈ 200 ms; 1 ≈ 600 ms; 6 ≈ 100 ms.

| Trabalhadores | Tempo total |
|---------------|-------------|
| 1 | 600 ms |
| 3 | 200 ms |
| 6 | 100 ms |

```
trabalhador 3 fez a tarefa 3
trabalhador 1 fez a tarefa 1
trabalhador 2 fez a tarefa 2
trabalhador 2 fez a tarefa 5
trabalhador 3 fez a tarefa 4
trabalhador 1 fez a tarefa 6
3 trabalhadores, 6 tarefas de 100 ms: levou 200ms
```

**5.1** São 6 tarefas de 100 ms. Com 1 trabalhador elas rodam uma depois da outra: 6 × 100 = 600 ms. Com 3, cada trabalhador faz 2 tarefas em paralelo com os outros: 2 × 100 = 200 ms. Com 6, cada um faz uma: 100 ms. Não precisa de trava porque as goroutines **não compartilham variável**: cada tarefa é entregue a um trabalhador só, pelo canal, e o próprio canal já é sincronizado (só um trabalhador recebe cada mensagem).

Canal sem buffer:

```
fatal error: all goroutines are asleep - deadlock!

goroutine 1 [chan send]:
main.main()
```

Com `make(chan int, 1)`:

```
1
```

**5.2** Num canal sem espaço, `ch <- 1` só termina quando alguém fizer `<-ch`. Mas quem iria receber é a própria `main`, na linha seguinte, que nunca é alcançada: a única goroutine fica parada para sempre, e o Go detecta e aborta (é um impasse com uma tarefa só). Com espaço para 1 mensagem, o envio guarda o valor no buffer e continua; a linha seguinte o retira.

---

## 6 · O compilador como guarda (Rust)

**Previsão:** não compila.

Erro da versão sem `move`:

```
error[E0373]: closure may outlive the current function, but it borrows `contador`, which is owned by the current function
 --> src/main.rs:7:27
  |
7 |     let t = thread::spawn(|| {
  |                           ^^ may outlive borrowed value `contador`
8 |         contador += 1;
  |         -------- `contador` is borrowed here
help: to force the closure to take ownership of `contador` (and any other referenced variables), use the `move` keyword
```

Com `move` (compila, com aviso de valor nunca lido):

```
0
```

Versão com `Arc` e `Mutex`:

```
contador = 400000
```

**6.1** `contador` é um `i32`, que é `Copy`. Com `move`, a closure leva uma **cópia** do número para dentro da thread; a thread soma 1 na cópia dela, e o `contador` de `main` continua 0. São duas variáveis diferentes, então não há corrida, mas também não há compartilhamento.

**6.2** Em Java o erro só apareceu **na execução**, com resultados errados e diferentes a cada vez (e com n = 1000 nem apareceu). Em Rust ele foi pego **na compilação**: o programa com corrida simplesmente não é aceito. A vantagem é que uma classe inteira de bugs não chega à produção. O custo é para quem programa: precisa entender posse, empréstimo, `Arc`, `Mutex` e brigar com o compilador até o código ser aceito.

---

## 7 · Uma thread só: o laço de eventos (JavaScript)

**Previsão:** A, D, C, B.

```
A
D
C
B
E: timer de 10 ms rodou depois de 300 ms
```

**7.1** A ordem foi **A, D, C, B**. A e D são código síncrono e rodam na hora. `then` vai para a fila de **microtarefas** e `setTimeout` vai para a fila de **macrotarefas** (timers). Quando o código atual termina, o laço de eventos esvazia primeiro as microtarefas (C) e só depois pega o próximo timer (B). Tempo 0 não quer dizer "agora", quer dizer "assim que a thread estiver livre".

**7.2** O timer de 10 ms ficou pronto aos 10 ms, mas a única thread estava presa em `ocupado(300)`, e nada interrompe código JavaScript em execução. Ele só rodou quando o laço terminou, aos 300 ms. Num servidor Node.js, um cálculo pesado de 300 ms trava **todos** os usuários durante esse tempo: nenhuma outra requisição é atendida. Cálculos pesados precisam ir para worker threads ou outro serviço.

---

## 8 · Fechamento

| Situação | Quem impede a corrida |
|----------|-----------------------|
| Java sem sincronização (P1) | Ninguém: há corrida |
| Java com `synchronized` (P3) | O programador, com sincronização |
| Go com canais (P5) | O projeto: não há memória compartilhada |
| Rust com `Arc` e `Mutex` (P6) | O compilador |
| JavaScript (P7) | Só existe uma thread |

**8.1** Condição de corrida, igual à da Prática 1 e da Figura 13.1: as duas requisições leem "1 lugar livre" antes de qualquer uma gravar "0 lugares", e as duas compram o mesmo assento. Para evitar, a verificação e a reserva precisam ser **uma operação atômica**: uma transação no banco com trava na linha do assento (`SELECT ... FOR UPDATE`) ou um `UPDATE assentos SET livre = false WHERE id = ? AND livre = true` conferindo se uma linha foi alterada. Quem não conseguir recebe "esgotado".

**8.2** Que um `contador++`, uma linha só, possa perder quase metade das somas, e que com 1000 repetições o mesmo programa pareça perfeito.

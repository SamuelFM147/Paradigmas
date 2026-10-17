# Aula 012 · Laboratório Funcional

Sebesta, Cap. 15 (p. 631-686).

| Prática | Linguagem | Tema |
|---------|-----------|------|
| 0 | Python | Funções puras × impuras |
| 1 | Python | Transparência referencial, composição, `map`, `reduce` |
| 2 | Scheme | `car`, `cdr`, `cons` e `QUOTE` |
| 3 | Scheme e Python | Recursão comum × recursão em cauda |
| 4 | Scheme | Três funções sobre listas |
| 5 | Haskell | Casamento de padrões, compreensões e avaliação preguiçosa |
| 6 | JavaScript e Java | Fechamentos, currying, lambdas e streams |
| 7 | Java, JS, Python | O laço vira pipeline |

---

## 0 · Puras ou impuras?

| Função | Classificação | Motivo |
|--------|---------------|--------|
| `def area(r): return 3.14159 * r * r` | **Pura** | Só depende de `r` |
| `def agora(): return datetime.now()` | **Impura** | Lê o relógio: cada chamada devolve outro valor |
| `def total(lista): return sum(lista)` | **Pura** | Só lê o parâmetro |
| `def registra(msg): print(msg)` | **Impura** | Efeito colateral: escreve na tela |
| `def sorteia(): return random.randint(1, 6)` | **Impura** | Resultado sorteado, não depende dos parâmetros |
| `def poe(lista, x): lista.append(x); return lista` | **Impura** | Modifica a lista recebida |

**0.1**

- `agora()` falha a **condição 1**: não tem parâmetro nenhum e mesmo assim devolve um valor diferente a cada chamada, porque depende do relógio, que está fora dela. Não dá para trocar `agora()` por um valor fixo.
- `poe(lista, x)` falha a **condição 2**: além de devolver a lista, ela **altera** a lista original de quem chamou. Chamar duas vezes `poe(l, 1)` não é o mesmo que chamar uma vez, e o `l` do chamador muda.

---

## 1 · Funções puras e formas funcionais (Python)

**Previsão:** linha 1 → `A-1 A-2`; linha 4 → 14; linha 7 → `[7, 3, 9] [3, 7, 9]`.

```
1: A-1 A-2
2: True
3: 8
4: 14
5: [4, 9, 16]
6: 29
7: [7, 3, 9] [3, 7, 9]
8: [3, 7, 9]
```

Com `compor(g, f)` na linha 4:

```
4: 18
```

**1.1** Porque `proximo_id` depende da variável global `contador`, que ela mesma altera a cada chamada. A mesma entrada `"A"` dá `A-1`, depois `A-2`: não há transparência referencial. Para testar, não basta saber o parâmetro: o resultado depende de quantas vezes a função já foi chamada antes, então cada teste depende da ordem dos outros e precisa zerar o estado global (p. 637).

**1.2**

- `compor(f, g)(4) = f(g(4)) = f(3 · 4) = f(12) = 12 + 2 = 14`
- `compor(g, f)(4) = g(f(4)) = g(4 + 2) = g(6) = 3 · 6 = 18`

A composição não é comutativa: a função da **direita** é aplicada primeiro. Trocar a ordem muda quem soma e quem multiplica.

**1.3** `sorted(notas)` **cria uma lista nova** ordenada e deixa `notas` intacta (linha 7). `notas.sort()` **altera a própria lista** e devolve `None` (linha 8). `sorted` segue o estilo funcional: é pura, não tem efeito colateral, o valor antigo continua existindo.

---

## 2 · Scheme: tudo é lista

**Previsão:** `(car '(A B C))` → `A`; `(cdr '(A B C))` → `(B C)`; `(cons '(A B) '(C D))` → `((A B) C D)`.

```
22
14
25
8
negativo
A
(B C)
(A B C)
((A B) C D)
B
```

Sem o apóstrofo, `(car (A B C))` dá erro: o Scheme reclama que `A` não está definido (variável não vinculada).

**2.1** O apóstrofo é o `QUOTE`: diz ao Scheme "**não avalie** isto, use como dado". Sem ele, `(A B C)` é tratado como uma chamada de função: o Scheme tenta avaliar `A` para descobrir qual função chamar com os parâmetros `B` e `C`, e como não existe nada chamado `A`, dá erro. É o preço de programa e dado terem a mesma forma: o `'` é o que separa um do outro.

**2.2** Terceiro elemento de `'(A B C)`:

```scheme
(mostra (car (cdr (cdr '(A B C)))))
```

```
C
```

`cdr` tira o `A` → `(B C)`; `cdr` de novo tira o `B` → `(C)`; `car` pega o `C`.

---

## 3 · Recursão e recursão em cauda

Passo a passo no simulador, com n = 4:

```
Recursão comum:            Recursão em cauda:
(fatorial 4)               (facthelper 4 1)
(* 4 (fatorial 3))         (facthelper 3 4)
(* 4 (* 3 (fatorial 2)))   (facthelper 2 12)
(* 4 (* 3 (* 2 (fatorial 1))))  (facthelper 1 24)
(* 4 (* 3 (* 2 1)))        24
24
```

Com n = 6 a recursão comum chega a 6 registros empilhados antes de começar a multiplicar; a em cauda continua com um só.

**Previsão:** o Scheme vai conseguir; o Python vai estourar a pilha.

Scheme:

```
#t
#f
2432902008176640000
2432902008176640000
1000000
```

Python (últimas linhas):

```
500
...
  File "p3b_recursao.py", line 6, in conta_ate
    return conta_ate(n - 1, acc + 1)     # a chamada é a última operação
  [Previous line repeated 996 more times]
RecursionError: maximum recursion depth exceeded
```

**3.1** Na recursão comum **cresce a pilha** (e a expressão pendente): cada chamada deixa uma multiplicação `(* n ...)` esperando a volta, então os registros de ativação se acumulam até chegar ao caso base. Na recursão em cauda nada fica pendente: o resultado parcial vai no parâmetro `parcial`, e a chamada recursiva é a última coisa feita, então o registro atual não é mais necessário.

**3.2** O Scheme **é obrigado** pela definição da linguagem a fazer a otimização da chamada em cauda (p. 656): ele reaproveita o registro de ativação, e um milhão de chamadas ocupam o espaço de uma, como um laço. O Python **não otimiza** chamadas em cauda e ainda limita a pilha a cerca de 1000 chamadas (`sys.getrecursionlimit()`). Com 500 funcionou; com um milhão passou do limite e deu `RecursionError: maximum recursion depth exceeded`, mesmo a função sendo recursiva em cauda.

---

## 4 · Mão na massa: três funções sobre listas

**4.1**

```scheme
; 1) quantos elementos a lista tem?
(define (tamanho lis)
  (if (null? lis)
      0
      (+ 1 (tamanho (cdr lis)))))

; 2) soma dos quadrados
(define (soma-quadrados lis)
  (if (null? lis)
      0
      (+ (* (car lis) (car lis)) (soma-quadrados (cdr lis)))))

; 3) só os números pares
(define (pares lis)
  (cond ((null? lis) '())
        ((even? (car lis)) (cons (car lis) (pares (cdr lis))))
        (else (pares (cdr lis)))))
```

Saída final:

```
tamanho: 3  ok
soma-quadrados: 14  ok
pares: (2 4 6)  ok
```

**4.2** A mais difícil foi `pares`, porque ela precisa **construir** uma lista nova e tem dois casos além da lista vazia (manter ou descartar o `car`). Nas três, `null?` é o caso base (lista vazia → `0`, `0` ou `'()`) e `cdr` é a chamada com o resto. O `car` muda de papel: em `tamanho` ele nem é usado (só conta 1); em `soma-quadrados` ele é elevado ao quadrado e somado; em `pares` ele é testado com `even?` e, se passar, vai para a frente do resultado com `cons`.

---

## 5 · Haskell: padrões, compreensões e preguiça

**Previsão:** `take 5 squares` → `[0,1,4,9,16]`; `primeiro 42 (div 1 0)` → `42`.

```
2432902008176640000
[1,2,4,7,14]
[2,2,4,7,8,9]
[0,1,4,9,16]
[0,1,4,9,16,25,36,49]
True
42
```

Com `print (member 17 squares)`: as 7 linhas acima aparecem e o programa fica rodando sem imprimir mais nada, até o OneCompiler encerrar por tempo.

**5.1** Por causa da **avaliação preguiçosa**: `squares` não é calculada quando é definida; cada elemento só é calculado quando alguém pede. `take 5` pede 5 elementos, então só 5 são gerados. Pelo mesmo motivo, `primeiro 42 (div 1 0)` não dá erro: `primeiro` ignora o segundo parâmetro (`_`), e no Haskell (não estrito) um parâmetro que não é usado nunca é avaliado. A divisão por zero nunca acontece. Em Java ou Scheme (estritos) o erro aconteceria antes da chamada.

**5.2** `member 16 squares` encontra 16 na posição 4 e o `||` para ali (`True || ...` não avalia o resto). Já 17 não é quadrado perfeito, então `member` continua procurando para sempre: a lista é infinita e a função não sabe que já passou do ponto. Correção: como `squares` é crescente, dá para desistir assim que achar um elemento maior que o procurado, como o `member2` do livro (p. 670):

```haskell
member2 n (m:x)
  | m < n     = member2 n x
  | m == n    = True
  | otherwise = False
```

Com isso, `member2 17 squares` para em 25 e devolve `False`.

---

## 6 · Você já usa funcional

**Previsão:** `mais10(5)` → 15; `[1, 2, 3].map(vezes(5))` → `[5, 10, 15]`.

JavaScript:

```
15
[ 5, 10, 15 ]
56
{ nome: 'Ana', nota: 7 } { nome: 'Ana', nota: 8 }
```

Java:

```
14
18
1050.0
[CABO, MONITOR, MOUSE, TECLADO]
```

**6.1** A função devolvida é um **fechamento** (closure): ela guarda uma ligação com o ambiente onde foi criada, em que `n = 10`. O registro de ativação de `somador` terminou, mas a variável `n` capturada continua existindo enquanto `mais10` existir, porque ainda há uma função que a referencia.

**6.2** Porque a ordem de aplicação é inversa. `f.compose(g)` aplica **primeiro `g`** e depois `f`: `f(g(4)) = 14`. `f.andThen(g)` aplica **primeiro `f`** e depois `g`: `g(f(4)) = 18`. O `compor(f, g)` da Prática 1 é `f(g(x))`, então corresponde a `f.compose(g)`.

---

## 7 · O laço vira pipeline

**7.1 Exercício 1 (Java)**

```java
import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> nums = List.of(3, 8, 5, 12, 7, 6);
        int soma = nums.stream()
            .filter(n -> n % 2 == 0)
            .mapToInt(n -> n * n)
            .sum();
        System.out.println(soma);
    }
}
```

**7.2 Exercício 3 (Python)**

```python
precos = [50.0, 120.0, 80.0, 200.0]
com_desconto = [p * 0.9 if p > 100 else p for p in precos]
print(com_desconto)
```

Também fiz o **Exercício 2 (JavaScript)**:

```javascript
const aprovados = alunos
  .filter((a) => a.nota >= 6)
  .map((a) => a.nome.toUpperCase());
console.log(aprovados.join(", "));
```

Saídas (iguais às versões com laço):

```
244
[50.0, 108.0, 80.0, 180.0]
ANA, CARLA, DAVI
```

**7.3** A funcional é mais fácil de **ler**: cada etapa diz *o que* faz (filtra pares, eleva ao quadrado, soma), sem variável acumuladora nem índice para acompanhar. A com laço é mais fácil de **depurar**: dá para pôr um `print` ou um breakpoint dentro do `if` e ver cada valor. No pipeline é preciso quebrar a cadeia ou usar algo como `.peek(System.out::println)` no Java.

---

## 8 · Fechamento

| Afirmação | Resposta |
|-----------|----------|
| Uma função pura pode ler uma variável global que muda. | **Falso** |
| Em Scheme, a recursão em cauda não faz a pilha crescer. | **Verdadeiro** |
| O Python otimiza a recursão em cauda. | **Falso** |
| Em Haskell, `[1 ..]` só é calculada à medida que é usada. | **Verdadeiro** |
| `filter`, `map` e `reduce` dispensam laço e variável acumuladora. | **Verdadeiro** |
| Currying transforma uma função de vários parâmetros numa cadeia de funções de um parâmetro. | **Verdadeiro** |

**8.1** A versão em Haskell (`sum (map (^3) [1 .. n])`) é mais fácil de entender: é a própria definição matemática "soma dos cubos de 1 a n", sem variável `sum` mudando nem índice. Em C é preciso simular a execução mentalmente para ter certeza de que o laço começa em 1 e termina em n. Eu aceitaria o custo de ~2x em sistemas onde a correção importa mais que o tempo de resposta e o gargalo não é CPU: regras de negócio, cálculo de folha de pagamento, compiladores, processamento de dados em lote, sistemas concorrentes (sem estado compartilhado, sem corrida). Não aceitaria em código de tempo real ou de desempenho crítico: motor de jogo, sistema embarcado com pouca memória, processamento de áudio e vídeo, laços internos de cálculo numérico.

**8.2** Que o `primeiro 42 (div 1 0)` devolve 42 sem erro: o Haskell nem chega a fazer a divisão.

### Desafio extra: o quicksort do livro

**8.3** O erro de impressão está no primeiro filtro: `b <- h` deveria ser `b <= h`.

```haskell
sort (h:t) = sort [b | b <- t, b <= h]
             ++ [h] ++
             sort [b | b <- t, b > h]
```

Com `b <- h`, o compilador entende "`b` tirado da lista `h`". Então `h` teria que ser uma lista. Mas `h` é um elemento da lista que está sendo ordenada (tipo `a`), e os `b` tirados de `h` vão para a mesma lista de resultado que o próprio `h` (tipo `a` também). Ou seja, `h :: a` e ao mesmo tempo `h :: [a]`. O compilador e recusa o programa com um erro do tipo **"Occurs check: cannot construct the infinite type: a ~ [a]"**: para os tipos fecharem, `a` teria que ser igual a `[a]`, um tipo infinito. Trocando `<-` por `<=` o filtro volta a ser uma comparação e o programa imprime `[2,2,4,7,8,9]`.

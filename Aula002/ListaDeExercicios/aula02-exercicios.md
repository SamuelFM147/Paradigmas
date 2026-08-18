# Aula 002 — Evolução das Principais Linguagens de Programação
### Exercícios resolvidos (10 de 20) — base: Sebesta, cap. 2


---
## Questão 1

**Enunciado:** A genealogia das linguagens não é uma escada de progresso. Explique essa afirmação e apresente dois fatores históricos que fazem uma linguagem influenciar outra sem necessariamente substituí-la.

**Resposta:**

Dizer que a genealogia das linguagens não é uma "escada de progresso" significa que o histórico das linguagens não segue uma linha reta em que cada nova linguagem é estritamente superior e substitui a anterior — como se cada degrau tornasse o degrau de baixo obsoleto. Na prática, o desenvolvimento das linguagens se parece mais com uma árvore (ou até um grafo) de influências cruzadas: linguagens antigas continuam em uso, coexistindo com as novas, e ideias migram em várias direções, não apenas "para frente".

Dois fatores históricos que explicam isso:

1. **Custo de substituição e legado (legacy):** uma vez que uma linguagem tem uma base instalada enorme — código em produção, programadores treinados, ferramentas construídas em torno dela — trocar de linguagem tem um custo altíssimo. É por isso que COBOL, criada no fim dos anos 1950, ainda roda em sistemas bancários e governamentais décadas depois de linguagens "mais modernas" existirem. A linguagem antiga não é substituída; ela continua paralela, influenciando e sendo influenciada por padrões novos.

2. **Especialização por domínio:** linguagens surgem para resolver problemas específicos (computação científica, processamento comercial, IA simbólica, sistemas embarcados). Uma linguagem nova em um domínio diferente não "substitui" uma linguagem antiga de outro domínio — ela coexiste. O que acontece é troca de características: um recurso de uma linguagem (como a estrutura de blocos do ALGOL) é absorvido por outra linguagem (Pascal, C) mesmo que a linguagem original perca relevância comercial. Isso é influência sem substituição.

**Referência:** Sebesta, cap. 2, páginas PDF 50–51.



---

## Questão 3

**Enunciado:** Compare Short Code, Speedcoding e os sistemas A-0/A-1/A-2 quanto ao problema enfrentado e à estratégia adotada. Por que chamá-los simplesmente de compiladores modernos seria impreciso?

**Resposta:**

**Problema comum:** no início dos anos 1950, programar diretamente em código de máquina era lento, propenso a erros e exigia conhecimento profundo do hardware específico. Havia demanda por uma forma mais próxima da notação matemática/humana, que a máquina pudesse processar.

- **Short Code** (John Mauchly, por volta de 1949-1950, para o BINAC/UNIVAC): foi um esquema **interpretativo** — o "código" (na verdade, expressões codificadas) era traduzido e executado linha a linha, em tempo de execução, sem gerar um programa em código de máquina separado. Isso trazia grande ganho de produtividade para o programador, mas com forte penalidade de desempenho, já que cada instrução era reinterpretada toda vez que era executada.

- **Speedcoding** (John Backus, IBM 704, 1953): também um **interpretador**, oferecia pseudo-operações para aritmética de ponto flutuante (que o 704 não tinha nativamente). Priorizava produtividade do programador sobre velocidade de execução e consumia mais memória.

- **Sistemas A-0, A-1, A-2** (Grace Hopper, UNIVAC, meados dos anos 1950): tinham uma estratégia diferente — mais próxima de um **montador/ligador (linker) de sub-rotinas**. O A-0 permitia referenciar sub-rotinas pré-escritas por códigos numéricos e "montá-las" em um programa completo; o A-2 evoluiu para uma automação maior desse processo, se aproximando mais da ideia de compilação do que os sistemas puramente interpretativos.

**Por que "compiladores modernos" é impreciso:** um compilador, no sentido atual, traduz o código-fonte uma única vez para um código-alvo independente, com fases de análise léxica, sintática, semântica e geração/otimização de código, produzindo um executável que roda sem reinterpretação. Short Code e Speedcoding eram **interpretadores**, não compiladores — não geravam código de máquina autônomo, e pagavam o preço de desempenho a cada execução. Os sistemas de Hopper estavam mais perto de compilação, mas eram primitivos: basicamente concatenavam sub-rotinas pré-escritas referenciadas por número, sem as noções modernas de análise sintática geral ou otimização. Chamar os três grupos indistintamente de "compiladores modernos" ignora que resolviam o mesmo problema com estratégias tecnicamente muito diferentes.

**Referência:** Sebesta, cap. 2, páginas PDF 53–56.



---

## Questão 5

**Enunciado:** Lisp surgiu em um contexto diferente de Fortran. Compare os domínios, a representação de dados e o estilo de computação favorecido pelas duas linguagens.

**Resposta:**

**Domínio:** Fortran (John Backus, IBM, 1957) nasceu para **computação científica e de engenharia** — cálculos numéricos pesados em máquinas como o IBM 704. Lisp (John McCarthy, MIT, 1958) nasceu no contexto da pesquisa em **inteligência artificial**, voltada para manipulação simbólica: representar conhecimento, lógica, listas de símbolos, não apenas números.

**Representação de dados:** em Fortran, a estrutura central é o **array/vetor numérico**, com tipos de dados fixos (inteiro, real) adequados a cálculos matemáticos. Em Lisp, a estrutura central é a **lista** (via s-expressions), uma estrutura recursiva e flexível que serve tanto para representar dados quanto o próprio código-fonte (homoiconicidade) — isso é essencial para representar símbolos e relações no estilo da IA simbólica da época.

**Estilo de computação:** Fortran favorece um estilo **imperativo**: sequências de atribuições, laços (DO), sub-rotinas que manipulam estado mutável para produzir um resultado numérico. Lisp favorece um estilo **funcional**: computação como avaliação de funções matemáticas, uso extensivo de recursão em vez de iteração, ênfase em expressões que retornam valores em vez de depender de efeitos colaterais (embora o Lisp original também tivesse recursos imperativos).

**Síntese do contraste:** Fortran foi otimizada para throughput numérico em hardware limitado da época; Lisp foi otimizada para manipulação simbólica flexível e definições recursivas, refletindo a necessidade da IA de representar e transformar estruturas de conhecimento, e não apenas calcular números.

**Referência:** Sebesta, cap. 2, páginas PDF 61–65.


---

## Questão 7

**Enunciado:** COBOL foi desenhada para processamento comercial. Mostre como domínio e público influenciaram sua legibilidade, seus registros e sua relação com FLOW-MATIC.

**Resposta:**

COBOL (1959-60, comitê CODASYL, fortemente influenciada por Grace Hopper) foi criada para **processamento de dados comerciais**: folha de pagamento, faturamento, controle de estoque — tarefas envolvendo grandes volumes de registros estruturados (arquivos), e não cálculo matemático complexo.

**Público-alvo e legibilidade:** como o público incluía não só programadores, mas também gestores de negócio e auditores que precisavam entender o código, o COBOL priorizou uma sintaxe **verbosa e próxima do inglês** (`MOVE`, `ADD`, `DISPLAY`, `IF ... THEN`) em vez de notação matemática concisa como em Fortran ou ALGOL. A ideia era que o código fosse, até certo ponto, autoexplicativo para quem não fosse programador de formação.

**Registros:** a estrutura de dado central do COBOL é o **registro** (record), organizado hierarquicamente por "níveis" (01, 05 etc.), modelado diretamente sobre os registros/formulários de negócio em papel — cada registro com campos nomeados (nome do cliente, valor, data), refletindo diretamente como as empresas já organizavam seus dados em fichas e arquivos.

**Relação com FLOW-MATIC:** FLOW-MATIC (Grace Hopper, UNIVAC, meados dos anos 1950) foi pioneira em usar comandos em inglês e em orientar-se por processamento de arquivos/registros comerciais. O COBOL herdou diretamente essa filosofia — e boa parte da estrutura de DATA DIVISION/PROCEDURE DIVISION — formalizando e padronizando as ideias de Hopper para criar uma linguagem comercial única, adotada de forma ampla pela indústria e pelo governo americano.

**Referência:** Sebesta, cap. 2, páginas PDF 72–76.


---

## Questão 9

**Enunciado:** APL, SNOBOL e SIMULA 67 seguiram direções distintas. Associe cada linguagem ao seu foco e identifique uma contribuição duradoura de cada uma.

**Resposta:**

- **APL** (Kenneth Iverson, IBM, início dos anos 1960): foco em **notação matemática concisa para arrays/matrizes**, permitindo operar sobre vetores e matrizes inteiros com símbolos únicos, extremamente compacto. **Contribuição duradoura:** popularizou a ideia de programação orientada a arrays (operações vetorizadas de alto nível), influência que aparece depois em ferramentas como MATLAB e bibliotecas como NumPy.

- **SNOBOL** (Ralph Griswold e outros, Bell Labs, início dos anos 1960): foco em **processamento de strings e casamento de padrões** (pattern matching) para manipulação de texto. **Contribuição duradoura:** técnicas sofisticadas de pattern matching que influenciaram o desenvolvimento posterior de expressões regulares e linguagens de script orientadas a texto (o próprio Griswold depois criou o Icon, e o espírito de SNOBOL ecoa em Perl).

- **SIMULA 67** (Ole-Johan Dahl e Kristen Nygaard, Noruega): foco em **simulação de eventos discretos**, introduzindo o conceito de "classe" para agrupar dados e procedimentos em torno de uma instância. **Contribuição duradoura:** é considerada a primeira linguagem orientada a objetos, com conceitos de objeto, classe e herança que influenciaram diretamente Smalltalk, C++, Java e praticamente toda a programação orientada a objetos posterior.

**Referência:** Sebesta, cap. 2, páginas PDF 85–87.



---

## Questão 11

**Enunciado:** Construa uma cadeia de influência que passe por ALGOL, Pascal e C. Depois contraste essa linhagem imperativa com a proposta declarativa de Prolog.

**Resposta:**

**Cadeia de influência:**

- **ALGOL 60** introduziu estrutura de blocos, controle de fluxo estruturado (`if-then-else`, `while`) e definição formal de sintaxe via BNF — grande influência sobre praticamente todas as linguagens imperativas seguintes.
- **Pascal** (Niklaus Wirth, 1970-71) é descendente direto do ALGOL: Wirth, insatisfeito com a complexidade do ALGOL 68, criou uma linguagem mais simples e sistemática, herdando a estrutura de blocos e o controle de fluxo do ALGOL, mas voltada para o **ensino de programação estruturada**, com tipagem mais rígida.
- **C** (Dennis Ritchie, Bell Labs, 1972) também bebe do estilo de controle de fluxo estruturado do ALGOL, mas reorienta o projeto para **programação de sistemas**: acesso direto a ponteiros e memória, eficiência para escrever o próprio Unix, priorizando controle de baixo nível sobre a segurança de tipos que o Pascal buscava.

**Contraste com Prolog:** enquanto a linhagem ALGOL → Pascal → C é **imperativa** — o programa especifica passo a passo *como* computar um resultado, através de atribuições e comandos que alteram estado —, o **Prolog** (Colmerauer e Kowalski, 1972) é **declarativo/lógico**: o programador declara fatos e regras (uma base de conhecimento) e faz consultas; é o mecanismo de inferência da linguagem (unificação + backtracking) que decide *como* chegar à resposta. Ou seja, a linhagem ALGOL trata de fluxo de controle explícito, e o Prolog trata de especificar relações e metas, deixando a busca da solução por conta do interpretador — duas filosofias fundamentalmente diferentes sobre o que é "programar".

**Referência:** Sebesta, cap. 2, páginas PDF 88–93.



---

## Questão 13

**Enunciado:** Ada resultou de requisitos e projeto em grande escala. Analise como confiabilidade, tipos, pacotes e concorrência se relacionam ao domínio de sistemas críticos.

**Resposta:**

Ada surgiu de uma iniciativa do Departamento de Defesa dos EUA (liderada por Jean Ichbiah, início dos anos 1980) para substituir centenas de linguagens diferentes usadas em sistemas embarcados/militares (aviônica, controle de mísseis, sistemas em tempo real) por uma única linguagem padronizada e altamente confiável. O domínio — **sistemas críticos de missão e segurança** — moldou diretamente suas características centrais:

- **Confiabilidade e tipos:** tipagem estática forte, com restrições de faixa de valores e tratamento explícito de exceções, para detectar erros o mais cedo possível (em tempo de compilação sempre que possível), já que uma falha de software nesses domínios pode ser catastrófica (ex.: software de voo).

- **Pacotes:** mecanismo de modularização (separação entre especificação e corpo) que permite encapsulamento e ocultamento de informação — essencial para projetos de defesa gigantescos, com múltiplas equipes trabalhando ao longo de décadas, exigindo componentes reutilizáveis e verificáveis de forma independente.

- **Concorrência:** modelo de tarefas (*tasking*) embutido na própria linguagem (tasks, rendezvous), permitindo expressar diretamente a natureza inerentemente concorrente de sistemas de controle embarcados (múltiplos sensores/atuadores operando simultaneamente), sem depender de bibliotecas de threading específicas de sistema operacional — o que favorece portabilidade entre diferentes plataformas de tempo real.

Em conjunto, essas escolhas refletem o objetivo central do Ada: disciplina de engenharia e previsibilidade para software de larga escala em que a vida humana pode depender do funcionamento correto do sistema.

**Referência:** Sebesta, cap. 2, páginas PDF 94–98.



---

## Questão 14

**Enunciado:** Compare o papel dos objetos em Smalltalk, C++ e Java. Inclua na resposta o compromisso de C++ com C e a estratégia de portabilidade de Java.

**Resposta:**

- **Smalltalk** (Alan Kay, Xerox PARC, anos 1970): é orientação a objetos **pura** — absolutamente tudo (inteiros, blocos de código, até as próprias classes) é um objeto, e toda computação ocorre por troca de mensagens entre objetos. Não existe "escape" procedural fora do paradigma de objetos.

- **C++** (Bjarne Stroustrup, primeira metade dos anos 1980): começou literalmente como "C com classes" — adicionou recursos de orientação a objetos (classes, herança, funções virtuais) **em cima do C**, mantendo compatibilidade quase total com a sintaxe, o modelo de memória de baixo nível e o desempenho do C. Esse compromisso com C significa que, em C++, usar objetos é **opcional**: a linguagem é multiparadigma (procedural, orientada a objetos, depois genérica), e o programador pode contornar a abstração com ponteiros crus e gerenciamento manual de memória — herança direta de não romper com o C.

- **Java** (Sun, James Gosling e equipe, meados dos anos 1990): os objetos são centrais, mas, diferente do Smalltalk, o Java mantém tipos primitivos (`int`, `boolean`) por questão de desempenho; e, diferente do C++, o Java remove ponteiros explícitos, herança múltipla de implementação e gerenciamento manual de memória, adotando coleta de lixo (garbage collection) automática e segurança de execução. A estratégia definidora do Java foi a **portabilidade**: "write once, run anywhere", via compilação para bytecode executado por uma Java Virtual Machine (JVM) em qualquer plataforma — em vez de compilar direto para código de máquina nativo como o C++ — trocando algum desempenho bruto por independência de plataforma, o que foi crucial para a adoção posterior do Java na Web e em ambientes corporativos.

**Referência:** Sebesta, cap. 2, páginas PDF 98–103.



---

## Questão 16

**Enunciado:** Compare Perl, JavaScript, PHP, Python, Ruby e Lua usando três eixos: domínio inicial, estruturas de dados e estratégia de implementação. Evite concluir que todas são iguais por serem chamadas de scripting.

**Resposta:**

| Linguagem | Domínio inicial | Estrutura de dados central | Estratégia de implementação |
|---|---|---|---|
| **Perl** (Larry Wall, 1987) | Administração de sistemas Unix / processamento de texto (substituindo scripts shell/awk/sed) | Escalares, arrays e *hashes* (arrays associativos) como cidadãos de primeira classe, com regex nativa | Interpretador que internamente compila para bytecode antes de executar — início rápido para tarefas de script |
| **JavaScript** (Brendan Eich, 1995) | Interatividade de páginas Web no navegador (manipulação de DOM) | Objetos dinâmicos/associativos quase universais (até arrays são objetos); herança baseada em protótipos, não classes | Originalmente interpretadores simples embutidos no navegador; motores modernos (ex.: V8) usam compilação JIT |
| **PHP** (Rasmus Lerdorf, 1994) | Geração dinâmica de páginas Web no servidor, embutido em HTML | O "array" do PHP funciona como array indexado e como mapa/dicionário ordenado simultaneamente | Interpretado/compilado para bytecode a cada requisição (ex.: Zend Engine), otimizado para o ciclo requisição-resposta |
| **Python** (Guido van Rossum, 1991) | Script de propósito geral com ênfase em legibilidade, expandido depois para ciência de dados, Web, IA | Listas, tuplas, dicionários e conjuntos como tipos ricos e nativos da linguagem | Implementação de referência (CPython) compila para bytecode executado em uma máquina virtual |
| **Ruby** (Yukihiro Matsumoto, meados dos 1990, Japão) | Script de propósito geral com foco em expressividade ("felicidade do programador"), depois popular via Rails na Web | Tudo é objeto (influência de Smalltalk), incluindo inteiros; arrays e hashes são objetos com métodos ricos | Originalmente um interpretador direto (MRI); versões posteriores adicionam uma VM de bytecode (YARV) |
| **Lua** (Roberto Ierusalimschy et al., PUC-Rio, 1993) | Linguagem de script embutível em aplicações hospedeiras, sobretudo jogos | Um único tipo "table", versátil, que serve como array, mapa/dicionário e base para objetos/módulos | VM de bytecode extremamente leve, projetada para fácil incorporação via API C compacta, priorizando footprint pequeno |

**Conclusão:** apesar do rótulo comum de "linguagem de scripting", os problemas originais que resolviam (ferramentas de texto Unix vs. DOM de navegador vs. templates Web vs. legibilidade geral vs. pureza de objetos vs. incorporação em outras aplicações), suas estruturas de dados centrais e suas estratégias de implementação são bastante diferentes entre si — tratá-las como equivalentes só porque são "scripting" apaga diferenças de projeto relevantes.

**Referência:** Sebesta, cap. 2, páginas PDF 107–113.



---

## Questão 19

**Enunciado:** Crie uma linha do tempo com oito linguagens de pelo menos quatro paradigmas. Para cada ligação, escreva o tipo de influência; não use apenas setas cronológicas.

**Resposta:**

Linguagens escolhidas (4 paradigmas: imperativo, orientado a objetos, funcional, lógico):

**Fortran (1957, imperativo) → ALGOL 60 (1960, imperativo):**
Influência = adoção e refinamento de estruturas de controle estruturadas e definição formal de sintaxe (BNF), superando o estilo mais livre e cheio de `GOTO` do Fortran.

**ALGOL 60 → Pascal (1971, imperativo/estruturado):**
Influência = descendência pedagógica direta; Pascal simplifica e sistematiza a estrutura de blocos e a tipagem do ALGOL para o ensino de programação estruturada.

**ALGOL 60 → C (1972, imperativo/sistemas):**
Influência = adoção do controle de fluxo estruturado, mas reorientado para acesso de baixo nível ao hardware, priorizando eficiência de sistema em vez de ensino ou cálculo numérico.

**Simula 67 (1967, orientado a objetos) → Smalltalk (1972-80, orientado a objetos):**
Influência = herança conceitual direta dos conceitos de "classe" e objeto, levados por Smalltalk a um paradigma puro, baseado inteiramente em troca de mensagens.

**Smalltalk → C++ (1985, orientado a objetos/multiparadigma):**
Influência = incorporação dos conceitos de OOP (classes, herança) enxertados sobre o C, mas sem adotar a pureza de "tudo é objeto" — C++ manteve o paradigma procedural como opção.

**Lisp (1958, funcional) → Prolog (1972, lógico/declarativo):**
Influência = indireta e contrastante — ambas nasceram do interesse da pesquisa em IA por manipulação simbólica, mas Prolog seguiu uma abordagem declarativa (unificação/resolução) bem diferente da avaliação funcional/recursiva do Lisp; é um exemplo de ramificação paralela dentro da mesma comunidade de pesquisa, não uma linhagem direta.

**Linhagem imperativa (Fortran→ALGOL→Pascal/C) vs. linhagem funcional/lógica (Lisp/Prolog):**
Influência = divergência de paradigma — a linhagem imperativa nasceu de necessidades numéricas/científicas e depois de engenharia geral; a linhagem funcional/lógica nasceu do raciocínio simbólico da IA. Isso evidencia quatro paradigmas distintos (imperativo, orientado a objetos, funcional, lógico) ramificando-se a partir de domínios de problema diferentes, e não uma única linha de sucessão.

**Referência:** Sebesta, cap. 2, páginas PDF 50–118.



---

## Próximos passos



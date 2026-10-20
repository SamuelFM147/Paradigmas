# Exercício: o que o Prolog responde?

Para cada item: **(a)** o que o Prolog responde? **(b)** qual regra explica?

Sebesta, Cap. 16. Consultas executadas no SWI-Prolog (SWISH).

| # | Tema | Consulta | Resposta | Regra que explica |
|---|------|----------|----------|-------------------|
| 1 | Unificação | `f(X, b) = f(a, Y).` | `X = a, Y = b` | Unificação casa os termos posição por posição |
| 1 | Unificação | `f(X, X) = f(a, b).` | `no` | `X` não pode ser `a` e `b` ao mesmo tempo |
| 2 | Aritmética | `X = 2 + 3.` | `X = 2+3` | `=` é unificação, não calcula |
| 2 | Aritmética | `X is 2 + 3.` | `X = 5` | `is` avalia a expressão da direita |
| 2 | Aritmética | `5 = 2 + 3.` | `no` | O número `5` não casa com a estrutura `+(2, 3)` |
| 3 | Ordem | `cor(F, vermelha).` | `F = maca ; F = morango` | Busca da primeira para a última cláusula |
| 4 | Listas | `[H \| T] = [a, b, c].` | `H = a, T = [b, c]` | A cauda é sempre uma lista |
| 4 | Listas | `[A, B \| R] = [1, 2].` | `A = 1, B = 2, R = []` | A cauda pode ser a lista vazia |
| 5 | Negação | `\+ gosta(bruno, prolog).` | `yes` | Mundo fechado: o que não se prova é falso |
| 5 | Negação | `\+ gosta(X, prolog).` | `no` | Negação por falha não gera valores para `X` |
| 6 | Ordem das cláusulas | `ancestral(ana, bia).` | Nunca responde: estouro de pilha | Recursão à esquerda: laço infinito |

---

## 1 · Unificação

```prolog
?- f(X, b) = f(a, Y).
   X = a, Y = b

?- f(X, X) = f(a, b).
   no
```

**Regra:** dois termos unificam quando têm o **mesmo functor**, o **mesmo número de parâmetros** e cada parâmetro unifica com o da mesma posição. No primeiro: `f` = `f`, `X` casa com `a` e `b` casa com `Y`. No segundo, a primeira posição instancia `X = a`; na segunda, `X` (que já vale `a`) teria de unificar com `b`. Dois átomos diferentes não unificam, então falha.

---

## 2 · Aritmética

```prolog
?- X = 2 + 3.
   X = 2+3

?- X is 2 + 3.
   X = 5

?- 5 = 2 + 3.
   no
```

**Regra:**

- `=` é **unificação**, e não atribuição nem comparação. `2 + 3` é só a estrutura `+(2, 3)`, e `X` passa a ser esse termo, sem cálculo nenhum.
- `is` **avalia** a expressão da direita e unifica o resultado com a esquerda (p. 703). Todas as variáveis da direita precisam estar instanciadas.
- `5 = 2 + 3` compara a **forma**: o número `5` e a estrutura `+(2, 3)` são termos diferentes, então dá `no`. Para comparar **valores**, usa-se `5 =:= 2 + 3`, que dá `yes`.

É a pegadinha de quem vem do Java: lá, `x = 2 + 3` calcula e atribui; em Prolog nada é calculado sem `is`.

---

## 3 · Ordem das respostas

```prolog
cor(maca, vermelha).
cor(banana, amarela).
cor(morango, vermelha).

?- cor(F, vermelha).
   F = maca ;
   F = morango
```

**Regra:** o Prolog procura nos fatos **de cima para baixo**, sempre na mesma ordem (p. 703). O primeiro fato casa (`F = maca`). Pedindo outra resposta (`;`), ele faz **retrocesso**: desfaz `F = maca` e continua a busca a partir do fato seguinte. `banana` não casa porque `amarela` ≠ `vermelha`, e `morango` casa. A ordem das respostas é a ordem das cláusulas no programa.

---

## 4 · Listas

```prolog
?- [H | T] = [a, b, c].
   H = a, T = [b, c]

?- [A, B | R] = [1, 2].
   A = 1, B = 2, R = []
```

**Regra:** `[H | T]` separa a lista em **cabeça** (o primeiro elemento) e **cauda** (o resto), como o `car` e o `cdr` do Scheme. A cauda é sempre uma **lista**: `T` é `[b, c]`, e não `b, c`. No segundo, `A` e `B` pegam os dois elementos e não sobra nada, então a cauda `R` é a **lista vazia** `[]`. A unificação funciona porque `[1, 2]` é, internamente, `[1 | [2 | []]]`.

---

## 5 · Negação e mundo fechado

```prolog
gosta(ana, prolog).

?- \+ gosta(bruno, prolog).
   yes

?- \+ gosta(X, prolog).
   no
```

**Regra:**

- `\+ G` é **negação por falha**: é verdadeiro quando o Prolog **não consegue provar** `G`. Não há fato dizendo que bruno gosta de prolog, então `gosta(bruno, prolog)` falha e o `\+` é satisfeito. Isso é a **hipótese do mundo fechado** (p. 713): o que não está na base é considerado falso. O `yes` não quer dizer que bruno não gosta de prolog, só que isso não pode ser provado.
- No segundo, `gosta(X, prolog)` **é provado** (com `X = ana`), então o `\+` falha e a resposta é `no`. Não é a pergunta "quem não gosta de prolog?": a negação por falha nunca gera valores para as variáveis. Para isso, `X` precisa estar instanciada **antes** da negação (por exemplo, `pessoa(X), \+ gosta(X, prolog)`).

---

## 6 · Ordem das cláusulas

```prolog
ancestral(X, Y) :- ancestral(Z, Y), pai(X, Z).
ancestral(X, Y) :- pai(X, Y).
pai(ana, bia).

?- ancestral(ana, bia).
```

**Resposta:** nunca responde. O programa entra em laço infinito até estourar a pilha:

```
Stack limit (1.0Gb) exceeded
  Probable infinite recursion (cycle):
    ancestral(_4936, bia)
    ancestral(_4962, bia)
```

(No GNU Prolog, do OneCompiler, a mensagem é `local stack overflow`.)

**Regra:** o Prolog tenta as cláusulas **na ordem em que foram escritas**, e os objetivos do corpo **da esquerda para a direita** (p. 711). Para provar `ancestral(ana, bia)`, ele usa a primeira cláusula, cujo primeiro objetivo é `ancestral(Z, bia)`. Para provar esse, usa a primeira cláusula de novo: `ancestral(Z', bia)`, e assim para sempre. Ele nunca chega a consultar `pai(ana, bia)`, que resolveria tudo. É **recursão à esquerda**.

Correção: pôr o caso base primeiro e a chamada recursiva **depois** de um objetivo que consulta um fato:

```prolog
ancestral(X, Y) :- pai(X, Y).
ancestral(X, Y) :- pai(X, Z), ancestral(Z, Y).
```

A lógica é a mesma, mas agora `ancestral(ana, bia)` responde `yes`. Em lógica pura a ordem não importaria; no Prolog, que sempre busca na mesma ordem, ela decide se o programa termina.

---

## Fechamento

As respostas que mais surpreendem quem vem do Java são a **2** (`=` não calcula), a **5** (`\+` não quer dizer "é falso", e sim "não consegui provar") e a **6** (a mesma lógica em outra ordem nunca termina). Todas vêm do mesmo ponto: o Prolog não **avalia expressões**, ele faz uma **busca** com regras fixas (unificação, cláusulas de cima para baixo, objetivos da esquerda para a direita, retrocesso).

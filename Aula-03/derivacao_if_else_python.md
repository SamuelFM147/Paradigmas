# Exemplo didático - Derivação de um comando if-else em Python

## Objetivo

Mostrar como um pequeno trecho de código pode ser construído a partir de regras gramaticais de uma linguagem real.

**Fonte de referência:** Python Software Foundation. *The Python Language Reference*, seção 8.1 — The if statement. Disponível em: docs.python.org/3/reference/compound_stmts.html. A gramática abaixo foi simplificada para fins didáticos, preservando a estrutura sintática essencial do comando `if-else`.

---

## 1. Código gerado

```python
if idade >= 18:
    print("Maior de Idade")
else:
    print("Menor de idade")
```

---

## 2. Regras gramaticais utilizadas

Para concentrar a derivação no comando de decisão, usamos uma gramática reduzida. Os símbolos entre `< >` são não terminais; os demais representam terminais da linguagem (incluindo os terminais especiais `NEWLINE`, `INDENT` e `DEDENT`, que representam, respectivamente, a quebra de linha e o aumento/diminuição do nível de indentação — mecanismo que o Python usa no lugar de chaves `{ }` para delimitar blocos).

```
<if_statement>     ::= if <condition> : <block> else : <block>
<condition>        ::= <identifier> <relop> <integer_literal>
<relop>            ::= >= | <= | > | < | ==
<block>            ::= NEWLINE INDENT <statement> DEDENT
<statement>        ::= print ( <string_literal> )
<identifier>       ::= idade
<integer_literal>  ::= 18
<string_literal>   ::= "Maior de Idade" | "Menor de idade"
```

*Observação: por simplicidade, optou-se por derivar `<statement>` diretamente em `print ( <string_literal> )`, sem passar por um não terminal intermediário `<function_call>`. Essa escolha foi aplicada de forma consistente nos dois blocos (if e else).*

---

## 3. Derivação

A derivação abaixo começa no símbolo não terminal `<if_statement>` e substitui, passo a passo, cada não terminal (sempre um por vez) até obter a sequência de terminais correspondente ao trecho desejado.

```
<if_statement>
⇒ if <condition> : <block> else : <block>
⇒ if <identifier> <relop> <integer_literal> : <block> else : <block>
⇒ if idade <relop> <integer_literal> : <block> else : <block>
⇒ if idade >= <integer_literal> : <block> else : <block>
⇒ if idade >= 18 : <block> else : <block>
⇒ if idade >= 18 : NEWLINE INDENT <statement> DEDENT else : <block>
⇒ if idade >= 18 : NEWLINE INDENT print ( <string_literal> ) DEDENT else : <block>
⇒ if idade >= 18 : NEWLINE INDENT print ( "Maior de Idade" ) DEDENT else : <block>
⇒ if idade >= 18 : NEWLINE INDENT print ( "Maior de Idade" ) DEDENT else : NEWLINE INDENT <statement> DEDENT
⇒ if idade >= 18 : NEWLINE INDENT print ( "Maior de Idade" ) DEDENT else : NEWLINE INDENT print ( <string_literal> ) DEDENT
⇒ if idade >= 18 : NEWLINE INDENT print ( "Maior de Idade" ) DEDENT else : NEWLINE INDENT print ( "Menor de idade" ) DEDENT
```

**Forma concreta em Python, com formatação real** (onde `NEWLINE` vira quebra de linha e `INDENT`/`DEDENT` viram o recuo do bloco):

```python
if idade >= 18:
    print("Maior de Idade")
else:
    print("Menor de idade")
```

---

## 4. Breve explicação textual

O comando `if-else` em Python permite executar um bloco de código quando uma condição é verdadeira e outro bloco alternativo quando ela é falsa. Neste exemplo, a condição `idade >= 18` é avaliada: se verdadeira, o programa executa o bloco indentado logo após o `:` do `if`, imprimindo `"Maior de Idade"`; caso contrário, executa o bloco indentado após o `:` do `else`, imprimindo `"Menor de idade"`.

Diferente de linguagens como Java ou Pascal, o Python não usa chaves para delimitar blocos — em vez disso, a própria gramática da linguagem trata a quebra de linha (`NEWLINE`) e a indentação (`INDENT`/`DEDENT`) como símbolos que abrem e fecham o bloco. A derivação demonstra que o código não surge arbitrariamente: ele é uma sentença válida porque pode ser obtido por substituições sucessivas a partir das regras da gramática, uma de cada vez, até restar apenas o código-fonte final.

---

### Observação sobre o processo de derivação

Uma derivação sintática consiste em partir de um símbolo não terminal inicial e aplicar, a cada passo, **uma única regra de produção** — substituindo um não terminal pelo seu lado direito na gramática — até que não reste nenhum símbolo não terminal na cadeia, restando apenas terminais (o código-fonte real). Quando há mais de um não terminal disponível para expandir (como ocorre aqui com o `<block>` do `if` e o `<block>` do `else`), convenciona-se expandir sempre o mais à esquerda primeiro, mantendo os demais intactos até chegar sua vez.

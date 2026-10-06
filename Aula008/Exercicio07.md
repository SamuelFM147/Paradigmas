# Exercício 07: qual é a saída?

Dos 6 trechos, **só 2 dão erro** (5 e 6). Os outros 4 (1, 2, 3 e 4) **rodam normalmente**. O resultado deles não é bug: é o comportamento definido pelas características de cada linguagem.

| # | Linguagem | Resultado | Dá erro? | Característica da linguagem que explica |
|---|-----------|-----------|----------|------------------------------------------|
| 1 | Python | `[1]` e `[1, 2]` | Não | Valor padrão avaliado uma vez, na definição da função |
| 2 | Java | `0 5` | Não | Passagem sempre por valor (referência copiada) |
| 3 | Python | `[2, 2, 2]` | Não | Closures capturam variáveis (escopo léxico, late binding) |
| 4 | C | `3` | Não | Variável `static` com duração de armazenamento global |
| 5 | Rust | não compila | **Sim** | Ownership e move |
| 6 | Python | `UnboundLocalError` | **Sim** | Regra de escopo: atribuição cria variável local |

---

# Parte 1: trechos que NÃO têm erro (explicar o porquê)

## 1 · Python

```python
def adicionar(item, lista=[]):
    lista.append(item)
    return lista

print(adicionar(1))
print(adicionar(2))
```

**(a) Saída**

```
[1]
[1, 2]
```

**(b) Por que acontece, segundo as características do Python**

- Em Python, `def` é um **comando executável**: quando ele roda, o objeto função é criado e os **valores padrão dos parâmetros são avaliados naquele momento, uma única vez**. Eles não são reavaliados a cada chamada.
- O `[]` vira um objeto lista que fica guardado dentro do objeto função (em `adicionar.__defaults__`).
- Listas são **mutáveis**, e o `append` altera o próprio objeto. Como toda chamada sem o argumento `lista` usa **o mesmo objeto**, a alteração da 1ª chamada aparece na 2ª.
- Resumindo: o padrão é compartilhado entre as chamadas porque é um único objeto mutável, criado na definição.

Isso é o comportamento definido da linguagem, e não um defeito. Quem quiser uma lista nova a cada chamada usa o idioma `lista=None` e cria a lista dentro da função, mas isso é uma escolha de estilo, não uma correção obrigatória.

---

## 2 · Java

```java
static void zera(int[] v, int n) {
    v[0] = 0;
    n = 0;
}
int[] v = {5, 5}; int n = 5;
zera(v, n);
System.out.println(v[0] + " " + n);
```

**(a) Saída**

```
0 5
```

**(b) Por que acontece, segundo as características do Java**

- Java usa **passagem de parâmetros somente por valor**. Não existe passagem por referência.
- **`n` (tipo primitivo):** o método recebe uma **cópia** do valor `5`. O `n = 0` altera só a variável local do método, e o `n` do chamador continua `5`.
- **`v` (array, que é um objeto):** a variável guarda uma **referência** para o array no heap. O que é copiado é o **valor dessa referência**. As duas referências (a de fora e a do parâmetro) apontam para o **mesmo array**. Por isso `v[0] = 0` modifica o array que o chamador também enxerga.
- Resumindo: o método consegue **alterar o conteúdo** do objeto apontado, mas não consegue **trocar a variável** do chamador. Com primitivos, nada do chamador é alterado.

---

## 3 · Python

```python
fs = [lambda: i for i in range(3)]
print([f() for f in fs])
```

**(a) Saída**

```
[2, 2, 2]
```

**(b) Por que acontece, segundo as características do Python**

- Python tem **escopo léxico com closures**: uma função interna pode usar variáveis do escopo onde foi definida.
- A closure captura a **variável** (a "célula" que guarda o valor), e não uma cópia do valor do momento da criação. Esse comportamento é conhecido como **late binding**: o valor só é lido **quando a função é chamada**.
- Na list comprehension há **uma única variável `i`**, reutilizada a cada iteração. Quando o laço termina, `i` vale `2`.
- As três lambdas compartilham essa mesma variável. Ao serem chamadas depois, todas leem `i = 2`, e o resultado é `[2, 2, 2]`.

Para quem quiser capturar o valor de cada iteração, existe o idioma `lambda i=i: i` (o padrão é avaliado na criação, como no trecho 1). Mas o código original está correto segundo as regras da linguagem.

---

## 4 · C

```c
int contador(void) {
    static int n = 0;
    return ++n;
}
// em main:
contador(); contador();
printf("%d\n", contador());
```

**(a) Saída**

```
3
```

**(b) Por que acontece, segundo as características do C**

- Em C, **escopo** (onde o nome é visível) e **duração de armazenamento** (quanto tempo a variável existe) são conceitos separados.
- Uma variável local comum tem duração **automática**: é criada na pilha a cada chamada e destruída ao sair da função.
- Com `static`, a variável continua com **escopo local** (só `contador` enxerga `n`), mas passa a ter duração **estática**: fica numa área de memória que existe durante **toda a execução do programa**.
- A inicialização `= 0` acontece **uma única vez**, antes de o programa começar (em tempo de carga), e não a cada chamada.
- `++n` é pré-incremento: incrementa e retorna o valor novo. As chamadas retornam 1, 2 e 3, e o `printf` imprime o valor da 3ª chamada.

---

# Parte 2: trechos COM erro

## 5 · Rust: erro de compilação

### Código com falha

```rust
fn dobra(v: Vec<i32>) -> Vec<i32> {
    v.iter().map(|x| x * 2).collect()
}

fn main() {
    let v = vec![1, 2, 3];
    let d = dobra(v);
    println!("{:?} {:?}", v, d);   // ERRO
}
```

Erro do compilador (resumido):

```
error[E0382]: borrow of moved value: `v`
```

### Por que dá erro, segundo as características do Rust

- Rust gerencia memória por **ownership (posse)**, sem coletor de lixo. Regras: cada valor tem **um único dono**, e quando o dono sai de escopo o valor é liberado.
- `Vec<i32>` guarda dados no heap e **não implementa `Copy`**. Ao passar `v` por valor para `dobra(v)`, a posse é **movida** para o parâmetro da função. Quando `dobra` termina, esse vetor é liberado.
- Depois do move, `v` em `main` fica **inválido**. Usar `v` no `println!` seria acessar memória já liberada (*use after free*), e o compilador recusa isso **em tempo de compilação**.

### Código corrigido

**Opção A (preferível): emprestar (*borrow*) com referência**

```rust
fn dobra(v: &[i32]) -> Vec<i32> {
    v.iter().map(|x| x * 2).collect()
}

fn main() {
    let v = vec![1, 2, 3];
    let d = dobra(&v);              // empresta, a posse continua em main
    println!("{:?} {:?}", v, d);    // [1, 2, 3] [2, 4, 6]
}
```

**Opção B: clonar antes de passar**

```rust
fn dobra(v: Vec<i32>) -> Vec<i32> {
    v.iter().map(|x| x * 2).collect()
}

fn main() {
    let v = vec![1, 2, 3];
    let d = dobra(v.clone());       // a cópia é movida, o original fica
    println!("{:?} {:?}", v, d);    // [1, 2, 3] [2, 4, 6]
}
```

---

## 6 · Python: erro de execução

### Código com falha

```python
total = 0

def adiciona(x):
    total = total + x
    return total

print(adiciona(5))
```

Erro:

```
UnboundLocalError: cannot access local variable 'total' where it is not associated with a value
```

### Por que dá erro, segundo as características do Python

- Python resolve nomes pela regra **LEGB** (Local, Enclosing, Global, Built-in).
- Antes de executar a função, o Python analisa o corpo dela: se houver **atribuição** a um nome em qualquer ponto (`total = ...`), esse nome é classificado como **local em toda a função**.
- Ao executar `total + x`, ele procura o `total` **local**, que ainda não recebeu valor, e lança `UnboundLocalError`. O `total = 0` global não é consultado.
- Apenas **ler** uma variável global dentro da função funcionaria. O problema é **atribuir** a ela sem declarar a intenção.

### Código corrigido

**Opção A: declarar `global`**

```python
total = 0

def adiciona(x):
    global total          # usa o total do escopo global
    total = total + x
    return total

print(adiciona(5))   # 5
```

**Opção B (preferível): sem estado global**

```python
def adiciona(total, x):
    return total + x

total = 0
total = adiciona(total, 5)
print(total)   # 5
```

# Exercício 08: qual é a saída e quando a decisão é tomada?

Para cada trecho: **(a)** qual é a saída? **(b)** a decisão foi tomada na **compilação** ou na **execução**?

| # | Linguagem | Saída | Decisão tomada na | Característica que explica |
|---|-----------|-------|-------------------|----------------------------|
| 1 | Java | `au` | **Execução** | Despacho dinâmico (método sobrescrito) |
| 2 | C++ | `A` | **Compilação** | Método não `virtual`: ligação estática |
| 3 | Java | `A B` | Campo: **compilação**; método: **execução** | Campos não são polimórficos, métodos são |
| 4 | Python | `1 2 2` | **Execução** | Tipagem e ligação totalmente dinâmicas; atributo de classe compartilhado |
| 5 | Java | `A` | **Compilação** | Método `static` não é sobrescrito, é ocultado |
| 6 | Go | `faz ... au` | **Compilação** | Embedding não é herança: sem despacho dinâmico pelo tipo embutido |

---

## 1 · Java

```java
class Animal {
  String som() { return "..."; }
}
class Cachorro extends Animal {
  String som() { return "au"; }
}
Animal x = new Cachorro();
System.out.println(x.som());
```

**(a) Saída**

```
au
```

**(b) Decisão: execução**

- Em Java, métodos de instância são **virtuais por padrão**.
- O tipo estático de `x` é `Animal`, mas o tipo dinâmico (o objeto real) é `Cachorro`.
- O compilador só verifica que `Animal` tem um método `som()`. **Qual** implementação roda é decidido em tempo de execução, pelo tipo real do objeto (**despacho dinâmico**, via tabela de métodos virtuais).

---

## 2 · C++

```cpp
struct A {
  void f() { cout << "A"; }
};
struct B : A {
  void f() { cout << "B"; }
};
B b; A *p = &b;
p->f();
```

**(a) Saída**

```
A
```

**(b) Decisão: compilação**

- Em C++, métodos **não são virtuais por padrão**. Sem a palavra `virtual`, não há despacho dinâmico.
- O compilador olha o tipo **estático** do ponteiro (`A*`) e já resolve a chamada para `A::f` (**ligação estática**).
- `B::f` apenas **esconde** `A::f`; não a sobrescreve. Com `virtual void f()` em `A`, a saída seria `B`, decidida em execução.

---

## 3 · Java

```java
class A { String nome = "A";
  String getNome() { return nome; } }
class B extends A { String nome = "B";
  String getNome() { return nome; } }
A x = new B();
System.out.println(x.nome + " " + x.getNome());
```

**(a) Saída**

```
A B
```

**(b) Decisão**

- `x.nome`: **compilação**. Campos **não são polimórficos**: o acesso é resolvido pelo tipo estático (`A`), então lê `A.nome` = `"A"`. O `nome` de `B` é um campo diferente, que apenas **esconde** o de `A`.
- `x.getNome()`: **execução**. É método de instância, então há despacho dinâmico pelo objeto real (`B`), que devolve o `nome` de `B` = `"B"`.

---

## 4 · Python

```python
class Contador:
    total = 0
    def __init__(self):
        Contador.total += 1
        self.id = Contador.total

a = Contador(); b = Contador()
print(a.id, b.id, a.total)
```

**(a) Saída**

```
1 2 2
```

**(b) Decisão: execução**

- Python é dinâmico: não há decisão de ligação na compilação; atributos e métodos são buscados em tempo de execução.
- `total` é **atributo de classe**, compartilhado por todas as instâncias. Cada `__init__` incrementa o mesmo contador (`Contador.total`).
- `self.id` é **atributo de instância**, copiando o valor do contador naquele momento: `a.id = 1`, `b.id = 2`.
- `a.total` não existe na instância, então a busca sobe até a classe e acha `Contador.total` = `2`.

---

## 5 · Java

```java
class A {
  static String quem() { return "A"; }
}
class B extends A {
  static String quem() { return "B"; }
}
A x = new B();
System.out.println(x.quem());
```

**(a) Saída**

```
A
```

**(b) Decisão: compilação**

- Métodos `static` pertencem à **classe**, não ao objeto, então **não participam de despacho dinâmico**.
- `B.quem()` **oculta** (hiding) `A.quem()`, não a sobrescreve.
- `x.quem()` é resolvido pelo tipo estático de `x` (`A`), equivalente a `A.quem()`. O objeto real (`B`) é ignorado.

---

## 6 · Go

```go
type Animal struct{}
func (Animal) Som() string { return "..." }
func (a Animal) Falar() string {
    return "faz " + a.Som()
}
type Cao struct{ Animal }
func (Cao) Som() string { return "au" }

fmt.Println(Cao{}.Falar(), Cao{}.Som())
```

**(a) Saída**

```
faz ... au
```

**(b) Decisão: compilação**

- Go **não tem herança**; `Cao` apenas **embute** `Animal` (composição). Os métodos de `Animal` são promovidos a `Cao`.
- `Cao{}.Falar()` chama `Animal.Falar` com o receptor `Animal` embutido. Dentro dele, `a` é do tipo `Animal`, então `a.Som()` resolve **estaticamente** para `Animal.Som` = `"..."`. O `Som` de `Cao` não é visível ali (não há "self virtual" como em Java).
- `Cao{}.Som()` chama diretamente o `Som` de `Cao` = `"au"`.
- Para polimorfismo em Go é preciso usar **interfaces**; embedding sozinho não faz despacho dinâmico.

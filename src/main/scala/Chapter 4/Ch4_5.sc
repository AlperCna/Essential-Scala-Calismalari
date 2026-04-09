//4.5 Working With Data

//Önceki bölümde veri nasıl kurulur ADT: Sum + Product görmüştük bu bölümde ise bu veriy nasık işleyeceğimiz göülüyor
// Bu veri nasıl işlenir
//Structural Recursion
/*
Structural recursion is the opposite of building data.
Veri oluşturma
B+C -> A
Structural Recursion
A -> B+C

case class A(b: B, c: C)
A yı kullanmak için onu parçalarsın b ve c  sonra bu parçalarla sonucu üretiriz

Structural recursion = veriyi parçalayarak çözmek

Neden önemli

Yazacağın kod, verinin yapısını takip eder.
yani veri nasıl tanımlanndıysa kod da aynı  şekilde takip eder


Structural Recursion'un 2 farklı versiyonu var
1-Polymorphism (OOP style)

method trait içinde tanımlanır

her subtype kendi implement eder

2 Pattern Matching (FP style)

dışarıda match ile ayrıştırılır

özet farkı polymorphismde nesne kendi ne yapacağını bilir pattern matchingde ben nesneye bakarım ve karar veririm
 */


//4.5.1 Structural Recursion using Polymorphism

//Polymorphizmin basit tanımı aynı method farklı classlarda farklı davranırdeme

//If we define a method in a trait, and have different implementaঞons in classes extending that trait, when we call that method the
//implementaঞon on the actual concrete instance will be used.


sealed trait A  {
  def foo : String
}

final case class B() extends A {
  def foo:String = "It's B"
}

final case class C() extends  A {
  def foo:String = "It's C"
}

//We declare a value with type A but we see the concrete implementaঞon on B
//or C is used.

val anA: A = B()

anA.foo

val anA : A = C()

anA.foo

//Çağrılan method → runtime’daki gerçek tipe göre çalışır
// yani tip a ama ggerçek nesne b

//Trait içinde default yazıp implementasyonu ovverride anahtar kelimesiyle değiştirebiliriz

sealed trait A {
  def foo: String =
    "It's A!"
}
final case class B() extends A {
  override def foo: String =
    "It's B!"
}
final case class C() extends A {
  override def foo: String =
    "It's C!"
}
val anA: A = B()
// anA: A = B()
anA.foo
// res2: String = It's B!


/*
The Product Type Polymorphism Paern
If A has a b (with type B) and a c (with type C), and we want to write a
method f returning an F, simply write the method in the usual way.
case class A(b: B, c: C) {
def f: F = ???
}
In the body of the method we must use b, c, and any method parameters
to construct the result of type F.

The Sum Type Polymorphism Paern
If A is a B or C, and we want to write a method f returning an F, define
f as an abstract method on A and provide concrete implementaঞons in
B and C.
sealed trait A {
def f: F
}
final case class B() extends A {
def f: F =
???
}
final case class C() extends A {
def f: F =
???
}
 */


//4.5.2 Structural Recursion using Paern Matching

//Aynı şey şimdi FP style ile yapılıyor
//Veriyi dışarıda parçala → sonucu üret

/*
case class A(b: B, c: C)
def f(a: A): F =
  a match {
    case A(b, c) => ...
  }
The Product Type Paern Matching Paern
If A has a b (with type B) and a c (with type C), and we want to write a
method f that accepts an A and returns an F, write
def f(a: A): F =
a match {
case A(b, c) => ???
}
In the body of the method we use b and c to construct the result of type
F.
Mantık: a’yı parçala b ve c’yi al sonucu üret

The Sum Type Paern Matching Paern
If A is a B or C, and we want to write a method f accepঞng an A and
returning an F, define a paern matching case for B and C.
def f(a: A): F =
a match {
case B() => ???
case C() => ???
}

hangi subtype olduğunu kontrol et

ona göre davran

 */

/*
Fark
Polymorphism:Davranış sınıfın içinde
Pattern Matching:Davranış dışarıda

Çok kritik fark

Polymorphism: animal.sound()

Pattern matching: animal match { ... }
 */


//4.5.3 A Complete Example

//Let’s look at a complete example of the algebraic data type and structural recursion paerns, using our familiar Feline data type.
//We start with a descripঞon of the data. A Feline is a Lion, Tiger, Panther,
//or Cat. We’re going to simplify the data descripঞon, and just say that a Cat has
//a String favouriteFood. From this descripঞon we can immediately apply
//our paern to define the data.

sealed trait Feline
final case class Lion() extends Feline
final  case class Tiger() extends Feline
final case class Panther() extends Feline
final case class Cat(favoriteFood :String) extends Feline

//Now let’s implement a method using both polymorphism and paern matching.
//Our method, dinner, will return the appropriate food for the feline in quesঞon.
//For a Cat their dinner is their favouriteFood. For Lions it is antelope, for
//Tigers it is ঞger food, and for Panthers it is licorice.
//We could represent food as a String, but we can do beer and represent it
//with a type. This avoids, for example, spelling mistakes in our code. So let’s
//define our Food type using the now familiar paerns.


sealed trait Food
case object Antelope extends Food
case object TigerFood extends Food
case object Licorice extends Food
final case class CatFood(food: String) extends Food

//Now we can implement dinner as a method returning Food. First using polymorphism:

sealed trait Feline {
  def dinner :Food
}
final case class Lion() extends Feline {
  def dinner: Food =
    Antelope
}
final case class Tiger() extends Feline {
  def dinner: Food =
    TigerFood
}
final case class Panther() extends Feline {
  def dinner: Food =
    Licorice
}
final case class Cat(favouriteFood: String) extends Feline {
  def dinner: Food =
    CatFood(favouriteFood)
}

//Mantık: her subtype kendi yemeğini bilir


// Şimdi diğer yöntem olan Pattern matching ile çözüm var
// Pattern matching de 2 çözüm var 1 rait içinde diğeri ise dış obje içinde

sealed trait Feline {
  def dinner: Food =
    this match {
      case Lion() => Antelope
      case Tiger() => TigerFood
      case Panther() => Licorice
      case Cat(favouriteFood) => CatFood(favouriteFood)
    }
}

object Diner {
  def dinner(feline: Feline): Food =
    feline match {
      case Lion() => Antelope
      case Tiger() => TigerFood
      case Panther() => Licorice
      case Cat(food) => CatFood(food)
    }
}

//Kod verinin şeklini takip eder
//Feline = Lion | Tiger | Panther | Cat(food)


//4.5.4 Choosing Which Paern to Use

//1. polymorphism;
//2. paern matching in the base trait; and
//3. paern matching in an external object (as in the Diner example above).

/*
1. İlk 2 yöntem aynı sonucu verir
ikisi de methodu class içinde tanımlar

Genelde trait içinde pattern matching tercih edilir çünkü daha az kod tekrarına yol açar

Polymorphmzde her classta ayrı yazıyoruz pattern matching daha kısa


2. Class içinde method yazmanın kısıtı
Yani class içinde method yazarsak sadece 1 implementation olabilir yani aslan sadece tek 1 tane dinner tanımı alabilir
Method çalışmak için ihtiyaç duıyduğu her şeye class içinde sahip olmalı

3.External object kullanmanın avantajı

object Diner {
  def dinner(f: Feline): Food = ...
}

burada birden fazla implementasyon yazabşkşrşz

object Diner
object FancyDiner
object CheapDiner hepsi farklı davranır

4. En önemli karar kuralı

GENEL KURAL
Eğer method sadece class’ın kendi verisine bağlıysa:
 class içinde yaz

Eğer method dış verilere bağlıysa:

dışarıda yaz (pattern matching)

5. Birden fazla impplementation varsa external object kullanılır

 */



/*
4.5.5 Object-Oriented vs Functional Extensibility

Functional Style (FP)
data ayrı
logic ayrı
pattern matching kullanılır
sealed trait kullanılır

 Object-Oriented Style (OO)
data + behavior birlikte
polymorphism kullanılır
open inheritance (sealed yok)


Functional Style Ne Demek

sadece veri var

methodlar dışarıda

object Diner {
  def dinner(f: Feline) = ...

  compiler bize daha çok yardım  eder çünkü sealed trait Feline
  compiler bütün subtypları bilir

match {
  case Lion()
}
eksik case varsa uyarır

FP → compile-time safety


OO style ne demek

methodlar class içinde

inheritance açık

trait Feline {
  def dinner: Food
}


Yeni class eklemek kolay:

class Leopard extends Felin

|                       | Yeni method eklemek | Yeni veri eklemek |
| --------------------- | ------------------- | ----------------- |
| OO (polymorphism)     | zor (kod değişir)   |  kolay           |
| FP (pattern matching) | kolay               |  zor             |


OO yaklaşım
Yeni class eklemek kolay
Ama yeni method eklemek zor
Neden

Yeni method eklemek için:

trait Feline {
  def newMethod: ...
}
tüm subclass’ları değiştirmen gerekir

FP yaklaşım
Yeni method eklemek kolay
Ama yeni class eklemek zor
Neden?

Yeni class ekleyince:

case class Leopard()
bütün match’leri güncellemen gerekir

Bu ne demek?
OO:
data extensible
behavior rigid
FP:
behavior extensible
data rigid


Scala ikisini de destekler

ama sealed trait kullanmak tercih edilir
çünkü daha güvenli compiler kontrolü var ve ug riski düşük
 */

//4.5.6 Exercises
//  4.5.6.1 Traffic Lights
//In the previous secঞon we implemented a TrafficLight data type like so:
//sealed trait TrafficLight
//case object Red extends TrafficLight
//case object Green extends TrafficLight
//case object Yellow extends TrafficLight
//Using polymorphism and then using paern matching implement a method
//  called next which returns the next TrafficLight in the standard Red ->
//  Green -> Yellow -> Red cycle. Do you think it is beer to implement this
//method inside or outside the class? If inside, would you use paern matching
//or polymorphism? Why?

//İlk olarak polymorphizm
sealed trait TrafficLight {
  def next: TrafficLight
}

case object Red extends TrafficLight{
  def next:TrafficLight = Green
}

case object Green extends TrafficLight{
  def next:TrafficLight = Yellow
}

case object Yellow extends TrafficLight {
  def next: TrafficLight =Red
}

// Şimdi pattern matching

sealed trait TrafficLight {
  def next:TrafficLight=
    this match{
      case Red => Green
      case Green => Yellow
      case Yellow => Red
    }
}

case object Red extends TrafficLight
case object Green extends TrafficLight
case object Yellow extends TrafficLight

// Bu case e göre pattern matching yapmak daha mantıklı çünkü next dışardan veriye bağımlı değil
//Pattern matching yapıyı daha mantıklı kılıyor

/*
4.5.6.2 Calculaঞon
In the last secঞon we created a Calculation data type like so:
sealed trait Calculation
final case class Success(result: Int) extends Calculation
final case class Failure(reason: String) extends Calculation
We’re now going to write some methods that use a Calculation to perform
a larger calculaঞon. These methods will have a somewhat unusual shape—this
is a precursor to things we’ll be exploring soon—but if you follow the paerns
you will be fine.
4.6. RECURSIVE DATA 109
Create a Calculator object. On Calculator define methods + and - that
accept a Calculation and an Int, and return a new Calculation. Here are
some examples
assert(Calculator.+(Success(1), 1) == Success(2))
assert(Calculator.-(Success(1), 1) == Success(0))
assert(Calculator.+(Failure("Badness"), 1) == Failure("Badness"))
 */

sealed trait Calculation
final case class Success(result: Int) extends Calculation
final case class Failure(reason: String) extends Calculation

object Calculator {
  object Calculator {
    def +(calc: Calculation, operand: Int): Calculation = ???
    def -(calc: Calculation, operand: Int): Calculation = ???
  }
}

// Şimdi structural recursion uygulanarak

object Calculator {
  def +(calc: Calculation, operand: Int): Calculation =
    calc match {
      case Success(result) => ???
      case Failure(reason) => ???
    }
  def -(calc: Calculation, operand: Int): Calculation =
    calc match {
      case Success(result) => ???
      case Failure(reason) => ???
    }
}

object Calculator {
  def +(calc: Calculation, operand: Int): Calculation =
    calc match {
      case Success(result) => Success(result + operand)
      case Failure(reason) => Failure(reason)
    }
  def -(calc: Calculation, operand: Int): Calculation =
    calc match {
      case Success(result) => Success(result - operand)
      case Failure(reason) => Failure(reason)
    }
}
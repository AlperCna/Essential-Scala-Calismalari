//4.3 Modeling Data with Traits

//Önceki bölümlerde daha çok zellikleri öğrenmiştik

//We’re going to shift our focus from language features to programming patterns.

//artık syyntaxdan patternlere geçiyor
// OOP is a has a ilişkisi
//Functional Programming dili sum type product type içeriyor
//Bunların birleşiminie algebraic data type deniyor
// Yani bu bölümün hedefi veri modelini analiz etmek doğru yapısal kalıba oturtmak onu Scala kodu olarak yazmak

//Yani öncce veri tipi kuruluyor sonra onun üzerine çalışan kod yazılıyor

//4.3.1 The Product Type Paern
//Bu desen bir şeyin başka şeylere sahip  olması durumunu modeller yani
// A has a B and C örneğin a Cat has a colour and a favourite food;
//a Visitor has an id and a creaঞon date; and so on

// product denmesinin sebebi matematikten geliyor bir tipin birden fazla bapımsız parçası çarpımsal şekilde büyür
//Product type bir nesnenin brden fazla bberiyi aynı anda taşıması

/*
Product Type Paern
If A has a b (with type B) and a c (with type C) write
case class A(b: B, c: C)
or
trait A {
def b: B
def c: C
}

örneğin A Cat has a colour and a favourite food

case class Cat(colour: String, favouriteFood: String)
 */






//4.4 The Sum Type Paern

//Bu desen verinin birden fazla farklı durumdan biri olmasını modeller.
//A is a B OR C

//For example, a Feline is a Cat, Lion, or Tiger;
//a Visitor is an Anonymous or User; and so on.

//Sum Type Paern
//If A is a B or C write
//sealed trait A
//final case class B() extends A
//final case class C() extends A

// sum type veri tipini birden farklı durumdan biri olması demektir




//4.4.1 Algebraic Data Types
//Product type ve sum type kullanan veri yapılarına algebraic data type denir
//Veri yapısı bu iki yapı taşından ya da bunların birleşiminden kuruluyorsa ADT dir
//ADT = veriyi mantıksal parçalar ve alternatifler üzerinden kuran modelleme yaklaşımı


//4.4.2 The Missing Paerns
/*
Biz iki eksen kullanduk is-a /has a ve and /or yapısını
has - and product type
is a or - sum type
bunu bir tablo ibi düşündüğümüzde 2 kısım boş kalıyor


1. is a and pattern
yani A hem B dir hem de C dir

trait B
trait C
trait A extends B with C

with ile birlikte bir trait birden fazla traiti extend edebilir
çok kullanılmayan bir pattern

nerde kullanılabilir
1.Modularity
cake pattern denilen bazı gelişmiş modülerlik yapılarında kullanılabilir

2.Implementation sharing
Birden falza sınır arasından implementasyon paylaşımı yapmak için ama bunu ana trait içinde default method olarak vermek istemediğimiz durumlarda

 */

/*
2. has a or pattern
A has a B or C
yani A nın içinde birr alan var ama bu alan iki farkl tipren biri olabilir

bu iki farklı şekilde yazılabilir
 */
//A has a D, and D is a B or C
trait A {
  def d: D
}
sealed trait D
final case class B() extends D
final case class C() extends D

//ilk olarak or yapısını arı tip olarak yapılır daha sonra o bunu içerir

//A has a D, and D is a B or C

sealed trait A
final case class D(b: B) extends A
final case class E(c: C) extends A

//4.4.4 Exercises
//4.4.4.1 Stop on a Dime
//A traffic light is red, green, or yellow. Translate this descripঞon into Scala code.

 sealed trait TrafficLight

case object Red extends TrafficLight
case object Green extends TrafficLight
case object Yellow extends TrafficLight


//4.4.4.2 Calculator
//A calculaঞon may succeed (with an Int result) or fail (with a String message).
//Implement this.

sealed trait Calculation
final case class Succes(result:Int) extends Calculation
final case class Failure(reason:String) extends Calculation

//4.4.4.3 Water, Water, Everywhere
//Boled water has a size (an Int), a source (which is a well, spring, or tap), and
//a Boolean carbonated. Implement this in Scala.

sealed trait Source
case object Well extends Source
case object Spring extends Source
case object Tap extends Source

final case class BottledWater(size: Int, source: Source, carbonated: Boolean)


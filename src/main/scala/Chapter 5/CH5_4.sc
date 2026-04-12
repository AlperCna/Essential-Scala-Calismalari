//5.4 Modelling Data with Generic Types

/*
Generic type’lar sayesinde:

generic product type yazabiliriz
farklı veri tiplerini tek yapı altında toplayabiliriz
optional gibi soyutlamalar yapabiliriz
 */

//5.4.1 Generic Product Types
// problem bir fonksiyonun birden fazla değer rdöndürmesi

//def intAndString: ??? = // ...
//def booleanAndDouble: ??? = // ...

//Kötü çözüm olarak şunu yapabiliriz önceden öğrendiğiim patternlerle  yazılabilir her yeni kombinasyon için yeni class

case class IntAndString(intValue: Int, stringValue: String)

case class BooleanAndDouble(booleanValue: Boolean, doubleValue: Double)

// ölçeklenemez

//Doğru çözüm Generic product type yazmak

//Pair[A, B] tek bir generi class yazmak

//           def intAndString: Pair[Int, String]
//           def booleanAndDouble: Pair[Boolean, Double]

/*
Bu yaklaşım:

inheritance kullanmaz
aggregation kullanır

Yani:

 verileri içine koyar (container gibi)
 */

//5.4.1.1 Exercise: Pair

// Implement the Pair class from above. It should store two values—one and
//two—and be generic in both arguments. Example usage:

case class Pair[A,B](one:A , two :B)

val pair = Pair[String, Int]("hi", 2)

pair.one

pair.two
val pair = Pair("hi", 2)
//bu şekilde yazsak bile yine tipler korunur

//A → birinci tip
//B → ikinci tip
//Pair → iki farklı tipi tek yapıda tutar

//5.4.2 Tuples

//Tuple pair in genelleştirilmiş hali oluyor
// Yani pair 2 eleman tutarken tuple N eleman 1 ile 22 arasında tutabiliyor

//Tuple2[A, B]
//Tuple3[A, B, C]
// ama daha kısa yazımı da var (A, B)

Tuple2("hi", 1) // unsugared syntax

("hi", 1) // sugared syntax

("hi", 1, true)


//We can define methods that accept tuples as parameters using the same syntax:

def tuplized[A, B](in: (A, B)) = in._1

tuplized(("a", 1))

//We can also paern match on tuples as follows:

(1, "a") match {
  case (a, b) => a + b
}

//Although paern matching is the natural way to deconstruct a tuple, each class
//also has a complement of fields named _1, _2 and so on:

val x = (1, "b", true)

x._2

x._3


//5.4.3 Generic Sum Types

//Sum type ı daha önceden görmüştük generic bu pattern i absract etmemizi sağlar

//Consider a method that, depending on the value of its parameters, returns one
//of two types:

def intOrString(input: Boolean) =
  if(input == true) 123 else "abc"

// Sxala tipi any olarak çıkarır çünkü bu yapı hem Int hem String dçnebiliyor bunların
// En küçük ortak tipi ant olmş oluyor

//Çözümü sum type kullanmak yani kendi tipimizi kullanıyoruz


//def intOrString(input: Boolean): Sum[Int, String] =
//  if(input == true) {
//    Left
//  } else {
//    Right[Int, String]("abc")
//  }


//How do we implement Sum? We just have to use the paerns we’ve already
//seen, with the addiঞon of generic types.


//5.4.3.1 Exercise: Generic Sum Type

/*
Şunu implement et:

Sum[A, B]
iki subtype:
Left
Right
ikisi de generic olacak
 */

sealed trait Sum [A,B]
final case class Left[A, B](value: A) extends Sum[A, B]
final case class Right[A, B](value: B) extends Sum[A, B]

Left[Int, String](1).value

Right[Int, String]("foo").value

val sum: Sum[Int, String] = Right("foo")


sum match {
  case Left(x) => x.toString
  case Right(x) => x
}

/*
Sum[A, B] → iki ihtimal
Left[A, B] → A taşır
Right[A, B] → B taşır
 */

//5.4.4 Generic Opঞonal Values

//Bazı işlemler değer döndürmeyebilir
/*
örnepin map'te key yok
api cevap vermedi dosya bulunnamadı gibi

Kötü çözümler
exception
null

Problem:
type system bunu zorlamaz
hata unutulabilir

Doğru çözüm: type ile modellemek

Optional value → ya var ya yok
 */

//5.4.4.1 Exercise: Maybe that Was a Mistake
//Create a generic trait called Maybe of a generic type A with two subtypes, Full
//containing an A, and Empty containing no value.

sealed trait Maybe[A]
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]

val perhaps: Maybe[Int] = Empty[Int]
val perhaps: Maybe[Int] = Full(1)


//5.4.6 Exercises
//5.4.6.1 Generics versus Traits
//Sum types and product types are general concepts that allow us to model
//almost any kind of data structure. We have seen two methods of wriঞng these
//types—traits and generics. When should we consider using each?


//Ulঞmately the decision is up to us. Different teams will adopt different programming styles. However, we look at the properঞes of each approach to
//inform our choices:
//Inheritance-based approaches—traits and classes—allow us to create permanent data structures with specific types and names. We can name every field
//and method and implement use-case-specific code in each class. Inheritance
//is therefore beer suited to modelling significant aspects of our programs that
//are re-used in many areas of our codebase.
//Generic data structures—Tuples, Options, Eithers, and so on—are extremely broad and general purpose. There are a wide range of predefined
//classes in the Scala standard library that we can use to quickly model relaঞonships between data in our code. These classes are therefore beer suited
//to quick, one-off pieces of data manipulaঞon where defining our own types
//would introduce unnecessary verbosity to our codebase.



//In this secঞon we implemented a sum type for modelling opঞonal data:
//sealed trait Maybe[A]
//final case class Full[A](value: A) extends Maybe[A]
//final case class Empty[A]() extends Maybe[A]
//Implement fold for this type.


sealed trait Maybe[A] {
  def fold[B](full: A => B, empty: B): B =
    this match {
      case Full(v) => full(v)
      case Empty() => empty
    }
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]


//5.4.6.3 Folding Sum
//In this secঞon we implemented a generic sum type:
//sealed trait Sum[A, B]
//final case class Left[A, B](value: A) extends Sum[A, B]
//final case class Right[A, B](value: B) extends Sum[A, B]
//Implement fold for Sum


sealed trait Sum[A, B] {
  def fold[C](left: A => C, right: B => C): C =
    this match {
      case Left(a) => left(a)
      case Right(b) => right(b)
    }
}
final case class Left[A, B](value: A) extends Sum[A, B]
final case class Right[A, B](value: B) extends Sum[A, B]

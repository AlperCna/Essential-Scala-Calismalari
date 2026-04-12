// 5.2 Functions

//Fonksiyonlar metgodlar üzerinde soyutlama yapmamızı sağlar
//  Methodları bir valye haline getirerek parametre olarak göndermek, geri döndürmek ve başka fonksiyonlara verilme işlemleri yapılabilir

//Kitaptaki örnek

sealed trait IntList {
  def length: Int =
    this match {
      case End => 0
      case Pair(hd, tl) => 1 + tl.length
    }
  def double: IntList =
    this match {
      case End => End
      case Pair(hd, tl) => Pair(hd * 2, tl.double)
    }
  def product: Int =
    this match {
      case End => 1
      case Pair(hd, tl) => hd * tl.product
    }
  def sum: Int =
    this match {
      case End => 0
      case Pair(hd, tl) => hd + tl.sum
    }
}
case object End extends IntList
case class Pair(hd: Int, tl: IntList) extends IntList

//Buradaki hepsinin yapısı aynı

//case End => ...
//case Pair(hd, tl) => ... tl.recursiveCall

//Yani hepsi structural recursion pattern kullanıyor
// Çok fazla tekrar var ve bunu soyutlamamız gerekir

//def abstraction(end: Int, f: ???): Int =
//  this match {
//    case End => end
//    case Pair(hd, tl) => f(hd, tl.abstraction(end, f))
//  }

  // end- End durumunda ne dönecek
  // f - head ile recursive sonucu nasıl birleştireceğiz

  //Buradaki sorun f in tipi ne olacak bilmiyoruz ama burada f bir function olacak


  /*
  Function method gibi çağrılır ama methoddan farklı olarak bir value'dır
  Yani değişkende tutulabilir, parametre olarak verilebilir, döndürülebilir
  */

  //Önceden apply methodunu görmüştük
  object add1 {
    def apply(in: Int) = in + 1
  }

add1(2) // 3

//Object + apply - function gibi davranır ama eksik olan şey genel function tpe sistemidir


//5.2.1 Funcঞon Types

/*
Function type şu şekilde yazılır
(A, B) => C
A ve B parametre tipleri
C dönüş tipini belirtir


(Int, Int) => Int 2 tane int alır ve int döner

A => B parametre gerekmez

f: (Int, Int) => Int
 */


/*
Funcঞon Type Declaraঞon Syntax
To declare a funcঞon type, write
(A, B, ...) => C
where
• A, B, ... are the types of the input parameters; and
• C is the type of the result.
If a funcঞon only has one parameter the parentheses may be dropped:
A => B
 */



//5.2.2 Funcঞon literals

//Scalada function oluşturmak için özel bir syntax var (parameters) => expression

val sayHi = () => "Hi!"
sayHi()


val add2 = (x: Int) => x + 1
add2(10)

val sum = (x: Int, y: Int) => x + y
sum(10, 20) // 30

//Type inference
//Bazen tipleri yazmak zorunda değiliz:

//(x) => x + 1  Scala tipi kendisi çıkarabilir

/*
In code where we know the argument types, we can someঞmes drop the type
annotaࢼons and allow Scala to infer them¹. There is no syntax for declaring the
result type of a funcঞon and it is normally inferred, but if we find ourselves
needing to do this we can put a type on the funcঞon’s body expression:

 */

(x: Int) => (x + 1): Int


/*
Funcঞon Literal Syntax
The syntax for declaring a funcঞon literal is
(parameter: type, ...) => expression
where - the opঞonal parameters are the names given to the funcঞon
parameters; - the types are the types of the funcঞon parameters; and -
the expression determines the result of the funcঞon.
 */

//5.2.3 Exercises

//5.2.3.1 A Beer Abstracঞon
//We started developing an abstracঞon over sum, length, and product which
//we sketched out as
//def abstraction(end: Int, f: ???): Int =
//this match {
//case End => end
//case Pair(hd, tl) => f(hd, tl.abstraction(end, f))
//}
//Rename this funcঞon to fold, which is the name it is usually known as, and
//finish the implementaঞon.

sealed trait IntList {
  def fold(end: Int, f: (Int, Int) => Int): Int =
    this match {
      case End => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }
}
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList


//Now reimplement sum, length, and product in terms of fold.

sealed trait IntList {
  def fold(end: Int, f: (Int, Int) => Int): Int =
    this match {
      case End => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }

  def length: Int =
    fold(0, (_, tl) => 1 + tl)
  def product: Int =
    fold(1, (hd, tl) => hd * tl)
  def sum: Int =
    fold(0, (hd, tl) => hd + tl)
}

case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList


//Is it more convenient to rewrite methods in terms of fold if they were implemented using paern matching or polymorphic? What does this tell us about
//the best use of fold?


//When using fold in polymorphic implementaঞons we have a lot of duplicaঞon; the polymorphic implementaঞons without fold were simpler to write.
//The paern matching implementaঞons benefied from fold as we removed
//the duplicaঞon in the paern matching.
//In general fold makes a good interface for users outside the class, but not
//necessarily for use inside the class.



//Why can’t we write our double method in terms of fold? Is it feasible we
//could if we made some change to fold?


//The types tell us it won’t work. fold returns an Int and double returns an
//  IntList. However the general structure of double is captured by fold. This
//is apparent if we look at them side-by-side:
def double: IntList =
  this match {
    case End => End
    case Pair(hd, tl) => Pair(hd * 2, tl.double)
  }
def fold(end: Int, f: (Int, Int) => Int): Int =
  this match {
    case End => end
    case Pair(hd, tl) => f(hd, tl.fold(end, f))
  }

//If we could generalise the types of fold from Int to some general type then
//we could write double. And that, dear reader, is what we turn to next.


//Implement a generalised version of fold and rewrite double in terms of it.

sealed trait IntList {
  def fold[A](end: A, f: (Int, A) => A): A =
    this match {
      case End => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }
  def length: Int =
    fold[Int](0, (_, tl) => 1 + tl)
  def product: Int =
    fold[Int](1, (hd, tl) => hd * tl)
  def sum: Int =
    fold[Int](0, (hd, tl) => hd + tl)
  def double: IntList =
    fold[IntList](End, (hd, tl) => Pair(hd * 2, tl))
}
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList
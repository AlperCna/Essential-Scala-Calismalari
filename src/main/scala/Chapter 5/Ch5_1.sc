//Chapter 5 Generics

//5.1 Generics
/*
Amaç tipler üzerinde soyutlama yapmak yani bir veri yapısını tek bir tipe bağlı kalmadan kullanmak
Özellikle collectionlarda çok kullanılır
 */

//5.1.1 Pandora’s Box

final case class Box[A](value: A)
// A type paremetresi box içine hangi tipi koyarsak o tip korunur

Box(2)

Box("hi")

//yani içeri ne koyarsan aynı tipte geri alırsın

//[A] yapısı type parameterdır Generic yapının hangi tipte çalışacağını belirler

//Ayrıca methodlarda da  type paremeter olabilir

def generic[A](in: A): A = in

//A sadece bu method içinde geçerlidir

generic[String]("foo")

generic(1)

//Type parameter, method parameter gibi çalışır:
//  Method çağrılırken - değer bağlanır
//Generic çağrılırken - tip bağlanır
//


/*
Type Parameter Syntax
We declare generic types with a list of type names within square brackets like [A, B, C]. By convenঞon we use single uppercase leers for
generic types.

Generic types can be declared in a class or trait declaraঞon in which

case they are visible throughout the rest of the declaraঞon.
case class Name[A](...){ ... }
trait Name[A]{ ... }
Alternaঞvely they may be declared in a method declaraঞon, in which
case they are only visible within the method.
def name[A](...){ ... }
 */


//5.1.2 Generic Algebraic Data Types
/*
We described type parameters as analogous to method parameters, and this
analogy conঞnues when extending a trait that has type parameters. Extending a trait, as we do in a sum type, is the type level equivalent of calling a
method and we must supply values for any type parameters of the trait we’re
extending.
In previous secঞons we’ve seen sum types like the following:
 */,

sealed trait Calculation
case class Success(result: Double)
case class Failure(reason: String)

// problem sadece double ile çalışıyor


sealed trait Result[A]
case class Success[A](result: A) extends Result[A]
case class Failure[A](reason: String) extends Result[A]

//Result[A]:  ya Success[A]
//ya Failure (String reason)


//Success - A tipinde değer taşır
//  Failure - A taşımıyor ama yine de [A] var
//Sebep:
//   Result[A] ile uyumlu olmak için type parameter’ı taşır


/*
Invariant Generic Sum Type Paern
If A of type T is a B or C write
sealed trait A[T]
final case class B[T]() extends A[T]
final case class C[T]() extends A[T]

 */


//5.1.3 Exercises
/*
5.1.3.1 Generic List
Our IntList type was defined as
sealed trait IntList
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList
Change the name to LinkedList and make it generic in the type of data
stored in the list
 */

sealed trait IntList
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList


sealed trait LinkedList[A]
case class End[A]() extends LinkedList[A]
final case class Pair[A](head:A, tail :LinkedList[A]) extends LinkedList[A]


/*
5.1.3.2 Working With Generic Types
There isn’t much we can do with our LinkedList type. Remember that types
define the available operaঞons, and with a generic type like A there isn’t a
concrete type to define any available operaঞons. (Generic types are made
concrete when a class is instanঞated, which is too late to make use of the
informaঞon in the definiঞon of the class.)
However, we can sঞll do some useful things with our LinkedList! Implement
length, returning the length of the LinkedList. Some test cases are below.
val example = Pair(1, Pair(2, Pair(3, End())))
assert(example.length == 3)
assert(example.tail.length == 2)
assert(End().length == 0)
 */


2. ÖRNEK YAPILACAK
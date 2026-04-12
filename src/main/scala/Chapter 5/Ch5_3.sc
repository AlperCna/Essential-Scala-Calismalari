//5.3 Generic Folds for Generic data

/*
Scala'da fold, bir koleksiyonun (liste, dizi vb.) öğelerini başlangıç değeri (seed)
kullanarak ikili bir işlemle tek bir sonuca indirgeyen fonksiyonel bir yöntemdir
 */

/*
Generic data varsa (LinkedList[A])
Biz A tipi hakkında bir şey bilmiyoruz
O yüzden işlem yapmak için kullanıcıdan function vermesini isteriz

Bu yüzden fold çok önemli:
 çünkü işlemi kullanıcıdan gelen function ile yapıyoruz
 */

//5.3.1  Fold

sealed trait LinkedList[A] {
  def fold[B](end: B, f: (A, B) => B): B =
    this match {
      case End() => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }

  def fold[B](end: B)(pair: (A, B) => B): B =
    this match {
      case End() => end
      case Pair(hd, tl) => pair(hd, tl.fold(end)(pair))
    }
}

final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
final case class End[A]() extends LinkedList[A]

//generic linked list tanımı  burada had artık int değil A

def foldOnIntList[A](end: A, f: (Int, A) => A, list: LinkedList[Int]): A =
  list match {
    case End() => end
    case Pair(hd, tl) => f(hd, foldOnIntList(end, f, tl))
  }

//Bu sadece IntList içindi çünkü head tipi Int idi.


/*
Mantık
end: B → boş liste sonucu
f: (A, B) => B → head + recursive sonucu birleştirir
sonuç tipi → B

Yani:

A -veri tipi
B - sonuç tipi
 */

//Yani fold structural recursion'un genelleştirilmiş  halidir
// Yani pattern matching + recursion ama işlemi kullanıcıdan alıyoruz

//Fold Pattern

/*
Fold Paern
For an algebraic datatype A, fold converts it to a generic type B. Fold is
a structural recursion with:
• one funcঞon parameter for each case in A;
• each funcঞon takes as parameters the fields for its associated
class;
• if A is recursive, any funcঞon parameters that refer to a recursive
field take a parameter of type B.
The right-hand side of paern matching cases, or the polymorphic methods as appropriate, consists of calls to the appropriate funcঞon.

Kural
Her case için bir function parametresi
Her function:
o case’in field’larını alır
Recursive alan varsa:
onun tipi → sonuç tipi (B) olur

 */


// Foldu adım adım çıkarma

//baase template

object FoldStepByStepTemplate {
  def foldTemplate[A, B](list: LinkedList[A], end: B, pair: (A, B) => B): B =
    list match {
      case End() => end
      case Pair(hd, tl) => pair(hd, foldTemplate(tl, end, pair))
    }
}

//2. adım her case için parametre eklemek End ve Pair parametreleri var o yüzden

object FoldStepByStepWithParameters {
  def fold[B, A](list: LinkedList[A], end: B, pair: (A, B) => B): B =
    list match {
      case End() => end
      case Pair(hd, tl) => pair(hd, fold(tl, end, pair))
    }
}

//3. adım tipleri belirlemek  End de field yok sadece sonuö dmner o yğzden tipi B olacak


//Pair' in iki adet paremetresi var head için A  tail ise recursive ve bu yüzden type ı B (A,B) -> B
object FoldFinalVersion {
  def fold[A, B](list: LinkedList[A], end: B, pair: (A, B) => B): B =
    list match {
      case End() => end
      case Pair(hd, tl) => pair(hd, fold(tl, end, pair))
    }
}

//End → direkt sonucu ver
//Pair → head + recursive sonucu function ile birleştir


/*
Bu yapı şunu söylüyor:
Her ADT için fold yazılabilir ve bu fold o veri yapısı üzerindeki tüm işlemleri ifade edebilir.

Yani:
map
sum
filter
transform

hepsi fold ile yazılabilir
*/


//5.3.2 Working with functions

/*
Bu bölümde 3 şey anlatılıyor:
kısa function yazma (placeholder)
method → function dönüşümü
multiple parameter list (type inference için)
*/

//5.3.2.1 Placeholder Syntax

// küçük fonksiyonları çok kısa yazmamıza olanak sağlar

val normalFunction: Int => Int = (a: Int) => a * 2

//placeholder ile

val placeholderFunction: Int => Int = ((_: Int) * 2)
//
//        _ + _ // expands to `(a, b) => a + b`
//        foo(_) // expands to `(a) => foo(a)`
//        foo(_, b) // expands to `(a) => foo(a, b)`
//        _(foo) // expands to `(a) => a(foo)`

def foo(x: Int): Int = x + 10
val b: Int = 5
val placeholderAdd: (Int, Int) => Int = _ + _
val placeholderFoo1: Int => Int = foo(_)
val placeholderFoo2: Int => Int = foo(_) + b


//5.3.3 Converting Methods to Functions

// bu yapı placeholder syntazda doğrudan bağlantılu

//Method ile function aynı şey değil.
object Sum {
  def sum(x: Int, y: Int) = x + y

}

val sumAsFunction: (Int, Int) => Int = Sum.sum _

//Çözümü underscore

val sumAsFunctionWithUnderscore: (Int, Int) => Int = Sum.sum _ // bu methodu functiona çevirir


//Bazı durumlarda scala eğer function bekliyorsa underscore yazmamıza gerek kalmaz

object MathStuff {
  def add1(num: Int) = num + 1
}

final case class Counter(value: Int) {
  def adjust(f: Int => Int): Counter = Counter(f(value))
}

val adjustedCounter: Counter = Counter(2).adjust(MathStuff.add1)

//Counter(2).adjust(MathStuff.add1)

//Scala şunu anlar:
//
//burada function lazım"
//
//method’u otomatik function’a çevirir


//5.3.3.1 Mulঞple Parameter Lists

//Scalada methodlar çoklu parametre listesi alabilirler  ama her parametre listesini ayırmalıyız


def example(x: Int)(y: Int) = x + y

val exampleResult: Int = example(1)(2)

//2 tane farklı kullanım alanı var

//1. kullanım daha  okunabilir kod yazmak için

val exampleList: LinkedList[Int] = Pair(1, Pair(2, Pair(3, End())))

val foldResultWithBlock: Int =
  exampleList.fold(0) { (elt, total) => total + elt }

//2. kullanım  type inference

// kural Aynı parametre listesinde → birbirinin tipini kullanamaz
//Farklı listelerde → kullanabilir

//Problemli hali

def foldSingleList[A, B](list: LinkedList[A], end: B, pair: (A, B) => B): B =
  list match {
    case End() => end
    case Pair(hd, tl) => pair(hd, foldSingleList(tl, end, pair))
  }

// doğru hali

def foldMultipleLists[A, B](list: LinkedList[A], end: B)(pair: (A, B) => B): B =
  list match {
    case End() => end
    case Pair(hd, tl) => pair(hd, foldMultipleLists(tl, end)(pair))
  }

//end → B’yi belirler
//sonra pair için bu B kullanılır
//type yazmamıza gerek kalmaz



//5.3.4 Exercises
//5.3.4.1 Tree
//A binary tree can be defined as follows:
//A Tree of type A is a Node with a le[ and right Tree or a Leaf with an element
//of type A.
//Implement this algebraic data type along with a fold method.


sealed trait Tree[A] {
  def fold[B](node: (B, B) => B, leaf: A => B): B
}
final case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A
] {
  def fold[B](node: (B, B) => B, leaf: A => B): B =
    node(left.fold(node, leaf), right.fold(node, leaf))
}
final case class Leaf[A](value: A) extends Tree[A] {
  def fold[B](node: (B, B) => B, leaf: A => B): B =
    leaf(value)
}



//Using fold convert the following Tree to a String
//val tree: Tree[String] =
//Node(Node(Leaf("To"), Leaf("iterate")),
//Node(Node(Leaf("is"), Leaf("human,")),
//Node(Leaf("to"), Node(Leaf("recurse"), Leaf("divine")))))
//Remember you can append Strings using the + method.


//If one type parameter is good, two type parameters are beer:
case class Pair[A, B](one: A, two: B)
//This is just the product type paern we have seen before, but we introduce
//generic types.

//Note that we don’t always need to specify the type parameters when we construct Pairs. The compiler will aempt to infer the types as usual wherever
//it can:
val pair = Pair("hi", 2)
// pair: Pair[String,Int] = Pair(hi,2)
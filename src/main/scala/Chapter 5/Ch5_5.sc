//5.5 Sequencing Computations

/*
Bu bölümde artık şunu varsayıyoruz:

Generic data (genel veri tipleri) öğrendik
Algebraic Data Types (ADT) öğrendik
fold (katlama / reduce) mantığını biliyoruz

Amaç

Bazı durumlarda fold kullanmak:

zor olabilir
ya da hiç mümkün olmayabilir

Bu yüzden alternatif pattern’lar kullanılır:

map ve flatMap

Bu iki yöntem:

Fold’dan daha kolay kullanım sağlar
Fold olmayan veri tiplerinde de çalışabilir
 */

//5.5.1 Map

//Wlimizde bir veri var map bunun içindeki deperleri balka bir şeye dönüştürmeye yarıyor

/*
Örnek olarak elimizde bir List[Int] var
Bir fonksiyon Int => User


amaç List[User]
her int -> user olacak liste yapısı değişmeden kalacak

Örnek 2 (Maybe / Optional)

Elimizde: Maybe[User]

Fonksiyon: User => Order

Amaç:
Maybe[Order]
Mantık:
Eğer User varsa → Order’a çevir
Yoksa → yine boş kal


Örnek 3 (Sum Type / Either gibi)

Elimizde: Sum[String, Order]

(ya hata ya da Order)

Fonksiyon: Order => Double

Amaç: Sum[String, Double]
Mantık:
Eğer hata varsa → dokunma
Eğer Order varsa → Double’a çevir


Genel Pattern

Tüm örneklerin ortak noktası:

F[A] + (A => B)  →  F[B]

Yani:

İçinde A olan bir yapı var
A’yı B’ye çeviriyoruz
Ama yapı (F) aynı kalıyor
 */

//Linked List üzerinde map implementasyonu

sealed trait LinkedList[A] {
  def map[B](fn: A => B): LinkedList[B] =
    this match {
      case Pair(hd, tl) => ???
      case End() => ???
    }
}
final case class Pair[A](head: A, tail: LinkedList[A]) extends
  LinkedList[A]
final case class End[A]() extends LinkedList[A]

//Anlamı: A tipini B’ye çevir Listeyi koru

//Case 1: Pair(hd, tl)
// Elimizde head - A  tail LinkedList[A] var



case Pair(hd, tl) => {
  val newTail: LinkedList[B] = tail.map(fn)
  // Combine newTail and head to create LinkedList[B]
}

//We can convert head to a B using fn, and then build a larger list from newTail
//and our B giving us the final soluঞon

case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))

//For End we don’t have any value of A to apply to the funcঞon. The only
//thing we can return is an End.
//Therefore the complete soluঞon is

sealed trait LinkedList[A] {
  def map[B](fn: A => B): LinkedList[B] =
    this match {
      case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))
      case End() => End[B]()
    }
}
case class Pair[A](hd: A, tl: LinkedList[A]) extends LinkedList[A]
case class End[A]() extends LinkedList[A]


//5.5.2 FlatMAap

//Bu bölümde önce birkaç örnek veriliyor ve sonra bu örneklerin ortak noktası çıkarılıyor.


/*
İlk örnek: LinkedList üzerinde
 şunu söylüyor: Elimizde bir kullanıcı listesi var
Her kullanıcı için onun sipariş listesine ulaşmak istiyoruz

Yani elimizde: LinkedList[User] ve bir fonksiyon var:
User => LinkedList[Order]

Amaç ise: LinkedList[Order]

Burada dikkat edilmesi gereken nokta şu:

Fonksiyon bir User alıp tek bir Order döndürmüyor.
Bir User alıp LinkedList[Order] döndürüyor.

İkinci örnek: Maybe üzerinde burada şöyle diyor:

Elimizde veritabanından yüklenmiş bir kullanıcıyı temsil eden optional bir değer var
Bu kullanıcının en son siparişini bulmak istiyoruz
Bu işlem de yine optional bir değer döndürüyor

Yani elimizde:Maybe[User]

ve bir fonksiyon: User => Maybe[Order]

Amaç: Maybe[Order]

Yine burada fonksiyon doğrudan Order döndürmüyor.
Maybe[Order] döndürüyor.

Üçüncü örnek: Sum type üzerinde şunu söylüyor:

Elimizde ya bir hata mesajı ya da bir Order tutan bir sum type var
Bu sipariş için kullanıcıya bir fatura e-postası göndermek istiyoruz
E-posta gönderme işlemi de ya hata mesajı ya da mesaj ID’si döndürüyor

Yani elimizde: Sum[String, Order]

ve fonksiyon: Order => Sum[String, Id]

Amaç:

Sum[String, Id]

Burada da yine fonksiyon sadece Id döndürmüyor.
Bir bağlam içinde sonuç döndürüyor:

Sum[String, Id]
Bu örneklerin ortak noktası



Elimizde:

F[A]

ve bir fonksiyon:

A => F[B]

var.

İstenilen sonuç ise:

F[B]

Bu işlemi yapan metoda flatMap denir.
 */


//FlatMap'i Maybe için implemente etmek

/*
LinkedList için flatMap implement etmek için bir append metoduna ihtiyaç var.

Bu yüzden burada LinkedList değil, Maybe üzerinden gidiliyor.
 */

// ilk olarak yapı şu şekilde veriliyor
sealed trait Maybe[A] {
  def flatMap[B](fn: A => Maybe[B]): Maybe[B] = ???
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]

//Maybe[A] temel trait
//Full[A] içinde bir değer taşıyor
//  Empty[A] ise boş durumu temsil ediyor

//flatMap metodunun tipi def flatMap[B](fn: A => Maybe[B]): Maybe[B]
//içeride bir A varsa,
//bu Ayı alan ve Maybe[B] döndüren bir fonksiyon uygulanacak,
//sonuç da Maybe[B] olacak.

//Öncekiyle aynı pattern kullanılıyor: bu bir structural recursion’dır ve tipler method body’lerini doldurmamızda bize rehberlik eder.


sealed trait Maybe[A] {
  def flatMap[B](fn: A => Maybe[B]): Maybe[B] =
    this match {
      case Full(v) => fn(v)
      case Empty() => Empty[B]()
    }
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]


/*
case Full(v) => fn(v)

Eğer elimizde Full(v) varsa, yani gerçekten bir değer varsa, bu değer fonksiyona veriliyor:

fn(v)

Fonksiyon zaten Maybe[B] döndürdüğü için sonuç doğrudan bu oluyor.

case Empty() => EmptyB

Eğer elimizde Empty() varsa, yani ortada bir değer yoksa, fonksiyon uygulanamıyor.

Bu durumda sonuç yine boş oluyor:

Empty[B]()
 */


//5.5.3 Functors and Monads

//Bu bölümde map ve flatmap sahibi tiplerin isimlerini veriyor

//Functor nedir : F[A] gibi bir tipe sahip olup map metodu olan yapıya functor denir.
//Yani bir yapı map destekliyorsa ona functor deniyor.

//Monad nedir?  Eğer bir functor’ın ayrıca flatMap metodu da varsa buna monad denir.

// Yani map varsa → functor
//map + flatMap varsa → monad

//Functor ya da monad olmak için biraz daha fazlası gerekir.
/*
genellikle point adı verilen bir constructor gerekir
ayrıca map ve flatMap işlemlerinin uyması gereken bazı algebraic laws vardır
 */


//map ve flatMap’in en doğrudan kullanım alanı listeler gibi collection class’lar olsa da,
// daha büyük resimde konu “computations’ı sıralamaktır” yani hesaplamaları sırayla yürütmektir.

def mightFail1: Maybe[Int] =
  Full(1)

def mightFail2: Maybe[Int] =
  Full(2)

def mightFail3: Maybe[Int] =
  Empty() // This one failed

//Burada:
//
//  mightFail1 başarılı ve Full(1) döndürüyor
//    mightFail2 başarılı ve Full(2) döndürüyor
//    mightFail3 başarısız ve Empty() döndürüyor

//Amaç
//Bu hesaplamaları birbiri ardına çalıştırmak istiyoruz. Eğer bunlardan herhangi biri başarısız olursa tüm hesaplama
//  başarısız olsun. Aksi halde elde edilen sayıların hepsini toplayalım.
//
//  Yani kural şu:
//
//  Her adım başarılıysa devam et
//Bir adım bile başarısızsa her şey başarısız olsun


mightFail1 flatMap { x =>
  mightFail2 flatMap { y =>
    mightFail3 flatMap { z =>
      Full(x + y + z)
    }
  }
}

//Burada sıra şu şekilde:
//mightFail1 çalışıyor, sonucu x
//sonra mightFail2 çalışıyor, sonucu y
//sonra mightFail3 çalışıyor, sonucu z
//hepsi başarılı olursa:
//Full(x + y + z)

//Bunun sonucu Empty olur.
//Çünkü mightFail3 zaten:
//Empty() döndürüyordu.
//Yani üçüncü adım başarısız olunca bütün computation başarısız oluyor.


//Eğer mightFail3’ü çıkarırsak ve sadece şu kalırsa:

mightFail1 flatMap { x =>
  mightFail2 flatMap { y =>
    Full(x + y)
  }
}

//bu computation başarılı olur ve sonuç Full(3) olur
//Çünkü:
//
//  mightFail1 = Full(1)
//mightFail2 = Full(2)
//
//ve toplam: 1+2 = 3


/*
Genel fikir, bir monad’ın bir değeri bir bağlam içinde temsil etmesidir.

Yani monad sadece çıplak bir değer değil, bir context içindeki değer demektir.

Bu bağlamın ne olduğu ise kullanılan monad’a göre değişir.

 context örnekleri

üç örnek veriyor:

1. Optional value

Örneğin veritabanından bir değer çekilirken elde edilebilecek optional değer.

2. Sum of values

Bu, bir hata mesajı ile hesaplanan değerden oluşan bir yapı olabilir.

3. List of values

Yani bir değerler listesi.

Map ve flatMap arasındaki fark

 son cümlede bunu net biçimde söylüyor:

map

Bir bağlam içindeki değeri yeni bir değere dönüştürmek istediğimizde kullanılır.
Ama bağlam aynı kalır.

flatMap

Bir değeri dönüştürmek ve aynı zamanda yeni bir bağlam sağlamak istediğimizde kullanılır.
 */


//
//5.5.4 Exercises
//  5.5.4.1 Mapping Lists
//Given the following list
val list: LinkedList[Int] = Pair(1, Pair(2, Pair(3, End())))
//• double all the elements in the list;
//• add one to all the elements in the list; and
//• divide by three all the elements in the list.

list.map(_ * 2)
list.map(_ + 1)
list.map(_ / 3)


//5.5.4.2 Mapping Maybe
//Implement map for Maybe.

sealed trait Maybe[A] {
  def flatMap[B](fn: A => Maybe[B]): Maybe[B] =
    this match {
      case Full(v) => fn(v)
      case Empty() => Empty[B]()
    }
  def map[B](fn: A => B): Maybe[B] =
    this match {
      case Full(v) => Full(fn(v))
      case Empty() => Empty[B]()
    }
}

final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]


//5.5.4.3 Sequencing Computaঞons
//We’re going to use Scala’s builঞn List class for this exercise as it has a
//flatMap method.
//Given this list
val list = List(1, 2, 3)
//return a List[Int] containing both all the elements and their negaঞon. Order
//is not important. Hint: Given an element create a list containing it and its
//negaঞon.


list.flatMap(x => List(x, -x))



//Given this list
val list: List[Maybe[Int]] = List(Full(3), Full(2), Full(1))
//return a List[Maybe[Int]] containing None for the odd elements. Hint: If
//x % 2 == 0 then x is even


list.map(maybe => maybe.flatMap[Int] { x => if (x % 2 == 0) Full(x)
else Empty() })



//5.5.4.4 Sum
//Recall our Sum type.
sealed trait Sum[A, B] {
def fold[C](left: A => C, right: B => C): C =
this match {
case Left(a) => left(a)
case Right(b) => right(b)
}
}
//final case class Left[A, B](value: A) extends Sum[A, B]
//final case class Right[A, B](value: B) extends Sum[A, B]
//To prevent a name collision between the built-in Either, rename the Left
//and Right cases to Failure and Success respecঞvely


sealed trait Sum[A, B] {
  def fold[C](error: A => C, success: B => C): C =
    this match {
      case Failure(v) => error(v)
      case Success(v) => success(v)
    }
}
final case class Failure[A, B](value: A) extends Sum[A, B]
final case class Success[A, B](value: B) extends Sum[A, B]



//Now things are going to get a bit trickier. We are going to implement map and
//flatMap, again using paern matching in the Sum trait. Start with map. The
//general recipe for map is to start with a type like F[A] and apply a funcঞon
//A => B to get F[B]. Sum however has two generic type parameters. To make
//it fit the F[A] paern we’re going to fix one of these parameters and allow
//map to alter the other one. The natural choice is to fix the type parameter
//associated with Failure and allow map to alter a Success. This corresponds
//to “fail-fast” behaviour. If our Sum has failed, any sequenced computaঞons
//don’t get run.
//In summary map should have type

//def map[C](f: B => C): Sum[A, C]


sealed trait Sum[A, B] {
  def fold[C](error: A => C, success: B => C): C =
    this match {
      case Failure(v) => error(v)
      case Success(v) => success(v)
    }
  def map[C](f: B => C): Sum[A, C] =
    this match {
      case Failure(v) => Failure(v)
      case Success(v) => Success(f(v))
    }
}
final case class Failure[A, B](value: A) extends Sum[A, B]
final case class Success[A, B](value: B) extends Sum[A, B]


//Now implement flatMap using the same logic as map.


sealed trait Sum[A, B] {
  def fold[C](error: A => C, success: B => C): C =
    this match {
      case Failure(v) => error(v)
      case Success(v) => success(v)
    }
  def map[C](f: B => C): Sum[A, C] =
  this match {
    case Failure(v) => Failure(v)
    case Success(v) => Success(f(v))
  }
  def flatMap[C](f: B => Sum[A, C]) =
    this match {
      case Failure(v) => Failure(v)
      case Success(v) => f(v)
    }
}
final case class Failure[A, B](value: A) extends Sum[A, B]
final case class Success[A, B](value: B) extends Sum[A, B]
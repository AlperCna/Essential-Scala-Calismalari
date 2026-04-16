//5.6 Variance

//Type parameter (generic) içeren tiplerde subtype ilişkilerini nasıl kontrol ederiz

//Bu konuyu anlamak için önceden konuştığımız maybe tipine geri dönüyoruz

sealed trait Maybe[A]
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]

//İdeal olarak, Empty içinde kullanılmayan type parametresini kaldırmak isteriz.

//Yani şu hale getirmek isteriz

sealed trait Maybe[A]
final case class Full[A](value: A) extends Maybe[A]
//case object Empty extends Maybe[???]


//Problem şu ki objeler tyoe oaremeter alamaz case object Empty  yazarsak [A] veremieuiz
// Bu durumda Empty’nin extends Maybe[...] kısmına somut bir tip vermemiz gerekir
//Hangi tipi vermeyliyiz diye sorduğumuzda şu iki tür olabilir Unit
//Nothing

//Ama bunlar da hatalara sebep olur

sealed trait Maybe[A]

final case class Full[A](value: A) extends Maybe[A]

case object Empty extends Maybe[Nothing]
// previously defined class Empty is not a companion to object Empty.
//Companions must be defined together; you may wish to use :paste mode for this.

              //val possible: Maybe[Int] = Empty
// <console>:14: error: type mismatch;
// found : Empty.type
// required: Maybe[Int]
// Note: Nothing <: Int (and Empty.type <: Maybe[Nothing]), but trait
//Maybe is invariant in type A.
// You may wish to define A as +A instead. (SLS 4.5)
// val possible: Maybe[Int] = Empty
//

//Empty bir Maybe[Nothing]’dir
//Ama Maybe[Nothing], Maybe[Int]’in subtype’ı değildir

//Bu problemi çözmek için  variance annotations kullanmamız gerekir


//5.6.1 Invariance, Covariance, and Contravariance
//Variance Scala’nın type system’inin zor konularından biridir
//
//Ayrıca: Genelde uygulama kodunda çok sık kullanılmaz

//Temel soru
//
//
//
//Eğer A, B’nin subtype’ıysa
//Foo[A], Foo[B]’nin subtype’ı mıdır?
//
//Cevap: Bu, Foo’nun variance’ına bağlıdır

//Variance nedir?
//Generic bir tipin subtype/supertype ilişkilerinin, type parameter’a göre nasıl değiştiğini belirler


//1. Invariance
//  Foo[T] invariant ise, Foo[A] ve Foo[B] arasında hiçbir ilişki yoktur
//A ve B arasında ilişki olsa bile fark etmez.
//  Önemli not
//  Scala’da generic tiplerin default davranışı invariant’tır


//2. Covariance
 //Foo[+T]
//Bu durumda:

//  Eğer A, B’nin supertype’ıysa
//Foo[A], Foo[B]’nin de supertype’ıdır

//Ek bilgi ( Scala’daki çoğu collection sınıfı covariant’tır)


//3. Contravariance

//  Foo[-T]

//Bu durumda:  Eğer A, B’nin supertype’ıysa
//Foo[A], Foo[B]’nin subtype’ıdır

//Metindeki örnek

//Metin şunu söylüyor:
//  Contravariance’a örnek olarak sadece fonksiyon argümanlarını biliyorum



//5.6.2 Function Types

//Scala’da 0’dan 22 argümana kadar fonksiyonlar için 23 tane built-in generic class vardır

trait Function0[+R] {
  def apply: R
}
trait Function1[-A, +B] {
  def apply(a: A): B
}
trait Function2[-A, -B, +C] {
  def apply(a: A, b: B): C
}
// and so on...

//Fonksiyonlar:
//argümanlarında contravariant
//dönüş tipinde covariant


//Bu neden ilk başta garip görünüyor?
//Bu durum ilk bakışta mantıksız gelebilir


//Fonksiyonları anlamak için örnek

case class Box[A](value: A) {
  /** Apply `func` to `value`, returning a `Box` of the result. */
  def map[B](func: Function1[A, B]): Box[B] =
    Box(func(value))
}

//Bu map metoduna hangi fonksiyonları güvenli şekilde verebiliriz?

/*
A → B fonksiyonu

Açıkça uygundur

Çünkü tipler birebir uyuyor.

2. A → B’nin subtype’ı

Bu da uygundur

Sebep: Fonksiyonun sonucu, B’nin tüm özelliklerine sahip olacaktır

Buradan çıkarım

Bu durum fonksiyonların return type’ında covariant olduğunu gösterir

3. A’nın supertype’ını alan fonksiyon

Bu da uygundur

Sebep: Box içindeki A değeri, fonksiyonun beklediği özelliklere sahiptir

4. A’nın subtype’ını alan fonksiyon

Bu uygun değildir

Sebep: Box içindeki değer aslında A’nın farklı bir subtype’ı olabilir

Yani: fonksiyon daha spesifik bir şey bekliyor olabilir
ama bizim elimizdeki değer o olmayabilir
Genel sonuç (metne göre)

Fonksiyonlar:

Argümanlarında contravariant
Return type’ında covariant
Bölümün Genel Özeti

Variance

Generic tiplerde subtype ilişkisini kontrol eder

Problem

Maybe[Nothing], Maybe[Int]’in subtype’ı değildir
çünkü Maybe invariant’tır

Çözüm

Variance annotations kullanmak gerekir

Üç varyans türü
Invariant → ilişki yok
Covariant → aynı yön
Contravariant → ters yön
Fonksiyonlar
Argüman → contravariant
Return → covariant
Box örneği

Hangi fonksiyonların güvenli olduğunu analiz ederek bu durum açıklanır
 */


//5.6.3 Covariant Sum Types

//Bu bölümde artık variance annotations kullanarak, başta yaşanan problemi çözüyoruz.

//Önceki bölümde problem şuydu:
//Empty bir Maybe[Nothing]
//ama Maybe[Nothing], Maybe[Int]’in subtype’ı değil
//çünkü Maybe invariant

//Çözüm: Maybe’yi covariant yapmak

sealed trait Maybe[+A]
final case class Full[A](value: A) extends Maybe[A]
case object Empty extends Maybe[Nothing]

// yazarak maybe covariant hale geldi

val perhaps: Maybe[Int] = Empty
//Artık beklediğimiz davranışı elde ediyoruz
//Empty, tüm Full değerlerinin subtype’ıdır

//Bu pattern, generic sum type’lar için en yaygın kullanılan pattern’dır

/*
Covariant Generic Sum Type Paern
If A of type T is a B or C, and C is not generic, write

sealed trait A[+T]
final case class B[T](t: T) extends A[T]
case object C extends A[Nothing]
This paern extends to more than one type parameter. If a type parameter is not needed for a specific case of a sum type, we can subsঞtute
Nothing for that parameter.

 */

// 5.6.4  Contravariant Posiঞon
//Covariant type parameter ile, method/func parametrelerinin (contravariant) etkileşimi
//Bu problemi göstermek için covariant bir Sum geliştireceğiz

//5.6.4.1 Exercise: Covariant Sum

//Implement a covariant Sum using the covariant generic sum type paern.

sealed trait Sum[+A, +B]
final case class Failure[A](value: A) extends Sum[A, Nothing]
final case class Success[B](value: B) extends Sum[Nothing, B]


//5.6.4.2 Exercise: Some sort of flatMap

sealed trait Sum[+A, +B] {
  def flatMap[C](f: B => Sum[A, C]): Sum[A, C] =
    this match {
      case Failure(v) => Failure(v)
      case Success(v) => f(v)
    }
}
final case class Failure[A](value: A) extends Sum[A, Nothing]
final case class Success[B](value: B) extends Sum[Nothing, B]

//error: covariant type A occurs in contravariant position in type B =>
//Sum[A,C] of value f
//def flatMap[C](f: B => Sum[A, C]): Sum[A, C] =


//Problem ne?

//Basit örnek: Box
case class Box[+A](value: A) {
  def set(a: A): Box[A] = Box(a)
}

//Bu neden hata verir?
//Hata: covariant type A occurs in contravariant position


//Sebep Fonksiyonlar (ve methodlar) parametrelerinde contravariant’tır

/*
Durum analizi
A → covariant olarak tanımlandı
ama set(a: A) içinde:
A parametre olarak kullanılıyor
yani contravariant position’da
Çelişki
A covariant
ama kullanıldığı yer contravariant

Bu yüzden hata oluşur.

“Contravariant position” ne demek?


Compiler’ın dediği “contravariant position” tam olarak budur
 */

//Çözüm
//A’nın supertype’ını alan yeni bir type tanımla

case class Box[+A](value: A) {
  def set[AA >: A](a: AA): Box[AA] = Box(a)
}
//Bu kod artık compile olur


/*
FlatMap’e geri dönüş

şimdi tekrar flatMap problemine dönüyor.

Durum
f bir parametredir
bu yüzden contravariant position’dadır
f’nin tipi
B => Sum[A, C]
Supertype analizi

f’nin supertype’ı:

B’de covariant
A ve C’de contravariant
Durum kontrolü
B → covariant → problem yok
C → invariant → problem yok
A → covariant → ama contravariant position’da → problem var
Çözüm

Box örneğindeki çözümü burada da uygula
 */

sealed trait Sum[+A, +B] {
  def flatMap[AA >: A, C](f: B => Sum[AA, C]): Sum[AA, C] =
    this match {
      case Failure(v) => Failure(v)
      case Success(v) => f(v)
    }
}
final case class Failure[A](value: A) extends Sum[A, Nothing]
final case class Success[B](value: B) extends Sum[Nothing, B]


/*
Contravariant Posiঞon Paern
If A of a covariant type T and a method f of A complains that T is used
in a contravariant posiঞon, introduce a type TT >: T in f.
case class A[+T]() {
def f[TT >: T](t: TT): A[TT] = ???
}

 */


//5.6.5 Type Bounds

//Az önce contravariant position pattern’de type bounds kullandık
//
//Type bounds nedir?

//Type bounds, hem subtype hem supertype kısıtlamaları belirlemeyi sağlar

//Syntax
//Subtype bound
//  A <: Type
//
//Anlamı:
//
//  A, Type’ın subtype’ı olmalıdır


//Supertype bound
//  A >: Type
//
//Anlamı:
//
//  A, Type’ın supertype’ı olmalıdır

//case class WebAnalytics[A <: Visitor]
//(visitor: A, pageViews: Int, searchTerms: List[String], isOrganic:Boolean)

//Visitor ya da onun herhangi bir subtype’ını saklayabilir




//7 Type classes

//Type class, Scala’da mevcut bir sınıfa (class) yeni özellikler eklemeyi sağlar
//inheritance kullanmadan
//orijinal kodu değiştirmeden

//Kütüphane senin değil
//Ama o tipe yeni davranış ekliyorsun

//| Yaklaşım   | Yeni Method Eklemek | Yeni Veri Eklemek  |
//| ---------- | ------------------- | ------------------ |
//| OO         | Mevcut kod değişir  | Değişmez           |
//| FP         | Değişmez            | Mevcut kod değişir |
//| Type Class | İkisi de mümkün     | İkisi de mümkün    |


//Type class en esnek çözüm

//Type class aslında bir trait ama triat'i class'ın içine yazmıyoruz dışardan bağlıyoruz

//Trait → class’ın içinde
//Type class → class’tan bağımsız


//Temel fikir:
//Davranış (implementation) ile veri (type) ayrılır
//Yani:
//Veri: A
//Davranış: TypeClass[A]


//7.1 Type Class Instances

//Bir type class’ın gerçek implementasyonu
//Örnek:
//Ordering[Int]
//Bu: Int’leri nasıl karşılaştıracağımızı belirler


//7.1.1 Ordering

//Ordering Nedir?
//Scala’da hazır gelen bir type class
//Amaç:
//→ İki şeyi karşılaştırmak

//compare(a, b)

import scala.math.Ordering

val minOrdering = Ordering.fromLessThan[Int](_ < _)
// minOrdering: scala.math.Ordering[Int] = scala.math.Ordering$$anon$9 @6b14fb5c

val maxOrdering = Ordering.fromLessThan[Int](_ > _)
// maxOrdering: scala.math.Ordering[Int] = scala.math.Ordering$$anon$9 @3924e0f4

List(3, 4, 2).sorted(minOrdering)
// res0: List[Int] = List(2, 3, 4)

List(3, 4, 2).sorted(maxOrdering)
// res1: List[Int] = List(4, 3, 2)

//sorted metodu çalışmak için bir Ordering ister


//Type class pattern şunu yapar:
//
//Davranışı (Ordering) ayrı tutar
//Veriden (Int) bağımsız yapar
//
// Bu şu demek: Aynı tipe farklı davranışlar ekleyebiliriz


//7.1.2 Implicit Values

//Her seferinde şunu yazmak zor: .sorted(minOrdering)

//It can be inconvenient to conঞnually pass the type class instance to a method
//when we want to repeatedly use the same instance. Scala provides a convenience, called an implicit value, that allows us to get the compiler to pass the
//type class instance for us. Here’s an example of use:


implicit val ordering = Ordering.fromLessThan[Int](_ < _)

List(2, 4, 3).sorted
// res2: List[Int] = List(2, 3, 4)
List(1, 7 ,5).sorted
// res3: List[Int] = List(1, 5, 7)

//Bu Nasıl Çalışıyor?
//Compiler şunu yapıyor:
//Method implicit param istiyor mu?
//Scope’ta uygun implicit var mı?
//Varsa → otomatik veriyor


//sorted aslında şöyle:
//def sorted(implicit ord: Ordering[A])
// Bu yüzden implicit çalışıyor


//7.1.3 Declaring Implicit Values

//We can tag any val, var, object or zero-argument def with the implicit
//keyword, making it a potenঞal candidate for an implicit parameter.
//
//implicit val exampleOne = ...
//implicit var exampleTwo = ...
//implicit object exampleThree = ...
//implicit def exampleFour = ...

//An implicit value must be declared within a surrounding object, class, or trait.

//7.1.4 Implicit Value Ambiguity

//What happens when mulঞple implicit values are in scope? Let’s ask the console.


implicit val minOrdering = Ordering.fromLessThan[Int](_ < _)
implicit val maxOrdering = Ordering.fromLessThan[Int](_ > _)
//List(3,4,5).sorted
// <console>:17: error: ambiguous implicit values:
// both value ordering of type => scala.math.Ordering[Int]
// and value minOrdering of type => scala.math.Ordering[Int]
// match expected type scala.math.Ordering[Int]
// List(3,4,5).sorted
// ^
// <console>:12: error: ambiguous implicit values:
// both value ordering of type => scala.math.Ordering[Int]
// and value minOrdering of type => scala.math.Ordering[Int]
// match expected type scala.math.Ordering[Int]
// List(3,4,5).sorted

//The rule is simple: the compiler will signal an error if there is any ambiguity in
//which implicit value should be used. aynı tipten sadece 1 impliccit olmalı




//7.1.6 Exercises
//  7.1.6.1 More Orderings
//Define an Ordering that orders Ints from lowest to highest by absolute value.
//  The following test cases should pass.


val absOrdering = Ordering.fromLessThan[Int]{ (x, y) =>
  Math.abs(x) < Math.abs(y)
}

assert(List(-4, -1, 0, 2, 3).sorted(absOrdering) == List(0, -1, 2, 3,
  -4))
assert(List(-4, -3, -2, -1).sorted(absOrdering) == List(-1, -2, -3,
  -4))



//Now make your ordering an implicit value, so the following test cases work.
implicit val absOrdering = Ordering.fromLessThan[Int]{ (x, y) =>
  Math.abs(x) < Math.abs(y)
}
assert(List(-4, -1, 0, 2, 3).sorted == List(0, -1, 2, 3, -4))
assert(List(-4, -3, -2, -1).sorted == List(-1, -2, -3, -4))





//7.1.6.2 Raঞonal Orderings
//Scala doesn’t have a class to represent raঞonal numbers, but we can easily
//implement one ourselves.
final case class Rational(numerator: Int, denominator: Int)
//Implement an Ordering for Rational to order raঞonals from smallest to
//largest. The following test case should pass.
//assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted ==
//List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))

implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
  (x.numerator.toDouble / x.denominator.toDouble) <
    (y.numerator.toDouble / y.denominator.toDouble)
)
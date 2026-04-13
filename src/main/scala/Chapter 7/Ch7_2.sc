//7.2 Organising Type Class Instances

//Önceki kısımda type class instance yazmayı gördük. Mesela Ordering[Int], Ordering[Rational] gibi. Ama burada artık şu pratik soruya geçiliyor:
//
//Bu implicit instance’ları nereye yazacağız?
//Her implicit’i local scope’a mı koyacağız?
//Companion object’e mi koyacağız?
//Ayrı bir object içine mi koyacağız?
//Compiler bunları ararken hangi yerlere bakıyor?


//7.2.1 Implicit Scope

//Compiler bir metoda implicit parametre vermeye çalışırken rastgele her yere bakmaz.
// Baktığı yerlerin belirli bir kümesi vardır. Bu kümeye: implicit scope


//compiler, bir implicit parametreyi doldurmak istediğinde implicit scope içinde arama yapar ve bu implicit scope
// birkaç parçadan oluşur; ayrıca bu parçaların bazıları diğerlerinden daha yüksek önceliğe sahiptir.
//Yani burada iki fikir var:
//
//Compiler her implicit’i her yerden bulmaz
//Bulduğu adaylar arasında öncelik kuralları vardır


//Implicir scope'ın ilk parçası local scope

//implicit scope’un ilk parçası, normal identifier’ları bulduğumuz alandır. Yani normalde bir
// isim çözümlemesi yaparken nerelere bakıyorsak, implicit için de ilk bakılan yerlerden biri orasıdır. Buna şunlar dahil:
//local scope içinde tanımlanan isimler
//enclosing class/object/trait içinde tanımlananlar
//import ile içeri alınanlar


//mplicit scope’un ikinci parçası: companion objects
//Implicit scope sadece local scope’tan ibaret değil. Compiler ayrıca, implicit parametreyle ilgili türlerin companion object’lerine de bakar.
// Bu, Scala type class sisteminin en güçlü yanlarından biridir. Çünkü kullanıcı ayrıca import yapmadan da bir instance bulunabilir.


def sorted[B >: A](implicit ord: math.Ordering[B]): List[A]

//Burada compiler hangi companion object’lere bakar?

//Ordering’in companion object’i
//B tipinin companion object’i (A ya da onun supertype’ı olabilir)
//Yani burada aranan implicit Ordering[B] olduğu için, compiler hem
// type class’ın kendisinin companion object’ine hem de type parameter’ın companion object’ine bakar


//Bu kuralın pratik sonucu
//Kendi tiplerimizin companion object’inde type class instance tanımlayabiliriz ve compiler bunları kullanıcı ayrıca import etmeden bulur.


//In the previous secঞon we defined an Ordering for a Rational type we created. Let’s see how we can use the companion object to make this Ordering
//easier to use.
//First let’s define the ordering in the local scope.

final case class Rational(numerator: Int, denominator: Int)

//Bu tip için bir Ordering tanımlanacak.

//Önce local scope içinde ordering tanımlanıyor
//İlk örnekte ordering, Example.example() metodunun içindeki local scope’a yazılıyor:

object Example {
  def example() = {
    implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
      (x.numerator.toDouble / x.denominator.toDouble) <
        (y.numerator.toDouble / y.denominator.toDouble)
    )
    assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
      ==
      List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
  }
}
// Bu beklenildiği gibi çalışıyor çünkü
//sorted bir implicit Ordering istiyor
//local scope içinde implicit val ordering var
//tipi Ordering[Rational]
//compiler onu buluyor
//sıralama gerçekleşiyor
//Bu tamamen local scope üzerinden çözülüyor. Burada companion object’e gitmeye bile gerek yok. Çünkü elinin altında zaten uygun bir implicit var.



//Sonra ordering local scope’tan çıkarılıyor
//Şimdi metin aynı instance’ı alıp ayrı bir object içine taşıyor:

final case class Rational(numerator: Int, denominator: Int)
object Instance {
  implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) <
      (y.numerator.toDouble / y.denominator.toDouble)
  )
}


object Example {
  def example =
    assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
      ==
      List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
}
//// <console>:16: error: No implicit Ordering defined for Rational.
//// assert(List(Rational(1, 2), Rational(3, 4), Rational(1,
//3)).sorted ==
////

//sonuç compile error

//Instance.ordering diye bir implicit var
//ama o implicit local scope’ta değil
//import edilmemiş
//Rational companion object’inde de değil
//compiler implicit scope içinde onu göremiyor
//Yani “bir yerde implicit tanımlı olması” yetmez; compiler’ın baktığı yerlerde olması gerekir.


//Finally let’s move the type class instance into the companion object of
//Rational and see that the code compiles again.

final case class Rational(numerator: Int, denominator: Int)
object Rational {
  implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) <
      (y.numerator.toDouble / y.denominator.toDouble)
  )
}


Object Example {
  def example() =
    assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
      ==
      List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
}

// bu sefer kod derleniyor ve çalışıyor

//Çünkü sorted, Ordering[Rational] arıyor ve compiler implicit scope içinde
// Rational companion object’ine bakıyor. Orada da implicit val ordering var. Dolayısıyla artık import gerekmeden bulunuyo


/*
Type Class Instance Packaging: Companion Objects
When defining a type class instance, if
1. there is a single instance for the type; and
2. you can edit the code for the type that you are defining the instance for
then define the type class instance in the companion object of the type.

 */


//7.2.2 Implicit Priority

//Tamam, compiler implicit’i nerede arıyor anladık. Peki birden fazla yerde bulursa ne yapıyor?”
//İşte bu kısım priority yani öncelik kuralları.


//Ordering companion object’inde zaten bazı hazır instance’lar var. Özellikle Int için hazır bir Ordering[Int] var.
// Buna rağmen önceki bölümde biz kendi Ordering[Int] değerlerimizi tanımlayabildik ve her zaman ambiguity çıkmadı.
// Bunun sebebini anlamak için öncelik kurallarını bilmek gerekiyor.


//Ambiguity error sadece aynı öncelik düzeyinde birden fazla uygun implicit varsa ortaya çıkar.
// Aksi halde en yüksek öncelikli implicit seçilir.


//Pratikte en önemli priority kuralı
// tam kurallar aslında oldukça karmaşık, ama çoğu durumda bizi ilgilendiren pratik sonuç şudur:
//local scope, companion object’te bulunan instance’lardan daha yüksek önceliğe sahiptir.

//Demek ki:
//local scope’ta bir implicit varsa
//companion object’te de varsa
//compiler local scope’takini seçer


//Let’s see this in pracঞce, by defining an Ordering for Rational within the
//local scope.


final case class Rational(numerator: Int, denominator: Int)
object Rational {
  implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) <
      (y.numerator.toDouble / y.denominator.toDouble)
  )
}

//Bu, küçükten büyüğe sıralama yapıyor.
//Sonra Example içinde başka bir implicit tanımlanıyor:

object Example {
  implicit val higherPriorityImplicit = Ordering.fromLessThan[Rational
  ]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) >
      (y.numerator.toDouble / y.denominator.toDouble)
  )
  def example() =
    assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
      ==
      List(Rational(3, 4), Rational(1, 2), Rational(1, 3)))
}
//Bu ise tam tersini yapıyor:
//
//  büyükten küçüğe sıralıyor
//
//Sonraki testte beklenen sıra da buna göre değiştiriliyor:


/*
higherPriorityImplicit, companion object’teki ordering’den farklı bir sıralama tanımlar
buna rağmen kod hem derlenir hem çalışır
bu da priority kurallarının etkisini gösterir

Sebep net:
companion object’te default ordering var
local scope’ta başka ordering var
local scope daha yüksek öncelikli
compiler local scope’takini seçiyor

Dolayısıyla companion object’teki yok sayılmıyor, ama bu kullanımda bastırılmış oluyor.
 */


/*
Type Class Instance Packaging: Companion Objects Part 2
When defining a type class instance, if
1. there is a single good default instance for the type; and
2. you can edit the code for the type that you are defining the instance for

then define the type class instance in the companion object of the type. This
allows users to override the instance by defining one in the local scope
whilst sঞll providing sensible default behaviour.
 */


//7.2.3 Packaging Implicit Values Without Companion Objects

//ğer iyi bir default instance yoksa
//ya da birden fazla iyi default varsa
//o zaman instance’ları companion object’e koymamalıyız. Bunun yerine kullanıcıdan açıkça bir instance’ı local scope’a import etmesini istemeliyiz


//Çünkü bazı tipler için tek bir “doğal” sıralama yoktur.
//
//Mesela Rational için düşünebiliriz:
//
//küçükten büyüğe sıralama mı default?
//büyükten küçüğe mi?
//mutlak değere göre mi?
//paydaya göre mi?
//sadeleştirilmiş değere göre mi?
//
//Eğer tek bir açık, doğal, evrensel doğru yoksa companion object’e bir tanesini gömmek iyi tasarım olmayabilir.


//Her implicit instance’ı kendi object’i içine koy.

final case class Rational(numerator: Int, denominator: Int)
object RationalLessThanOrdering {
  implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) <
      (y.numerator.toDouble / y.denominator.toDouble)
  )
}
object RationalGreaterThanOrdering {
  implicit val ordering = Ordering.fromLessThan[Rational]((x, y) =>
    (x.numerator.toDouble / x.denominator.toDouble) >
      (y.numerator.toDouble / y.denominator.toDouble)
  )
}

//Sonra kullanıcı ihtiyacına göre şunu yapar:
//
//import RationalLessThanOrdering._
//veya
//
//import RationalGreaterThanOrdering.



//7.2.5 Exercises
//7.2.5.1 Ordering Orders
//Here is a case class to store orders of some arbitrary item.
//final case class Order(units: Int, unitPrice: Double) {
//val totalPrice: Double = units * unitPrice
//}
//We have a requirement to order Orders in three different ways:
//1. by totalPrice;
//2. by number of units; and
//3. by unitPrice.
//Implement and package implicits to provide these orderings, and jusঞfy your
//packaging.


final case class Order(units: Int, unitPrice: Double) {
  val totalPrice: Double = units * unitPrice
}
object Order {
  implicit val lessThanOrdering = Ordering.fromLessThan[Order]{ (x, y)
  =>
    x.totalPrice < y.totalPrice
  }
}
object OrderUnitPriceOrdering {
  implicit val unitPriceOrdering = Ordering.fromLessThan[Order]{ (x, y
                                                                 ) =>
    x.unitPrice < y.unitPrice
  }
}
object OrderUnitsOrdering {
  implicit val unitsOrdering = Ordering.fromLessThan[Order]{ (x, y) =>
    x.units < y.units
  }
}
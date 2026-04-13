//7.8 Implicit Conversions

//Şu ana kadar iki şey gördük:
//Type enrichment → implicit class
//Type class → implicit val + implicit param


//Implicit conversion nedir?
// Bir tipi otomatik olarak başka bir tipe çeviren implicit fonksiyon

//Implicit conversions are a more general form of implicit classes. We can tag any
//single-argument method with the implicit keyword to allow the compiler to
//implicitly use the method to perform automated conversions from one type
//to another:


class B {
  def bar = "This is the best method ever!"
}
class A
implicit def aToB(in: A): B = new B()
new A().bar
// res2: String = This is the best method ever!


//Normalde: A class’ında bar yok → hata
//Ama compiler şunu yapar:
//A’da bar yok → hata gördü
//implicit conversion aradı
//A → B dönüşümünü buldu
//şunu yazdı:
//aToB(new A()).bar


//Implicit class ile farkı
// implicit class aslında bunun “kısayolu”

//Implicit class
//implicit class AOps(a: A) {
//  def bar = ...
//}
//Aslında arka planda:
//implicit def aToAOps(a: A): AOps = new AOps(a)

//implicit class = wrapper + conversion
//implicit conversion = direkt dönüşüm


//import scala.language.implicitConversions bu yazılmadan çalışamaz


//7.8.2 Designing with Implicit Conversions

//The power of implicit conversions tends to cause problems for newer Scala developers.
// We can easily define very general type conversions that play strange
//games with the semanঞcs of our programs:

implicit def intToBoolean(int: Int) = int == 0
if(1) "yes" else "no"
// res3: String = no
if(0) "yes" else "no"
// res4: String = yes


//1- Type class ve enrichment kullan
//
//mümkünse implicit conversion kullanma
//
// 2. implicit class / val / param tercih et
//
// conversion yerine daha kontrollü mekanizmalar
//
//3. Implicit’leri düzgün package et
//
// her yerde açık olmasın
// sadece gerektiği yerde import et
//
//✔ 4. Genel tip dönüşümlerinden kaçın
//Int → Boolean
//Any → String
//
//iyi:
//
//spesifik dönüşümler
//dar kapsam


//| Özellik     | Implicit Class | Implicit Conversion |
//| ----------- | -------------- | ------------------- |
//| Güvenli     | ✔              | ❌                   |
//| Kontrollü   | ✔              | ❌                   |
//| Debug kolay | ✔              | ❌                   |
//| Önerilen    | ✔              | ❌                   |




//7.8.3 Exercises
//7.8.3.1 Implicit Class Conversion
//Any implicit class can be reimplemented as a class paired with an implicit
//method. Re-implement the IntOps class from the type enrichment secঞon
//in this way. Verify that the class sঞll works the same way as it did before.

object IntImplicits {
  class IntOps(n: Int) {
    def yeah() =
      times(_ => println("Oh yeah!"))
    def times(func: Int => Unit) =
      for(i <- 0 until n) func(i)
  }
  implicit def intToIntOps(value: Int) =
    new IntOps(value)
}


//The code sঞll works the same way it did previously. The implicit conversion is
//  not available unঞl we bring it into scope:
  5.yeah()
// <console>:18: error: value yeah is not a member of Int
// 5.yeah()
// ^
//Once the conversion has been brought into scope, we can use yeah and times
//  as usual:

import IntImplicits._
5.yeah()
// Oh yeah!
// Oh yeah!
// Oh yeah!
// Oh yeah!
// Oh yeah!
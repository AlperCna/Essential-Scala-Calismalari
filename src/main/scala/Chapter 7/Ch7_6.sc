//7.6 Combining Type Classes and Type Enrichment

//Implicit class tek başına kullanılabilir ama genelde type class ile birlikte kullanılır.

//Neden birlikte kullanıyoruz?
//
//Şu iki şeyi aynı anda istiyoruz:
//1. Type class avantajları
//Aynı tipe farklı davranış
//Dış kütüphane tiplerine destek
//Esneklik

//2. OOP gibi kullanım
//p.toHtml gibi doğal syntax


//implicit class HtmlOps[T](data: T) {
//  def toHtml(implicit writer: HtmlWriter[T]) =
//    writer.toHtml(data)
//}
//
////This allows us to invoke our type-class paern on any type for which we have
////an adapter as if it were a built-in feature of the class:
//
//
//new ExtraStringMethods("the quick brown fox").numberOfVowels

//This gives us many benefits. We can extend exisঞng types to give them new
//funcঞonality, use simple syntax to invoke the funcঞonality, and choose our
//preferred implementaঞon by controlling which implicits we have in scope.



//7.6.2 Exercises
//7.6.2.1 Drinking the Kool Aid
//Use your newfound powers to add a method yeah to Int, which prints Oh
//yeah! as many ঞmes as the Int on which it is called if the Int is posiঞve, and
//is silent otherwise. Here’s an example of usage:
//2.yeah()
//3.yeah()
//-1.yeah()
//When you have wrien your implicit class, package it in an IntImplicits
//object.


implicit class IntOps(n: Int) {
  def yeah() = for{ _ <- 0 until n } println("Oh yeah!")
}
2.yeah()
// Oh yeah!
// Oh yeah!



//7.6.2.2 Times
//Extend your previous example to give Int an extra method called times that
//accepts a funcঞon of type Int => Unit as an argument and executes it n
//ঞmes. Example usage:
//3.times(i => println(s"Look - it's the number $i!"))
//For bonus points, re-implement yeah in terms of times.


object IntImplicits {
  implicit class IntOps(n: Int) {
    def yeah() =
      times(_ => println("Oh yeah!"))
    def times(func: Int => Unit) =
      for(i <- 0 until n) func(i)
  }
}




//7.6.3 Easy Equality
//Recall our Equal type class from a previous secঞon.
trait Equal[A] {
def equal(v1: A, v2: A): Boolean
}
//Implement an enrichment so we can use this type class via a triple equal (===)
//method. For example, if the correct implicits are in scope the following should
//work.
//"abcd".===("ABCD") // Assumes case-insensitive equality implicit


trait Equal[A] {
  def equal(v1: A, v2: A): Boolean
}
object Equal {
  def apply[A](implicit instance: Equal[A]): Equal[A] =
    instance
  implicit class ToEqual[A](in: A) {
    def ===(other: A)(implicit equal: Equal[A]): Boolean =
      equal.equal(in, other)
  }
}


implicit val caseInsensitiveEquals = new Equal[String] {
  def equal(s1: String, s2: String) =
    s1.toLowerCase == s2.toLowerCase
}
import Equal._
"foo".===("FOO")
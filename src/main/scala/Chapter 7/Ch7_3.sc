import java.util.Date
//7.3 Creating  Type classes

//Önceki bölümlerde:
//type class instance yazmayı gördük
//implicits ile onları kullanmayı gördük
//instance’ları nasıl organize edeceğimizi gördük
//Ama bütün bunlarda hazır bir type class kullanıyorduk, özellikle Ordering.

//7.3.1 Elements of Type Classes

//There are four components of the type class paern:
//• the actual type class itself;
//• the type class instances;
//• interfaces using implicit parameters; and
//• interfaces using enrichment and implicit parameters.
//We have already seen type class instances and talked briefly about implicit parameters. Here we will look at defining our own type class, and in the following
//secঞon we will look at the two styles of interface.


//7.3.2 Creaঞng a Type Class

//Let’s start with an example—converঞng data to HTML. This is a fundamental
//operaঞon in any web applicaঞon, and it would be great to be able to provide
//a toHtml method across the board in our applicaঞon.
//One implementaঞon strategy is to create a trait we extend wherever we want
//this funcঞonality:

trait HtmlWriteable {
  def toHtml: String
}

//Burada bir trait var. “HTML’e çevrilebilir” anlamında düşünülmüş.
//Sonra Person bu trait’i extend ediyor:

final case class Person(name: String, email: String) extends HtmlWriteable {
  def toHtml = s"<span>$name &lt;$email&gt;</span>"
}

//Ve sonra kullanım geliyor:

Person("John", "john@example.com").toHtml
// res1: String = <span>John &lt;john@example.com&gt;</span>


//This soluঞon has a number of drawbacks. First, we are restricted to having
//just one way of rendering a Person. If we want to list people on our company
//homepage, for example, it is unlikely we will want to list everybody’s email
//addresses without obfuscaঞon.


// Second, this paern can only be
//applied to classes that we have wrien ourselves. If we want to render a
//java.util.Date to HTML, for example, we will have to write some other
//form of library funcঞon.

//İkinci yaklaşım: pattern matching ile merkezi bir render fonksiyonu


object HtmlWriter {
  def write(in: Any): String =
    in match {
      case Person(name, email) => ???
      case d: Date => ???
      case _ => throw new Exception(s"Can't render ${in} to HTML")
    }
}

//Bunda da bir takım problemle rvar

//1 We have lost type safety because
//there is no useful supertype that covers just the elements we want to render
//and no more.

//2 We can’t have more than one implementaঞon of rendering for
//a given type.

//3 We also have to modify this code whenever we want to render
////a new type.


//bu sorunların hepsini HTML render işlemini bir adapter sınıfa taşıyarak aşabiliriz.

trait HtmlWriter[A] {
  def write(in: A): String
}

//bu trait bizim type class’ımız oluyor.
//
//Buradaki en önemli nokta:
//trait generic
//yani A diye bir type parameter alıyor
//
//Bu ne demek?  HtmlWriter[A] demek: “A tipini HTML’e dönüştürebilen bir yazıcı”

object PersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} &lt;${person.email}&gt;</span>"
}

//PersonWriter, Person tipini HTML’e çevirebilir

PersonWriter.write(Person("John", "john@example.com"))
// res3: String = <span>John &lt;john@example.com&gt;</span>

//Bu yapı inheritance’a göre çok daha esnek.
// Çünkü Person sınıfının içine hiçbir şey koymak zorunda değiliz. Davranış dışarıda.


//Sahip olmadığımız tipler için de instance yazabiliyoruz

import java.util.Date
object DateWriter extends HtmlWriter[Date] {
  def write(in: Date) = s"<span>${in.toString}</span>"
}
DateWriter.write(new Date)
// res5: String = <span>Mon Jul 06 10:52:14 UTC 2020</span>


//Aynı tipe birden fazla implementasyon verebiliyoruz


object ObfuscatedPersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} (${person.email.replaceAll("@", " at ")})</span>"
}
ObfuscatedPersonWriter.write(Person("John", "john@example.com"))
// res6: String = <span>John (john at example.com)</span>




/*
Type Class Paern
A type class is a trait with at least one type variable. The type variables specify the concrete types the type class instances are defined
for. Methods in the trait usually use the type variables.
trait ExampleTypeClass[A] {
def doSomething(in: A): Foo
}

 */

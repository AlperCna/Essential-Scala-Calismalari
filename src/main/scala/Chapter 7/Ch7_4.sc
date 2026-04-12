//7.4 Implicit Parameter and Interfaces

//Type class yazmayı öğrendik (HtmlWriter[A])
//Instance yazmayı öğrendik (PersonWriter)
//Packaging öğrendik
//
//Ama şu problem var:
//
//Kullanım hâlâ çok zahmetli
case class Person(name:String ,email:String)
trait HtmlWriter[A] {
  def write(in: A): String
}
object PersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} &lt;${person.email}&gt;</span>"
}


//Eğer büyük bir veri yapısını render ediyorsak:
//her yerde PersonWriter
//her yerde DateWriter
//her yerde ObfuscatedWriter
//taşımak zorundayız
//
// Bu yönetilemez hale gelir

//7.4.1 Implicit Parameter Lists

//Type class instance’ları elle taşımak yerine compiler’a taşıt

object HtmlUtil {
  def htmlify[A](data: A)(implicit writer: HtmlWriter[A]): String = {
    writer.write(data)
  }
}

//ki parametre alıyor:
//data: A
//writer: HtmlWriter[A] (implicit)

//(implicit writer: HtmlWriter[A])
// implicit tek parametreye değil, tüm listeye uygulanır

//The implicit keyword applies to the whole parameter list, not just an individual parameter. This makes the parameter list opঞonal—when we call
//HtmlUtil.htmlify we can either specify the list as normal

HtmlUtil.htmlify(Person("John", "john@example.com"))(PersonWriter)
// res1: String = <span>John &lt;john@example.com&gt;</span>

//Otomatik kullanım
//  HtmlUtil.htmlify(Person("John", "john@example.com"))


//Compiler şunu yaptı:
//
//HtmlWriter[Person] lazım
//scope’a baktı
//buldu (implicit val)
//otomatik verdi


// We have already learned about implicit values, but let’s
//see a quick example to refresh our memory. First we define an implicit value.

implicit object ApproximationWriter extends HtmlWriter[Int] {
  def write(in: Int): String =
    s"It's definitely less than ${((in / 10) + 1) * 10}"
}

//When we use HtmlUtil we don’t have to specify the implicit parameter if an
//implicit value can be found.
//HtmlUtil.htmlify(2)

//Compiler bunu yapar:
//HtmlUtil.htmlify(2)(ApproximationWriter)


//7.4.2 Interfaces Using Implicit Parameters


//A complete use of the type class paern requires an interface using implicit
//parameters, along with implicit type class instances. We’ve seen two examples
//already: the sorted method using Ordering, and the htmlify method above.
//The best interface depends on the problem being solved, but there is a paern
//that occurs frequently enough that it is worth explaining here.
//In many case the interface defined by the type class is the same interface we
//want to use. This is the case for HtmlWriter – the only method of interest is
//write. We could write something like

object HtmlWriter {
  def write[A](in: A)(implicit writer: HtmlWriter[A]): String =
    writer.write(in)
}

//ereksiz “wrapper function”
//büyüdükçe karmaşık olur


//We can avoid this indirecঞon (which becomes more painful to write as our
//interfaces become larger) with the following construcঞon:


object HtmlWriter {
  def apply[A](implicit writer: HtmlWriter[A]): HtmlWriter[A] =
    writer
}


HtmlWriter[Person].write(Person("Noel", "noel@example.org"))


//HtmlWriter[Person] çağrıldı
//apply çalıştı
//implicit HtmlWriter[Person] bulundu
//geri döndü
//.write(...) çağrıldı


//Yani aslında:
//HtmlWriter[Person]
//
// şu demek:
//
//implicit HtmlWriter[Person]


/*
Type Class Interface Paern
If the desired interface to a type class TypeClass is exactly the methods
defined on the type class trait, define an interface on the companion
object using a no-argument apply method like
object TypeClass {
def apply[A](implicit instance: TypeClass[A]): TypeClass[A] =
instance
}
 */
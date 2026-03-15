//3.4 Case class

//Case class Scalada çok kullanılan bir yapıdır.
// Normal bir Class'ı companion objeyive bir çok varsayılan dafranışı tek seferde üretir
//Yani daha az kod yazma gibi avantajları vardır

case class Person(firstName:String, lastName: String) {
  def name = firstName + " " + lastName
}

//Case class tanımlayınca scala otomatk olarak Class ı oluşturur yani Person tipinde nesneler oluşturulabilir

val dave = new Person("Dave","Gurnell")

dave.name

//Ayrıca case class satesinde aynı isimli bir companion object otomatik olarak oluşuyor

Person


//3.4.1 Features of a case class

//1 Constructor parametreleri otomatik olarak field olur normalde  val ile tanımlamak gerekiyordu
//Ama case class da otomatik olarak field gibi davranıyor

dave.firstName


// 2 Otomatik olarak toString gelir normal classlarda bllek adresi gibi başka şeyler de gelebiliyordu

dave

// 3 Anlamlı equals ve hashcode methodları gelir

new Person("Noel", "Welsh").equals(new Person("Noel", "Welsh"))

new Person("Noel", "Welsh") == new Person("Noel", "Welsh")

// yani iki nesnenin fieldları aynıysa bunlar eşit kabul edilir


// 4 Otomatik olarak copy methodu gelir aynı fieldlar ve valuelara sahip yeni bir obje oluşturur

dave.copy()

// copy mevcut nesnen,n kendisini döndürmez aynı field değerlerine sahip yeni bir obje oluşturur

// ayrıca copy methodu parametre de alabilir aynı verileri opsiyonel olarak kabul eder ama yeni parametre de alabilir

dave.copy(firstName = "Dave2")

dave.copy(firstName = "Dave2").name


dave.copy(lastName = "Gurnell2")
dave.copy(lastName = "Gurnell2").name


//Scala'da == Javadaki gibi referans kıyaslaması yapmaz equals methoduna gider
// eq ise referans kimliğine gider yani aynı nesne mi aynı memory mi object mi kontrol eder

new Person("Noel", "Welsh") eq (new Person("Noel", "Welsh"))

dave eq dave

// 5 iki traiti otomatik olarak implement eder

//java.io.Serializable
//Bu trait nesnenin serialize edilebilir olmasını sağlar.
//Yani nesne bir akışa dönüştürülüp saklanabilir ya da iletilebilir.

//scala.Product
//kaç field’ı var

//case class’ın adı ne

//bileşenleri neler

//gibi bilgiler incelenebilir  genelde doprudan kullanılmaz


//3.4.2 Features of a case class companion object

//Case class  companion object de üretir

//Case class'ın companion objecti otomatik olarak constructor ile aynı parametreleri alan
//bir apply methodu içerir o yüzden direkt yazabiliriz

Person("Dave", "Gurnell")

Person.apply("Dave", "Gurnell")

//yani new yazmadan nesne oluşturabiliyoruz

Person("Dave", "Gurnell") == Person("Noel", "Welsh")

Person("Dave", "Gurnell") == Person("Dave", "Gurnell")

//Case classlar pattern matcihngde de iyi çalışır

/*
Case Class Declaraঞon Syntax
The syntax to declare a case class is
case class Name(parameter: type, ...) {
declarationOrExpression ...
}
where
• Name is the name of the case class;
• the opঞonal parameters are the names given to constructor parameters;
• the types are the types of the constructor parameters;
• the opঞonal declarationOrExpressions are declaraঞons or
expressions.
 */

//3.4.3 Case objects
//Eğer bir case classta constructor parametrasi yooksa onun yerine case object yazmak daha uygundur

case object Citizen {
  def firstName = "John"
  def lastName = "Doe"
  def name = firstName + " " + lastName
}

Citizen.toString
Citizen.name

//3.4.5 Exercises
//  3.4.5.1 Case Cats
//Recall that a Cat has a String colour and food. Define a case class to represent a Cat.

case class Cat(colour:String, food:String) {
}

val oswald = Cat("Black", "Milk")


//3.4.5.2 Roger Ebert Said it Best…
//No good movie is too long and no bad movie is short enough.
//  The same can’t always be said for code, but in this case we can get rid of a
//  lot of boilerplate by converঞng Director and Film to case classes. Do this
//conversion and work out what code we can cut.

case class Director(firstName:String, lastName:String,yearOfBirth:Int) {
  def name: String = s"$firstName $lastName"
}

object Director {
  def older(director1:Director, director2:Director):Director =
    if(director1.yearOfBirth< director2.yearOfBirth) director1 else
  director2
}

case class Film(name:String,yearOfRelease:Int,imdbRating:Double,director:Director) {
  def directorsAge = yearOfRelease- director.yearOfBirth

  def isDirectedBy(director: Director) = this.director==director

}

object Film {
  def newer(film1: Film, film2: Film): Film =

  if (film1.yearOfRelease < film2.yearOfRelease) film1 else film2
  def highestRating(film1: Film, film2: Film): Double = {
    val rating1 = film1.imdbRating
    val rating2 = film2.imdbRating
    if (rating1 > rating2) rating1 else rating2
  }
  def oldestDirectorAtTheTime(film1: Film, film2: Film): Director =
    if (film1.directorsAge > film2.directorsAge) film1.director else
      film2.director
}

//Ne kazandırdı case class kodu daha kısa yazmamıza olanak tanıdı equal metodlarını tanıdı
//copy kodunu yazmamıza gerek kalmadı otomatik veriyor
//apply yazmamıza gerek kalmadı
// toStringe gerek kalmadı yazmaya elle


//3.4.5.3 Case Class Counter
//  Reimplement Counter as a case class, using copy where appropriate. Addiঞonally iniঞalise count to a default value of 0.

case class Counter(count: Int = 0) {
  def dec = copy(count = count - 1)
  def inc = copy(count = count + 1)
}


  Counter(0)

Counter().inc

Counter().inc.dec == Counter().dec.inc


//3.4.5.4 Applicaঞon, Applicaঞon, Applicaঞon
//What happens when we define a companion object for a case class? Let’s see.
//Take our Person class from the previous secঞon and turn it into a case class
//(hint: the code is above). Make sure you sঞll have the companion object with
//the alternate apply method as well.

case class Person(firstName: String, lastName: String) {
  def name = firstName + " " + lastName
}
object Person {
  def apply(name: String): Person = {
    val parts = name.split(" ")
    apply(parts(0), parts(1))
  }
}

//apply otomatik oluşturuluyor ama override edilebilir

// 3.1 Classes

//Önceki bölümde nasıl obje oluşturacağımızı ve bunlarla methodlar ile ileticim kurmayı öğrenmiştik

//Classlar bi obje oluşturma şablonudur ve ayrıca bir type tamınlar Yani person classından oluşturularn tüm objeler pereson tipinde o lur

//3.1.1 Defining a class

class Person {
  val firstName= "Noel" //field
  val lastName = "Welsh" // field

  def name = firstName + " " + lastName

}

//Obje tanımlamada olduğu gibi class tanımlama da bir isim atanır ve bu bir expression değildir
// Ama object name de olduğu gibi class ismini bir expressionda kullanamayız yani objelerin kendi tipi ve değeri vardı ama classın yok

// Person

// Clastan bir obje oluşturmak için NEW operatörü kullanılır

val noel = new Person

noel.firstName

//Objenin tipinin Person olduğunu görüyoruz bu kimlik de objenin memory adresini temsil eden benzersiz bir id dir

noel

val newNoel = new Person

val anotherNewNoel= new Person

//yani heer seferinde yeni bir obje oluşur

//This means we can write a method that takes any Person as a parameter:
object alien {
  def greet(p: Person) =
    "Greetings, " + p.firstName + " " + p.lastName
}

//Herhangi bir person objecti kabul eder

alien.greet(noel)
// Noel objesi



//3.1.2 Constructors

//Problemimiz şu bu class yapımızda oluşturulan tüö personlar Noal Welsh olur ve bu kullanışlı değildir
//farklı farklı isimler tanımlamak istediğimizde uygun olmaz bunun için Constructor k ullanırız

//Constructor obje oluştururken parametre vermemizi sağlar

class Person2(first: String, last :String) {
  val firstName = first
  val lastName = last
  def name = first + " " + last
}

val alper = new Person2("Alper" , "Can")

alper.name


//Parametre olan first ve last sadece class'ın gövdesinde kullanılabilir bu verilere ulaşmak  için
// bir field veya bir method kullanmalıyız objenin dışından ulaşmak için

//Ama scala constructor parametrelerinden otomatik field oluşturabilir yani tekrar tanımlamaya gerek kalmaz

class Person3(val firstName: String, val lastName: String) {
  def name = firstName + " " + lastName
}

new Person3("Dave", "Gurnell").firstName

//Bu yazım kolaylığı sağlar (Kendine not peki şöyle yapılabilir mi)

val eren = new Person3("Eren", "Özel")

eren.name

//yapılabilirmiş.


//Scalada genellikle val kullanılır var kullanılmaz val değiştirelemez

/*

Class Declaraঞon Syntax

The syntax for declaring a class is
class Name(parameter: type, ...) {
declarationOrExpression ...
}

or
class Name(val parameter: type, ...) {
declarationOrExpression ...
}

where
• Name is the name of the class;
• the opঞonal parameters are the names given to constructor parameters;
• the types are the types of the constructor parameters;
• the opঞonal declarationOrExpressions are declaraঞons or
expressions.
 */


//3.1.3 Default and Keyword Parameters

//Scala'da method ve constructor keyword parametrelerini ve default valueları destekler

//Keyword paraöetrelerde paremetre sırası önemli değildir

new Person3(lastName = "Last", firstName = "First")

//Default parametreler parametreleredefault parametre verilebilir

def greet(firstName: String = "Some", lastName: String = "Body") =
  "Greetings, " + firstName + " " + lastName + "!"

greet("Busy")

//keywordları default parametrelerle birleştirmek önceki parametreleri atlamamıza ve yalnızca sonraki parametreler için değer sağlamamıza olanak tanır.

greet(lastName = "Dave")

/*
Keyword Parameters
Keyword parameters are robust to changes in the number and order
of parameters. For example, if we add a title parameter to the
greet method, the meaning of keywordless method calls changes but

keyworded calls remain the same:

def greet(title: String = "Citizen", firstName: String = "Some",
lastName: String = "Body") =
"Greetings, " + title + " " + firstName + " " + lastName + "!"

greet("Busy") // this is now incorrect
// res10: String = Greetings, Busy Some Body!

greet(firstName = "Busy") // this is still correct
// res11: String = Greetings, Citizen Busy Body!
This is parঞcularly useful when creaঞng methods and constructors with
a large number of parameters.
 */


//3.1.4 Scala's Type Hiearcy
//Scalada her şey object'dir

//Scala'da En üst type Any'dir any ise 2 alt türe ayrılır AnyVal ve AnyRef
//AnyVal Primitive typeları içerir Int, Double, Boolean, Float gibi
//AnyRef ise Reference typelardır classes,objects,ssstring

//Hiearcy'nin en dibinde 2 tane özel tip vardır Nothing ve Null
// Nothing throw expressionlarını belirtir ve null ise null değerini belirtir

def badness = throw new Exception("Error")
// badness: Nothing
def otherbadness = null
// otherbadness: Null
val bar = if(true) 123 else badness
// bar: Int = 123
val baz = if(false) "it worked" else otherbadness
// baz: String = null


/*
3.1.5 Take Home Points

Class:object üretmek için template’tir.

Class syntax
class Name(parameters) {
}
Object oluşturma
new ClassName(...)

Fields Object içinde saklanan değerler.

Methods Object üzerinde çalışan hesaplamalar.

Keyword parameters Parametre isimleriyle çağırma.

Default parameters Varsayılan parametre değerleri.

Scala Type HierarchyEn üst type:Any

Alt tipler:

AnyVal
AnyRef
 */

//3.1.6 Exercises

//3.1.6.1 Cats, Again
//Recall the cats from a previous exercise:
//
//Name Colour Food
//Oswald Black Milk
//Henderson Ginger Chips
//Quenঞn Tabby and white Curry
//Define a class Cat and then create an object for each cat in the table above.

class Cat(val name:String, val colour:String,val food:String) {
}

val Oswald= new Cat("Oswald", "Black","Milk")
val Henderson= new Cat("Henderson","Ginger","Chips")
val Quentin  = new Cat("Quentin","Tabby and white", "Curry")

//3.1.6.2 Cats on the Prowl
//Define an object ChipShop with a method willServe. This method should
//accept a Cat and return true if the cat’s favourite food is chips, and false
//otherwise.

object ChipShop {
  def willServe(cat: Cat): Boolean  =
    if(cat.food=="Chips")
      true
    else
      false
}

ChipShop.willServe(Henderson)


//3.1.6.3 Directorial Debut
//Write two classes, Director and Film, with fields and methods as follows:
//• Director should contain:
//– a field firstName of type String
//– a field lastName of type String
//– a field yearOfBirth of type Int
//– a method called name that accepts no parameters and returns the
//full name
//• Film should contain:
//– a field name of type String
//– a field yearOfRelease of type Int
//– a field imdbRating of type Double
//– a field director of type Director
//66 CHAPTER 3. OBJECTS AND CLASSES
//– a method directorsAge that returns the age of the director at
//the ঞme of release
//– a method isDirectedBy that accepts a Director as a parameter and returns a Boolean
//  Copy-and-paste the following demo data into your code and adjust your constructors so that the code works without modificaঞon:
//val eastwood = new Director("Clint", "Eastwood", 1930)
//val mcTiernan = new Director("John", "McTiernan", 1951)
//val nolan = new Director("Christopher", "Nolan", 1970)
//val someBody = new Director("Just", "Some Body", 1990)
//val memento = new Film("Memento", 2000, 8.5, nolan)
//val darkKnight = new Film("Dark Knight", 2008, 9.0, nolan)
//val inception = new Film("Inception", 2010, 8.8, nolan)
//val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7,
//  eastwood)
//val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9,
//  eastwood)
//val unforgiven = new Film("Unforgiven", 1992, 8.3, eastwood)
//val granTorino = new Film("Gran Torino", 2008, 8.2, eastwood)
//val invictus = new Film("Invictus", 2009, 7.4, eastwood)
//val predator = new Film("Predator", 1987, 7.9, mcTiernan)
//val dieHard = new Film("Die Hard", 1988, 8.3, mcTiernan)
//val huntForRedOctober = new Film("The Hunt for Red October", 1990,
//  7.6, mcTiernan)
//val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8,
//  mcTiernan)
//eastwood.yearOfBirth
//// res16: Int = 1930
//dieHard.director.name
//// res17: String = John McTiernan
//invictus.isDirectedBy(nolan)
//// res18: Boolean = false


//Implement a method of Film called copy. This method should accept the
//same parameters as the constructor and create a new copy of the film. Give
//each parameter a default value so you can copy a film changing any subset of
//its values:
//  highPlainsDrifter.copy(name = "L'homme des hautes plaines")
//// res19: Film = Film(L'homme des hautes plaines,1973,7.7,Director(
//Clint,Eastwood,1930))
//thomasCrownAffair.copy(yearOfRelease = 1968,
//  director = new Director("Norman", "Jewison", 1926))
//// res20: Film = Film(The Thomas Crown Affair,1968,6.8,Director(Norman
//,Jewison,1926))
//inception.copy().copy().copy()
//// res21: Film = Film(Inception,2010,8.8,Director(Christopher,Nolan
//,1970))


 class Director( val firstName: String, val lastName:String, val yearOfBirth : Int) {
   def name: String = firstName+ "" + lastName

   def copy(
             firstName: String = this.firstName,
             lastName: String = this.lastName,
             yearOfBirth: Int = this.yearOfBirth): Director =
     new Director(firstName, lastName, yearOfBirth)

 }

class Film(val name: String, val yearOfRelease: Int, val imdbRating: Double, val director: Director)
{
  def directorsAge: Int =
    yearOfRelease -director.yearOfBirth

  def isDirectedBy (director: Director) :Boolean =
    director == director

  def copy(
            name: String = this.name,
            yearOfRelease: Int = this.yearOfRelease,
            imdbRating: Double = this.imdbRating,
            director: Director = this.director): Film =
    new Film(name, yearOfRelease, imdbRating, director)

}

val eastwood = new Director("Clint", "Eastwood", 1930)
val mcTiernan = new Director("John", "McTiernan", 1951)
val nolan = new Director("Christopher", "Nolan", 1970)
val someBody = new Director("Just", "Some Body", 1990)
val memento = new Film("Memento", 2000, 8.5, nolan)
val darkKnight = new Film("Dark Knight", 2008, 9.0, nolan)
val inception = new Film("Inception", 2010, 8.8, nolan)
val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7,
  eastwood)
val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9,
  eastwood)
val unforgiven = new Film("Unforgiven", 1992, 8.3, eastwood)
val granTorino = new Film("Gran Torino", 2008, 8.2, eastwood)
val invictus = new Film("Invictus", 2009, 7.4, eastwood)
val predator = new Film("Predator", 1987, 7.9, mcTiernan)
val dieHard = new Film("Die Hard", 1988, 8.3, mcTiernan)
val huntForRedOctober = new Film("The Hunt for Red October", 1990,
  7.6, mcTiernan)
val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8,
  mcTiernan)

eastwood.yearOfBirth

dieHard.director.name


invictus.isDirectedBy(nolan)


highPlainsDrifter.copy(name = "L'homme des hautes plaines")


thomasCrownAffair.copy(yearOfRelease = 1968,
  director = new Director("Norman", "Jewison", 1926))

inception.copy().copy().copy()


//3.1.6.4 A Simple Counter
//  Implement a Counter class. The constructor should take an Int. The methods inc and dec should increment and decrement the counter respecঞvely
//  returning a new Counter. Here’s an example of the usage:
//new Counter(10).inc.dec.inc.inc.count
//// res23: Int = 12

class Counter(val count: Int) {
  def dec = new Counter(count-1)
  def inc = new Counter(count+1)

}

new Counter(10).inc.dec.inc.inc.count


//3.1.6.5 Counঞng Faster
//Augment the Counter from the previous exercise to allow the user can opঞonally pass an Int parameter to inc and dec. If the parameter is omied it
//  should default to 1

class Counter(val count: Int) {
  def dec(amount: Int = 1) = new Counter(count - amount)
  def inc(amount: Int = 1) = new Counter(count + amount)
}

new Counter(10).inc(5).count


//3.1.6.6 Addiঞonal Counঞng
//Here is a simple class called Adder.
//class Adder(amount: Int) {
//  def add(in: Int) = in + amount
//}
//Extend Counter to add a method called adjust. This method should accept
//  an Adder and return a new Counter with the result of applying the Adder to
//the count.

class Adder(amount: Int) {
  def add(in: Int) = in + amount
}

class Counter(val count: Int) {

  def inc(amount: Int = 1): Counter =
    new Counter(count + amount)

  def dec(amount: Int = 1): Counter =
    new Counter(count - amount)

  def adjust(adder: Adder): Counter =
    new Counter(adder.add(count))

}

val counter = new Counter(10)
val adder = new Adder(5)

counter.adjust(adder).count

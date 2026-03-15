import java.util.jar.Attributes.Name
//3.3 Companion Objects

//Bazen bir method mantıksal olarak bir class'a ait olur ama bbelirli bir objeye bağlı değildir
//yani bazı işlemler class ile ilgilidir ama bir instance gerektirmez
//javada bunun için sttic methodlar kullanılır

//Scala'da static keyword kullanılmaz singleton object kullanılır bu özel nesneye de companmion ombject denir
// Companion object bir class ile aynı isimde tanımlanan object dir

class Timestamp(val seconds:Long)

object Timestamp {
  def apply(hours: Int, minutes: Int, seconds: Int): Timestamp =
    new Timestamp(hours*60*60 + minutes*60 + seconds)
}

Timestamp(1,1,1).seconds
/*
Timestamp → class
Timestamp → object

Companion object içinde bir apply methodu var.
 def apply(hours: Int, minutes: Int, seconds: Int): Timestamp =
   new Timestamp(hours*60*60 + minutes*60 + seconds)

Bu method:saat + dakika + saniye bilgilerini alıp toplam saniye hesaplıyor.

Kullanım Timestamp(1,1,1).seconds
Scala çıktısı: 3661
 */

//Normalde obbje oluşturmak için new Timestamp() yazmamız gerekirdi ama companion object
// içerisindeki apply methodu sayesinde direkt yazabildik

//Companion objcet ek constructor  oluşturmak için kullanılır

//Scala programmers almost always prefer to implement additional constructors as apply methods on an object with the same name as the class

//Scala'da 2 tane farklı namespace vardır b,r type namespace bir de value namespace bu yüzden karşmaşa çıkmaz aynı isim yüzünden

//the companion object is not an instance of the class
// yani bu timestamp classının bir nesnesi değildir singleton object with its own type dır

Timestamp //note that the type is `Timestamp.type`, not `Timestamp`

//Companion Object Syntax
//To define a companion object for a class, in the same file as the class
//define an object with the same name.
//class Name {
//...
//}
//object Name {
//...
//}



//3.3.2 Exercises
//  3.3.2.1 Friendly Person Factory
//  Implement a companion object for Person containing an apply method that
//  accepts a whole name as a single string rather than individual first and last
//  names.
//    Tip: you can split a String into an Array of components as follows:
//val parts = "John Doe".split(" ")
//// parts: Array[String] = Array(John, Doe)
//parts(0)
//// res3: String = John


class Person(val firstName: String, val lastName: String) {
  def name: String =
    s"$firstName $lastName"
}


object Person {
  def apply (name:String) : Person = {
    val parts= name.split(" ")
    new Person(parts(0),parts(1))
  }
}

Person.apply("Alper Can").firstName
Person("Eren Ozel").firstName

//3.3.2.2 Extended Body of Work
//Write companion objects for Director and Film as follows:

//• the Director companion object should contain:
//– an apply method that accepts the same parameters as the constructor of the class and returns a new Director;
//– a method older that accepts two Directors and returns the
//  oldest of the two.
//• the Film companion object should contain:
//– an apply method that accepts the same parameters as the constructor of the class and returns a new Film;
//– a method highestRating that accepts two Films and returns
//the highest imdbRating of the two;
//– a method oldestDirectorAtTheTime that accepts two Films
//and returns the Director who was oldest at the respecঞve ঞme
//of filming.


class Director(val firstName: String, val lastName: String, val yearOfBirth: Int) {
  def name: String =
    s"$firstName $lastName"
  def copy(
            firstName: String = this.firstName,
            lastName: String = this.lastName,
            yearOfBirth: Int = this.yearOfBirth) =
    new Director(firstName, lastName, yearOfBirth)
}

object Director {
  def apply(firstName:String , lastName : String , yearOfBirth : Int) : Director =
    new Director(firstName, lastName, yearOfBirth)

  def older(director1: Director,director2: Director) : Director =
  if(director1.yearOfBirth< director2.yearOfBirth) director1 else director2
}


class Film(val name: String, val yearOfRelease: Int, val imdbRating: Double, val director: Director) {
  def directorsAge =
    director.yearOfBirth - yearOfRelease
  def isDirectedBy(director: Director) =
    this.director == director
  def copy(
            name: String = this.name,
            yearOfRelease: Int = this.yearOfRelease,
            imdbRating: Double = this.imdbRating,
            director: Director = this.director) =
    new Film(name, yearOfRelease, imdbRating, director)
}

object Film {
  def apply (name: String, yearOfRelease: Int, imdbRating: Double ,director: Director ) : Film =
    new Film(name,yearOfRelease, imdbRating, director)

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


//3.3.2.3 Type or Value?
//The similarity in naming of classes and companion objects tends to cause confusion for new Scala developers. When reading a block of code it is important
//to know which parts refer to a class or type and which parts refer to a singleton
//object or value.
//  This is the inspiraঞon for the new hit quiz, Type or Value?, which we will be
//  piloঞng below. In each case idenঞfy whether the word Film refers to the
//type or value:

val prestige: Film = bestFilmByChristopherNolan()

//Type çünkü değişenin tiğini yazıyor


new Film("Last Action Hero", 1993, mcTiernan)
//Type!—this is a reference to the constructor of Film. The constructor is part of
//the class Film, which is a type


Film("Last Action Hero", 1993, mcTiernan)
//Value çünkü burada çağırılan şey companion object

Film.newer(highPlainsDrifter, thomasCrownAffair)
//Value!—newer is another method defined on the singleton object Film

Film.type
//Value!—This is tricky! You’d be forgiven for geমng this one wrong.
//  Film.type refers to the type of the singleton object Film, so in this case Film
//is a reference to a value. However, the whole fragment of code is a type.






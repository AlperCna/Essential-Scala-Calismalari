//3.5 Pattern Matching

// şimdiye kadar onjelerle method çağırarak veya field okuyarak etkileşim kuulıyordu
//Case classlar ile pattern matching yapılabiliryor.

//Pattern matching verinin yapsına bakaraak kodların çalışmasını sağlayan bir mekanizmaadır
//Pattern matching is like an extended if expression.

case class Person(firstName: String, lastName: String)

Person("Luke", "Skywalker")
Person("Han", "Solo")
Person("Noel", "Welsh")
// normalde bu şekilde temsil edilebilir

//Stormtrooper örneği

object Stormtrooper {
  def inspect(person: Person):String =
    person match {
      case Person("Luke", "Skywalker") => "Stop, rebel scum!"
      case Person("Han", "Solo") => "Stop, rebel scum!"
      case Person(first, last) => s"Move along, $first"

    }
}

//person match person nesnesinin yapısını incele
//sırayla caselere bakar

Stormtrooper.inspect(Person("Noel","Welsh"))

Stormtrooper.inspect(Person("Han","Solo"))

/*
Paern Matching Syntax
The syntax of a paern matching expression is

expr0 match {
case pattern1 => expr1
case pattern2 => expr2
...
}
where
• the expression expr0 evaluates to the value we match;
• the paerns, or guards, pattern1, pattern2, and so on are
checked against this value in order; and
• the right-hand side expression (expr1, expr2, and so on) of the
first paern that matches is evaluated.
Paern matching is itself an expression and thus evaluates to a value—
the value of the matched expression.
In reality paerns are compiled to a more efficient form than a sequence of tests,
but the semanঞcs are the same.
 */

//3.5.1 Pattern Syntax

//Name Pattern herhangi bir değeri kabul eder ve değişkene bağlar
//case Person(first, last) değerleri bağla


//UnderScore Pattern = _ her şeyi kabul eder ama değeri yok sayar
//case Person(first, _)  yani firstname önemli ikindi değer önemli değil

//Literal Pattern = Belirli bir değeri eşleştirir
//case Person("Han", "Solo") sadece han solo olma durumunda çalışır

//Constructor Pattern = case class'ın yapısına göre eşleşme yapılır
//Person(first, last)

//3.5.3 Exercises
//  3.5.3.1 Feed the Cats
//  Define an object ChipShop with a method willServe. This method should
//accept a Cat and return true if the cat’s favourite food is chips, and false otherwise. Use paern matching.


case class Cat(name: String, colour: String, food: String)

object ChipShop {

  def willServe(cat: Cat): Boolean =
    cat match {

      case Cat(_, _, "Chips") => true

      case Cat(_, _, _) => false

    }

}
Cat("asd","asd","asd")

ChipShop.willServe(Cat("Alper","Can","Chips"))


//3.5.3.2 Get Off My Lawn!
//  In this exercise we’re going to write a simulator of my Dad, the movie criঞc.
//  It’s quite simple: any movie directed by Clint Eastwood gets a raঞng 10.0, any
//movie directed by John McTiernan gets a 7.0, while any other movie gets a 3.0.
//  Implement an object called Dad with a method rate which accepts a Film and
//  returns a Double. Use paern matching.

case class Director(firstName: String, lastName: String, yearOfBirth: Int)

case class Film(name: String, yearOfRelease: Int, imdbRating: Double, director: Director)

object Dad {
  def rate(film: Film): Double =
    film match {
      case Film(_, _, _, Director("Clint", "Eastwood", _)) => 10.0
      case Film(_, _, _, Director("John", "McTiernan", _)) => 7.0
      case _ => 3.0
    }
}

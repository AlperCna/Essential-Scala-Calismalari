//4.2 This or That and Nothing Else: Sealed Traits

//Kavramsal olarak başka durum olmadığında kullanılır
//Örneğin önceki örnekte Visitor içerisinde Anonymous veya User vardı başka bir durum yoktu ama bizim yaptığımız tersi
//Type sistemine de bunu belirtiriz

//Sealed trait, alt türleri kontrollü ve sınırlı olan traittir

//sealed anahtar kelimesiyle yazılır

import java.util.Date

sealed trait Visitor {
  def id:String
  def createdAt: Date
  def age: Long = new Date().getTime() - createdAt.getTime()
}

//sealed kelimesi bu trait'i extend eden bütün doğrudan alt türler , aynı dosyada tanımlanmak zorundadır
// visitor subtypelarını aynı dosyada açmak zorundayız farklı dosyada olmaz

//Sealed traitin en büyük avantajı exhaustive pattern matching yani bütün olasılıkları karşılayan demek
//Bütün durumları ele almak demek
//Yani pattern matchingde bütün durumları ele aldın mı diye kontrol eder ve uyarı verir


//def missingCase(v: Visitor) =
//  v match {
//   case User(_, _, _) => "Got a user"
//  }

  //Visitor sealed yani derleyici sealed Trait'in bütün subtyplerını biliyor yani User ve Anonymous
  // ama sadece user ı yazmışız o yüzden hata veriyor compiler


  //Eğer unsealed trait olsaydı böyle bir hata olmazdı çünkü normal trait açık uçludur
  // Yani derleyici şöyle düşünür beşki başya dosyada subtypelar vardır belki sonradan eklenecek gibi

  //Sealed trait yalnızca doğrudan subtypeları sınırlar. sealed trait, trait’in kendisini extend edecek doğrudan subtype’ları aynı dosyaya sınırlar.
//Ama o subtype’ların kendileri başka yerlerde tekrar extend edilebilir.

sealed trait Visitor { /* ... */ }
final case class User(/* ... */) extends Visitor
final case class Anonymous(/* ... */) extends Visitor

//yani burda visitor sealed olduğu için doğrudan visitoru extend edecek bir class başka dosyada açılamaz ama eğer user açık bırakılmışsa user extend edilebilir

//class SpecialUser() extends User()

//burada final anahtar kelimesini kullandığımız için User ve anonymous daha fazla genişletilemez
// hepsi kapalı olmuş olur.


/*
Sealed Trait Paern
If all the subtypes of a trait are known, seal the trait
sealed trait TraitName {
...
}
Consider making subtypes final if there is no case for extending them
final case class Name(...) extends TraitName {
...
}
Remember subtypes must be defined in the same file as a sealed trait.
 */

/*
4.2.2 Exercises
4.2.2.1 Prinঞng Shapes
Let’s revisit the Shapes example from Secঞon [@sec:traits:shaping-up-2].
First make Shape a sealed trait. Then write a singleton object called Draw with
an apply method that takes a Shape as an argument and returns a descripঞon
of it on the console. For example:
Draw(Circle(10))
// res1: String = A circle of radius 10.0cm
Draw(Rectangle(3, 4))
// res2: String = A rectangle of width 3.0cm and height 4.0cm
Finally, verify that the compiler complains when you comment out a case
clause
 */

sealed trait Shape {
  def sides : Int
  def perimeter: Double
  def area :Double
}

case class Circle(radius:Double) extends Shape {
  val sides = 1
  val perimeter = 2* math.Pi * radius
  val area = math.Pi * radius * radius
}

case class Rectangle( widht: Double , height:Double) extends Shape {
  val sides = 4
  val perimeter = 2* widht + 2* height
  val area = widht * height
}

case class Square ( size: Double ) extends Shape{
  val sides =4
  val perimeter = 4* size
  val area = size * size
}
object Draw {
  def apply(shape: Shape): String = shape match {
    case Rectangle(width, height) =>
      s"A rectangle of width ${width}cm and height ${height}cm"
    case Square(size) =>
      s"A square of size ${size}cm"
    case Circle(radius) =>
      s"A circle of radius ${radius}cm"
  }
}

Draw(Circle(10))

Draw(Rectangle(3, 4))

/*
4.2.2.2 The Color and the Shape
Write a sealed trait Color to make our shapes more interesঞng.
• give Color three properঞes for its RGB values;
• create three predefined colours: Red, Yellow, and Pink;
• provide a means for people to produce their own custom Colors with
their own RGB values;
• provide a means for people to tell whether any Color is “light” or “dark”.
A lot of this exercise is le[ deliberately open to interpretaঞon. The important
thing is to pracঞce working with traits, classes, and objects.
Decisions such as how to model colours and what is considered a light or dark
colour can either be le[ up to you or discussed with other class members.
Edit the code for Shape and its subtypes to add a colour to each shape.

Finally, update the code for Draw.apply to print the colour of the argument
as well as its shape and dimensions:
• if the argument is a predefined colour, print that colour by name:
Draw(Circle(10, Yellow))
// res8: String = A yellow circle of radius 10.0cm
• if the argument is a custom colour rather than a predefined one, print
the word “light” or “dark” instead.
You may want to deal with the colour in a helper method.
 */




/*
4.2.2.3 A Short Division Exercise
Good Scala developers don’t just use types to model data. Types are a great
way to put arঞficial limitaঞons in place to ensure we don’t make mistakes in
our programs. In this exercise we will see a simple (if contrived) example of
this—using types to prevent division by zero errors.
Dividing by zero is a tricky problem—it can lead to excepঞons. The JVM has
us covered as far as floaঞng point division is concerned but integer division is
sঞll a problem:
1.0 / 0.0
// res31: Double = Infinity
1 / 0
// java.lang.ArithmeticException: / by zero
// ... 1024 elided
Let’s solve this problem once and for all using types!

Create an object called divide with an apply method that accepts two Ints
and returns DivisionResult. DivisionResult should be a sealed trait with
two subtypes: a Finite type encapsulaঞng the result of a valid division, and
an Infinite type represenঞng the result of dividing by 0.
Here’s some example usage:
val x = divide(1, 2)
// x: DivisionResult = Finite(0)
val y = divide(1, 0)
// y: DivisionResult = Infinite
Finally, write some sample code that calls divide, matches on the result, and
returns a sensible descripঞon.
 */

sealed trait DivisionResult

final case class Finite(value: Int) extends DivisionResult
case object Infinite extends DivisionResult

object divide {
  def apply(num:Int, den:Int):DivisionResult =
    if (den == 0) Infinite else Finite(num/den)
}

divide(1, 0) match {
  case Finite(value) => s"It's finite: ${value}"
  case Infinite => s"It's infinite"
}


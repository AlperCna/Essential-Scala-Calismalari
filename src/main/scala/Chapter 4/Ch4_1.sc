// 4 Traits

//Traitler birden fazla classı ortak bir fikir altında toplamaya yarar
//Chapter 4 neden var
//Chapter 2 neydi?
/*
Scala’da her şey expression’dır

her expression bir value üretir

her value’nin bir type’ı vardır

objelerle method çağırarak etkileşime gireriz

Yani daha çok tek tek değerler ve objeler sevitesindeydi
 */

/*
Chapter 3 neydi?
class ile object üretiriz

case class ile veri modelleriz

companion object ile yardımcı constructor/factory mantığı kurarız

pattern matching ile case class’ları parçaları

Benzer objeleri aynı şablondan nasıl üretirim: Class

 */

//Trait nedir trait birden fazla class'ın ortak özelliklerini ve ortak davranışlarını taşıyan bir yapıdır
//Yani farklı class'ların "aynı aileden" sayılmasını sağlayan bir üst yapıdır

//Traitin amacı ortak type oluşturmaktır, kod tekrarını azaltır ve polymorphism sağlar
//Trait kavrama jaavada interface mantığına benzer yani
//method imzası içermez method implementasyonu içerebilir


//4.1.1 An Example of Trait
//bir site düşünelim ziyaretçiler olacak  kullanıcılar ve anonim ziyaretçi olacak trait olmadan şu  şekilde yapılır

import java.util.Date

case class Anonymous(id: String, createdAt: Date = new Date())

case class User(id: String, email: String, createdAt: Date = new Date())

//provlem kod tekrar ediyo id ve created at özellikleri aynı
// bir diğer problem ortak type olmaması anonymous da user da ziyaretçi olarak geçer
//javadaki üst class mantığı gibi

trait Visitor {
  def id:String // Unique id assigned to each user
  def createdAt: Date // Date this user first visited the site

  def age : Long = new Date().getTime - createdAt.getTime
}
//Visitor olan her şey id sahibi olmalı createdAt sahibi olmalı
//Traitin içinde abstract method olabilir sadece sözleşme belirtir
//Ya da concrete method olabilir implementasyona sahiptir
//trait içindeki concrete methodlar alt lasslar tarafından miras alınır

case class Anonymous(id:String,createdAt:Date = new Date())extends Visitor

case class User(id: String, email: String, createdAt: Date = new Date()) extends Visitor

//Visitor raiti tanımlandı ve Anonymous ve user subtyplearı oluşturuldu extend anahtar kelimesiyle

//trait sayseinde artık şöyle bir method yazılabilir

def older(v1:Visitor,v2:Visitor): Boolean =
  v1.createdAt.before(v2.createdAt)

older(Anonymous("1"), User("2", "test@example.com"))


//Trait Syntax
//  To declare a trait we write
//trait TraitName {
//  declarationOrExpression ...
//}
//To declare that a class is a subtype of a trait we write
//class Name(...) extends TraitName {
//...
//}
//More commonly we’ll use case classes, but the syntax is the same
//case class Name(...) extends TraitName {
//...
//}


//4.1.2 Traits Compared to Classes

//Fark 1 - Trait constructor içermez traitten obje direkt oluşturalamaz
// onun yerine traitleri class oluşturmak için kullanırız daha sonrasında o classdan bir obje oluştururuz

// Fark 2 - Abstract method içerebilir, isim ve imzaya sahip olabilir ama impelementasyona sahip değildir
//Ama trait'i extend enden class bunu implemente etmek zorunadır

//Fark 3-  Concrete method içerebilir : trait içinde implementasyonu olan methodlar da olabilir
// def age: Long = ...

trait Visitor {
  def id: String // Unique id assigned to each user
  def createdAt: Date // Date this user first visited the site
  // How long has this visitor been around?
  def age: Long = new Date().getTime - createdAt.getTime
}

//Visitorde 2  tane abstract method var bu methodların implementasyonu yok ama extend
// edilen classlar tarafından implemente edilmeli

//Visitorden oluşturularn visitorun fieldlarını ve methodlarını kalıtımla alır


val anon = Anonymous("anon1")
anon.createdAt
anon.age

/*
4.1.3 TAKE HOME POINTS

Trait class’lar üzerinde soyutlama sağlar
class → objects
trait → classes

Trait ortak super type sağlar

Örneğin:
User
Anonymous aynı type olabilir:

Visitor
Trait abstract ve concrete method içerebilir
abstract method → implementasyon yok
  concrete method → implementasyon var
Trait doğrudan object üretmez

Trait sadece: class’lar için yapı sağlar
  Trait polymorphism sağlar

Bir method: Visitor bekliyorsa şu class’lar kabul edilir:
User
Anonymous


  Objects → class ile soyutlanır
Classes → trait ile soyutlanır

Ve trait sayesinde: farklı class’lar aynı type altında çalışabilir
*/
//
//4.1.4 Exercises
//  4.1.4.1 Cats, and More Cats
//Demand for Cat Simulator 1.0 is exploding! For v2 we’re going to go beyond
//the domesঞc cat to model Tigers, Lions, and Panthers in addiঞon to the Cat.
//  Define a trait Feline and then define all the different species as subtypes of
//  Feline. To make things interesঞng, define:
//• on Feline a colour as before;
//• on Feline a String sound, which for a cat is "meow" and is "roar"
//for all other felines;
//• only Cat has a favourite food; and
//• Lions have an Int maneSize.

trait Feline {
  def colour:String
  def sound:String
}

case class Lion(colour:String, maneSize:Int) extends Feline{
  val sound ="roar"
}

case class Tiger(colour:String) extends Feline {
  val sound= "roar"
}

case class Panther(colour: String) extends Feline {
  val sound = "roar"
}
case class Cat(colour: String, food: String) extends Feline {
  val sound = "meow"
}


/*
4.1.4.2 Shaping Up With Traits
Define a trait called Shape and give it three abstract methods:
• sides returns the number of sides;
• perimeter returns the total length of the sides;
• area returns the area.
Implement Shape with three classes: Circle, Rectangle, and Square. In
each case provide implementaঞons of each of the three methods. Ensure that
the main constructor parameters of each shape (e.g. the radius of the circle)
are accessible as fields.
Tip: The value of π is accessible as math.Pi.
 */
trait Shape {
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

 val daire = Circle(2)

daire.perimeter

val kare = Square(2)
kare.area

//4.1.4.3 Shaping Up 2 (Da Streets)
//The soluঞon from the last exercise delivered three disঞnct types of shape.
//However, it doesn’t model the relaঞonships between the three correctly. A
//Square isn’t just a Shape—it’s also a type of Rectangle where the width and
//height are the same.
//Refactor the soluঞon to the last exercise so that Square and Rectangle are
//subtypes of a common type Rectangular.
//Tip: A trait can extend another trait.


sealed trait Rectangular extends Shape{
  def widht: Double
  def height: Double
  val sides = 4
  override val perimeter = 2* widht + 2* height
  override val area = widht * height
}

case class Square(size:Double) extends Rectangular {
  val widht = size
  val height = size
}

case class Rectangle(widht:Double , height :Double) extends Rectangular



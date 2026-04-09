// 2.4 Object Literals
/*
Şimdiye kadar Scala'da Int, String, Boolean gibi hazır built- in objeleri kullanduk bu bölümde kendi objelerimizi oluşturacağız
Object Literal kullanarak

Bir obje oluşturulrken expressipn değil declaration kullanıllır
Expression bir value üretirken Declaratiopn bir isim ile bir value'yu bağlar yani bir değere isim verir

Basit bir obje şu şekilde tanımlanabilir

bir değer üretmez sadece isim tanımlar Test objenin adıdır
 */

object Test {}

Test

// Bu tip önceki gördüğümüz typlara benzemz singleton type olarak tanımlanır.

/*
Objenin tanım yapısı

object name {
declarationOrExpression ...
}
where
• name is the name of the object; and
• the optional declarationOrExpressions are declarations or
expressions.
 */


//2.4.1

//Objelerle etkileşim kurmanın yolu methodlardır.

object Test2 {
  def name: String = "Probably the best object ever"
}
//Burada name isimli bir method oluşturuldu ve bu normal şekilde çağrılabilir

Test2.name

//Kitapta daha complex örnek olarak geçen yer

object Test3 {
  def hello(name: String)=
    "Hello" + name
}

//Bu parametre alan method olarak geçer

Test3.hello("Alper")


/*
Method Declaration Syntax
The syntax for declaring a method is

def name(parameter: type, ...): resultType =
bodyExpression
or

def name: resultType =
bodyExpression

where
• name is the name of the method;
• the optional parameters are the names given to parameters to
the method;
• the types are the types of the method parameters;
• the optional resultType is the type of the result of the method;
• the bodyExpression is an expression that calling the method
evaluates to.

Method parameters are opঞonal, but if a method has parameters their
type must be given. Although the result type is opঞonal it is good pracঞce to define it as it serves as (machine checked!) documentaঞon.
The term argument may be used interchangeably with parameter.

Return is Implicit
The return value of the method is determined by evaluaঞng the body—
there is no need to write return like you would in Java.

 */

//Scala'da genellikle return yoktur.


//2.4.2 Fields
/*
Obje içinde veri saklanabilir buna fields denir bunları tanımlamak için val veya var anahtar kelimeleri kullanılır
 val : değiştirilemez
 var: değiştirilebilir
 */

object Test4 {
  val name = "Noel"

  def hello(other: String): String =
    name + "says hi to" + other
}

Test4.hello("Dave")

/*
Field Declaration Syntax
The syntax for declaring a field is
val name: type = valueExpression
or
var name: type = valueExpression

where
• name is the name of the field;
• the optional type declaration gives the type of the field;
• the valueExpression evaluates to the object that is bound to
the name
 */

//Val değiştirilemez val değiştirilebilir. Her zaman val ı var a tercih et diyor


//2.4.3 Methods versus fields

/*
Method ve field benzer görünse de farkları vardır
Field: değer saklar ve bir kez hesaplanır
Method: hesaplama yapar ve her çağrıldığında çalışır
 */

object Test7 {
  val simpleField = {
    println("Evaluating simpleField")
    42
  }
  def noParameterMethod = {
    println("Evaluating noParameterMethod")
    42
  }
}

Test7

/*
Field: object oluşturulurken hesaplanır
değeri saklanır
tekrar hesaplanmaz.


Method:her çağrıldığında çalışır

her seferinde tekrar hesaplanır.
 */

Test7.simpleField
Test7.simpleField


Test7.noParameterMethod
Test7.noParameterMethod

//Fieldda değer saklanır ilk çalıştığında println gelmişti ama  tekrar çalıştırdığımızda gelmedi çünkü saklandı ve 42 dödnürdü
//Method ise her çağırıldığında println geldi çünkü tekrar çalışıyor

//2.4.5.1 Cat-o-maঞque
//The table below shows the names, colour, and favourite foods of three cats.
//Define an object for each cat. (For experienced programmers: we haven’t
//covered classes yet.)

object Oswald {
  val Colour = "Black"
  val Food = "Milk"
}

object Henderson {
  val Colour = "Ginger"
  val Food = "Chips"
}

object Quentin {
  val Colour = "Tabby and white"
  val Food = "Curry"
}

println(Oswald.Colour)
println(Oswald.Food)

println("Kedilerin renkleri " + Oswald.Colour + "," + Henderson.Colour + "," + Quentin.Colour)


//2.4.5.2 Square Dance!
//  Define an object called calc with a method square that accepts a Double
//as an argument and… you guessed it… squares its input. Add a method called
//  cube that cubes its input calling square as part of its result calculaঞon.

object calc {
  def square(x: Double) = x * x
  def cube(x:Double )= x * square(x)
}

calc.square(2)
calc.cube(3)

//
//2.4.5.3 Precise Square Dance!
//  Copy and paste calc from the previous exercise to create a calc2 that is
//  generalized to work with Ints as well as Doubles. If you have Java experience,
//this should be fairly straighorward. If not, read the soluঞon below.

object calc2 {
  def square(x: Double) = x * x

  def cube(x: Double) = x * square(x)

  def square(x: Int) = x * x

  def cube(x: Int) = x * square(x)
}

calc2.square(3)
calc2.square(3.0)


//2.4.5.4 Order of evaluaঞon
//  When entered on the console, what does the following program output, and
//what is the type and value of the final expression? Think carefully about the
//  types, dependencies, and evaluaঞon behaviour of each field and method.


object argh {
  def a = {
    println("a")
    1
  }
  val b = {
    println("b")
    a + 2 //3
  }
  def c = {
    println("c")
    a

    b + "c"
  }
}
argh.c + argh.b + argh.a

//2.4.5.5 Greeঞngs, human
//Define an object called person that contains fields called firstName and
//lastName. Define a second object called alien containing a method called
//greet that takes your person as a parameter and returns a greeঞng using their
//  firstName.
//    What is the type of the greet method? Can we use this method to greet other
//objects?

object person {
  val firstName = "Alper"
  val lastName = "Can"
}

object alien {

  def greet(p: person.type): String =
    "Hello " + p.firstName

}


alien.greet(person)

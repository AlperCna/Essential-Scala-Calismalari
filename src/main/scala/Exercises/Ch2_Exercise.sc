1+2*3
//type int value 7

"5".toInt + 2

if (3 > 5) "scala" else "java"

{
  val x = 3
  val y = 4
  x * y
}

//Method call alıştırma

"scala".take(2)

"HELLO".toLowerCase

"abcdef".take(2+2)


//Operator style alıştırması
1+2

1.+(2)


1+2+3

1.+(2).+(3)


4*5

4.*(5)

//İf expression

if(10 > 5) 100 else 200


if(false) "scala"


//Object ve Method

object Person {
  val firstName = "Alper"
  val lastName = " Can"

  def fullName : String =
    firstName + " " + lastName
}

Person.fullName


object Calculator {
  def double(x:Int): Int =
    x*2

  def triple(x:Int) : Int =
    x*3

  def sixTimes(x:Int): Int =
    double(triple(x))
}

Calculator.sixTimes(8)


//Çıktı ne olur

object Test {

  val x = {
    println("x")
    5
  }

  def y = {
    println("y")
    x + 1
  }

}

Test.y
Test.y
Test.x


object Demo {

  def a = {
    println("a")
    1
  }

  val b = {
    println("b")
    a + 2
  }

}

Demo.b
Demo.b
Demo.a

//Method yazma

object MathUtils {
  def square(x:Double) : Double =
    x*x

  def cube(x:Double): Double =
     square(x)* x

}

MathUtils.square(3)



//Block Expression

{
  val x = 5
  val y = 3
  x + y
}


{
  1
  2
  3
}


// Method vs Field


object Test {

  val x = {
    println("field")
    10
  }

  def y = {
    println("method")
    20
  }

}

Test.x
Test.x
Test.y
Test.y

import scala.io.StdIn.readInt
//6.5 Opঞons as Flow Control
//
//Option sadece veri tutmaz, aynı zamanda akış kontrolü (flow control) sağlar.
//Yani:

//if / else yazmak yerine
//  Option + for comprehension kullanabiliriz

//val optionA = readInt("123")
//val optionB = readInt("234")
//for {
//  a <- optionA
//  b <- optionB
//} yield a + b


//Bu kod ne yapıyor?
//optionA içinde değer varsa → a olur
//optionB içinde değer varsa → b olur
//sonra a + b yapılır

//Sonuç: Some(357)


//Bu kod aslında çok kritik bir şey yapıyor:
// Eğer herhangi bir Option None ise → direkt sonuç None

//Örnek
//val optionA = readInt("123")   // Some(123)
//val optionB = readInt("abc")   // None
//for {
//  a <- optionA
//  b <- optionB
//} yield a + b
//
//Sonuç:
//
//None

//Bu nasıl çalışıyor? (3 farklı bakış açısı)


//1. map + flatMap olarak düşünelim
//
//Bu for comprehension aslında şuna eşittir:
//  optionA.flatMap(a => optionB.map(b => a + b))

//Adım adım
//  optionA varsa → a alınır
//sonra optionB varsa → b alınır
//  sonra a + b yapılır
//  Ama biri None ise?
//flatMap → direkt None döner
//zincir kırılır
// Yani aslında bu kod:
//
//  sum fonksiyonunun aynısıdır



//2. Sequence gibi düşünelim
//
//Option’ı şöyle düşünelim:
//
//Some(x) → 1 elemanlı liste
//None    → boş liste
//Örnek
//optionA = Some(1)
//optionB = Some(2)
//→ aslında:
//[1] ve [2]
//For ne yapıyor?
//tüm kombinasyonları al
//→ sonuç:
//[1 + 2] = [3]
//Ama biri boşsa?
//[1] ve []
//→ sonuç: [] yani None

//Some × Some → Some
//Some × None → None
//None × Some → None

//3. Flow Control

//a <- optionA   → Eğer Some ise devam et
//                 Eğer None ise dur, None dön
//
//b <- optionB   → Eğer Some ise devam et
//                 Eğer None ise dur, None dön


//Yani aslında:
//if optionA var
//  if optionB var
//    sonucu hesapla
//  else
//    None
//else
//  None

//Ama biz bunu yazmadık! for comprehension bizim yerimize yaptı



//6.5.1 Exercises
//6.5.1.1 Adding Things
//Write a method addOptions that accepts two parameters of type
//Option[Int] and adds them together. Use a for comprehension to
//structure your code.

def addOptions(opt1:Option[Int], opt2: Option[Int])=
  for {
    a <- opt1
    b <- opt2
  }yield a+b


  //Write a second version of your code using map and flatMap instead of a for
//comprehension.

def addOptions2(opt1: Option[Int], opt2: Option[Int]) =
  opt1 flatMap
    {
    a=> opt2 map
    {
    b => a+b
  }
  }


  //6.5.1.2 Adding All of the Things
//Overload addOptions with another implementaঞon that accepts three
//Option[Int] parameters and adds them all together.

def addOptions(opt1: Option[Int], opt2: Option[Int], opt3: Option[Int
]) =
  for {
    a <- opt1
    b <- opt2
    c <- opt3
  } yield a + b + c


  //Write a second version of your code using map and flatMap instead of a for
//comprehension.

def addOptions(opt1: Option[Int], opt2: Option[Int], opt3: Option[Int]) =

  opt1 flatMap{ a=> opt2 flatMap { b=> opt3 map { c => a+b+c}}}


//6.5.1.3 A(nother) Short Division Exercise
//Write a method divide that accepts two Int parameters and divides one by
//the other. Use Option to avoid excepঞons when the denominator is 0.

def divide(numerator: Int, denominator: Int) =
  if(denominator == 0) None else Some(numerator / denominator)


  //Using your divide method and a for comprehension, write a method called
//divideOptions that accepts two parameters of type Option[Int] and divides one by the other:

def divideOptions(numerator: Option[Int], denominator: Option[Int]) =
  for {
    a <- numerator
    b <- denominator
    c <- divide(a, b)
  } yield c










//6.5.1.4 A Simple Calculator
  //A final, longer exercise. Write a method called calculator that accepts three
//string parameters:
//def calculator(operand1: String, operator: String, operand2: String):
//Unit = ???
//and behaves as follows:
//1. Convert the operands to Ints;
//2. Perform the desired mathemaঞcal operator on the two operands:
//• provide support for at least four operaঞons: +, -, * and /;
//• use Option to guard against errors (invalid inputs or division by
//zero).
//3. Finally print the result or a generic error message.
//Tip: Start by supporঞng just one operator before extending your method to
//other cases.



def calculator(operand1: String, operator: String, operand2: String):
Unit = {
  val result = for {
    a <- readInt(operand1)
    b <- readInt(operand2)
    ans <- operator match {
      case "+" => Some(a + b)
      case "-" => Some(a - b)
      case "*" => Some(a * b)
      case "/" => divide(a, b)
      case _ => None
    }
  } yield ans
  result match {
    case Some(number) => println(s"The answer is $number!")
    case None => println(s"Error calculating $operand1 $operator $operand2")
  }
}
//Another approach involves factoring the calculaঞon part out into its own private funcঞon:
def calculator(operand1: String, operator: String, operand2: String):
Unit = {
  def calcInternal(a: Int, b: Int) =
    operator match {
      case "+" => Some(a + b)
      case "-" => Some(a - b)
      case "*" => Some(a * b)
      case "/" => divide(a, b)
      case _ => None
    }
  val result = for {
    a <- readInt(operand1)
    b <- readInt(operand2)
    ans <- calcInternal(a, b)
  } yield ans
  result match {
    case Some(number) => println(s"The answer is $number!")
    case None => println(s"Error calculating $operand1 $operator $operand2")
  }
}


//For the enthusiasঞc only, write a second version of your code using flatMap
//and map.

//This version of the code is much clearer if we factor out the calculaঞon part
//  into its own funcঞon. Without this it would be very hard to read:
def calculator(operand1: String, operator: String, operand2: String):
Unit = {
  def calcInternal(a: Int, b: Int) =
    operator match {
      case "+" => Some(a + b)
      case "-" => Some(a - b)
      case "*" => Some(a * b)
      case "/" => divide(a, b)
      case _ => None
    }
  val result =
    readInt(operand1) flatMap { a =>
      readInt(operand2) flatMap { b =>
        calcInternal(a, b) map { result =>
          result
        }
      }
    }
  result match {
    case Some(number) => println(s"The answer is $number!")
    case None => println(s"Error calculating $operand1
      $operator $operand2")
  }
}
//6.4 Options
//Option şu genel fikri temsil eder
// Bir değer olabilir de olmayabilir de

//değer varsa → Some
//değer yoksa → None
//Bu, Java’daki null kullanımına alternatiftir.

//Neden Option kullanılır?
//Java’da: String s = null;
//→ risk: NullPointerException
//Scala’da: Option[String]
//→ güvenli kullanım


//Option sayesinde null kullanmayız
// hatalar azalır
// işlemleri zincirleyebiliriz

//6.4.1 Option, Some, None

//Option is a generic sealed trait with two subtypes—Some and None. Here is
//an abbreviated version of the code—we will fill in more methods as we go on:

//Option aslında şöyle bir yapıdır:
//sealed trait Option[+A]
//Alt türleri:
//Some[A] → değer var
//None → değer yok


//Some
//Some(5)
//→ içinde değer var

//None
//
//→ değer yok


sealed trait Option[+A] {
  def getOrElse[B >: A](default: B): B
  def isEmpty: Boolean
  def isDefined: Boolean = !isEmpty
  // other methods...
}
final case class Some[A](x: A) extends Option[A] {
  def getOrElse[B >: A](default: B) = x
  def isEmpty: Boolean = false
  // other methods...
}
case object None extends Option[Nothing] {
  def getOrElse[B >: Nothing](default: B) = default
  def isEmpty: Boolean = true

  // other methods...
}

//Örnek
def readInt(str: String): Option[Int] =
  if (str matches "-?\\d+") Some(str.toInt)
  else None


//string sayıysa → Some(Int)
//değilse → None

readInt("123")
readInt("abc")


//6.4.2 Extracting Values from Options


//There are several ways to safely extract the value in an opঞon without the risk
//of throwing any excepঞons.
//Alternaঞve 1: the getOrElse method—useful if we want to fall back to a default value:

readInt("abc").getOrElse(0)

//Mantık
//değer varsa → kullan
//yoksa → default


//Alternaঞve 2: paern matching—Some and None both have associated patterns that we can use in a match expression:


readInt("123") match {
  case Some(number) => number + 1
  case None => 0
}

//Mantık
//Some → değeri al
//None → alternatif yap


//Alternaঞve 3: map and flatMap—Option supports both of these methods,
//enabling us to chain off of the value within producing a new Option. This
//bears a more thorough explanaঞon—let’s look at it in a lile more detail.


//6.4.3 Opঞons as Sequences
//Temel fikir
//
//Option şöyle düşünülebilir:
//
//0 veya 1 elemanlı sequence

sealed trait Option[+A] {
  def getOrElse[B >: A](default: B): B
  def isEmpty: Boolean
  def isDefined: Boolean = !isEmpty
  def filter(func: A => Boolean): Option[A]
  def find(func: A => Boolean): Option[A]
  def map[B](func: A => B): Option[B]
  def flatMap[B](func: A => Option[B]): Option[B]
  def foreach(func: A => Unit): Unit
  def foldLeft[B](initial: B)(func: (B, A) => B): B
  def foldRight[B](initial: B)(func: (A, B) => B): B
}

//map ve flatMap ile Option Örnek: sum

def sum(optionA: Option[Int], optionB: Option[Int]): Option[Int] =
  optionA.flatMap(a => optionB.map(b => a + b))

//Mantık
//Durum 1: optionA = None
//
//→ direkt sonuç None
//
//Durum 2: optionA = Some
//
//→ devam eder
//
//→ optionB.map çalışır
//
//optionB durumları
//optionB = None
//
//→ sonuç None
//
//optionB = Some
//
//→ sonuç Some(a + b)
//
//Kural
//Her şey Some ise → Some
//Bir tanesi bile None ise → None

//Option küçük olduğu için bazı metodlar aynı işi yapar
//(örneğin filter ve find)


sum(readInt("1"), readInt("2"))
// res2: Option[Int] = Some(3)
sum(readInt("1"), readInt("b"))
// res3: Option[Int] = None
sum(readInt("a"), readInt("2"))
// res4: Option[Int] = None

//map ve flatMap:
//değeri çıkarmaz
//ama işlemleri güvenli şekilde bağlar

//Option → normal değere dönüş
//sum(...).getOrElse(0)

//Seq ve Option ilişkisi
//Çok önemli
//Seq(Option[A]) → flatMap → Seq[A]

sum(readInt("1"), readInt("b")).getOrElse(0)
//Seq(readInt("1"), readInt("b"), readInt("3")).flatMap(x => x)
// res6: Seq[Int] = List(1, 3
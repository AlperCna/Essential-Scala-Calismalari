//7.9 JSON Serialisation


//In this secঞon we have an extended example involving serializing Scala data
//to JSON, which is one of the classic use cases for type classes. The typical
//process for converঞng data to JSON in Scala involves two steps. First we
//convert our data types to an intermediate case class representaঞon, then we
//serialize the intermediate representaঞon to a string.
//Here is a suitable case class representaঞon of a subset of the JSON language.
//We have a sealed trait JsValue that defines a stringify method, and
//a set of subtypes for two of the main JSON data types—objects and strings:


sealed trait JsValue {
  def stringify: String
}
final case class JsObject(values: Map[String, JsValue]) extends
  JsValue {
  def stringify = values
    .map { case (name, value) => "\"" + name + "\":" + value.stringify
    }
    .mkString("{", ",", "}")
}
final case class JsString(value: String) extends JsValue {
  def stringify = "\"" + value.replaceAll("\\|\"", "\\\\$1") + "\""
}
//You should recognise this as the algebraic data type paern.
//We can construct JSON objects and serialize them as follows:
val obj = JsObject(Map("foo" -> JsString("a"), "bar" -> JsString("b"),
  "baz" -> JsString("c")))
// obj: JsObject = JsObject(Map(foo -> JsString(a), bar -> JsString(b) , baz -> JsString(c)))
obj.stringify
// res2: String = {"foo":"a","bar":"b","baz":"c"}


//7.9.1 Convert X to JSON

//Let’s create a type class for converঞng Scala data to JSON. Implement a
//JsWriter trait containing a single abstract method write that converts a
//value to a JsValue.

//The type class is generic in a type A. The write method converts a value of
//type A to some kind of JsValue.
trait JsWriter[A] {
def write(value: A): JsValue
}


//Now let’s create the dispatch part of our type class. Write a JsUtil object
//containing a single method toJson. The method should accept a value of an
//arbitrary type A and convert it to JSON.
//Tip: your method will have to accept an implicit JsWriter to do the actual
//conversion.


object JsUtil {
  def toJson[A](value: A)(implicit writer: JsWriter[A]) =
    writer write value
}



//Now, let’s revisit our data types from the web site visitors example in the
//Sealed traits secঞon:
import java.util.Date
sealed trait Visitor {
  def id: String
  def createdAt: Date
  def age: Long = new Date().getTime() - createdAt.getTime()
}
final case class Anonymous(
                            id: String,
                            createdAt: Date = new Date()
                          ) extends Visitor
final case class User(
                       id: String,
                       email: String,
                       createdAt: Date = new Date()
                     ) extends Visitor


//Write JsWriter instances for Anonymous and User


implicit object AnonymousWriter extends JsWriter[Anonymous] {
  def write(value: Anonymous) = JsObject(Map(
    "id" -> JsString(value.id),
    "createdAt" -> JsString(value.createdAt.toString)
  ))
}
implicit object UserWriter extends JsWriter[User] {
  def write(value: User) = JsObject(Map(
    "id" -> JsString(value.id),
    "email" -> JsString(value.email),
    "createdAt" -> JsString(value.createdAt.toString)
  ))
}



//Given these two definiঞons we can implement a JsWriter for Visitor as
//follows. This uses a new type of paern – a: B – which matches any value of
//type B and binds it to a variable a:
implicit object VisitorWriter extends JsWriter[Visitor] {
def write(value: Visitor) = value match {
case anon: Anonymous => JsUtil.toJson(anon)
case user: User => JsUtil.toJson(user)
}
}
//Finally, verify that your code works by converঞng the following list of users to
//JSON:
val visitors: Seq[Visitor] = Seq(Anonymous("001", new Date), User("003", "dave@xample.com", new Date))


visitors.map(visitor => JsUtil.toJson(visitor))









//7.9.2 Preমer Conversion Syntax
//Let’s improve our JSON syntax by combining type classes and type enrichment.
//Convert JsUtil to an implicit class with a toJson method. Sample usage:
Anonymous("001", new Date).toJson

implicit class JsUtil[A](value: A) {
  def toJson(implicit writer: JsWriter[A]) =
    writer write value
}

implicit object StringWriter extends JsWriter[String] {
  def write(value: String) = JsString(value)
}
implicit object DateWriter extends JsWriter[Date] {
  def write(value: Date) = JsString(value.toString)
}

implicit object AnonymousWriter extends JsWriter[Anonymous] {
  def write(value: Anonymous) = JsObject(Map(
    "id" -> value.id.toJson,
    "createdAt" -> value.createdAt.toJson
  ))
}
implicit object UserWriter extends JsWriter[User] {
  def write(value: User) = JsObject(Map(
    "id" -> value.id.toJson,
    "email" -> value.email.toJson,
    "createdAt" -> value.createdAt.toJson
  ))
}
implicit object VisitorWriter extends JsWriter[Visitor] {
  def write(value: Visitor) = value match {
    case anon: Anonymous => anon.toJson
    case user: User => user.toJson
  }
}
//7.7 Using Type Classes

//Type class kullanırken yazımı nasıl kısaltırız ve gerektiğinde implicit’e nasıl erişiriz?
//İki konu var:
//Context Bounds
//implicitly


//7.7.1 Context Bounds

//Normalde type class kullanırken şöyle yazıyorduk

def pageTemplate[A](body: A)(implicit writer: HtmlWriter[A]): String =
{
  val renderedBody = body.toHtml
  s"<html><head>...</head><body>${renderedBody}</body></html>"
}

//writer parametresi var
//ama biz hiç kullanmıyoruz
//sadece toHtml çalışsın diye gerekiyor

def pageTemplate[A : HtmlWriter](body: A): String = {
  val renderedBody = body.toHtml
  s"<html><head>...</head><body>${renderedBody}</body></html>"
}

//[A : HtmlWriter]
//
//aslında şunun kısası:
//
//[A](implicit writer: HtmlWriter[A])

//“A tipi için HtmlWriter var olmalı”
//ama ismini yazmıyoruz
//sadece varlığını garanti ediyoruz

//Ne zaman kullanılır?
// Eğer implicit’i direkt kullanmıyorsan
//sadece başka bir method’a geçmesini istiyorsan


/*
Context Bound Syntax
A context bound is an annotaঞon on a generic type variable like so:
[A : Context]
It expands into a generic type parameter [A] along with an implicit parameter for a Context[A].
 */


//7.7.2 Implicitly

//Context bound kullanınca:
//
//[A : HtmlWriter]
//
// implicit var ama adı yok
//
//Ama bazen implicit’e erişmek isteriz
//
//İşte burada: implicitly devreye girer


//Context bounds give us a short-hand syntax for declaring implicit parameters,
//but since we don’t have an explicit name for the parameter we cannot use it in
//our methods. Normally we use context bounds when we don’t need explicit
//access to the implicit parameter, but rather just implicitly pass it on to some
//other method. However if we do need access for some reason we can use the
//implicitly method.

case class Example(name: String)
implicit val implicitExample = Example("implicit")


implicitly[Example]
// res0: Example = Example(implicit)


implicitly[Example] == implicitExample
// res1: Boolean = true

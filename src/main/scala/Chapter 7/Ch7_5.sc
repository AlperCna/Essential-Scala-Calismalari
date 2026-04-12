//7.5  Enriched Interfaces

//A second type of type class interface, called type enrichment¹ allow us to create
//interfaces that act as if they were methods defined on the classes of interest.
//For example, suppose we have a method called numberOfVowels:

//Type class yazdık ✔
//Implicit parametre kullandık ✔
//apply pattern gördük ✔
//
//Ama hâlâ kullanım biraz “yapay”:

def numberOfVowels(str: String) =
  str.filter(Seq('a', 'e', 'i', 'o', 'u').contains(_)).length

numberOfVowels("the quick brown fox")
// res0: Int = 5

//"hello".numberOfVowels şunu yapabilmek daha iyi olurdu yani metgod sanki class'ın içindeymiş gibi

//Problem
//String class’ını değiştiremeyiz
//Person class’ını her zaman değiştirmek istemeyiz


//Çözüm: Type Enrichment
//
//Mevcut class’a dışarıdan method eklemek
//
//Scala’da bunu sağlayan şey: implicit class

//7.5.1 Implicit Classes

//Let’s build up implicit classes piece by piece. We can wrap String in a class
//that adds our numberOfVowels:

class ExtraStringMethods(str: String) {
  val vowels = Seq('a', 'e', 'i', 'o', 'u')

  def numberOfVowels =
    str.toList.filter(vowels contains _).length
}
  // manuel wrapper
new ExtraStringMethods("the quick brown fox").numberOfVowels

//  bu şekilde kullanışır ama her seferinde new yazmak zordur


// implicit class çözümü

implicit class ExtraStringMethods(str: String) {
  val vowels = Seq('a','e','i','o','u')

  def numberOfVowels =
    str.toList.filter(vowels contains _).length
}


"the quick brown fox".numberOfVowels


/*
"abc".numberOfVowels yazdığımızda
compiler hata görüyor

String class’ında numberOfVowels yok

Şunu yapıyor:

“Bu hatayı düzeltebilir miyim?” diye bakar
implicit class arar
String → başka type dönüşümü var mı?
bulur: ExtraStringMethods
sonra
Şunu otomatik yazar:

new ExtraStringMethods("abc").numberOfVowels

 Bu olayın adı Type Enrichment

Tanım Mevcut bir tipe yeni method ekliyormuş gibi davranmak


Kural 1: implicit class nerede tanımlanmalı?
object içinde
class içinde
trait içinde

 top-level olmaz

Kural 2: implicit class = implicit value gibi davranır
scope önemli
import önemli


Kural 3: sadece 1 implicit class kullanılır

A → B → C → method
Yani:
implicit class A(x: String)
implicit class B(x: A)
 compiler chain yapmaz


 Type class ile bağlantısı
 implicit class + type class

Örnek
implicit class HtmlOps[A](value: A) {
  def toHtml(implicit writer: HtmlWriter[A]): String =
    writer.write(value)
}
Kullanım
p.toHtml
Bu nasıl çalışır?
toHtml yok → hata
implicit class bulunur
HtmlOps(p) yapılır
içinde writer implicit olarak bulunur
write çalışır

 */
//6.2 Working with Sequences

//Artık tek tek elemanlarla uğraşmak yerine sequence’in tamamı üzerinde işlem yapmayı öğreniyoruz.
//
//Yani:
//loop yazmak yerine
//daha kısa, daha temiz ve declarative (ne yapılacağını söyleyen) bir tarz kullanıyoruz

//6.2.1

//Temel fikir
//
//Sequence ile çalışırken genelde şunu yapmak isteriz:
//
//her elemana tek tek erişmek yerine
//tüm koleksiyonu bir bütün olarak işlemek
//
//Scala bunun için güçlü metodlar verir:
//
//map
//flatMap
//vs.
//
//Bu yöntemler sayesinde:
//
//daha az kod yazarız
//daha okunabilir olur
//fonksiyonel programlama yaklaşımı kullanılır

//6.2.2

//Let’s start with something simple—suppose we want to double every element
//of a sequence. You might wish to express this as a loop. However, this requires
//wriঞng several lines of looping machinery for only one line of actual doubling
//funcঞonality.
//Map nedir?
//
//map, sequence içindeki her elemana bir fonksiyon uygular
//ve sonuçlardan yeni bir sequence üretir.

val sequence = Seq(1, 2, 3)
// sequence: Seq[Int] = List(1, 2, 3)
sequence.map(elt => elt * 2)
// res0: Seq[Int] = List(2, 4, 6)

//If we use placeholder syntax we can write this even more compactly:

sequence.map(_ * 2)


/*
Genel yapı:

Seq[A] + (A => B)  →  Seq[B]

Yani:

elimizde Seq[A] var
her elemana A => B fonksiyonu uygulanır
sonuç Seq[B] olur
 */

//Problemli durum (neden map yetmez?)
//
//Örnek:

  Seq("a", "wet", "dog").map(_.permutations.toList)

//Sonuç:

//  Seq[List[String]]

//Yani:

//  her string → bir listeye dönüşüyor
//    sonuç: liste içinde listeler
//Neden sorun?
//
//Biz aslında şunu istiyoruz:

//  Seq[String]

//Ama elde ettiğimiz:

//  Seq[List[String]]

//Yani yapı iç içe (nested) oldu.


//6.2.3 FlatMap

//FlatMap nedir?
//flatMap, map + flatten gibi çalışır.
//her elemana fonksiyon uygular
//çıkan sequence’leri tek bir sequence haline getirir

Seq("a", "wet", "dog").flatMap(_.permutations.toList)

//Tip mantığı
//Seq[A] + (A => Seq[B]) → Seq[B]
//
//Bu, map’ten farkıdır.

//flatMap is similar to map except that it expects our funcঞon to return a sequence. The sequences for each input element are appended together. For
//example:

Seq(1, 2, 3).flatMap(num => Seq(num, num * 10))
// res6: Seq[Int] = List(1, 10, 2, 20, 3, 30)

//Map vs FlatMap farkı (çok kritik)
//Method	Dönen şey
//map	Seq[Seq[B]] olabilir
//flatMap	her zaman düz Seq[B]


//The end result is (nearly) always the same type as the original sequence:
//aList.flatMap(...) returns another List, aVector.flatMap(...)
//returns another Vector, and so on:

import scala.collection.immutable.Vector

Vector(1, 2, 3).flatMap(num => Seq(num, num * 10))
// res7: scala.collection.immutable.Vector[Int] = Vector(1, 10, 2, 20, 3, 30)


//6.2.4 Folds

//Problem: Toplama işlemi
//
//Elimizde bir Seq[Int] var ve tüm elemanları toplamak istiyoruz.
//
//Örnek:
//
//Seq(1, 2, 3) → 6
//
//Ama burada map ve flatMap kullanamayız.


//Neden map / flatMap olmaz?
//
//İki sebep var:
//map ve flatMap tek parametreli (unary) fonksiyon ister
//ama + işlemi iki parametreli (binary)
//map ve flatMap her zaman sequence döndürür
//ama biz tek bir değer (Int) istiyoruz


//Ek önemli problemler
//1. Boş sequence durumu
//Seq()
//Toplama yaparsak sonuç ne olmalı?
//→ Genelde 0
//Bu değere initial value (başlangıç değeri) denir.
//2. İşlem sırası
//+ işlemi commutative ama her işlem öyle değil.
//Bazı işlemlerde:

//sıra önemli olabilir
//bu yüzden soldan mı sağdan mı yapılacağını seçmemiz gerekir
//Method   We have     We provide                              We get
//  ???     Seq[Int]   0 and (Int, Int) => Int                  InT


//Çözüm: Fold
//
//Bu işi yapan metodlar:
//
//foldLeft
//foldRight
//
//Bunlara genel olarak fold denir.

//Fold nedir?
//Bir sequence’i dolaşır ve:
//  bir başlangıç değeri ile başlar
//her adımda sonucu günceller
//  en sonunda tek bir değer döner

//Method    We have       We provide              We get
//foldLeft  Seq[A]    B and (B, A) => B            B
//foldRight Seq[A]   B and (A, B) => B            B



//Tip yapısı
//foldLeft
//Seq[A] + B + (B, A) => B  →  B
//foldRight
//Seq[A] + B + (A, B) => B  →  B

//foldLeft nasıl çalışır?
//Seq(1, 2, 3).foldLeft(0)(_ + _)
//Adım adım:
//((0 + 1) + 2) + 3
//→ sonuç: 6
//Önemli:
//İşlem soldan başlar


//foldRight nasıl çalışır?
//Seq(1, 2, 3).foldRight(0)(_ + _)
//Adım adım:
//1 + (2 + (3 + 0))
//→ sonuç: 6
//Önemli:
//İşlem sağdan başlar

//| Özellik        | foldLeft    | foldRight |
//| -------------- | ----------- | --------- |
//| Başlangıç      | soldan      | sağdan    |
//| Fonksiyon tipi | (B, A)      | (A, B)    |
//  | Kullanım       | daha yaygın | daha az   |


//6.2.5 Foreach

//Foreach nedir?
//foreach, sequence’i dolaşır ama:
//sonuç döndürmez
//sadece side-effect için kullanılır


//Tip yapısı
//Seq[A] + (A => Unit) → Unit


List(1, 2, 3).foreach(num => println("And a " + num + "..."))


//6.2.6 Algebra of Transformations

//En önemli kısım
//Hangi metodu kullanacağını tiplerden anlayabilirsin
//Mantık
//3 şeyi düşün:
//Elinde ne var? → Seq[A]
//Elindeki fonksiyon ne?
//Ne elde etmek istiyorsun?
//Buna göre metod seçilir.

//  | Elimizde | Fonksiyon       | Sonuç  | Kullan    |
//  | -------- | --------------- | ------ | --------- |
//  | Seq[A]   | A => Unit       | Unit   | foreach   |
//  | Seq[A]   | A => B          | Seq[B] | map       |
//  | Seq[A]   | A => Seq[B]     | Seq[B] | flatMap   |
//  | Seq[A]   | B + (B, A) => B | B      | foldLeft  |
//  | Seq[A]   | B + (A, B) => B | B      | foldRight |


/*
Nasıl kullanılır?
1. Soruyu oku
2. Tipleri düşün
3. Tabloya bak
4. Methodu seç

Örnek düşünme
“Her elemanı dönüştür” → map
“Her eleman birden fazla şey üretir” → flatMap
“Tek sonuç üret” → fold
“Sadece işlem yap (print vs)” → foreach
 */


//6.2.7 Exercises

case class Film(
                 name: String,
                 yearOfRelease: Int,
                 imdbRating: Double)
case class Director(
                     firstName: String,
                     lastName: String,
                     yearOfBirth: Int,
                     films: Seq[Film])
val memento = new Film("Memento", 2000, 8.5)
val darkKnight = new Film("Dark Knight", 2008, 9.0)
val inception = new Film("Inception", 2010, 8.8)
val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7)
val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9)
val unforgiven = new Film("Unforgiven", 1992, 8.3)
val granTorino = new Film("Gran Torino", 2008, 8.2)
val invictus = new Film("Invictus", 2009, 7.4)
val predator = new Film("Predator", 1987, 7.9)
val dieHard = new Film("Die Hard", 1988, 8.3)
val huntForRedOctober = new Film("The Hunt for Red October", 1990,
  7.6)
val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8)
val eastwood = new Director("Clint", "Eastwood", 1930,
  Seq(highPlainsDrifter, outlawJoseyWales, unforgiven, granTorino,
    invictus))
val mcTiernan = new Director("John", "McTiernan", 1951,
  Seq(predator, dieHard, huntForRedOctober, thomasCrownAffair))
val nolan = new Director("Christopher", "Nolan", 1970,
  Seq(memento, darkKnight, inception))
val someGuy = new Director("Just", "Some Guy", 1990,
  Seq())
val directors = Seq(eastwood, mcTiernan, nolan, someGuy)

//The goals of this exercise are for you to learn your way around the collecঞons API, but more importantly to learn to use types to drive implementaঞon.
//When approaching each exercise you should answer:
//1. What is the type of the data we have available?
//2. What is the type of the result we want?
//3. What is the type of the operaঞons we will use?
//When you have answered these quesঞons look at the type table above to find
//the correct method to use. Done in this way the actual programming should
//be straighorward.
//6.2.7.1 Heroes of the Silver Screen
//These exercises re-use the example code from the Intranet Movie Database
//exercise from the previous secঞon:
//Nolan Films

//Starঞng with the definiঞon of nolan, create a list containing the names of the
//films directed by Christopher Nolan

nolan.films.map(_.name)


//Starঞng with the definiঞon of directors, create a list containing the names
//of all films by all directors.

directors.flatMap(director => director.films.map(film => film.name))

//Vintage McTiernan
//Starঞng with mcTiernan, find the date of the earliest McTiernan film.
//Tip: you can concisely find the minimum of two numbers a and b using
//math.min(a, b).

mcTiernan.films.sortWith { (a, b) =>
  a.yearOfRelease < b.yearOfRelease
}.headOption
//We can also do this by using a fold.
  mcTiernan.films.foldLeft(Int.MaxValue) { (current, film) =>
    math.min(current, film.yearOfRelease)
  }


//High Score Table
//Starঞng with directors, find all films sorted by descending IMDB raঞng:


directors.
  flatMap(director => director.films).
  sortWith((a, b) => a.imdbRating > b.imdbRating)

//Starঞng with directors again, find the average score across all films:
val films = directors.flatMap(director => director.films)
films.foldLeft(0.0)((sum, film) => sum + film.imdbRating) / films.
  length


//Tonight’s Lisࢼngs
//Starঞng with directors, print the following for every film: "Tonight only!
//FILM NAME by DIRECTOR!"

directors.foreach { director =>
  director.films.foreach { film =>
    println(s"Tonight! ${film.name} by ${director.firstName} ${
      director.lastName}!")
  }
}


//From the Archives
//Finally, starঞng with directors again, find the earliest film by any director:

directors.
  flatMap(director => director.films).
  sortWith((a, b) => a.yearOfRelease < b.yearOfRelease).
  headOption








//6.2.7.2 Do-It-Yourself
//Now we know the essenঞal methods of Seq, we can write our own versions
//of some other library methods.
//Minimum
//Write a method to find the smallest element of a Seq[Int].


//This is another fold. We have a Seq[Int], the minimum operaঞon is (Int,
//  Int) => Int, and we want an Int. The challenge is to find the zero value.
//What is the idenঞty for min so that min(x, identity) = x. It is posiঞve infinity, which in Scala we can write as Int.MaxValue (see, fixed width numbers
//do have benefits).
//Thus the soluঞon is:
def smallest(seq: Seq[Int]): Int =
  seq.foldLeft(Int.MaxValue)(math.min)



  //Unique
//Given Seq(1, 1, 2, 4, 3, 4) create the sequence containing each number only once. Order is not important, so Seq(1, 2, 4, 3) or Seq(4, 3,
//2, 1) are equally valid answers. Hint: Use contains to check if a sequence
//contains a value.


def insert(seq: Seq[Int], elt: Int): Seq[Int] = {
  if(seq.contains(elt))
    seq
  else
    elt +: seq
}
def unique(seq: Seq[Int]): Seq[Int] = {
  seq.foldLeft(Seq.empty[Int]){ insert _ }
}
unique(Seq(1, 1, 2, 4, 3, 4))



//Reverse
//Write a funcঞon that reverses the elements of a sequence. Your output does
//not have to use the same concrete implementaঞon as the input. Hint: use
//foldLeft.

def reverse[A](seq: Seq[A]): Seq[A] = {
  seq.foldLeft(Seq.empty[A]){ (seq, elt) => elt +: seq }
}



//Map
//Write map in terms of foldRight.

def map[A, B](seq: Seq[A], f: A => B): Seq[B] = {
  seq.foldRight(Seq.empty[B]){ (elt, seq) => f(elt) +: seq }
}


//Fold Le[
//Write your own implementaঞon of foldLeft that uses foreach and mutable
//state. Remember you can create a mutable variable using the var keyword,
//and assign a new value using =. For example
//var mutable = 1
//// mutable: Int = 1
//mutable = 2
//// mutable: Int = 2


def foldLeft[A, B](seq: Seq[A], zero: B, f: (B, A) => B): B = {
  var result = zero
  seq.foreach { elt => result = f(result, elt) }
  result
}
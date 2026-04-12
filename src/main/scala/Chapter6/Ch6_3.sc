//6.3 For Comprehensions


//Bu bölümün amacı: map, flatMap, foreach gibi metodları öğrendik
//ama bunlar çok iç içe yazılınca karmaşık oluyor
//Bunu çözmek için Scala bize özel bir syntax verir:
//
//→ for comprehension

//Scala’daki for: Java’daki klasik for loop değildir
//tamamen farklı bir mantıkla çalışır
//Yani:
//index ile dönmez
//koleksiyonlar üzerinde çalışır

//Basit örnek
//Map ile

Seq(1, 2, 3).map(_ * 2)

//For comprension ile

for {
  x <- Seq(1, 2, 3)
} yield x * 2


//Bu ifadeye generator denir.
//Anlamı: sequence’i dolaş her elemanı x olarak al


//Yield ne yapar?
//her iterasyonda üretilen sonucu toplar
//yeni bir sequence oluşturur


//Genel mantık
//for comprehension: koleksiyon üzerinde dolaşır
//her elemanı değişkene bağlar
//yield ile sonucu üretir
//sonuçları yeni bir koleksiyonda toplar


//Basit örneklerde map daha kısa.
//
//Ama işler karmaşıklaşınca:
//flatMap(...).map(...).flatMap(...) okunmaz hale gelir


val data = Seq(Seq(1), Seq(2, 3), Seq(4, 5, 6))

data.flatMap(_.map(_ * 2))

//This is geমng complicated. The equivalent for comprehension is much more
//… comprehensible.


for {
  subseq <- data
  element <- subseq
} yield element * 2
// res3: Seq[Int] = List(2, 4, 6, 8, 10, 12

//önce dış sequence gezilir
//sonra iç sequence gezilir
//her element işlenir


//This gives us an idea of what the for comprehensions does. A general for
//comprehension:
//for {
//x <- a
//y <- b
//z <- c
//} yield e

//translates to:
//a.flatMap(x => b.flatMap(y => c.map(z => e)))


//Eğer yield yazmazsak:
//sonuç dönmez
//sadece işlem yapılır
//tip: Unit


for {
  seq <- Seq(Seq(1), Seq(2, 3))
  elt <- seq
} println(elt * 2)

//The equivalent method calls use flatMap as usual and foreach in place of
//the final map:

//a.flatMap(x => b.flatMap(y => c.foreach(z => e)))


//Birkaç farklı yazım şekili bulunuyor

//We can use parentheses instead of braces to delimit the generators in a for
//loop. However, we must use semicolons to separate the generators if we do.
//Thus:

//for (
//  x <- a;
//  y <- b;
//  z <- c
//) yield e
//is equivalent to:
//for {
//  x <- a
//  y <- b
//  z <- c
//} yield e


//Some developers prefer to use parentheses when there is only one generator
//and braces otherwise:

for(x <- Seq(1, 2, 3)) yield {
  x * 2
}

//We can also use braces to wrap the yield expression and convert it to a block
//as usual:
//for {
//// ...
//} yield {
//// ...
//}


//6.3.1 Exercises
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
//(More) Heroes of the Silver Screen
//Repeat the following exercises from the previous secঞon without using map or
//flatMap:
//Nolan Films
//List the names of the films directed by Christopher Nolan.


for {
  film <- nolan.films
} yield film.name

//Cinephile
//List the names of all films by all directors.

for {
  director <- directors
  film <- director.films
} yield film.name

//
//High Score Table
//Find all films sorted by descending IMDB raঞng:

val films = for {
  director <- directors
  film <- director.films
} yield film
films sortWith { (a, b) =>
  a.imdbRating > b.imdbRating
}


//Tonight’s Lisࢼngs
//Print the following for every film: "Tonight only! FILM NAME by
//DIRECTOR!"

for {
  director <- directors
  film <- director.films
} println(s"Tonight! ${film.name} by ${director.firstName}!")

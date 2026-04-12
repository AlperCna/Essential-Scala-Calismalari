// Chapter 6 Collections

// Koleksiyonlar programlamada birden fazla veriyi bir arada tutmak için kullanılan yapooaşrdır

// bir sayı şistesi bir kullanıcı listesi bir kelime kümesi anahtar- değer eşleştirmeleri gibi


//Scala öğrenirken koleksiyonları iyi anlamak çok önemlidir.
// Çünkü gerçek programlarda sürekli bunlarla çalışılır.

// Bu chapterda işlenecek olan yapılar
//1. Sequences
//
//Bir sıralı veri yapısıdır. Elemanların belirli bir sırası vardır. Mesela:
//Seq(1, 2, 3)
//
//Burada 1 başta, 2 ortada, 3 sonda. Yani sıra önemlidir.
//
//2. Options
//
//Bir değerin var olup olmadığını güvenli şekilde temsil eder.
//
//Mesela bir yerde sonuç bulunabilir ya da bulunamayabilir. Bunun için Scala:
//
//Some(değer)
//None
//
//kullanır.
//
//Bu, önceki bölümlerde gördüğünüz Maybe yapısının Scala’daki hazır halidir.
//
//3. Maps
//
//Anahtar-değer çiftleri tutar. Örneğin:
//
//öğrenci numarası → öğrenci adı
//kullanıcı adı → kullanıcı bilgisi
//4. For comprehensions
//
//Koleksiyonlar üzerinde daha okunabilir işlemler yazmayı sağlar.
//
//5. Monads
//
//Önceden biraz girilmiş bir konu. Burada özellikle Option ve for comprehension ile ilişkisi gösterilecek.
//
//6. Sets
//
//Tekrarsız elemanlardan oluşan koleksiyon.
//
//7. Ranges
//
//Bellekte bütün elemanları tek tek saklamadan büyük sayı aralıklarını temsil etmeye yarar. Örneğin 1 to 1000000 gibi.


//6.1 Sequences

//A sequence is a collection of items with a defined and stable order.
//Sequence, elemanlarının belirli ve sabit bir sırası olan bir koleksiyondur.

//sequence oluşturma

val sequence = Seq(1, 2, 3)

//Bu örnek Scala koleksiyonlarının çok önemli bir özelliğini hemen gösteriyor: interface ile implementation’ın ayrılması.
//Bu değerin türü Seq[Int]


//6.1.1 Basic operations

//6.1.1.1 Accessing elements

sequence.apply(0)
// res0: Int = 1

sequence(0) // sugared syntax
// res1: Int = 1


//sequence(3)
// java.lang.IndexOutOfBoundsException: 3,


//We can also access the head and tail of the sequence:

sequence.head
// res5: Int = 1
sequence.tail
// res6: Seq[Int] = List(2, 3)
sequence.tail.head
// res7: Int = 2


//Bazı işlemler sadece sequence boş değilse anlamlıdır

//Seq().head
// java.util.NoSuchElementException: head of empty list
// at scala.collection.immutable.Nil$.head(List.scala:337)
// ...
//Seq().tail
// java.lang.UnsupportedOperationException: tail of empty list
// at scala.collection.immutable.Nil$.tail(List.scala:339)
// ..

sequence.headOption
// res17: Option[Int] = Some(1)
Seq().headOption
// res18: Option[Nothing] = None

//6.1.2 Sequence length

sequence.length


//6.1.3 Searching for elements

//There are a few ways of searching for elements.

//1. contains
sequence.contains(2)


//2. find

//The find method is like a generalised version of contains

sequence.find(_ == 3)
// res21: Option[Int] = Some(3)
sequence.find(_ > 4)
// res22: Option[Int] = None

//Buradaki _ == 3 ifadesi bir fonksiyondur.
//
//Anlamı: bir eleman al
//3’e eşit mi kontrol et
//Sequence’de ilk eşleşen eleman 3 olduğu için sonuç:
//Some(3)

//3.filter

//The filter method is a variant of find that returns all the matching elements in the sequence


sequence.filter(_ > 1)
// res23: Seq[Int] = List(2, 3)

//find → ilk eşleşeni bulur
//filter → bütün eşleşenleri toplar

//6.1.4 Sorting Elements

//We can use the sortWith method to sort a list using a binary function.

sequence.sortWith(_ > _)
// res24: Seq[Int] = List(3, 2, 1)

//_ > _ ne demek?
//
//Bu, iki parametre alan kısa yazımlı bir fonksiyondur.
//Açık hali aşağı yukarı şöyle düşünülebilir:
//(a, b) => a > b
//Yani: ilk eleman ikinci elemandan büyükse true değilse false
//Bu da sıralamaya şunu söyler:
//“Büyük olan önce gelsin.”
//
//Sonuç olarak liste descending order yani azalan sırada olur:

//6.1.5 Appending / Prepending Elements

//:+ (sona ekleme)
//Sequence’in sonuna eleman eklemek için kullanılır.
sequence :+ 4 → List(1, 2, 3, 4)
//Metod şeklinde yazılabilir ama infix (operatör gibi) yazım daha yaygındır.


//+: (başa ekleme)
//
//Sequence’in başına eleman eklemek için kullanılır.
sequence.+:(0)
// res27: Seq[Int] = List(0, 1, 2, 3)
0 +: sequence

//Önemli nokta:
//  Sonu : ile biten metodlar right-associative’tir.
//  Bu yüzden operatör ters yazılır (eleman solda, sequence sağda).


//++ (birleştirme)
//
//İki sequence’i birleştirir.
sequence ++ Seq(4, 5, 6)
// res29: Seq[Int] = List(1, 2, 3, 4, 5, 6)

//
//Fark:
//
//  :+ / +: → tek eleman ekler
//++ → iki sequence’i birleştirir


//6.1.6 Lists

//Seq’in varsayılan implementasyonu List’tir.
//Klasik linked list yapısıdır.
//
//Bazı Scala kütüphaneleri doğrudan List ile çalışır, bu yüzden bilmek önemli.

Nil
//Boş listeyi temsil eder.
//Nil → List()


//:: (prepend - listeye özel)
//
//Listeye baştan eleman eklemek için kullanılır.

val list = 1 :: 2 :: 3 :: Nil
// list: List[Int] = List(1, 2, 3)
4 :: 5 :: list
// res32: List[Int] = List(4, 5, 1, 2, 3)

//
//List(...) (constructor)
//
//Liste oluşturmanın klasik yolu:
//  List(1, 2, 3)
List(1, 2, 3)


//::: (liste birleştirme)
//
//List’e özel bir ++ versiyonudur.

List(1, 2, 3) ::: List(4, 5, 6)


//Önemli ayrım
//::, ::: → sadece List’e özel
//+:, :+, ++ → tüm sequence’lerde çalışır


//List’in performans özellikleri:
//head, tail, prepend (::) → O(1) (çok hızlı)
//append, index ile erişim → O(n) (yavaş)
//Yani:
//baştan ekleme hızlı
//sondan ekleme yavaş


//List → performans bilerek kullanılır
//Seq → daha generic, implementasyon değiştirilebilir


//6.1.7 Importing Collections
//Seq ve List otomatik gelir, import gerekmez.

//Diğer koleksiyonlar
//
//Bazıları import ister (örneğin Vector, Queue).

import scala.collection.immutable.Vector
Vector(1, 2, 3)


//Wildcard import

//Tüm paketi import edebilirsin:

import scala.collection.immutable._
Queue(1, 2, 3)

//We can also use import to bring methods
// and fields into scope from a singleton:

import scala.collection.immutable.Vector.apply
apply(1, 2, 3)
//
//We can write import statements anywhere in our code—imported idenঞfiers
//  are lexically scoped to the block where we use them:


// `empty` is unbound here
def someMethod = {
  import scala.collection.immutable.Vector.empty
  // `empty` is bound to `Vector.empty` here
  empty[Int]
}
// `empty` is unbound here again



//6.1.9 Exercises
//6.1.9.1 Documentaঞon
//Discovering Scala’s collecঞon classes is all about knowing how to read the
//API documentaঞon. Look up the Seq and List types now and answer the
//following:
//• There is a synonym of length defined on Seq—what is it called?
//• There are two methods for retrieving the first item in a List – what are
//they called and how do they differ?
//• What method can be used to display the elements of the sequence as
//a string?
//• What method of Option can be used to determine whether the opঞon
//contains a value?
//Tip: There is a link to the Scala API documentaঞon at http://scala-lang.org.



//The synonym for length is size.
//  The methods for retrieving the first element in a list are: - head —returns A,
//throwing an excepঞon if the list is empty - headOption—returns Option[A],
//returning None if the list is empty
//  The mkString method allows us to quickly display a Seq as a String:
  Seq(1, 2, 3).mkString(",") // returns "1,2,3"
Seq(1, 2, 3).mkString("[ ", ", ", " ]") // returns "[ 1, 2, 3 ]"
//Options contain two methods, isDefined and isEmpty, that we can use as
//a quick test:
  Some(123).isDefined // returns true
Some(123).isEmpty // returns false
None.isDefined // returns false
None.isEmpty // returns true



//6.1.9.2 Animals
//Create a Seq containing the Strings "cat", "dog", and "penguin". Bind it
//to the name animals.

val animals  = Seq("cat","dog","penguin")


//Append the element "tyrannosaurus" to animals and prepend the element
//"mouse".

"mouse" +: animals :+ "tyrannosaurus"

//What happens if you prepend the Int 2 to animals? Why? Try it out… were
//you correct?

//The returned sequence has type Seq[Any]. It is perfectly valid to return a
//supertype (in this case Seq[Any]) from a non-destrucঞve operaঞon.
//2 +: animals
//You might expect a type error here, but Scala is capable of determining the
//least upper bound of String and Int and seমng the type of the returned
//sequence accordingly.
//In most real code appending an Int to a Seq[String] would be an error. In
//pracঞce, the type annotaঞons we place on methods and fields protect against
//this kind of type error, but be aware of this behaviour just in case.



//6.1.9.3 Intranet Movie Database
//Let’s revisit our films and directors example from the Classes chapter.
//The code below is a parঞal rewrite of the previous sample code in which Films
//is stored as a field of Director instead of the other way around. Copy and
//paste this into a new Scala worksheet and conঞnue with the exercises below:
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
//Using this sample code, write implementaঞons of the following methods:


//• Accept a parameter numberOfFilms of type Int—find all directors who
//have directed more than numberOfFilms:


def directorsWithBackCatalogOfSize(numberOfFilms: Int): Seq[Director]
=
  directors.filter(_.films.length > numberOfFilms)


  //• Accept a parameter year of type Int—find a director who was born
//before that year:

def directorBornBefore(year: Int): Option[Director] =
  directors.find(_.yearOfBirth < year)


//Accept two parameters, year and numberOfFilms, and return a list
//of directors who were born before year who have also directed more
//than than numberOfFilms:

def directorBornBeforeWithBackCatalogOfSize(year: Int, numberOfFilms:
Int): Seq[Director] = {
  val byAge = directors.filter(_.yearOfBirth < year)
  val byFilms = directors.filter(_.films.length > numberOfFilms)
  byAge.filter(byFilms.contains)
}


//• Accept a parameter ascending of type Boolean that defaults to true.
//Sort the directors by age in the specified order:

def directorsSortedByAge(ascending: Boolean = true) =
  directors.sortWith { (a, b) =>
    if(ascending) {
      a.yearOfBirth < b.yearOfBirth
    } else {
      a.yearOfBirth > b.yearOfBirth
    }
  }

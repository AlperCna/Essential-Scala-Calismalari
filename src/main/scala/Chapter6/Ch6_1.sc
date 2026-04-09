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

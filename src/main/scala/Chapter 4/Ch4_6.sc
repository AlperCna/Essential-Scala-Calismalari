//4.6 Recursive Data

/*
Recursive data kendisini kullanarak tanımlanan veridir yani bir veri tipinin içinde aynı veri tipinden bir para bulunur
teorik olarak sınırsız uzunlukta yapılar kurulabilir

en klasik örnek olarak liste ağaç veya iç içe expression yapıları verileblir
 */

final case class Broken(broken: Broken)

//Kitapta bu yanlış bir örnek olarak geçiyor
// çünkü broken üretmek için içinde bir broken lazım o broken için de yine başka bir broken lazım ve
// bu böyle sonsuza kadar gidiyo

/*
yani başlanguç noktası yok bitiş noktası yok bu yüzden veri üretilemiyor

Geçerli bir recursive data tanımı yapmak için mutlaka recursion’ı bitiren bir base case gerekir
 */

sealed trait IntList
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList

/*
Intlist şu iki şetden biri olabilir End ve Pair(hea,tail)
Bir IntList ya boş listedir ya da Int ile bir başka IntList'ten oluşur

Burada recursion tail de Intlist oldupu için recursion olmuş oluyor

Base case hangisi?
case object End extends IntList

Bu, recursion’ı durduran durumdur.

Yani zincirin son halkasıdır.
Eğer End olmasaydı liste hiçbir zaman tamamlanamazdı.
 */

Pair(1, Pair(2, Pair(3, End)))

/*
mantık şu
en sonda End var
onun üstüne 3
onun üstüne 2
onun üstüne 1

açılmış hali şu şekilde
 */

val d = End
val c = Pair(3, d)
val b = Pair(2, c)
val a = Pair(1, b)
/*
Burada:

d = boş liste
c = sadece 3
b = 2, 3
a = 1, 2, 3
 */

//Her düğüm sadece “tek eleman + geri kalan liste” tutuyor ama aynı zamanda kendi başına tam bir listeyi de temsil ediyor.

//Kitap buna singly-linked list diyor. Yani tek yönlü bağlı liste.

//Her düğüm:
//kendi değerini tutar (head)
//sıradaki kısmı gösterir (tail)
//Bu yüzden zincir gibi düşünülür.


/*
Recursive algebraic data type üzerinde çalışırken yine daha önce gördüğümüz structural recursion mantığını kullanırız. Fark şu:
Veri recursive ise, metotta da recursive call gerekir
 */

val example = Pair(1, Pair(2, Pair(3, End)))

assert(sum(example) == 6)
assert(sum(example.tail) == 5)
assert(sum(End) == 0)

def sum(list: IntList): Int = ???

/*
Neden End için sonuç 0?
Çünkü boş listenin toplamı 0 alınır.
Burada kitap özellikle şunu anlatıyor:

Base case için, yaptığın işlemin identity değerini döndürmelisin.

Toplama için identity nedir?
a + 0 = a
Yani toplamada identity = 0
 */

//Şimdi de structural recursionu ile iskeleti kuracağız

def sum(list: IntList): Int =
  list match {
    case End => ???
    case Pair(hd, tl) => ???
  }

  /*
  Bu, recursive ADT üstünde çalışmanın temel şablonudur.
Her case’i ayrı ele alırsın:

base case
recursive case
*/

/*
Finally we have to decide on the bodies of our cases. We have already decided
that 0 is answer for End. For Pair we have two bits of informaঞon to guide
us. We know we need to return an Int and we know that we need to make a
recursive call on tl. Let’s fill in what we have
*/
def sum(list: IntList): Int =
  list match {
    case End => 0
    case Pair(hd, tl) => ??? sum(tl)
  }

  //The recursive call will return the sum of the tail of the list, by definiঞon. Thus
//the correct thing to do is to add hd to this result. This gives us our final result:

def sum(list: IntList): Int =
  list match {
    case End => 0
    case Pair(hd, tl) => hd + sum(tl)
  }

//4.6.1 Understanding the Base Case and Recursive Case
//Bu başlığın özü base case nasıl seçilir onu anlatıyor
//Base case için genelde yaptığın işlemin identity değerini döndürürsün
/*

Örnekler
toplama → 0
çarpma → 1

Çünkü:

  a + 0 = a
a * 1 = a

Yani boş yapı, sonucu bozmamalı.

 For the base case we should generally return the idenࢼty for the funcঞon we’re trying to compute. The idenঞty is an element that doesn’t
change the result. E.g. 0 is the idenঞty for addiঞon, because a + 0 ==
a for any a. If we were calculaঞng the product of elements the idenঞty
would be 1 as a * 1 == a for all a.

Recursive case içinn recursive çağrının doğru sonucu döndüreceğini varsayıp
ve tam cevaba ulaşmak için buna ne eklememiz gerektiğini düşünürüz
sum örneğinde:

sum(tl) tail’in toplamını verir
tam toplam için sadece hd eksik
o yüzden hd + sum(tl)


For the recursive case, assume the recursion will return the correct result and work out what you need to add to get the correct answer. We
saw this for sum, where we assume the recursive call will give us the
correct result for the tail of the list and we then just add on the head.
 */


//Recursive ADT için genel pattern

sealed trait RecursiveExample
final case class RecursiveCase(recursion: RecursiveExample) extends RecursiveExample
case object BaseCase extends RecursiveExample

//Yani recursive veri tanımlarken:
//en az bir recursive case
//en az bir base case gerekir.


//Recursive Structural Recursion Paern
/*
Recursive veri üzerinde method yazarken:

recursive eleman görürsen recursive call yap
base case görürsen işlemin identity değerini döndür

When wriঞng structurally recursive code on a recursive algebraic data
type:
• whenever we encounter a recursive element in the data we make
a recursive call to our method; and
• whenever we encounter a base case in the data we return the
idenঞty for the operaঞon we are performing.

 */


//4.6.2 Tail Recursion
//Normal recursionda her çağrı stack'e yeni frame koyar
//Liste çok uzunsa stack büyür bu da risk yaratır

//Kitap diyor ki Scala bazı recursive fonksiyonları optimize edebilir; buna tail recursion denir. Ama bunun belli şartları vardır.

/*
Tail call nedir
Bir method call’dan sonra çağıran method hemen o sonucu döndürüyorsa, bu bir tail call’dur.
 */

def method1: Int =
  1
def tailCall: Int =
  method1

//Burada tailCall, method1 çağrısından sonra başka bir şey yapmıyor.
//Direkt sonucu döndürüyor. O yüzden tail call.

def notATailCall: Int =
  method1 + 2

//bu tail call değil çünkü
//önce method1 çağrılıyor
//sonra üstüne + 2 yapılıyor
//yani çağrıdan sonra ekstra iş var
//Bu yüzden son pozisyonda değil.


//Kitapta şu önrek veriliyor

import scala.annotation.tailrec

@tailrec
def sum(list: IntList): Int =
  list match {
    case End => 0
    case Pair(hd, tl) => hd + sum(tl)
  }

  /*
  Compiler hata veriyor, çünkü recursive call:

sum(tl)

çağrıldıktan sonra hâlâ iş var:

hd + ...

Yani recursive call son işlem değil.
Bu yüzden tail position’da değil.
*/

@tailrec
def sum(list: IntList, total: Int = 0): Int =
  list match {
    case End => total
    case Pair(hd, tl) => sum(tl, total + hd)
  }

  /*
  Burada mantık şu:

toplamı dışarıda biriktiriyoruz
recursive call son işlem oluyor
çağrı döndükten sonra ekstra iş kalmıyor

Bu yüzden tail recursive oluyor.
*/

/*
Accumulator neden işe yarıyor?

Eski sürümde:

hd + sum(tl)

önce recursion, sonra toplama vardı.

Yeni sürümde:

sum(tl, total + hd)

toplama recursion’dan önce yapılıyor.
Recursive çağrı artık direkt son işlem.

Yani düşünce şu:

sonucu sonda kurmak yerine
ilerlerken biriktir
*/


/*
@tailrec annotation ne işe yarar?

@tailrec, compiler’a şunu söyler:

“Ben bu method’un tail recursive olduğunu düşünüyorum, kontrol et.”

Eğer gerçekten değilse compiler hata verir. Bu çok faydalı çünkü yanlışlıkla
tail recursion bozulursa hemen anlaşılır.


Her non-tail recursive fonksiyon, genelde bir accumulator eklenerek
tail recursive hale dönüştürülebilir. Ama bunun stack allocation’ı heap
allocation’a çevirdiğini, bazen kazanç bazen kayıp olabileceğini de söylüyor.

tail recursion her zaman “tek doğru” değildir
ama büyük recursive işlemlerde çok değerlidir
*/

/*
Scala’da çoğu zaman doğrudan tail recursive fonksiyon yazmak yerine standard library’deki collection araçlarını kullanırız. Çünkü onlar zaten bu tür işleri iyi kapsar. Ama kendi veri
tipini yazarken veya optimizasyon yaparken tail recursion bilmek önemlidir.
 */
//6.8 Maps And Sers

//6.8.1 Maps

// Map keyy -> value turan bir yapıdır

//her key’in bir karşılığı vardır
//key’ler benzersizdir

val example = Map("a" -> 1, "b" -> 2, "c" -> 3)
// example: scala.collection.immutable.Map[String,Int] = Map(a -> 1, b -> 2, c -> 3)

//The type of the resulঞng map is Map[String,Int], meaning all the keys are
//type String and all the values are of type Int.

//-> constructor bir tuple oluşturur
//Map aslında bu tupleleri alır

"a" -> 1
// res0: (String, Int) = (a,1)


//6.8.1.1 Accessing values using keys

//There are two main
//methods for doing this: apply and get.


example("a") // The same as example.apply("a")
// res1: Int = 1
example.get("a")
// res2: Option[Int] = Some(1)

// apply eğer bir key bulamazsa exeption döner, ama get none döner bu yüzden daha güvenlidir



//example("d")
//// java.util.NoSuchElementException: key not found: d
//// at scala.collection.MapLike$class.default(MapLike.scala:228)
//// at scala.collection.AbstractMap.default(Map.scala:59)
//// at scala.collection.MapLike$class.apply(MapLike.scala:141)
//// at scala.collection.AbstractMap.apply(Map.scala:59)
//// ... 302 elided
//java.util.NoSuchElementException: key not found: d
//// <console>:2: error: ';' expected but ':' found.
//// java.util.NoSuchElementException: key not found: d
//// ^

//example.get("d")
//// res4: Option[Int] = None

//Bir diğeri ise getorElse bir default value kabul eder ve key yoksa onu döndürür

example.getOrElse("d", -1)
// res5: Int = -1


//6.8.1.2 Determining membership

//The contains method determines whether a map contains a key.

example.contains("a")
// res6: Boolean = true

//6.8.1.3 Determining size

//Finding the size of a map is just as easy as finding the size of a sequence

example.size
// res7: Int = 3

//6.8.1.4 Adding and removing elements

//Map in default implementasyonu değişmez yani mevcut map değişmez yeni map oluşur

//We can add new elements using the + method. Note that, as with Java’s
//HashMap, keys are overwrien and order is non-determinisঞc

example.+("c" -> 10, "d" -> 11, "e" -> 12)
// res8: scala.collection.immutable.Map[String,Int] = Map(e -> 12, a -> 1, b -> 2, c -> 10, d -> 11)

//We can remove keys using the - method:

example.-("b", "c")
// res9: scala.collection.immutable.Map[String,Int] = Map(a -> 1)


//Aynı key varsa → overwrite olur

//If we are only specifying a single argument, we can write + and - as infix operators.

example + ("d" -> 4) - "c"
// res10: scala.collection.immutable.Map[String,Int] = Map(a -> 1, b -> 2, d -> 4)

//Note that we sঞll have to write the pair "d" -> 4 in parentheses because +
//and -> have the same precedence.


//6.8.1.5 Mutable maps

//The scala.collection.mutable package contains several mutable implementaঞons of Map

val example2 = scala.collection.mutable.Map("x" -> 10, "y" -> 11, "z"
  -> 12)
// example2: scala.collection.mutable.Map[String,Int] = Map(z -> 12, y -> 11, x -> 10)

//mmutable: yeni map döner
//Mutable: aynı map değişir

//The in-place mutaঞon equivalents of + and - are += and -= respecঞvely.

example2 += ("x" -> 20)
// res11: example2.type = Map(z -> 12, y -> 11, x -> 20)
example2 -= ("y", "z")
// res12: example2.type = Map(x -> 20)

//Ayrıca güncelleme işlei de yapabiliyoruz

example2("w") = 30
example2
// res14: scala.collection.mutable.Map[String,Int] = Map(w -> 30, x -> 20)

//Bu: değer döndürmez
// map’i direkt değiştirir



//6.8.1.6 Sorted maps

//Normal maplerde keylerin sırası garanti değildir

Map("a" -> 1) + ("b" -> 2) + ("c" -> 3) + ("d" -> 4) + ("e" -> 5)
// res15: scala.collection.immutable.Map[String,Int] = Map(e -> 5, a -> 1, b -> 2, c -> 3, d -> 4)

//Scala also provides ordered immutable and mutable versions of a ListMap
//class that preserves the order in which keys are added:

scala.collection.immutable.ListMap("a" -> 1) + ("b" -> 2) + ("c" -> 3)+ ("d" -> 4) + ("e" -> 5)
//// res16: scala.collection.immutable.ListMap[String,Int] = Map(a -> 1,
//b -> 2, c -> 3, d -> 4, e -> 5)


//interface (kullanım) aynı
//sadece davranış (ordering + performans) değişir



//6.8.1.7 map and flatMap

//Map de aslında:
// Map[A, B] = collection of (A, B)
//Yani: Tuple2[A, B] listesi gibi çalışır


example.map(pair => pair._1 -> pair._2 * 2)
// res17: scala.collection.immutable.Map[String,Int] = Map(a -> 2, b -> 4, c -> 6)

//key aynı kaldı
//value değişti


//Note that the resulঞng object is also a Map as you might expect. However,
//what happens when the funcঞon we supply doesn’t return a pair? What does
//map return then? Is it a compile error? Let’s try it.

example.map(pair => pair._1 + " = " + pair._2)
// res18: scala.collection.immutable.Iterable[String] = List(a = 1, b = 2, c = 3)

//Neden Map olmadı?
//
//Çünkü: artık (key, value) yok
//sadece String var
//Kural pair döndürürsen → Map
// başka bir şey döndürürsen → Iterable


//flatMap öorneği

example.flatMap {
  case (str, num) =>
    (1 to 3).map(x => (str + x) -> (num * x))
}
// res19: scala.collection.immutable.Map[String,Int] = Map(c3 -> 9, b2 -> 4, b3 -> 6, c2
// -> 6, b1 -> 2, c1 -> 3, a3 -> 3, a1 -> 1, a2 -> 2)

//Mantık
//Her eleman için:
//3 yeni eleman üret
//hepsini birleştir


//Aynısının for hali

for{
  (str, num) <- example
  x <- 1 to 3
} yield (str + x) -> (num * x)
// res20: scala.collection.immutable.Map[String,Int] = Map(c3 -> 9, b2 -> 4, b3 -> 6, c2 -> 6,
// b1 -> 2, c1 -> 3, a3 -> 3, a1 -> 1, a2 > 2)

//flatMap + map = for comprehension


//Eğer pair dönmezsek

for{
  (str, num) <- example
  x <- 1 to 3
} yield (x + str) + "=" + (x * num)
// res21: scala.collection.immutable.Iterable[String] = List(1a=1, 2a
// =2, 3a=3, 1b=2, 2b=4, 3b=6, 1c=3, 2c=6, 3c=9)

//pair yoksa → Map olmaz
// normal koleksiyon döner

//6.8.2 Sets
//Set nedir?
//
//Set şu demek:
// sırasız (unordered) bir koleksiyon
//aynı elemandan sadece 1 tane olabilir (duplicate yok)

//Set’i şöyle düşünebilirim:
//Liste gibi ama sırası yok
//Map gibi ama sadece key var (value yok)

val s = Set(1, 2, 3, 3, 2)
//Set(1, 2, 3)

//Set duplicate kabul etmez

//Temel Operasyonlar
//  1. Eleman ekleme (+)
s + 4

//Yeni set:
//  Set(1, 2, 3, 4)

// aynı eleman eklenirse değişmez

s + 3

//→ yine: Set(1, 2, 3)

//2. Eleman silme (-)
s - 2

//Sonuç: Set(1, 3)

//3. Set birleştirme (++)
Set(1,2) ++ Set(2,3)

//Sonuç: Set(1,2,3)

// union (birleşim)

//4. Set çıkarma (--)
Set(1,2,3) -- Set(2,3)

//Sonuç: Set(1)
//Mantık fark (difference)

//5. contains
  s.contains(2)

//Sonuç: true


//6. apply
  s(2)

//Sonuç: true


// apply = contains ile aynı

//7. size
  s.size

//→ eleman sayısı

//8. map
  s.map(x => x * 2)

//Sonuç: Set(2, 4, 6)
//Mantık :her elemana işlem uygula sonuç yine Set

//Önemli :Duplicate yine yok:

  Set(1,2,3).map(_ % 2)

//Sonuç: Set(1, 0)


//9. flatMap
  s.flatMap(x => Set(x, x*10))
//Mantık
//her eleman için yeni set üret
//  hepsini birleştir
//  Örnek
//Set(1,2)


1 → Set(1,10)
2 → Set(2,20)

//Sonuç: Set(1,10,2,20)

//Mutable Set
//  1. +=
//  s += 4  aynı set değişir

//2. -=
//  s -= 2

//eleman silinir
//
//Önemli fark Immutable:

  s + 4

//yeni set Mutable: s += 4 aynı set değişir



//
//6.8.3 Exercises
//  6.8.3.1 Favorites
//  Copy and paste the following code into an editor:
val people = Set(
  "Alice",
  "Bob",
  "Charlie",

"Derek",
"Edith",
"Fred")
val ages = Map(
  "Alice" -> 20,
  "Bob" -> 30,
  "Charlie" -> 50,
  "Derek" -> 40,
  "Edith" -> 10,
  "Fred" -> 60)
val favoriteColors = Map(
  "Bob" -> "green",
  "Derek" -> "magenta",
  "Fred" -> "yellow")
val favoriteLolcats = Map(
  "Alice" -> "Long Cat",
  "Charlie" -> "Ceiling Cat",
  "Edith" -> "Cloud Cat")
//Use the code as test data for the following exercises:

//  Write a method favoriteColor that accepts a person’s name as a parameter
//and returns their favorite colour.

def favoriteColor(person: String): Option[String] =
  favoriteColors.get(person)



  //Update favoriteColor to return a person’s favorite color or beige as a default.

def favoriteColor(person: String): String =
  favoriteColors.get(person).getOrElse("beige")


 //Write a method printColors that prints everyone’s favorite color!

def printColors() = for {
  person <- people
} println(s"${person}'s favorite color is ${favoriteColor(person)}!")
//or:
def printColors() = people foreach { person =>
  println(s"${person}'s favorite color is ${favoriteColor(person)}!")
}


//Write a method lookup that accepts a name and one of the maps and returns
//the relevant value from the map. Ensure that the return type of the method
//matches the value type of the map.

def lookup[A](name: String, values: Map[String, A]) =
  values get name



  //Calculate the color of the oldest person:

val oldest: Option[String] =
  people.foldLeft(Option.empty[String]) { (older, person) =>
    if(ages.getOrElse(person, 0) > older.flatMap(ages.get).getOrElse
    (0)) {
      Some(person)
    } else {
      older
    }
  }
val favorite: Option[String] =
  for {
    oldest <- oldest
    color <- favoriteColors.get(oldest)
  } yield color



  //6.8.4 Do-It-Yourself Part 2
//Now we have some pracঞce with maps and sets let’s see if we can implement
//some useful library funcঞons for ourselves.
//6.8.4.1 Union of Sets
//Write a method that takes two sets and returns a set containing the union
//of the elements. Use iteraঞon, like map or foldLeft, not the built-in union
//method to do so!

def union[A](set1: Set[A], set2: Set[A]): Set[A] = {
  set1.foldLeft(set2){ (set, elt) => (set + elt) }
}


//6.8.4.2 Union of Maps
//Now let’s write union for maps. Assume we have two Map[A, Int] and
//add corresponding elements in the two maps. So the union of Map('a' ->
//1, 'b' -> 2) and Map('a' -> 2, 'b' -> 4) should be Map('a' -> 3,
//'b' -> 6)


def union[A](map1: Map[A, Int], map2: Map[A, Int]): Map[A, Int] = {
  map1.foldLeft(map2){ (map, elt) =>
    val (key, value1) = elt
    val value2 = map.get(key)
    val total = value1 + value2.getOrElse(0)
    map + (key -> total)
  }
}


//6.8.4.3 Generic Union
//There are many things that can be added, such as strings (string concatenaঞon),
//sets (union), and of course numbers. It would be nice if we could generalise our
//union method on maps to handle anything for which a sensible add operaঞon
//can be defined. How can we go about doing this?

def union[A, B](map1: Map[A, B], map2: Map[A, B], add: (B, B) => B):
Map[A, B] = {
  map1.foldLeft(map2){ (map, elt) =>
    val (k, v) = elt
    val newV = map.get(k).map(v2 => add(v, v2)).getOrElse(v)
    map + (k -> newV)
  }
}

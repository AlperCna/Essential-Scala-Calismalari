//6.10 Generating Random Data

//Bu bölümde rastgele veri üretme fikri var. Ama burada “gerçekten rastgele sayı üretmekten” çok, olası sonuçları modellemek ve
// bunları birbirine bağlı şekilde üretmek anlatılıyor.
//
//Bunun kullanım alanları şunlar:
//test verisi üretmek
//property based testing
//probabilistic programming
//generative art
//Yani temel fikir şu: Bir şeyleri sadece tek tek yazmak yerine, kurallara göre otomatik üretebilmek.


//6.10.1 Random Words

//We’ll start by generaঞng text. Imagine we wanted to generate (somewhat)
//realisঞc text, perhaps to use as a placeholder to fill in parts of a website design.
//If we took a large amount of real text we could analyse to work out for each
//word what the most common words following it are. Such a model is known
//as a Markov chain.
//To keep this example to a reasonable size we’re going to deal with a really
//simplified version of the problem, where all sentences have the form subjectverb-object. For example, “Noel wrote code”.
//Write a program to generate all possible sentences given the following model:
//• subjects are List("Noel", "The cat", "The dog");
//• verbs are List("wrote", "chased", "slept on"); and
//• objects are List("the book", "the ball", "the bed")

val subjects = List("Noel", "The cat", "The dog")
val verbs = List("wrote", "chased", "slept on")
val objects = List("the book", "the ball", "the bed")
def allSentences: List[(String, String, String)] =
  for {
    subject <- subjects
    verb <- verbs
    obj <- objects
  } yield (subject, verb, obj)


//önce bir subject seçiyorum
//sonra o subject için bütün verb’leri deniyorum
//sonra her verb için bütün object’leri deniyorum
//en sonunda string olarak cümleyi üretiyorum
//Bu yüzden sonuçta bütün kombinasyonlar gelir.


//This model creates some clearly nonsensical sentences. We can do beer by
//making the choice of verb dependend on the subject, and the object depend
//on the verb




//Gelişmiş model: Koşullu seçim
//Burada artık daha iyi bir model kuruyorum.
//Yani:
//subject sabit listeden geliyor
//ama verb, seçilen subject’e göre değişiyor
//object de seçilen verb’e göre değişiyor
//Buna conditional distribution mantığı deniyor.
//Yani sonraki seçim, öncekine bağlı.


def verbsFor(subject: String): List[String] =
  subject match {
    case "Noel" => List("wrote", "chased", "slept on")
    case "The cat" => List("meowed at", "chased", "slept on")
    case "The dog" => List("barked at", "chased", "slept on")
  }
def objectsFor(verb: String): List[String] =
  verb match {
    case "wrote" => List("the book", "the letter", "the code")
    case "chased" => List("the ball", "the dog", "the cat")
    case "slept on" => List("the bed", "the mat", "the train")
    case "meowed at" => List("Noel", "the door", "the food cupboard")
    case "barked at" => List("the postman", "the car", "the cat")
  }
def allSentencesConditional: List[(String, String, String)] =
  for {
      subject <- subjects
    verb <- verbsFor(subject)
    obj <- objectsFor(verb)
  } yield (subject, verb, obj)




  //6.10.2 Probabiliঞes


//Şimdiye kadar tüm olasılıkları üretiyordum ama hepsine eşit davranıyordum.
//
//Ama gerçek hayatta bazı sonuçlar daha olasıdır.
//
//Mesela:
//bazı kelimeler daha sık gelir
//bazı cümleler daha doğaldır
//bazı sonuçlar daha yüksek ihtimallidir
//Bu yüzden artık sadece sonucu değil, onun olasılığını da tutmak istiyorum.


//Let’s extend our model to work on List[(A, Double)], where A is the type
//of data we are generaঞng and the Double is a probability. We’re sঞll enumeraঞng all possibiliঞes but we’re now associaঞng a probability with each possible
//outcome


//Start by defining a class Distribution that will wrap a List[(A, Double)].
//(Why?)

//There are no subtypes involved here, so a simple final case class will do.
//We wrap the List[(A, Double)] within a class so we can encapsulate manipulaঞng the probabiliঞes—external
// code can view the probabiliঞes but probably shouldn’t be directly working with them.

final case class Distribution[A](events: List[(A, Double)])


//We should create some convenience constructors for Distribution.
//A useful one is uniform which will accept a List[A] and create a
//Distribution[A] where each element has equal probability. Make it
//so.


def uniform[A](atoms: List[A]): Distribution[A] = {
  val p = 1.0 / atoms.length
  Distribution(atoms.map(a => a -> p))
}
// uniform: [A](atoms: List[A])Distribution[A]


//What are the other methods we must add to implement the models we’ve
//seen so far? What are their signatures?


def flatMap[B](f: A => Distribution[B]): Distribution[B]
//and
def map[B](f: A => B): Distribution[B]


//Now implement these methods. Start with map, which is simpler. We might
//  end up with elements appearing mulঞple ঞmes in the list of events a[er calling
//  map. That’s absolutely ok.


final case class Distribution[A](events: List[(A, Double)]) {
  def map[B](f: A => B): Distribution[B] =
    Distribution(events map { case (a, p) => f(a) -> p })
}


//Now implement flatMap. To do so you’ll need to combine the probability of
//an event with the probability of the event it depends on. The correct way to
//do so is to mulঞply the probabiliঞes together. This may lead to unnormalised
//probabiliঞes—probabiliঞes that do not sum up to 1. You might find the following two uঞliঞes useful, though you don’t need to normalise probabiliঞes or
//  ensure that elements are unique for the model to work.
final case class Distribution[A](events: List[(A, Double)]) {
  def normalize: Distribution[A] = {
    val totalWeight = (events map { case (a, p) => p }).sum
    Distribution(events map { case (a,p) => a -> (p / totalWeight) })
  }
  def compact: Distribution[A] = {
    val distinct = (events map { case (a, p) => a }).distinct
    def prob(a: A): Double =
      (events filter { case (x, p) => x == a } map { case (a, p) => p
      }).sum
    Distribution(distinct map { a => a -> prob(a) })
  }
}


final case class Distribution[A](events: List[(A, Double)]) {
  def map[B](f: A => B): Distribution[B] =
    Distribution(events map { case (a, p) => f(a) -> p })
  def flatMap[B](f: A => Distribution[B]): Distribution[B] =
    Distribution(events flatMap { case (a, p1) =>
      f(a).events map { case (b, p2) => b -> (p1 * p2) }
    }).compact.normalize
  def normalize: Distribution[A] = {
    val totalWeight = (events map { case (a, p) => p }).sum
    Distribution(events map { case (a,p) => a -> (p / totalWeight) })
  }

  def compact: Distribution[A] = {
    val distinct = (events map { case (a, p) => a }).distinct
    def prob(a: A): Double =
      (events filter { case (x, p) => x == a } map { case (a, p) => p
      }).sum
    Distribution(distinct map { a => a -> prob(a) })
  }
}
object Distribution {
  def uniform[A](atoms: List[A]): Distribution[A] = {
    val p = 1.0 / atoms.length
    Distribution(atoms.map(a => a -> p))
  }
}

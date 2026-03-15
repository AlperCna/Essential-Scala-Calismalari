// 3.2 Objects as Funcঞons

//Önceki kısmın son excersie inde şöyle bir şey vardı Adder şeklinde

class Adder(amount: Int) {
  def add(in: Int): Int = in + amount
}
// Bu ne yapıyordu bir sayı ekleyen object oluşturuyordu
//Adder aslında bir hesaplamayı temsil eden bir nesnedir

//Yani adder bir hesaplama nesnesidir bir method gibi davranır ama methoddan farklı olarak bir değer gibi taşınabilir

//Scalada hesaplama yapan nesnelere functions denir
//These objects are
//called funcࢼons, and are the basis of funcࢼonal programming.


//3.2.1 The apply method

//Scalada bir n esnenin fonksiyopn gibi çağrılabilmesi için özel bir yöntem vardır bu yöntem apply methodudur

//In Scala, by convention, an object can be ‘called’ like a function if it has a method called apply.

//Yani bir nesne içinde apply methpdu varsa o nesne fonksiyon gibi çağrılablir

//Function Applicaঞon Syntax
//The method call object.apply(parameter, ...) can also be written as object(parameter, ...)

class Adder(amount: Int) {
  def apply(in: Int): Int = in + amount
}

val add3 = new Adder(3)

add3.apply(2)

add3(4) // shorthand for add3.apply(4)

//Bunun sayesinde nesneler fonksiyon gibi görünür ve kullanılabilir


//3.2.3 Exercises
//  3.2.3.1 When is a Funcঞon not a Funcঞon?
//We’ll get a chance to write some code at the end of the next secঞon. For now
//  we should think about an important theoreঞcal quesঞon:
//  How close does funcঞon applicaঞon syntax get us to creaঞng truly reusable
//    objects to do computaঞons for us? What are we missing?


//Şu anda yaptığımız şey object ve aplly method kullanmak yani function like object oluyor
//Yani scaladaki gerçek fonksiyon sistemi henüz yok
//Eksik olan şeyi scaladaki gerçek fonskiyon tipleri Int te int şeklinde falan

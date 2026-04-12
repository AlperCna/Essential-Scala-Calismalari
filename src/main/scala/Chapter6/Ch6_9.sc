//6.9 Ranges

//So far we’ve seen lots of ways to iterate over sequences but not much in the
//way of iteraঞng over numbers. In Java and other languages it is common to
//write code like

//for(i = 0; i < array.length; i++) {
//  doSomething(array[i])
//}


//Scalada bunun karşılğı range

//Range = sayı dizisi (integer sequence)
//Yani:
//başlangıç
//bitiş
//adım (step) bilgisiyle oluşan bir sayı listesi.

1 until 10
// res0: scala.collection.immutable.Range = Range(1, 2, 3, 4, 5, 6, 7, 8, 9)

//10 dahil değil


//By default the step size is 1, so trying to go from high to low gives us an empty
//Range

10 until 1
// res1: scala.collection.immutable.Range = Range()

//We can recঞfy this by specifying a different step, using the by method on
//Range.

10 until 1 by -1
// res2: scala.collection.immutable.Range = Range(10, 9, 8, 7, 6, 5, 4, 3, 2)

//Now we can write the Scala equivalent of our Java program.

for(i <- 99 until 0 by -1) println(i + " bottles of beer on the wall!"
)
// 99 bottles of beer on the wall!
// 98 bottles of beer on the wall!
// 97 bottles of beer on the wall!


//This gives us a hint of the power of ranges. Since they are sequences we
//can combine them with other sequences in interesঞng ways. For example, to
//create a range with a gap in the middle we can concatenate two ranges:
(1 until 10) ++ (20 until 30)
// res7: scala.collection.immutable.IndexedSeq[Int] = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)


//Note that the result is a Vector not a Range but this doesn’t maer. As they
//are both sequences we can use both them in a for comprehension without any
//code change!

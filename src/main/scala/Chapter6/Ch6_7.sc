//6.7 For Comprehensions Redux

//Bu bölümde for comprehension’ın daha gelişmiş kullanım şekilleri var.
//Yani artık sadece dolaşmak değil, filtreleme, paralel dolaşma, pattern matching ve ara değişken kullanma öğreniyoruz.

//6.7.1 Filtering

//For comprehension içinde if kullanarak elemanları filtreleyebiliriz.

for(x <- Seq(-2, -1, 0, 1, 2) if x > 0) yield x
// res0: Seq[Int] = List(1, 2)

//sequence’i dolaşıyor
//sadece x > 0 olanları alıyor
//diğerlerini atıyor

//Normalde: if (x > 0)
//Ama burada: if x > 0
//Yani parantez yok
//Neden
//Bu aslında: filter veya withFilter çalıştırıyor.
//Kısa mantık
// for + if = filter

//6.7.2 Parallel Iteration

//Another common problem is to iterate over two or more collecঞons in parallel.
//For example, say we have the sequences Seq(1, 2, 3) and Seq(4, 5, 6)
//and we want to add together elements with the same index yielding Seq(5,
//7 , 9). If we write

for {
  x <- Seq(1, 2, 3)
  y <- Seq(4, 5, 6)
} yield x + y
// res1: Seq[Int] = List(5, 6, 7, 6, 7, 8, 7, 8, 9)

//Çünkü bu: iç içe döngü yapar
//Yani:
//1 ile tüm listeyi toplar
//sonra 2 ile tüm listeyi
//sonra 3 ile tüm listeyi


//The soluঞon is to zip together the two sequences, giving a sequence containing pairs of corresponding elements

Seq(1, 2, 3).zip(Seq(4, 5, 6))
// res2: Seq[(Int, Int)] = List((1,4), (2,5), (3,6))

//With this we can easily compute the result we wanted

for(x <- Seq(1, 2, 3).zip(Seq(4, 5, 6))) yield { val (a, b) = x; a + b
}
// res3: Seq[Int] = List(5, 7, 9)


//Someঞmes you want to iterate over the values in a sequence and their indices.
//For this case the zipWithIndex method is provided.

for(x <- Seq(1, 2, 3).zipWithIndex) yield x
// res4: Seq[(Int, Int)] = List((1,0), (2,1), (3,2))

//Kısa mantık
//zip = paralel dolaş
// zipWithIndex = indeksle dolaş


//6.7.3 Paern Matching

//The paern on the le[ hand side of a generator is not named accidentally. We
//can include any paern there and only process results matching the paern.
//This provides another way of filtering results. So instead of

for(x <- Seq(1, 2, 3).zip(Seq(4, 5, 6))) yield { val (a, b) = x; a + b
}
// res5: Seq[Int] = List(5, 7, 9)


for((a, b) <- Seq(1, 2, 3).zip(Seq(4, 5, 6))) yield a + b
// res6: Seq[Int] = List(5, 7, 9)

//val (a, b) = x yazmamıza gerek kalmadı

//6.7.4 Intermediate Results

//It is o[en useful to create an intermediate result within a sequence of generators. We can do this by inserঞng an assignment expression like so:

for {
  x <- Seq(1, 2, 3)
  square = x * x
  y <- Seq(4, 5, 6)
} yield square * y

//Adım adım
//x = 1 → square = 1
//x = 2 → square = 4
//x = 3 → square = 9
//
//Sonra: y ile çarpılıyor

//// res7: Seq[Int] = List(4, 5, 6, 16, 20, 24, 36, 45, 54)
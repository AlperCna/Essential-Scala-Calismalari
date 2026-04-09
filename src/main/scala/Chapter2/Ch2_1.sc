"Hello World"
// çıktıda  gördüğümüz yapı scala terminaldeki ile aynı yapı
// programın veri tipini ve programın ürettiği sonucu verir

"Hello world!".toUpperCase
//Scalada Her şey bir expressiondur,  Expression değer üreten kod parçasına denir.

//Bu yazdıklarımız literal expressionlar yani doğrudan yazılan sabit değer


//Literal expression yazdığımız kod
// Value kodun çalıştırılmış sonucu


"Hello".toUpperCase()
1.toByte
//2_1_1



//Scala içerisinde compile time ve Run time olmak üzere iki kısım bulunuyor
//  diğer dillerde olduğu gibi

 // Compile time kodu kontrol eden zaman

//toUpperCase."Hello world!"
// <console>:2: error: identifier expected but string literal found.
// toUpperCase."Hello world!"


//.toUpperCase
// <console>:13: error: value toUpperCase is not a member of Int
// 2.toUpperCase

// bir değişkeni  0 a bölmek runtime error çıkarır

//2 / 0
//// java.lang.ArithmeticException: / by zero
//// ... 362 elided


//2.1.4.1 Type and Value
//  Using the Scala console or worksheet, determine the type and value of the
//following expressions:

//  1 + 2
// Type int ve value 3

//"3".toInt
// tipi int ve value 3

//"foo".toInt
println("foo")

println("foo".toInt)
// hata veriyor tipi int ama bir value oluşturmuyor.
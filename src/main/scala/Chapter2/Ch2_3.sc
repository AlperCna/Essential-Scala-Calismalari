//2.3 Literal Objects
/*
Bu bölümde scala'daki literal expressions anlatılıyor
Literal programda doğrudan yazılan sabit değerdir

42 tek başına bir literaldir

literal ve Value farkı
literal: programda yazdığımız ifade
value: program çalışınca oluşan gerçek veri

42
Programda yazılan:literal

Program çalışınca oluşan:value = 42

Yani literal:program metninde bulunur

value:bilgisayarın hafızasında bulunur
 */


//2.3.1 Numbers
/*
Scala sayılar için java ile aynı tipleri kullanır
  Int 32 Bit Integer
  Double 64 bit floating point
  Float 32-bit  floating point
  Long 64 bit integers
 */

42

42.0

42.0f

42L

//2.3.2 Booleans
//Boolean javadaki ile aynı true veya false
true
false


// 2.3.3 Characters
//Char tek tırnak içinde yazılır tek karakteri temsil eder
'a'


// 2.3.4 Strings javadaki ile aynı

"this is a string"


"the\nusual\tescape characters apply"

//Escape Characters
//String içinde özel karakterler kullanılabilir.

//  \n  → yeni satır
//\t  → tab


//2.3.5 Null

//Javadakiyle aynı çok kullanılmaz kendi türü vardır null diye

null


//2.3.6 Unit
/*
Unit : () şeklinde yazılır ve javadaki void tipine karşılık gelir

Unit bir expression anlamlı bir değer üretmediğinde kullanılır örneğin println sadece ekrana yazdırır bir değer üretmez
Bu nedenle sonucu unit dir

Unit çok önemlidir çünkü Scalada birçok yapı expressiondur ve her expressionun bir type ve value değeri olmalıdır
Ama bazı exğressionlar anlamlı değer üretemez bu durumda Scala Unit tipini kullanır.
 */





//2.3.8 Exercises
//  2.3.8.1 Literally Just Literals
//  What are the values and types of the following Scala literals?

42
true
123L
42.0

//
//2.3.8.2 Quotes and Misquotes
//  What is the difference between the following literals? What is the type and
//value of each?

'a'
"a"

//biri char biri string

//2.3.8.4 Learning By Mistakes
//  What is the type and value of the following literal? Try wriঞng it on the REPL
//  or in a Scala worksheet and see what happens!
  'Hello world'

//Error çünkü tk tırnak char belirtirken kullanılır
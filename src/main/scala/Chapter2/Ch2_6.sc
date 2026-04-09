// 2.6 Compound Expressions
// Conditionals (if ifadeleri)
//Blocks (blok ifadeleri)

//2.6.1 Condiঞonals
//Bir koşula göre hangi expression’ın çalışacağını seçer.

if(1 < 2) "Yes" else "No"

/*
Condiঞonals are Expressions
Scala’s if statement has the same syntax as Java’s. One important difference is that Scala’s
condiࢼonal is an expression—it has a type and returns a value.

 */


//Scala'da if bir expressiondur yani typı vardır ve value üretir

if(1 < 2) println("Yes") else println("No")

/*
Condiঞonal Expression Syntax
The syntax for a condiঞonal expression is

if(condition)
trueExpression
else
falseExpression

where

• condition is an expression with Boolean type;
• trueExpression is the expression evaluated if condition evaluates to true; and
• falseExpression is the expression evaluated if condition
evaluates to false.

 */

//2.6.2 Blocks
//Birden fazla expressionu bir arada çalıştıran yapıdır

// {} içinde yazılır

//Şçşndeki ifadeleri sırayla çalıştırır son expression'ın değerini döndürür

{ 1; 2; 3 }

//çünkü block sonucu son expressiondur

//Block genellikle şu durumlarda kullanılır: birden faza işlem yapılacaksa ve side efect gerekiyorsa

{
  println("This is a side-effect")
  println("This is a side-effect as well")
}

// Ayrıca blok içinde geçici değişklenler tanımlayabiliriz
//We can also use a block when we want to name intermediate results, such as

def name: String = {
  val title = "Professor"
  val name = "Funkenstein"
  title + " " + name
}

name

/*

Block Expression Syntax
The syntax of a block expression is
{
declarationOrExpression ...
expression
}
where
• the opঞonal declarationOrExpressions are declaraঞons or
expression; and
• expression is an expression determining the type and value of
the block expression
 */

//2.6.4 Exercises
//  2.6.4.1 A Classic Rivalry
//  What is the type and value of the following condiঞonal?

if (1>2) "alien" else "predator"
//type string value predator


//2.6.4.2 A Less Well Known Rivalry
//  What about this condiঞonal?
if(1 > 2) "alien" else 2001

// iki farklı  tip olduğu için scala ortak tip bulur ve Any olur value 2001

//2.6.4.3 An if Without an else
//  What about this condiঞonal?
if(false) "hello"

//bu durumda Unit döner ()

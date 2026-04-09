//2.2 Objeler Önceki kısımda expression types ve values leri görmüştük ve bütün valueların
//obje olduğunu öğrendik



//2.2.1 Obje veri ve o veri üzerine yapılan işlemlerin bir arada bulunduğu yapıdır.
// Obje 2 şey içerir data ve operations 2 e bir objedir
//datası int 2 işlemleri ise matematiksel işlemlerdir

//Operaitons metgodlar olarak bilinir yani obje üzerinde yapılan işlemlerdir.
// Field ise obje içinde saklanan veridir


// 2.2.2 Method Calls
// Objelerle etkileşim kurmanın yolu method çağırmaktır. Örneğin önceki kısımlarda gördüğümüz toUpperCase yapısı methoddur

"hello".toUpperCase()

//Bazı metodlar çalışmak için parametre isterler bu metodun nasıl çalıştığını kontrol eder
//Örneğin bir stringdem belirli sayıda karakter almak için parametre kullanırız

"abcdef".take(3)

//Method Call Syntax

//anExpression.methodName(param1, ...)
//or
//anExpression.methodName

//• anExpression is any expression (which evaluates to an object)
//• methodName is the name of the method
//• the opঞonal param1, ... is one or more expressions evaluaঞng
//  to the parameters to the method

"Hello".toUpperCase
//Scala'da method çağrıları bir expressiondur yani yeni bir value üretir bu yüzden de metodlar zincir halinde kullanılabilir
"hello".toUpperCase.toLowerCase

//Metod çağrılarında evaluation şu sırayla olur önce ana expression çalışır sonra parametreler hesaplanır en son method çağırılır yani soldan sağa bir yapı vardır
"Hello world!".take(2 + 3)
//expression "Hello world!" is evaluated first, then 2 + 3 (which requires
//evaluating 2 and then 3 first), then finally "Hello world!".take(5).


// 2.2.3 Operators

//Scala'da neredeyse h er şey method'tur bu nedenle matematiksel peratorler bile aslında method'tur.
//Her değer ir nesne olduğu için int ve boolean gibi temel veri tipleri üzerinde de metodlar çağırılabilir
//Javada int ve boolean object olmadığı için böyle bir durum yoktur.

123.toShort //'Short' Scala2 da bu şekilde tanımlanıyormuş

123.toByte //Byte da ayn şekilde

//Eğer int bir objeyse basit matematik operatörler de method mudur Evet!

43-3+2

43.-(3).+(2) // Eski syntax Scala 2.10

/*
Scala'da method çağrıları bazen daha okunabilir olması için infix notationda da yazılabilir
Any Scala expression writen a.b(c) can also be writen a b c.

Tek parametre alan her method infix operator notationa göre yazılabilir
"the quick brown fox" split " "


Scala'da operatörler matematikteki gibi öncelik kurallarına sahiptir
*/

2*3+4*5

(2*3) + (4*5)

2 * (3 + 4) * 5

/*
2.2.4 Take Home Points
Scalada tüm valuelar objedir, Objelerle etkileşim kurmanın yolu method çağırmaktır

The syntax for a method call is
  anExpression.methodName(parameter, ...)
or
anExpression methodName parameter

Scala’da operatörlerin çoğu aslında method’tur.

Scala’da kod yazarken: expressions ,types, values kavramlrını birlikte düşünmek gerekir.

Bu yaklaşım Scala kodunu daha kısa ve daha anlaşılır hale getirir.
*/


//2.2.5 Exercies

//2.2.5.1 Operator Style
//Rewrite in operator-style
"foo".take(1)
"foo" take 1

1+2+3
1.+(2).+(3)

/*
2.2.5.2 Subsঞtuঞon
  What is the difference between the following expressions? What are the similariঞes?
  1 + 2 + 3
   6

 İki ifade de aynı value'yı üretir ikisi de int değerdir.

 İlki hesaplama yapan expressiondur diğeri doğrudan literal value'dır
 */

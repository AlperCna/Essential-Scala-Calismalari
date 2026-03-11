// 2.5 Writing Methods
// Method yazarlen 6 adımlı sistematik bir yöntem varmış bu kısım onu anlatıyor

//Define an object called calc with a method square that accepts a Double as an
//argument and… you guessed it… squares its input. Add a method called cube that
//  cubes its input, calling square as part of its result calculaࢼon.  üzerinde çalışılacak kısım bu

/*
2.5.1 Idenঞfy the Input and Output
İlk olarak methodun hangi veri tipini aldığını ve hangi veri tipini döndürdüğünü bilmemiz gerekiyor
Örnekte veri tipimiz Double

2.5.2 Prepare Test Cases

Sadece type bilmek yetmez.

Çünkü: Double Double olan çok fazla fonksiyon olabilir.
Bu yüzden test case yazılır.

Scala’da basit test için: assert kullanılır.

square için test
assert(square(2.0) == 4.0)
assert(square(3.0) == 9.0)
assert(square(-2.0) == 4.0)



2.5.3 Write the Declaraঞon
 Bu adımda methodun gövdesi olmadan sadece tanımı yazılıyor ??? kullanarak henüz implemente edilmediği söyleniyor

def square(in: Double): Double =
???

2.5.4 Run the Code
Şimdiki adımda kod çalıştırılıyor compile oluyor mu testler gerçekten fail oluyor mu gibisinden

2.5.5 Write the Body
Şimdiki adımda methodun içi yazılıyor

2.5.5.1 Consider the Result Type
İlk teknik dönüş tipine bakmak bizim durumumuzda double. Double değeri nasıl oluşturabiliriz. Bir literal kullanabiliriz ama bu doğru olmaz.
Bir method yyazabiliriz ve bu da bizi bir sonraki teknike götürür

2.5.5.2 Consider the Input Type
Input paremetreye baktık bizim input parametremiz Double. We bi double oluşturmamız gerekriğine karar verdik bununla ne yapabiliri<
Square yapmak için " doğru operasyon sonuç olarak methodumuzu yazabiliriz içini doldurup

def square(in: Double): Double =
in * in

2.5.6 Run the Code, Again
Sonuç olarak kodu tekrardan çalıştırırız





Process for Wriঞng Methods
We have a six-step process for wriঞng methods in a systemaঞc way.
1. Idenঞfy the type of the inputs and output of the method.
2. Write some test cases for the expected output of the method
given example input. We can use the assert funcঞon to write
down these cases.
3. Write the method declaraঞon using ??? for the body like so:
def name(parameter: type, ...): resultType =
???
4. Run the code to check the test cases do in fact fail.
5. Write the body of the method. We currently have two techniques to apply here:
• consider the result type and how we can create an instance of it;
and
• consider the input type and methods we can call to transform it
to the result type.

6. Run the code again and check the test cases pass.


 */


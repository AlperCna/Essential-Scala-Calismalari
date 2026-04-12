import scala.util.Try
//6.6 Monads


//We’ve seen that by implemenঞng a few methods (map, flatMap, and opঞonally filter and foreach), we can use any class with a for comprehension. In
//the previous chapter we learned that such a class is called a monad. Here we
//are going to look in a bit more depth at monads.


//Monad, hesaplamaları sırayla yapmamızı sağlayan bir yapı gibi düşünülebilir.
//Ama bunu yaparken bazı teknik dertleri bizim yerimize halleder.
//Yani biz sadece: “önce bunu al” “sonra bununla bunu yap” “en sonunda şu sonucu üret” diye düşünürüz.
//Arka tarafta ise monad: değer var mı yok mu birden fazla sonuç mu var sonuç henüz gelmedi mi işlemler nasıl bağlanacak gibi ayrıntıları yönetir.

//6.6.1 What’s in a Monad?

//Monad kavramını açıklamak zordur çünkü çok geneldir.
//
//Çünkü monad tek bir veri yapısı değildir. Daha çok ortak bir çalışma mantığıdır.
//Yani farklı yapılar aynı “monad gibi davranma” özelliğine sahip olabilir.
//
//Kitap bu yüzden teoriye boğmak yerine örneklerle anlatıyor. Bu en iyi yaklaşım.

//Monad nedir?
//Monad, genel olarak şöyle düşünülebilir:
//Bir generic type’tır ve hesaplamaları sırayla bağlamamıza yardım eder.
//Ama bunu yaparken bazı teknik ayrıntıları soyutlar, yani gizler.


//1. “Generic type” ne demek?
//Mesela:
//Option[Int]
//Seq[Int]
//Future[Int]

//Bunların hepsi bir tür kabın içindeki değer gibi düşünülebilir.
//Yani sadece düz Int ile uğraşmıyoruz.
//Onun yerine:
//belki vardır belki yoktur
//bir tane değildir, bir sürüdür
//henüz hazır değildir, gelecekte gelecek gibi özel durumlar vardır.

//Monad işte bu tarz “sarmalanmış” değerlerle çalışmayı kolaylaştırır.


//2 “Sequence computations” ne demek?
//Bu, işlemleri sırayla yapmak demek.
//
//Mesela:
//önce ilk sayıyı al
//sonra ikinci sayıyı al
//sonra topla

//Bu normalde çok basit görünüyor. Ama eğer: sayı yoksa birden fazla sayı varsa sayı henüz gelmemişse işler karışıyor.

//Monad burada devreye giriyor ve diyor ki: “Sen sadece işlemin mantığını yaz. Teknik ayrıntıyı ben hallederim.”


//Monad, hesaplamaları sıraya koymamızı sağlar ama bazı teknik detayları soyutlar.
//Yani biz asıl problemimize odaklanırız.
//Monad ise arkadaki plumbing işini yapar.
//“Plumbing” burada mecazi anlamda kullanılıyor. Yani boru tesisatı gibi düşünüyoruz:
//parçalar nasıl bağlanacak
//veri nasıl akacak
//hata olursa ne olacak
//gibi teknik bağlantıları monad hallediyor.



//Örnek 1: Option bir monaddır
//Option neyi soyutlar?
//Option ile çalışırken teknik problem şudur:
//“Değer var mı yok mu?”
//Mesela:
//Some(5)
//None
//Eğer değer yoksa normalde sürekli kontrol yapmak gerekir:
//bu değer var mı?
//yoksa dur
//varsa devam et
//Ama Option monad olduğu için bunları map, flatMap, for ile çok temiz şekilde yazabiliyoruz.

for {
  a <- getFirstNumber // getFirstNumber returns Option[Int]
  b <- getSecondNumber // getSecondNumber returns Option[Int]
} yield a + b
// The final result is an Option[Int]---the result of
// applying `+` to `a` and `b` if both values are present


//Burada:
//getFirstNumber → Option[Int]
//getSecondNumber → Option[Int]
//Yani iki sayı da aslında garanti değil. Biri None olabilir.
//Ama kodun görünüşüne bakınca biz sadece şunu yazıyoruz:
//a’yı al
//b’yi al
//topla
//
//Biz “biri yoksa ne olacak?” kısmını yazmıyoruz.
//Onu Option’ın flatMap ve map mantığı hallediyor.

//ikisi de varsa → Some(a + b)
//biri bile yoksa → None
//Yani burada monad bize şu kolaylığı sağlıyor:
//“Değerlerin var olup olmadığını dert etmeden mantığı yaz.”

//
//Örnek 2: Seq bir monaddır
//Seq neyi soyutlar?
//Seq ile teknik problem farklıdır.
//  Burada sorun “değer var mı yok mu?” değil.
//  Burada sorun “bir sürü olası değer var” olmasıdır.
//  Mesela
//  ilk tarafta birçok sayı olabilir
//ikinci tarafta da birçok sayı olabilir
//Bunların tüm kombinasyonları düşünülmelidir.
//
//Normalde bunu elle yapmaya kalksak:
//
//  dış döngü
//    iç döngü
//    her eşleşmeyi toplama
//
//gibi uğraşmak gerekir.
//
//  Ama Seq monad olduğu için yine aynı yapıyı yazabiliyoruz:

for {
  a <- getFirstNumbers // getFirstNumbers returns Seq[Int]
  b <- getSecondNumbers // getSecondNumbers returns Seq[Int]
} yield a + b
// The final result is a Seq[Int]---the results of
// applying `+` to all combinations of `a` and `b`


//Diyelim:
//getFirstNumbers = Seq(1, 2)
//getSecondNumbers = Seq(10, 20)
//O zaman bu yapı şunları üretir:
//1 + 10
//1 + 20
//2 + 10
//2 + 20
//Yani bütün kombinasyonları hesaplar.
//Sonuç:
//Seq(11, 21, 12, 22)

//Burada monad bize şunu düşündürmüyor:
//kaç kombinasyon var?
//iç içe döngü nasıl kurulacak?
//sonuçlar nasıl toplanacak?
//Biz sadece mantığı yazıyoruz:
//“a ile b’yi topla.”
//Gerisini Seq hallediyor.



//Örnek 3: Future bir monaddır
//Future neyi soyutlar?
//Burada teknik problem şu:
//Sonuç şu anda elimizde değil.
//Daha sonra gelecek. Yani işlem asenkron.
//Bu durumda normalde:
//bekleme
//callback
//zamanlama
//bloklama olmaması gibi dertler olur.
//Ama Future monad olarak kullanıldığında yine aynı yazım ortaya çıkıyor:

for {
  a <- getFirstNumber // getFirstNumber returns Future[Int]
  b <- getSecondNumber // getSecondNumber returns Future[Int]
} yield a + b
// The final result is a Future[Int]---a data structure
// that will eventually allow us to access the result of
// applying `+` to `a` and `b`


//Kod görünüşte yine aynı.
//Ama bu kez:
//  a bir Future[Int] içinden geliyor
//b de öyle
//Yani sonuç hemen hazır değil.
//  Ama biz yine “iki sayıyı topla” diye düşünüyoruz.
//
//Monad burada bize şunu sağlıyor:
//
//  Asenkronluğu düşünmeden işlem mantığını yazabilmek.


// Problemlerdi yorum satırlarını kaldırırsak aslında üç örnek de aynı gözüküyor ama aslında 3 farklı problem var
//değer olabilir veya olmayabilir → Option
//birden fazla değer olabilir → Seq
//değer sonra gelecek olabilir → Future


//Monadlar problemin bir kısmını unutmamıza izin verir.
//Buradaki “unutmak” kötü anlamda değil.
//Yani o kısmı düşünmek zorunda kalmıyoruz.
//Mesela:
//Option ile uğraşırken “değer var mı?” derdi arka plana gider
//Seq ile uğraşırken “kaç kombinasyon var?” derdi arka plana gider
//Future ile uğraşırken “ne zaman gelecek?” derdi arka plana gider
//Biz ise sadece iş mantığına odaklanırız.
//Burada iş mantığı örnekte sadece:
//iki sayıyı toplamak






//6.6.2 Exercises
//6.6.2.1 Adding All the Things ++
//We’ve already seen how we can use a for comprehension to neatly add together three opঞonal values. Let’s extend this to other monads. Use the following definiঞons:
//import scala.util.Try
val opt1 = Some(1)
val opt2 = Some(2)
val opt3 = Some(3)
val seq1 = Seq(1)
val seq2 = Seq(2)
val seq3 = Seq(3)
val try1 = Try(1)
val try2 = Try(2)
val try3 = Try(3)
//Add together all the opঞons to create a new opঞon. Add together all the
//sequences to create a new sequence. Add together all the trys to create a
//new try. Use a for comprehension for each. It shouldn’t take you long!



for {
  x <- opt1
  y <- opt2
  z <- opt3
} yield x + y + z
for {
  x <- seq1
  y <- seq2
  z <- seq3
} yield x + y + z
for {
  x <- try1
  y <- try2
  z <- try3
} yield x + y + z
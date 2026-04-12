//7.6 Combining Type Classes and Type Enrichment

//Implicit class tek başına kullanılabilir ama genelde type class ile birlikte kullanılır.

//Neden birlikte kullanıyoruz?
//
//Şu iki şeyi aynı anda istiyoruz:
//1. Type class avantajları
//Aynı tipe farklı davranış
//Dış kütüphane tiplerine destek
//Esneklik

//2. OOP gibi kullanım
//p.toHtml gibi doğal syntax


implicit class HtmlOps[T](data: T) {
  def toHtml(implicit writer: HtmlWriter[T]) =
    writer.toHtml(data)
}

//This allows us to invoke our type-class paern on any type for which we have
//an adapter as if it were a built-in feature of the class:


new ExtraStringMethods("the quick brown fox").numberOfVowels

//This gives us many benefits. We can extend exisঞng types to give them new
//funcঞonality, use simple syntax to invoke the funcঞonality, and choose our
//preferred implementaঞon by controlling which implicits we have in scope.
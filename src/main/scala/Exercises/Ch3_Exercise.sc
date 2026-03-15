//Class

class Person(firstName :String ,lastName: String) {

  val firstname =firstName
  val lastname = lastName
  def name = firstname + " " + lastname
}

val Alper = new Person("Alper","Can")
Alper.firstname
Alper.lastname
Alper.name

// ya da

class Person1 (val firstName: String, val lastName: String) {
  def name = firstName + " " + lastName
}

//Type person
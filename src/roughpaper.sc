

case class Person(firstName: String, lastName: String)

val me = Person("Daniel", "Spiewak")
val first = me.firstName
val last = me.lastName

if (me == Person(first, last)) {
  println("Found myself!")
  println(me)
}



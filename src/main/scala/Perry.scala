import akka.actor.Actor.Receive
import akka.actor.{Props, ActorSystem, Actor, ActorLogging}

case class Ticket(quantity: Int)
case class FullPitcher(number:Int)
case class EmptyPitcher(number:Int)

class Bartender extends Actor with ActorLogging{
  var total =0;
  override def receive: Receive = {

    case Ticket(amount) =>
      total = total + amount

      log.info(s"i am sending you the pitcher ${sender.path.name}")

      Thread.sleep(1000)

      for( number <- 1 to amount) {
        log.info(s"beer is ready! ${sender.path.name}")

      sender ! FullPitcher(number)
      }

    case EmptyPitcher(number) =>
      total match {
        case 1 =>
          log.info("we are closing, time to go dude!")
          context.system.shutdown()

        case n =>
          //if(n>0)
          total = total - 1
          log.info(s"you are a quick drinker ${sender.path.name}! ")
      }

      log.info(s"Total is: $total")
  }
}

class Person extends Actor with ActorLogging {
  override def receive: Actor.Receive ={

    case FullPitcher(number) =>
      log.info("Cheers Mate")
      sender ! EmptyPitcher(number)
  }
}

object Perry extends App {

  val system = ActorSystem("PerryGezelligBar")

  val bartender = system.actorOf(Props(new Bartender),"Hank")
  val kelly = system.actorOf(Props(new Person),"Kelly")
  val sjoerdJob = system.actorOf(Props(new Person) , "SjoerdJob")
  val nate = system.actorOf(Props(new Person), "Nate")

  bartender.tell(Ticket(10) ,kelly )
  bartender.tell(Ticket(1) ,sjoerdJob )
  bartender.tell(Ticket(3) ,nate )


}

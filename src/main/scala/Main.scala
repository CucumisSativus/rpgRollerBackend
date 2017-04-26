import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("rpgRoller")
    implicit val materializer = ActorMaterializer()
  }
}

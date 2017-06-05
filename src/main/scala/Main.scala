import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import net.cucumbersome.rpgRoller.warhammer.player.{ActorsController, CombatActor, InMemoryActorRepository, Statistics}

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("rpgRoller")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val repo = new InMemoryActorRepository(initialCombatActors)
    val controller = new ActorsController(repo)

    Http().bindAndHandle(controller.route, "localhost", 8080)

  }

  def initialCombatActors: List[CombatActor] = List(
    CombatActor(
      id = new CombatActor.Id("1"),
      name = new CombatActor.Name("Player 1"),
      hp = new CombatActor.Health(10),
      statistics = Statistics.buildUnsafe(1,2,3,4,5,6,7,8,9,10)
    ),
     CombatActor(
      id = new CombatActor.Id("2"),
      name = new CombatActor.Name("Player 2"),
      hp = new CombatActor.Health(20),
      statistics = Statistics.buildUnsafe(2,3,4,5,6,7,8,9,10,11)
    )
  )
}

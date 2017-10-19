import akka.actor.ActorSystem
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import net.cucumbersome.rpgRoller.warhammer.player.{ActorsController, CombatActor, InMemoryActorRepository, Statistics}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
import net.cucumbersome.rpgRoller.warhammer.swagger.SwaggerDocService
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import akka.http.scaladsl.server.Directives._
object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("rpgRoller")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val repo = new InMemoryActorRepository(initialCombatActors)
    val controller = new ActorsController(repo)

    val conf = ConfigFactory.load
    val port = conf.getInt("port")
    val domain = conf.getString("domain")

    val swaggerService = new SwaggerDocService(domain, port)
    val routes =  cors()(controller.route ~ swaggerService.routes)
    Http().bindAndHandle(routes, domain, port)

  }

  def initialCombatActors: List[CombatActor] = List(
    CombatActor(
      id = new CombatActor.Id("1"),
      name = new CombatActor.Name("Player 1"),
      hp = new CombatActor.Health(10),
      statistics = Statistics(
        1.toWs,
        2.toBs,
        3.toStr,
        4.toTg,
        5.toAg,
        6.toIt,
        7.toPer,
        8.toWp,
        9.toFel,
        10.toInfl
      )
    ),
     CombatActor(
      id = new CombatActor.Id("2"),
      name = new CombatActor.Name("Player 2"),
      hp = new CombatActor.Health(20),
      statistics = Statistics(
        2.toWs,
        3.toBs,
        4.toStr,
        5.toTg,
        6.toAg,
        7.toIt,
        8.toPer,
        9.toWp,
        10.toFel,
        11.toInfl
      )
    )
  )
}

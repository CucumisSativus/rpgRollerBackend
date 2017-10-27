import akka.actor.ActorSystem
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import net.cucumbersome.rpgRoller.warhammer.player.{ActorsController, CombatActor, Statistics}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
import net.cucumbersome.rpgRoller.warhammer.swagger.SwaggerDocService
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import akka.http.scaladsl.server.Directives._
import net.cucumbersome.rpgRoller.warhammer.combat.{CombatController, CombatHandler}
import net.cucumbersome.rpgRoller.warhammer.infrastructure.CommandGateway
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.InMemoryActorRepository
import org.slf4j.LoggerFactory
object Main {
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(getClass)

    implicit val system = ActorSystem("rpgRoller")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher
    val config = ConfigFactory.load

    val repo = new InMemoryActorRepository(initialCombatActors)
    val actorsController = new ActorsController(repo)
    logger.info("Initialized actors controller")

    val port = config.getInt("endpoint.port")
    val domain = config.getString("endpoint.domain")

    val combatHandler = system.actorOf(CombatHandler.props("combatHandler"), "combatHandler")
    val commandGateway = system.actorOf(CommandGateway.props(combatHandler), "commandGateway")

    val combatController = new CombatController(commandGateway, repo)

    logger.info("initialized combat controller")
    val swaggerService = new SwaggerDocService(domain, port)
    logger.info("initialized swagger service")

    val routes =  cors()(combatController.route ~ actorsController.route ~ swaggerService.routes)
    logger.info("application ready!")

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

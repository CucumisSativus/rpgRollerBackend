import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.mongodb.event.{ClusterClosedEvent, ClusterDescriptionChangedEvent, ClusterListener, ClusterOpeningEvent}
import com.typesafe.config.{Config, ConfigFactory}
import net.cucumbersome.rpgRoller.warhammer.combat.{CombatController, CombatHandler}
import net.cucumbersome.rpgRoller.warhammer.infrastructure.CommandGateway
import net.cucumbersome.rpgRoller.warhammer.infrastructure.mongo.CollectionBuilder
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.{ActorRepository, MongoDbActorRepository}
import net.cucumbersome.rpgRoller.warhammer.player.ActorsController
import net.cucumbersome.rpgRoller.warhammer.swagger.SwaggerDocService
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.{MongoClient, MongoClientSettings, ServerAddress}
import org.slf4j.LoggerFactory

import collection.JavaConverters._

object Main {
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(getClass)

    implicit val system = ActorSystem("rpgRoller")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher
    val config = ConfigFactory.load

    val repo = initializeActorsRepository(config)
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

    val routes = cors()(combatController.route ~ actorsController.route ~ swaggerService.routes)
    logger.info("application ready!")

    Http().bindAndHandle(routes, domain, port)
  }

  def initializeActorsRepository(config: Config): ActorRepository = {
    val mongoUri = config.getString("mongo.uri")
    val mongoDatabaseName = config.getString("mongo.database")
    val mongoClient = MongoClient(mongoUri)
    val database = mongoClient.getDatabase(mongoDatabaseName)
    val actorCollection = CollectionBuilder.buildActorsCollection(database)
    new MongoDbActorRepository(actorCollection)
  }


  case object MongoClusetListener extends ClusterListener {
    val logger = LoggerFactory.getLogger(getClass)

    override def clusterOpening(event: ClusterOpeningEvent): Unit = {
      logger.info(s"Cluster opened $event")
    }

    override def clusterClosed(event: ClusterClosedEvent): Unit = {
      logger.info(s"Cluster close $event")
    }

    override def clusterDescriptionChanged(event: ClusterDescriptionChangedEvent): Unit = {
      logger.info(s"Cluster description changed $event")
    }
  }

}

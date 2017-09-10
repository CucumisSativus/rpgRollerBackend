package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import net.cucumbersome.RouteSpec
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.InitCombat
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.player.{ActorRepository, CombatActor, CombatActorPresenter, InMemoryActorRepository}
import net.cucumbersome.test.MockedCombatIdGenerator
import spray.json._

class CombatControllerSpec extends RouteSpec {
  "A combat controller" when {
    "initializing new combat" should {
      val expectedId = "myId:3"
      val generator = MockedCombatIdGenerator(expectedId)
      "initialize it without any actors" in {
        val handler = buildHandler
        val repository = new InMemoryActorRepository(List())
        val route = getRoute(handler, repository, generator)

        val requestBody = CreateCombatParameters(List()).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe CombatPresenter(expectedId, List()).toJson
        }

      }

      "initialize it with actors" in {
        val actors = random[CombatActor](2).toList
        val handler = buildHandler
        val repository = new InMemoryActorRepository(actors)
        val route = getRoute(handler, repository, generator)

        val requestBody = CreateCombatParameters(actors.map(_.id.data)).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe CombatPresenter(expectedId, actors.map(CombatActorPresenter.fromCombatActor.get)).toJson
        }
      }
    }
    "adding actor to an existing combat" should {
      "add actors if combat is there" in {
        val handler = buildHandler
        val combatId = "combatId"
        val (actor1, actor2, actor3, actor4) = build4Actors
        val actors = List(actor1, actor2, actor3, actor4)
        val repository = new InMemoryActorRepository(List(actor1, actor2, actor3, actor4))

        val requestBody = AddActorsToCombatParameters(combatId, List(actor3.id.data, actor4.id.data)).toJson.compactPrint

        handler ! InitCombat(combatId, List(actor1, actor2))

        val route = getRoute(handler, repository)
        Patch(s"/combat/$combatId/add-actors").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe CombatPresenter(combatId, actors.map(CombatActorPresenter.fromCombatActor.get)).toJson
        }

      }
    }
  }

  def getRoute(handler: ActorRef, repo: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator): Route = {
    new CombatController(handler, repo, idGenerator).route
  }

  def build4Actors: (CombatActor, CombatActor, CombatActor, CombatActor) = {
    random[CombatActor](4) match {
      case a1 :: a2 :: a3 :: a4 :: Nil => (a1, a2, a3, a4)
    }
  }

  def generateId: String = {
    UUID.randomUUID().toString
  }

  def buildHandler: ActorRef = {
    val id = UUID.randomUUID().toString
    system.actorOf(CombatHandler.props(id), id)
  }
}

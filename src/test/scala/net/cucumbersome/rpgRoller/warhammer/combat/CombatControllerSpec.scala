package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import net.cucumbersome.RouteSpec
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.InitCombat
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.infrastructure.CommandGateway
import net.cucumbersome.rpgRoller.warhammer.player.{ActorRepository, CombatActor, InMemoryActorRepository}
import net.cucumbersome.test.MockedCombatIdGenerator
import spray.json._

class CombatControllerSpec extends RouteSpec {
  "A combat controller" when {
    val expectedId = "myId:3"
    val generator = MockedCombatIdGenerator(expectedId)
    "initializing new combat" should {
      "initialize it without any actors" in {
        val gateway = buildGateway
        val repository = new InMemoryActorRepository(List())
        val route = getRoute(gateway, repository, generator)

        val requestBody = CreateCombatParameters(List()).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe CombatPresenter(expectedId, List()).toJson
        }

      }

      "initialize it with actors" in {
        val actors = random[CombatActor](2).toList
        val gateway = buildGateway
        val repository = new InMemoryActorRepository(actors)
        val route = getRoute(gateway, repository, generator)

        val requestBody = CreateCombatParameters(actors.map(_.id.data)).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          val inCombatActorPresenters = actors.map(a => InCombatActor.buildFromCombatActor(a, idGenerator = mockedActorIdGenerator(generator))).map(InCombatActorPresenter.fromInCombatActor.get)
          val presenter = CombatPresenter(expectedId, inCombatActorPresenters)
          responseAs[String].parseJson mustBe presenter.toJson
        }
      }
    }
    "adding actor to an existing combat" should {
      "add actors if combat is there" in {
        val gateway = buildGateway
        val combatId = "combatId"
        val (actor1, actor2, actor3, actor4) = build4Actors
        val actors = List(actor1, actor2, actor3, actor4)
        val repository = new InMemoryActorRepository(List(actor1, actor2, actor3, actor4))

        val requestBody = AddActorsToCombatParameters(combatId, List(actor3.id.data, actor4.id.data)).toJson.compactPrint

        gateway ! InitCombat(combatId, List(actor1, actor2).map(a => InCombatActor.buildFromCombatActor(a, idGenerator = mockedActorIdGenerator(generator))))

        val route = getRoute(gateway, repository, generator)
        Patch(s"/combat/$combatId/add-actors").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          val inCombatActorPresenters = actors.map(a => InCombatActor.buildFromCombatActor(a, idGenerator = mockedActorIdGenerator(generator))).map(InCombatActorPresenter.fromInCombatActor.get)
          val presenter = CombatPresenter(combatId, inCombatActorPresenters)
          responseAs[String].parseJson mustBe presenter.toJson
        }

      }
    }

    "removing actor from an existing combat" should {
      "remove actors if combat is there" in {
        val gateway = buildGateway
        val combatId = "combatId"
        val (actor1, actor2, actor3, actor4) = build4Actors
        val actors = List(actor1, actor2, actor3, actor4).map(a => InCombatActor.buildFromCombatActor(a))
        val actorsWhichShouldBeKept = actors.take(2)
        val actorsToBeRemoved = actors.diff(actorsWhichShouldBeKept)
        val repository = new InMemoryActorRepository(List(actor1, actor2, actor3, actor4))

        val requestBody = RemoveActorsFromCombatParameters(combatId, actorsToBeRemoved.map(_.id.data)).toJson.compactPrint

        gateway ! InitCombat(combatId, actors)

        val route = getRoute(gateway, repository, generator)
        Patch(s"/combat/$combatId/remove-actors").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          val inCombatActorPresenters = actorsWhichShouldBeKept.map(InCombatActorPresenter.fromInCombatActor.get)
          val presenter = CombatPresenter(combatId, inCombatActorPresenters)
          responseAs[String].parseJson mustBe presenter.toJson
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

  def buildGateway: ActorRef = {
    val id = UUID.randomUUID().toString
    val combatHandler = system.actorOf(CombatHandler.props(id), id)
    system.actorOf(CommandGateway.props(combatHandler))
  }

  def mockedActorIdGenerator(idGenerator: MockedCombatIdGenerator): () => InCombatActor.Id = () => InCombatActor.Id(idGenerator.generateId)
}

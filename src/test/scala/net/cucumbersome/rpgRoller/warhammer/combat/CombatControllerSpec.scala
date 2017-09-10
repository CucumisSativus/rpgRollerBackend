package net.cucumbersome.rpgRoller.warhammer.combat

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import akka.testkit.TestProbe
import net.cucumbersome.RouteSpec
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.CombatIdGenerator
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.InitCombat
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.player.{ActorRepository, CombatActor, InMemoryActorRepository}
import net.cucumbersome.test.MockedCombatIdGenerator
import spray.json._

class CombatControllerSpec extends RouteSpec {
  "A combat controller" when {
    "initializing new combat" should {
      val expectedId = "myId:3"
      val generator = MockedCombatIdGenerator(expectedId)
      "initialize it without any actors" in {
        val handler = TestProbe()
        val repository = new InMemoryActorRepository(List())
        val route = getRoute(handler, repository, generator)
        val expectedCommand = InitCombat(expectedId, List())

        val requestBody = CreateCombatParameters(List()).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe NewCombat(expectedId).toJson
        }

        handler.expectMsg(expectedCommand)
      }

      "initialize it with actors" in {
        val actors = random[CombatActor](2).toList
        val handler = TestProbe()
        val repository = new InMemoryActorRepository(actors)
        val expectedCommand = InitCombat(expectedId, actors)
        val route = getRoute(handler, repository, generator)

        val requestBody = CreateCombatParameters(actors.map(_.id.data)).toJson.compactPrint
        Get("/combat/new").withEntity(ContentTypes.`application/json`, requestBody) ~> route ~> check {
          responseAs[String].parseJson mustBe NewCombat(expectedId).toJson
        }

        handler.expectMsg(expectedCommand)
      }
    }
  }

  def getRoute(handler: TestProbe, repo: ActorRepository, idGenerator: CombatIdGenerator): Route = {
    new CombatController(handler.ref, repo, idGenerator).route
  }
}

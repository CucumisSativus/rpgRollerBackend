package net.cucumbersome.rpgRoller.warhammer.player

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorPresenter.fromCombatActor
import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor
import spray.json._
class ActorsControllerSpec extends UnitSpec with ScalatestRouteTest with RandomDataGenerator with JsonFormats{
  private val convertActorToPresenter = fromCombatActor.get _
  "Actors controller" when {
    "Getting list or one actor" should {
      val (actor1, actor2) = random[CombatActor](2) match {
        case a1 :: a2 :: Nil => (a1, a2)
      }

      val repo = new InMemoryActorRepository(List(actor1, actor2))
      val routes = new ActorsController(repo).route
      "return list of actors" in {
        Get("/actors") ~> routes ~> check(

          responseAs[String].parseJson mustBe List(actor1, actor2).map(convertActorToPresenter).toJson
        )
      }
      "return one actor" in {
        Get(s"/actor/${actor1.id.data}") ~> routes ~> check(
          responseAs[String].parseJson mustBe convertActorToPresenter(actor1).toJson
        )
      }

      "return 404 if no actor is found" in {
        Get(s"/actor/not_existing") ~> routes ~> check(
          response.status mustBe StatusCodes.NotFound
        )
      }
    }
    "Creating new actor" should {
      val repo = new InMemoryActorRepository(List())
      val routes = new ActorsController(repo).route

      val validActor = random[CombatActor]
      val invalidActor = validActor.copy(
        id = new player.CombatActor.Id("1234567890"),
        statistics = validActor.statistics.copy(
          influence = new Statistics.Influence(-1)
        )
      )

      "should create a valid actor" in {
        repo.clear()
        val request = HttpRequest(
          method = HttpMethods.POST,
          uri = "/actors",
          entity = HttpEntity(MediaTypes.`application/json`, convertActorToPresenter(validActor).toJson.compactPrint)
        )

        request ~> routes ~> check{
          response.status mustBe StatusCodes.OK
        }

        repo.actors mustBe List(validActor)
      }

      "should reject invalid actor" in {
        repo.clear()
        val request = HttpRequest(
          method = HttpMethods.POST,
          uri = "/actors",
          entity = HttpEntity(MediaTypes.`application/json`, convertActorToPresenter(invalidActor).toJson.compactPrint)
        )

        request ~> routes ~> check{
          response.status mustBe StatusCodes.BadRequest
        }

        repo.actors mustBe List()
      }
    }
  }
}

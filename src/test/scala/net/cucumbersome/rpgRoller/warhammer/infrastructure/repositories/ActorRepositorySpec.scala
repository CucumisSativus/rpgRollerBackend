package net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class ActorRepositorySpec extends UnitSpec with RandomDataGenerator {

  import cats.syntax.option._

  import scala.concurrent.ExecutionContext.Implicits.global
  "An actor repository" when {
    "finding by id" should {
      "find one actor by id" in {
        val actors = random[CombatActor](6).toList
        val expectedActor = actors.head

        val repo = new InMemoryActorRepository(actors)
        futureValue(repo.find(expectedActor.id)) mustBe expectedActor.some
      }

    }
    "filtering by name" should {
      val actor = random[CombatActor].copy(name = new player.CombatActor.Name("test name 1"))
      val repo = new InMemoryActorRepository(List(actor))

      "filter by name" in {
        val expression = FilterExpression.ByName("test")
        futureValue(repo.filter(expression)) mustBe List(actor)
      }
    }

    "filtering by health" should {
      val actor = random[CombatActor].copy(hp = new CombatActor.Health(5))
      val repo = new InMemoryActorRepository(List(actor))

      "filter by healt" in {
        val expression = FilterExpression.ByHealth(5)
        futureValue(repo.filter(expression)) mustBe List(actor)
      }
    }

    "filtering by id" should {
      "filter by id" in {
        val actors = random[CombatActor](6).toList
        val expectedActors = actors.take(2)

        val repo = new InMemoryActorRepository(actors)
        val expression = FilterExpression.ByIds(expectedActors.map(_.id.data))
        futureValue(repo.filter(expression)) mustBe expectedActors
      }
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository.FilterExpression

class ActorRepositorySpec extends UnitSpec with RandomDataGenerator {

  import scala.concurrent.ExecutionContext.Implicits.global

  "An actor repository" when {
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

      "filter by name" in {
        val expression = FilterExpression.ByHealth(5)
        futureValue(repo.filter(expression)) mustBe List(actor)
      }
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.combat

import cats.syntax.option._
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.combat.InCombatActor.Name
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class InCombatActorSpec extends UnitSpec with RandomDataGenerator {

  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  "InCombatActor" should {

    val combatActor = random[CombatActor]

    val id = InCombatActor.Id("MyCustomId")
    val idGenerator = () => id

    "be properly builded from CombatActor when name is provided" in {
      val obtainedActor = InCombatActor.buildFromCombatActor(combatActor, idGenerator = idGenerator)

      obtainedActor.id mustBe id
      obtainedActor.name.data must include(combatActor.name.data)
      obtainedActor.initiative mustBe None
      obtainedActor.currentHealth mustBe combatActor.hp
      obtainedActor.actor mustBe combatActor
    }

    "be properly builded from CombatActor when no name is provided" in {
      val obtainedActor = InCombatActor.buildFromCombatActor(combatActor,
        name = Name("My name").some, idGenerator = idGenerator)

      obtainedActor.id mustBe id
      obtainedActor.name must not be Name("My Name")
      obtainedActor.initiative mustBe None
      obtainedActor.currentHealth mustBe combatActor.hp
      obtainedActor.actor mustBe combatActor
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.combat.initiative

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorOptics.modifyAgility
import net.cucumbersome.rpgRoller.warhammer.player.Statistics.Agility
class InitiativeHandlerSpec extends UnitSpec with RandomDataGenerator{
  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor
  "An initiative handler" should {
    val firstPlayer = modifyAgility.modify(_ => new Agility(11))(random[CombatActor])
    val middlePlayer = modifyAgility.modify(_ => new Agility(10))(random[CombatActor])
    val lastPlayer = modifyAgility.modify(_ => new Agility(8))(random[CombatActor])

    val initialPlayers = List(
      middlePlayer,
      lastPlayer,
      firstPlayer
    )

    val sort = InitiativeHandler.generateInitiativeAndSort(() => 0) _
    "sort by initiative" in {
      val expectedPlayers = List(
        firstPlayer,
        middlePlayer,
        lastPlayer
      )

      val obtainedPlayers = sort(initialPlayers)
      obtainedPlayers mustBe expectedPlayers
    }
  }
}

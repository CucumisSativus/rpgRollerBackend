package net.cucumbersome.rpgRoller.warhammer.combat.domain.initiative

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.combat.domain.InCombatActor.modifyAgility
import net.cucumbersome.rpgRoller.warhammer.combat.domain.InCombatActor
import net.cucumbersome.rpgRoller.warhammer.player.Statistics.Agility
class InitiativeSpec extends UnitSpec with RandomDataGenerator{

  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  "An initiative handler" should {
    val (_firstPlayer, _middlePlayer, _lastPlayer) = build3Actors
    val firstPlayer = modifyAgility.modify(_ => new Agility(11))(_firstPlayer)
    val middlePlayer = modifyAgility.modify(_ => new Agility(10))(_middlePlayer)
    val lastPlayer = modifyAgility.modify(_ => new Agility(8))(_lastPlayer)

    val initialPlayers = List(
      middlePlayer,
      lastPlayer,
      firstPlayer
    )

    val sort = Initiative.generateInitiativeAndSort(() => 0) _
    "sort by initiative" in {
      val expectedPlayerIds = List(
        firstPlayer.id,
        middlePlayer.id,
        lastPlayer.id
      )

      val obtainedPlayers = sort(initialPlayers)
      obtainedPlayers.map(_.id) mustBe expectedPlayerIds
    }
  }

  def build3Actors: (InCombatActor, InCombatActor, InCombatActor) = {
    random[InCombatActor](3) match {
      case a1 :: a2 :: a3 :: Nil => (a1, a2, a3)
    }
  }
}

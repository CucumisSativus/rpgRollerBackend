package net.cucumbersome.rpgRoller.warhammer.initiative

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.Player
import net.cucumbersome.rpgRoller.warhammer.Player.Agility

class InitiativeHandlerSpec extends UnitSpec with RandomDataGenerator{
  import net.cucumbersome.test.PlayerGenerator.arbitraryPlayer
  "An initiative handler" should {
    val firstPlayer = random[Player].copy(agility = new Agility(11))
    val middlePlayer = random[Player].copy(agility = new Agility(10))
    val lastPlayer = random[Player].copy(agility = new Agility(8))

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

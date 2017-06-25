package net.cucumbersome.rpgRoller.warhammer.combat

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatSpec extends UnitSpec with RandomDataGenerator{
  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  "A combat" when {
    val (firstPlayer, midPlayer, lastPlayer) = random[CombatActor](3) match{
      case f :: m :: l :: Nil => (f, m, l)
    }

    val combat = Combat(List(
      firstPlayer,
      midPlayer,
      lastPlayer
    ))
    "adding new players" should {
      "add it properly" in {
        val combatManip = for {
          _ <- Combat.addActor(List(random[CombatActor]))
        } yield Unit
        val (newCombat, _) = combatManip.run(combat).value
        newCombat.combatActors.length mustBe combat.combatActors.length +1
      }
    }

    "sorting by initative" should {
      "does not change count or objects" in {
        val combatManip = for{
          _ <- Combat.sortByInitiative(() => 1)
        } yield Unit
        val (newCombat, _) = combatManip.run(combat).value
        newCombat.combatActors.length mustBe combat.combatActors.length
        newCombat.combatActors must contain allOf(firstPlayer, midPlayer, lastPlayer)
      }
    }

    "removing players from combat" should {
      "removes player" in {
        val combatManip = for{
          removed <- Combat.removeActors(List(midPlayer))
        } yield removed
        val (newCombat, removedActors) = combatManip.run(combat).value
        removedActors mustBe List(midPlayer)
        newCombat.combatActors mustNot contain only(midPlayer)
        newCombat.combatActors must contain allOf(firstPlayer, lastPlayer)
      }
    }

    "updating players health" should {
      "update the health" in {
        val newHealth = new CombatActor.Health(10)
        val combatManip = for{
          removed <- Combat.updateHealth(midPlayer, newHealth)
        } yield removed
        val (newCombat, removed) = combatManip.run(combat).value
        removed.length mustBe 0
        newCombat.combatActors(1).hp mustBe newHealth
      }
      "remove the player if health drops to 0" in {
        val newHealth = new CombatActor.Health(0)
        val combatManip = for{
          removed <- Combat.updateHealth(midPlayer, newHealth)
        } yield removed

        val (newCombat, removedPlayers) = combatManip.run(combat).value
        newCombat.combatActors mustNot contain(midPlayer)
        removedPlayers mustBe List(midPlayer.copy(hp = newHealth))
      }
      "remove the player if health drops below 0" in {
        val newHealth = new CombatActor.Health(-5)
        val combatManip = for{
          removed <- Combat.updateHealth(midPlayer, newHealth)
        } yield removed

        val (newCombat, removedPlayers) = combatManip.run(combat).value
        newCombat.combatActors mustNot contain(midPlayer)
        removedPlayers mustBe List(midPlayer.copy(hp = newHealth))
      }
    }
  }
}

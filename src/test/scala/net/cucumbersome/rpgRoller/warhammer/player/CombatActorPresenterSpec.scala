package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
class CombatActorPresenterSpec extends UnitSpec with RandomDataGenerator{
  "Combat actor presenter" when {
    "converting from conbat actor to presenter" should {
      "do it properly" in {
        val ca = random[CombatActor]
        val obtained = CombatActorPresenter.fromCombatActor.get(ca)

        obtained.id mustBe ca.id.data
        obtained.name mustBe ca.name.data
        obtained.hp mustBe ca.hp.data

        val caStats = ca.statistics
        val obtainedStats = obtained.statistics

        obtainedStats.weaponSkill mustBe caStats.weaponSkill.data
        obtainedStats.ballisticSkill mustBe caStats.ballisticSkill.data
        obtainedStats.strength mustBe caStats.strength.data
        obtainedStats.toughness mustBe caStats.toughness.data
        obtainedStats.agility mustBe caStats.agility.data
        obtainedStats.intelligence mustBe caStats.intelligence.data
        obtainedStats.perception mustBe caStats.perception.data
        obtainedStats.willPower mustBe caStats.willPower.data
        obtainedStats.fellowship mustBe caStats.fellowship.data
        obtainedStats.influence mustBe caStats.influence.data
      }
    }
    "converting from presenter to combat actor" should {
      "do it properly" in {
        val cap = random[CombatActorPresenter]
        val obtained = CombatActorPresenter.fromCombatActor.reverseGet(cap)

        obtained.id.data mustBe cap.id
        obtained.name.data mustBe cap.name
        obtained.hp.data mustBe cap.hp

        val capStats = cap.statistics
        val obtainedStats = obtained.statistics

        capStats.weaponSkill mustBe obtainedStats.weaponSkill.data
        capStats.ballisticSkill mustBe obtainedStats.ballisticSkill.data
        capStats.strength mustBe obtainedStats.strength.data
        capStats.toughness mustBe obtainedStats.toughness.data
        capStats.agility mustBe obtainedStats.agility.data
        capStats.intelligence mustBe obtainedStats.intelligence.data
        capStats.perception mustBe obtainedStats.perception.data
        capStats.willPower mustBe obtainedStats.willPower.data
        capStats.fellowship mustBe obtainedStats.fellowship.data
        capStats.influence mustBe obtainedStats.influence.data
      }
    }
  }
}

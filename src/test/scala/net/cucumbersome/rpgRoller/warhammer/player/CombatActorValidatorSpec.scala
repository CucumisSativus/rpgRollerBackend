package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor._

class CombatActorValidatorSpec extends UnitSpec{
  import net.cucumbersome.rpgRoller.warhammer.player.CombatActor._
  import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions.intToStatisticsConversions
  "Combat validator" should {
    "return valid combat actor if everything is ok" in {
      val ca = CombatActor(
        id = new Id("aa"),
        name = new Name("bb"),
        statistics = Statistics(
          weaponSkill =  1.toWs,
          ballisticSkill =  2.toBs,
          strength = 3.toStr,
          toughness = 4.toTg,
          agility = 5.toAg,
          intelligence = 6.toIt,
          perception = 7.toPer,
          willPower = 8.toWp,
          fellowship = 9.toFel,
          influence = 10.toInfl
        ),
        hp = new Health(11)
      )

     assert(CombatActorValidator.validate(ca).isValid)
    }

    "return invalid if int statistics is wrong" in {
      val ca = CombatActor(
        id = new Id("aa"),
        name = new Name("bb"),
        statistics = Statistics(
          weaponSkill =  1.toWs,
          ballisticSkill =  2.toBs,
          strength = 3.toStr,
          toughness = (-4).toTg,
          agility = 5.toAg,
          intelligence = 6.toIt,
          perception = 7.toPer,
          willPower = 8.toWp,
          fellowship = 9.toFel,
          influence = 10.toInfl
        ),
        hp = new Health(11)
      )

      val validationResult = CombatActorValidator.validate(ca)
      assert(!validationResult.isValid)
    }

    "return invalid if string statistics is wrong" in {
      val ca = CombatActor(
        id = new Id(""),
        name = new Name("bb"),
        statistics = Statistics(
          weaponSkill =  1.toWs,
          ballisticSkill =  2.toBs,
          strength = 3.toStr,
          toughness = 4.toTg,
          agility = 5.toAg,
          intelligence = 6.toIt,
          perception = 7.toPer,
          willPower = 8.toWp,
          fellowship = 9.toFel,
          influence = 10.toInfl
        ),
        hp = new Health(11)
      )

      val validationResult = CombatActorValidator.validate(ca)
      assert(!validationResult.isValid)
    }
  }


}

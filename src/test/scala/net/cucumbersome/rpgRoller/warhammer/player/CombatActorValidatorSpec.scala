package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.UnitSpec

class CombatActorValidatorSpec extends UnitSpec{
  "Combat validator" should {
    "return valid combat actor if everything is ok" in {
      val ca = CombatActorPresenter(
        id = "aa",
        name = "bb",
        statistics = CombatActorPresenter.Statistics(
          weaponSkill = 1,
          ballisticSkill = 2,
          strength = 3,
          toughness = 4,
          agility = 5,
          intelligence = 6,
          perception = 7,
          willPower = 8,
          fellowship = 9,
          influence = 10
        ),
        hp = 11
      )

     assert(CombatActorValidator.validate(ca).isValid)
    }

    "return invalid if int statistics is wrong" in {
      val ca = CombatActorPresenter(
        id = "aa",
        name = "bb",
        statistics = CombatActorPresenter.Statistics(
          weaponSkill = 1,
          ballisticSkill = 2,
          strength = 3,
          toughness = 4,
          agility = -5,
          intelligence = 6,
          perception = 7,
          willPower = 8,
          fellowship = 9,
          influence = 10
        ),
        hp = 11
      )

      val validationResult = CombatActorValidator.validate(ca)
      assert(!validationResult.isValid)
    }

    "return invalid if string statistics is wrong" in {
      val ca = CombatActorPresenter(
        id = "",
        name = "bb",
        statistics = CombatActorPresenter.Statistics(
          weaponSkill = 1,
          ballisticSkill = 2,
          strength = 3,
          toughness = 4,
          agility = 5,
          intelligence = 6,
          perception = 7,
          willPower = 8,
          fellowship = 9,
          influence = 10
        ),
        hp = 11
      )
      val validationResult = CombatActorValidator.validate(ca)
      assert(!validationResult.isValid)
    }
  }


}

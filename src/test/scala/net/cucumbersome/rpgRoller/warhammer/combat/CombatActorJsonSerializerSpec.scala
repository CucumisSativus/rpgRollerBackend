package net.cucumbersome.rpgRoller.warhammer.combat

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorPresenter._
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActorPresenter, JsonFormats}
import spray.json._

class CombatActorJsonSerializerSpec extends UnitSpec with JsonFormats with RandomDataGenerator {
  "CombatActor json serializer" should {
    "serialize to json" in {
      val obj = random[CombatActorPresenter]

      val expectedJson =
        s"""
           |{
           | "id": "${obj.id}",
           | "name": "${obj.name}",
           | "hp": ${obj.hp},
           | "statistics": {
           |  "weaponSkill": ${obj.statistics.weaponSkill},
           |  "ballisticSkill": ${obj.statistics.ballisticSkill},
           |  "strength": ${obj.statistics.strength},
           |  "toughness": ${obj.statistics.toughness},
           |  "agility": ${obj.statistics.agility},
           |  "intelligence": ${obj.statistics.intelligence},
           |  "perception": ${obj.statistics.perception},
           |  "willPower": ${obj.statistics.willPower},
           |  "fellowship": ${obj.statistics.fellowship},
           |  "influence": ${obj.statistics.influence}
           | }
           |}
         """.stripMargin
      obj.toJson mustBe expectedJson.parseJson
    }

    "serialize from json" in {
      val expectedJson =
        s"""
           |{
           | "name": "Test name",
           | "hp": 15,
           | "id": "test-id",
           | "statistics": {
           |  "weaponSkill": 1,
           |  "ballisticSkill": 2,
           |  "strength": 3,
           |  "toughness": 4,
           |  "agility": 5,
           |  "intelligence": 6,
           |  "perception": 7,
           |  "willPower": 8,
           |  "fellowship": 9,
           |  "influence": 10
           | }
           |}
         """.stripMargin

      val expectedCombat = CombatActorPresenter(
        name = "Test name",
        hp = 15,
        id = "test-id",
        statistics = Statistics(
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
        )
      )

      expectedJson.parseJson.convertTo[CombatActorPresenter] mustBe expectedCombat
    }

  }

}

package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorPresenter.{Statistics => StatsP}
import spray.json._

class StatisticsCombatActorJsonSerializerSpec extends UnitSpec with CombatActorJsonFormats with RandomDataGenerator {
  "Statistics json serializer" should {
    "serialize to json" in {
      val obj = random[StatsP]

      val expectedJson =
        s"""
           |{
           | "weaponSkill": ${obj.weaponSkill},
           | "ballisticSkill": ${obj.ballisticSkill},
           | "strength": ${obj.strength},
           | "toughness": ${obj.toughness},
           | "agility": ${obj.agility},
           | "intelligence": ${obj.intelligence},
           | "perception": ${obj.perception},
           | "willPower": ${obj.willPower},
           | "fellowship": ${obj.fellowship},
           | "influence": ${obj.influence}
           | }
         """.stripMargin

      obj.toJson mustBe expectedJson.parseJson
    }

    "serialize from json" in {
      val expectedJson =
        s"""
           |{
           | "weaponSkill": 1,
           | "ballisticSkill": 2,
           | "strength": 3,
           | "toughness": 4,
           | "agility": 5,
           | "intelligence": 6,
           | "perception": 7,
           | "willPower": 8,
           | "fellowship": 9,
           | "influence": 10
           | }
         """.stripMargin

      val expectedStats = StatsP(
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

      expectedJson.parseJson.convertTo[StatsP] mustBe expectedStats
    }
  }
}

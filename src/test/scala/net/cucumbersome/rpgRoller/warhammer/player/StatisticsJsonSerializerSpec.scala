package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import spray.json._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
class StatisticsJsonSerializerSpec extends UnitSpec with JsonFormats with RandomDataGenerator{
  "Statistics json serializer" should {
    "serialize to json" in {
      val obj = random[Statistics]

      val expectedJson =
        s"""
           |{
           | "weaponSkill": ${obj.weaponSkill.data},
           | "ballisticSkill": ${obj.ballisticSkill.data},
           | "strength": ${obj.strength.data},
           | "toughness": ${obj.toughness.data},
           | "agility": ${obj.agility.data},
           | "intelligence": ${obj.intelligence.data},
           | "perception": ${obj.perception.data},
           | "willPower": ${obj.willPower.data},
           | "fellowship": ${obj.fellowship.data},
           | "influence": ${obj.influence.data}
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

      val expectedStats = Statistics(
        weaponSkill = 1.toWs,
        ballisticSkill = 2.toBs,
        strength = 3.toStr,
        toughness = 4.toTg,
        agility = 5.toAg,
        intelligence = 6.toIt,
        perception = 7.toPer,
        willPower = 8.toWp,
        fellowship = 9.toFel,
        influence = 10.toInfl
      )

      expectedJson.parseJson.convertTo[Statistics] mustBe expectedStats
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import spray.json._
import net.cucumbersome.rpgRoller.warhammer.player.StatisticsConversions._
class StatisticsJsonSerializerSpec extends UnitSpec with StatisticsJsonSerializer with RandomDataGenerator{
  "Statistics json serializer" should {
    "serialize to json" in {
      val obj = random[Statistics]

      val expectedJson =
        s"""
           |{
           | "weaponSkill": ${obj.weaponSkill.data},
           | "balisticSkill": ${obj.balisticSkill.data},
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
           | "balisticSkill": 2,
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
        1.toWs,
        2.toBs,
        3.toStr,
        4.toTg,
        5.toAg,
        6.toIt,
        7.toPer,
        8.toWp,
        9.toFel,
        10.toInfl
      )

      expectedJson.parseJson.convertTo[Statistics] mustBe expectedStats
    }
  }
}

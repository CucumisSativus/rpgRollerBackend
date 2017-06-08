package net.cucumbersome.rpgRoller.warhammer.player

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import spray.json._

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

      val expectedStats = Statistics.buildUnsafe(1, 2, 3, 4, 5, 6, 7, 8 , 9, 10)

      expectedJson.parseJson.convertTo[Statistics] mustBe expectedStats
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.combat

import spray.json._
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, JsonFormats, Statistics}
import net.cucumbersome.rpgRoller.warhammer.player.StatisticsConversions._

/**
  * Created by pokora on 11.06.2017.
  */
class CombatActorJsonSerializerSpec extends UnitSpec with JsonFormats with RandomDataGenerator {
  "CombatActor json serializer" should {
    "serialize to json" in {
      val obj = random[CombatActor]

      val expectedJson =
        s"""
           |{
           | "id": "${obj.id.data}",
           | "name": "${obj.name.data}",
           | "hp": ${obj.hp.data},
           | "statistics": {
           |  "weaponSkill": ${obj.statistics.weaponSkill.data},
           |  "ballisticSkill": ${obj.statistics.ballisticSkill.data},
           |  "strength": ${obj.statistics.strength.data},
           |  "toughness": ${obj.statistics.toughness.data},
           |  "agility": ${obj.statistics.agility.data},
           |  "intelligence": ${obj.statistics.intelligence.data},
           |  "perception": ${obj.statistics.perception.data},
           |  "willPower": ${obj.statistics.willPower.data},
           |  "fellowship": ${obj.statistics.fellowship.data},
           |  "influence": ${obj.statistics.influence.data}
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

      val expectedCombat = CombatActor(
        name = new CombatActor.Name("Test name"),
        hp = new CombatActor.Health(15),
        id = new CombatActor.Id("test-id"),
        statistics = Statistics(
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
      )

      expectedJson.parseJson.convertTo[CombatActor] mustBe expectedCombat
    }

  }

}

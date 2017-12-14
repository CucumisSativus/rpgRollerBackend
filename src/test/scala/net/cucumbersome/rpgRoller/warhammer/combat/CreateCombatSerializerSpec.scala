package net.cucumbersome.rpgRoller.warhammer.combat

import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorJsonFormats
import spray.json._
class CreateCombatSerializerSpec extends UnitSpec with CombatActorJsonFormats with CombatJsonSerializer {
  "Create combat serializer" when {
    "writting to json" should {
      "return properly formatted json" in {
        val params = CreateCombatParameters()
        params.toJson.compactPrint mustBe """{"actorIds":[]}"""
      }
    }

    "reading from json" should {
      "read it properly when actorIds params are there" in {
        val json = """{"actorIds":["1"]}"""
        val obtained = json.parseJson.convertTo[CreateCombatParameters]
        obtained.actorIds.head mustBe "1"
      }

      "read it properly when no actorsIds params are present" in {
        val json = """{}"""
        val obtained = json.parseJson.convertTo[CreateCombatParameters]
        obtained.actorIds mustBe empty
      }
    }
  }
}

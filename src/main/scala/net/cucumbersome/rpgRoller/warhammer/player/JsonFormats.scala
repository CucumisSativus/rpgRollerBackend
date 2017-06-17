package net.cucumbersome.rpgRoller.warhammer.player

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import net.cucumbersome
import net.cucumbersome.rpgRoller
import net.cucumbersome.rpgRoller.warhammer
import net.cucumbersome.rpgRoller.warhammer.player

trait JsonFormats extends DefaultJsonProtocol {

  private def playerFields = Seq(
    "weaponSkill", "ballisticSkill", "strength", "toughness", "agility",
    "intelligence", "perception", "willPower", "fellowship", "influence"
  )

  implicit object PlayerJsonFormat extends RootJsonFormat[Statistics] {
    override def read(json: JsValue): Statistics = {
      json.asJsObject.getFields(playerFields: _*) match {
        case Seq(JsNumber(ws), JsNumber(bs), JsNumber(s), JsNumber(t), JsNumber(ag), JsNumber(int), JsNumber(per), JsNumber(wp), JsNumber(fel), JsNumber(inf)) =>
          Statistics(
            weaponSkill = new Statistics.WeaponSkill(ws.toInt),
            ballisticSkill = new Statistics.BallisticSkill(bs.toInt),
            strength = new Statistics.Strength(s.toInt),
            toughness = new Statistics.Toughness(t.toInt),
            agility = new Statistics.Agility(ag.toInt),
            intelligence = new Statistics.Intelligence(int.toInt),
            perception = new Statistics.Perception(per.toInt),
            willPower = new Statistics.WillPower(wp.toInt),
            fellowship = new Statistics.Fellowship(fel.toInt),
            influence = new Statistics.Influence(inf.toInt)
          )
        case _ => throw new DeserializationException("Player expected")
      }
    }

    override def write(obj: Statistics): JsValue = JsObject(
      "weaponSkill" -> JsNumber(obj.weaponSkill.data),
      "ballisticSkill" -> JsNumber(obj.ballisticSkill.data),
      "strength" -> JsNumber(obj.strength.data),
      "toughness" -> JsNumber(obj.toughness.data),
      "agility" -> JsNumber(obj.agility.data),
      "intelligence" -> JsNumber(obj.intelligence.data),
      "perception" -> JsNumber(obj.perception.data),
      "willPower" -> JsNumber(obj.willPower.data),
      "fellowship" -> JsNumber(obj.fellowship.data),
      "influence" -> JsNumber(obj.influence.data)
    )

  }

  private def combatActorFields = Seq(
    "id", "name", "hp", "statistics"
  )

  implicit object CombatJsonFormat extends RootJsonFormat[CombatActor] {
    override def read(json: JsValue): CombatActor = {
      json.asJsObject.getFields(combatActorFields: _*) match {
        case Seq(JsString(id), JsString(name), JsNumber(hp), statistics :JsObject) =>
          CombatActor(
            id = new CombatActor.Id(id.toString),
            name = new CombatActor.Name(name.toString),
            statistics = statistics.convertTo[Statistics],
            hp = new CombatActor.Health(hp.toInt)
          )
        case _ => throw new DeserializationException("Combat expected")
      }
    }

    override def write(obj: CombatActor): JsValue = JsObject(
      "id" -> JsString(obj.id.data),
      "name" -> JsString(obj.name.data),
      "statistics" -> JsObject(
        "weaponSkill" -> JsNumber(obj.statistics.weaponSkill.data),
        "ballisticSkill" -> JsNumber(obj.statistics.ballisticSkill.data),
        "strength" -> JsNumber(obj.statistics.strength.data),
        "toughness" -> JsNumber(obj.statistics.toughness.data),
        "agility" -> JsNumber(obj.statistics.agility.data),
        "intelligence" -> JsNumber(obj.statistics.intelligence.data),
        "perception" -> JsNumber(obj.statistics.perception.data),
        "willPower" -> JsNumber(obj.statistics.willPower.data),
        "fellowship" -> JsNumber(obj.statistics.fellowship.data),
        "influence" -> JsNumber(obj.statistics.influence.data)
      ),
      "hp" -> JsNumber(obj.hp.data)
    )

  }

}

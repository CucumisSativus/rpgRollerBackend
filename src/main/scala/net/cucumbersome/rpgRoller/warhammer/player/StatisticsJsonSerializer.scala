package net.cucumbersome.rpgRoller.warhammer.player
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import net.cucumbersome
import net.cucumbersome.rpgRoller
import net.cucumbersome.rpgRoller.warhammer
import net.cucumbersome.rpgRoller.warhammer.player
trait StatisticsJsonSerializer extends DefaultJsonProtocol{
  private def jsonFields = Seq(
    "weaponSkill", "balisticSkill", "strength", "toughness", "agility",
    "intelligence", "perception", "willPower", "fellowship", "influence"
  )

  implicit object PlayerJsonFormat extends RootJsonFormat[Statistics] {
    override def read(json: JsValue): Statistics = {
      json.asJsObject.getFields(jsonFields:_*) match {
        case Seq(JsNumber(ws), JsNumber(bs), JsNumber(s), JsNumber(t), JsNumber(ag), JsNumber(int), JsNumber(per), JsNumber(wp), JsNumber(fel), JsNumber(inf)) =>
          Statistics(
            new Statistics.WeaponSkill(ws.toInt),
            new Statistics.BalisticSkill(bs.toInt),
            new Statistics.Strength(s.toInt),
            new Statistics.Toughness(t.toInt),
            new Statistics.Agility(ag.toInt),
            new Statistics.Intelligence(int.toInt),
            new Statistics.Perception(per.toInt),
            new Statistics.WillPower(wp.toInt),
            new Statistics.Fellowship(fel.toInt),
            new Statistics.Influence(inf.toInt)
          )
        case _ => throw new DeserializationException("Player expected")
      }
    }

    override def write(obj: Statistics): JsValue = JsObject(
      "weaponSkill" -> JsNumber(obj.weaponSkill.data),
      "balisticSkill" -> JsNumber(obj.balisticSkill.data),
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
}

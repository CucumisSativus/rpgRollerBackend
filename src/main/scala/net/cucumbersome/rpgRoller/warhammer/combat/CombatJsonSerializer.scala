package net.cucumbersome.rpgRoller.warhammer.combat

import net.cucumbersome.rpgRoller.warhammer.player.CombatActorJsonFormats
import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonFormat}
import spray.json._
trait CombatJsonSerializer extends DefaultJsonProtocol with CombatActorJsonFormats {

  implicit val inCombatActorPresenterJson: RootJsonFormat[InCombatActorPresenter] = jsonFormat5(InCombatActorPresenter.apply)
  implicit val combatPresenterJson: RootJsonFormat[CombatPresenter] = jsonFormat2(CombatPresenter.apply)

  case class CreateCombatParameters(actorIds: Array[String] = Array())

  implicit val createCombatJson: RootJsonFormat[CreateCombatParameters] = new RootJsonFormat[CreateCombatParameters] {
    override def write(obj: CombatJsonSerializer.this.CreateCombatParameters): JsValue = JsObject(Map("actorIds" -> obj.actorIds.toJson))

    override def read(json: JsValue): CreateCombatParameters = {
      json.asJsObject("not an object").getFields("actorIds") match {
        case Seq(JsArray(values)) => CreateCombatParameters(values.map(_.convertTo[String]).toArray)
        case Seq() => CreateCombatParameters()
        case _ => throw new IllegalArgumentException("wrong format")
      }
    }
  }
  implicit val combatInitializedJson: RootJsonFormat[NewCombatResponse] = jsonFormat1(NewCombatResponse)
  implicit val addActorsToCombatJson: RootJsonFormat[AddActorsToCombatParameters] = jsonFormat1(AddActorsToCombatParameters)
  implicit val removeActortsFromConbatJson: RootJsonFormat[RemoveActorsFromCombatParameters] = jsonFormat1(RemoveActorsFromCombatParameters)
  case class NewCombatResponse(combatId: String)

  case class AddActorsToCombatParameters(actorIds: Array[String])

  case class RemoveActorsFromCombatParameters(actorIds: Array[String])


}

object CombatJsonSerializer extends CombatJsonSerializer
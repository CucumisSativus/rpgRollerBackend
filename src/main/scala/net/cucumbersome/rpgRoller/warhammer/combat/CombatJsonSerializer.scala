package net.cucumbersome.rpgRoller.warhammer.combat

import net.cucumbersome.rpgRoller.warhammer.player.CombatActorJsonFormats
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait CombatJsonSerializer extends DefaultJsonProtocol with CombatActorJsonFormats {

  implicit val combatPresenterJson: RootJsonFormat[CombatPresenter] = jsonFormat2(CombatPresenter.apply)

  case class CreateCombatParameters(actorIds: List[String])

  implicit val createCombatJson: RootJsonFormat[CreateCombatParameters] = jsonFormat1(CreateCombatParameters.apply)
  implicit val combatInitializedJson: RootJsonFormat[NewCombatResponse] = jsonFormat1(NewCombatResponse)
  implicit val addActorsToCombatJson: RootJsonFormat[AddActorsToCombatParameters] = jsonFormat2(AddActorsToCombatParameters)

  case class NewCombatResponse(combatId: String)

  case class AddActorsToCombatParameters(combatId: String, actorIds: List[String])


}

object CombatJsonSerializer extends CombatJsonSerializer
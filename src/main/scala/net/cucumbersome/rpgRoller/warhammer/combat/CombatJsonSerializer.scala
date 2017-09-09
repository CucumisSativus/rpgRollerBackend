package net.cucumbersome.rpgRoller.warhammer.combat

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait CombatJsonSerializer extends DefaultJsonProtocol {

  case class CreateCombatParameters(actorIds: List[String])

  implicit val combatInitializedJson: RootJsonFormat[NewCombat] = jsonFormat1(NewCombat)

  implicit val createCombatJson: RootJsonFormat[CreateCombatParameters] = jsonFormat1(CreateCombatParameters.apply)

  case class NewCombat(combatId: String)

}

object CombatJsonSerializer extends CombatJsonSerializer
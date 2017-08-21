package net.cucumbersome.rpgRoller.warhammer.combat

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait CombatJsonSerializer extends DefaultJsonProtocol {

  case class CreateCombatParameters(actorIds: List[String])

  implicit val createCombatJson: RootJsonFormat[CreateCombatParameters] = jsonFormat1(CreateCombatParameters.apply)
}

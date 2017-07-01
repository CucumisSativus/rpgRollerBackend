package net.cucumbersome.rpgRoller.warhammer.player

import spray.json._


trait JsonFormats extends DefaultJsonProtocol {
  implicit val statisticsFormat: RootJsonFormat[CombatActorPresenter.Statistics] =
    jsonFormat10(CombatActorPresenter.Statistics)

  implicit val combatActorFormat: RootJsonFormat[CombatActorPresenter] =
    jsonFormat4(CombatActorPresenter.apply)
}

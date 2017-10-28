package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.rpgRoller.warhammer.player.CombatActor.{Health, Id, Name}


case class CombatActor(id: Id, name: Name, statistics: Statistics, hp: Health)

object CombatActor{

  case class Id(data: String) extends AnyVal with Serializable

  case class Health(data: Int) extends AnyVal

  case class Name(data: String) extends AnyVal
}
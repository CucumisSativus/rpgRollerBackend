package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.rpgRoller.warhammer.player.CombatActor.{Health, Id, Name}

case class CombatActor(id: Id, name: Name, statistics: Statistics, hp: Health)

object CombatActor{

  class Id(val data: String) extends AnyVal with Serializable {
    override def toString: String = data
  }
  class Health(val data: Int) extends AnyVal{
    override def toString: String = data.toString
  }
  class Name(val data: String) extends AnyVal{
    override def toString: String = data
  }
}
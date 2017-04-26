package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.rpgRoller.warhammer.player.CombatActor.{Health, Name}

case class CombatActor(name: Name, statistics: Statistics, hp: Health)

object CombatActor{
  class Health(val data: Int) extends AnyVal{
    override def toString: String = data.toString
  }
  class Name(val data: String) extends AnyVal{
    override def toString: String = data
  }
}
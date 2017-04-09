package net.cucumbersome.rpgRoller.warhammer
import Player._
case class Player(
                   weaponSkill: WeaponSkill,
                   balisticSkill: BalisticSkill,
                   strength: Strength,
                   toughness: Toughness,
                   agility: Agility,
                   intelligence: Intelligence,
                   perception: Perception,
                   willPower: WillPower,
                   fellowship: Fellowship,
                   influence: Influence
                 )
object Player {

  class WeaponSkill(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class BalisticSkill(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Strength(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Toughness(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Agility(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Intelligence(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Perception(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class WillPower(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Fellowship(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

  class Influence(val data: Int) extends AnyVal {
    override def toString: String = data.toString
  }

}
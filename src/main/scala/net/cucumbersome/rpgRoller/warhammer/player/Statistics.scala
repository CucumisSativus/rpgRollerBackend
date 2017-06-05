package net.cucumbersome.rpgRoller.warhammer.player

import net.cucumbersome.rpgRoller.warhammer.player.Statistics._
final case class Statistics(
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
object Statistics {
  def buildUnsafe(ws: Int, bs: Int, str: Int, tg: Int, ag: Int, int: Int, per: Int, wp: Int, fel: Int, infl: Int): Statistics = {
    Statistics(
      new WeaponSkill(ws),
      new BalisticSkill(bs),
      new Strength(str),
      new Toughness(tg),
      new Agility(ag),
      new Intelligence(int),
      new Perception(per),
      new WillPower(wp),
      new Fellowship(fel),
      new Influence(infl)
    )
  }
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

  class RemainingHealth(val data: Int) extends AnyVal{
    override def toString: String = data.toString
  }
}
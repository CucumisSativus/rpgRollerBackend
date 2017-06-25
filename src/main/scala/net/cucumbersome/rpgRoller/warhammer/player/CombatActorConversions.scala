package net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.Statistics._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor._
object CombatActorConversions {
  implicit class intToStatisticsConversions(x: Int){
    def toWs: WeaponSkill = new WeaponSkill(x)
    def toBs: BallisticSkill = new BallisticSkill(x)
    def toStr: Strength = new Strength(x)
    def toTg: Toughness = new Toughness(x)
    def toAg: Agility = new Agility(x)
    def toIt: Intelligence = new Intelligence(x)
    def toPer: Perception = new Perception(x)
    def toWp: WillPower = new WillPower(x)
    def toFel: Fellowship = new Fellowship(x)
    def toInfl: Influence = new Influence(x)
    def toHp: Health = new Health(x)
  }

  implicit class stringToStatisticsConversions(x: String){
    def toName: Name = new Name(x)
    def toId: Id = new Id(x)
  }
}

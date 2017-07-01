package net.cucumbersome.rpgRoller.warhammer.player
import monocle.Iso


case class CombatActorPresenter(
                               id: String,
                               name: String,
                               hp: Int,
                               statistics: CombatActorPresenter.Statistics
                               )
object CombatActorPresenter{
  import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
  import net.cucumbersome.rpgRoller.warhammer.player.{Statistics => CaStatistics}

  private lazy val combatActorToPresenter = (c: CombatActor) =>
    CombatActorPresenter(
      id = c.id.data,
      name = c.name.data,
      hp = c.hp.data,
      statistics = Statistics(
        weaponSkill = c.statistics.weaponSkill.data,
        ballisticSkill = c.statistics.ballisticSkill.data,
        strength = c.statistics.strength.data,
        toughness = c.statistics.toughness.data,
        agility = c.statistics.agility.data,
        intelligence = c.statistics.intelligence.data,
        perception = c.statistics.perception.data,
        willPower = c.statistics.willPower.data,
        fellowship = c.statistics.fellowship.data,
        influence = c.statistics.influence.data
      )
    )
  private lazy val presenterToCombatActor = (c: CombatActorPresenter) =>
      CombatActor(
        id = c.id.toId,
        name = c.name.toName,
        hp = c.hp.toHp,
        statistics = CaStatistics(
          weaponSkill = c.statistics.weaponSkill.toWs,
          ballisticSkill = c.statistics.ballisticSkill.toBs,
          strength = c.statistics.strength.toStr,
          toughness = c.statistics.toughness.toTg,
          agility = c.statistics.agility.toAg,
          intelligence = c.statistics.intelligence.toIt,
          perception = c.statistics.perception.toPer,
          willPower = c.statistics.willPower.toWp,
          fellowship = c.statistics.fellowship.toFel,
          influence = c.statistics.influence.toInfl
        )
      )
  val fromCombatActor: Iso[CombatActor, CombatActorPresenter] =
    Iso[CombatActor, CombatActorPresenter](combatActorToPresenter)(presenterToCombatActor)

  case class Statistics(
                         weaponSkill: Int,
                         ballisticSkill: Int,
                         strength: Int,
                         toughness: Int,
                         agility: Int,
                         intelligence: Int,
                         perception: Int,
                         willPower: Int,
                         fellowship: Int,
                         influence: Int
                       )
}

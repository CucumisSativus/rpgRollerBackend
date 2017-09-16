package net.cucumbersome.rpgRoller.warhammer.combat

import net.cucumbersome.rpgRoller.warhammer.player.CombatActorPresenter

case class CombatPresenter(id: String, actors: List[CombatActorPresenter])

object CombatPresenter {
  lazy val combatToCombatPresenter: (String, Combat) => CombatPresenter = (id: String, c: Combat) => {
    CombatPresenter(id, c.combatActors.map(CombatActorPresenter.fromCombatActor.get))
  }
}

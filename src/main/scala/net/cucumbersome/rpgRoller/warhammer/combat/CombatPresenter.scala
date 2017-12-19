package net.cucumbersome.rpgRoller.warhammer.combat

import io.swagger.annotations.{ApiModel, ApiModelProperty}
import monocle.Iso
import net.cucumbersome.rpgRoller.warhammer.combat.domain.{Combat, InCombatActor}
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, CombatActorPresenter}

case class CombatPresenter(id: String, actors: Array[InCombatActorPresenter])

object CombatPresenter {
  lazy val combatToCombatPresenter: (String, Combat) => CombatPresenter = (id: String, c: Combat) => {
    CombatPresenter(id, c.combatActors.map(InCombatActorPresenter.fromInCombatActor.get).toArray)
  }
}

case class InCombatActorPresenter(id: String, name: String,
                                  currentHealth: Int, initiaive: Option[Int], actor: CombatActorPresenter)

object InCombatActorPresenter {
  private lazy val actorToPresenter: (InCombatActor) => InCombatActorPresenter = (actor) => {
    InCombatActorPresenter(
      id = actor.id.data,
      name = actor.name.data,
      currentHealth = actor.currentHealth.data,
      initiaive = actor.initiative.map(_.data),
      actor = CombatActorPresenter.fromCombatActor.get(actor.actor)
    )
  }
  private lazy val toCombatActor: (InCombatActorPresenter) => InCombatActor = (presenter) => {
    InCombatActor(
      id = InCombatActor.Id(presenter.id),
      name = InCombatActor.Name(presenter.name),
      currentHealth = CombatActor.Health(presenter.currentHealth),
      initiative = presenter.initiaive.map(InCombatActor.Initiative.apply),
      actor = CombatActorPresenter.fromCombatActor.reverseGet(presenter.actor)
    )
  }
  val fromInCombatActor: Iso[InCombatActor, InCombatActorPresenter] = Iso[InCombatActor, InCombatActorPresenter](actorToPresenter)(toCombatActor)
}

package net.cucumbersome.rpgRoller.warhammer.combat.domain

import cats.data._
import net.cucumbersome.rpgRoller.warhammer.combat.domain.initiative.Initiative
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

private[combat] case class Combat(combatActors: List[InCombatActor])

private[domain] object Combat{
  def addActor(players: List[InCombatActor]): State[Combat, Unit] = State[Combat, Unit] {
   case Combat(currentPlayers) => (Combat(currentPlayers ++ players), Unit)
  }

  def sortByInitiative(roll: () => Int): State[Combat, Unit] =  State[Combat, Unit] {
    case Combat(players) => (Combat(Initiative.generateInitiativeAndSort(roll)(players)), Unit)
  }

  def removeActors(ids: List[InCombatActor.Id]): State[Combat, List[InCombatActor]] = State[Combat, List[InCombatActor]] {
    case Combat(players) =>
      val newActors = players.filterNot(p => ids.contains(p.id))
      (Combat(newActors), players.diff(newActors))
  }

  def updateHealth(actor: InCombatActor, newHealth: CombatActor.Health): State[Combat, List[InCombatActor]] = State[Combat, List[InCombatActor]] {
    case Combat(players) =>
      val newPlayers = players.map(ca => if (ca == actor) actor.copy(currentHealth = newHealth) else ca)
      val playersToBeRemoved = newPlayers.filter(ca => ca.currentHealth.data <= 0)
      (
        Combat(newPlayers.filterNot(playersToBeRemoved.contains(_))),
        playersToBeRemoved
      )
  }

  def empty: Combat = Combat(List())

}


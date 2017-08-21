package net.cucumbersome.rpgRoller.warhammer.combat

import cats.data._
import net.cucumbersome.rpgRoller.warhammer.combat.initiative.Initiative
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

case class Combat(combatActors: List[CombatActor])

object Combat{
  def addActor(players: List[CombatActor]): State[Combat, Unit] =  State[Combat, Unit] {
   case Combat(currentPlayers) => (Combat(currentPlayers ++ players), Unit)
  }

  def sortByInitiative(roll: () => Int): State[Combat, Unit] =  State[Combat, Unit] {
    case Combat(players) => (Combat(Initiative.generateInitiativeAndSort(roll)(players)), Unit)
  }

  def removeActors(actors: List[CombatActor]) : State[Combat, List[CombatActor]] = State[Combat, List[CombatActor]] {
    case Combat(players) => (Combat(players.filterNot(actors.contains(_))), actors)
  }

  def updateHealth(actor: CombatActor, newHealth: CombatActor.Health): State[Combat, List[CombatActor]] = State[Combat, List[CombatActor]]{
    case Combat(players) =>
      val newPlayers = players.map( ca => if(ca == actor) actor.copy(hp = newHealth) else ca)
      val playersToBeRemoved = newPlayers.filter(ca => ca.hp.data <= 0)
      (
        Combat(newPlayers.filterNot(playersToBeRemoved.contains(_))),
        playersToBeRemoved
      )
  }

  def empty: Combat = Combat(List())

}


package net.cucumbersome.rpgRoller.warhammer.combat.initiative

import cats.syntax.option._
import net.cucumbersome.rpgRoller.warhammer.combat.InCombatActor
import net.cucumbersome.rpgRoller.warhammer.combat.InCombatActor.{Initiative => ActorInitiative}

object Initiative {
  private lazy val getInitiative = (i: Option[ActorInitiative]) =>
    i.getOrElse(throw new IllegalStateException("Initiative cannot be none at this moment!"))

  def generateInitiativeAndSort(random: () => Int)(players: List[InCombatActor]): List[InCombatActor] = {
    val generate = generateIniiative(random) _
    players.map(generate).sortBy(a => getInitiative(a.initiative).data).reverse
  }

  private def generateIniiative(random: () => Int)(actor: InCombatActor): InCombatActor = {
    val agi = actor.actor.statistics.agility.data
    val initiative = ActorInitiative(agi + random())
    actor.copy(initiative = initiative.some)
  }
}

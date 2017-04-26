package net.cucumbersome.rpgRoller.warhammer.combat.initiative

import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, Statistics}


object InitiativeHandler {
  def generateInitiativeAndSort(random: () => Int)(players: List[CombatActor]): List[CombatActor] ={
    val generate = generateIniiative(random) _
    players.map(generate).sortBy(_._2).reverse.map(_._1)
  }
  private def generateIniiative(random: () => Int)(combatActor: CombatActor): (CombatActor, Int) =
    (combatActor, combatActor.statistics.agility.data + random())
}

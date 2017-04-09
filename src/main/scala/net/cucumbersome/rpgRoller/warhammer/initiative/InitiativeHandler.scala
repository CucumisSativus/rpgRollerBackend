package net.cucumbersome.rpgRoller.warhammer.initiative

import net.cucumbersome.rpgRoller.warhammer.Player

import scala.util.Random

object InitiativeHandler {
  def generateInitiativeAndSort(random: () => Int)(players: List[Player]): List[Player] ={
    val generate = generateIniiative(random) _
    players.map(generate).sortBy(_._2).reverse.map(_._1)
  }
  private def generateIniiative(random: () => Int)(player: Player): (Player, Int) =
    (player, player.agility.data + random())
}

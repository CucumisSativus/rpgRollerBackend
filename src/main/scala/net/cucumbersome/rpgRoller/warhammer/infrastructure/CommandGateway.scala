package net.cucumbersome.rpgRoller.warhammer.infrastructure

import akka.actor.{Actor, ActorRef, Props}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.CombatCommand

class CommandGateway(combatHandler: ActorRef) extends Actor {
  override def receive: Receive = {
    case c: CombatCommand => combatHandler.forward(c)
  }
}

object CommandGateway {
  def props(combatHandler: ActorRef): Props = Props(new CommandGateway(combatHandler))
}

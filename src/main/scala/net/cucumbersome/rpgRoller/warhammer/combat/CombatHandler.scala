package net.cucumbersome.rpgRoller.warhammer.combat

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatHandler(id: String) extends PersistentActor with ActorLogging{
  import CombatHandler._
  override def persistenceId: String = id
  private var state: Combat = Combat.empty

  override def receiveRecover: Receive = {
    case evt: CombatEvent => handleEvent(evt)
    case RecoveryCompleted => log.info("Recovery completed!")
  }

  override def receiveCommand: Receive = {
    case InitCombat(actors) => persist(CombatInitialized(actors))(handleEvent)
    case GetCombat() => sender ! GetCombatResponse(state)
  }

  private def handleEvent(evt: CombatEvent): Unit = evt match {
    case CombatInitialized(actors) => ???
  }
}

object CombatHandler{
  def props(id: String): Props = Props(new CombatHandler(id))
  sealed trait CombatEvent
  case class CombatInitialized(actors: List[CombatActor]) extends CombatEvent

  case class InitCombat(actors: List[CombatActor])
  case class GetCombat()
  case class GetCombatResponse(combat: Combat)
}

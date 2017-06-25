package net.cucumbersome.rpgRoller.warhammer.combat

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatHandler(actorId: String) extends PersistentActor with ActorLogging{
  import CombatHandler._
  override def persistenceId: String = actorId
  private var state: Map[String, Combat] = Map()

  override def receiveRecover: Receive = {
    case evt: CombatEvent => handleEvent(evt)
    case RecoveryCompleted => log.debug("Recovery completed!")
  }

  override def receiveCommand: Receive = {
    case InitCombat(id, actors) => persist(CombatInitialized(id, actors))(handleEvent)
    case AddActors(id, actors) => persist(ActorsAdded(id, actors))(handleEvent)
    case RemoveActors(id, actors) => persist(ActorsRemoved(id, actors))(handleEvent)
    case GetCombat(id) => sender ! GetCombatResponse(id, getCombat(id))
  }

  private def handleEvent(evt: CombatEvent): Unit = evt match {
    case CombatInitialized(id, actors) =>
      val (newState, _) = Combat.addActor(actors).run(Combat.empty).value
      state = state + (id -> newState)
    case ActorsAdded(id, actors) =>
      val (newState, _) = Combat.addActor(actors).run(getCombat(id)).value
      state = state + (id -> newState)
    case ActorsRemoved(id, actorsToBeRemoved) =>
      val (newState, _) = Combat.removeActors(actorsToBeRemoved).run(getCombat(id)).value
      state = state + (id -> newState)
  }

  private def getCombat(id: String): Combat = {
    state.getOrElse(id, Combat.empty)
  }
}

object CombatHandler{
  def props(actorId: String): Props = Props(new CombatHandler(actorId))

  sealed trait CombatEvent
  case class CombatInitialized(id: String, actors: List[CombatActor]) extends CombatEvent
  case class ActorsAdded(id: String, actors: List[CombatActor]) extends CombatEvent
  case class ActorsRemoved(id: String, actors: List[CombatActor]) extends CombatEvent

  case class InitCombat(id: String, actors: List[CombatActor])

  case class GetCombat(id: String)
  case class GetCombatResponse(id: String, combat: Combat)

  case class AddActors(id: String, actors: List[CombatActor])

  case class RemoveActors(id: String, actors: List[CombatActor])
}

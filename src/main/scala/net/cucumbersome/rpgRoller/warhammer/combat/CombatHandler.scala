package net.cucumbersome.rpgRoller.warhammer.combat

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import cats.syntax.option._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatHandler(actorId: String) extends PersistentActor with ActorLogging {

  import CombatHandler._

  override def persistenceId: String = actorId

  private var state: Map[String, Combat] = Map()

  override def receiveRecover: Receive = {
    case evt: CombatEvent => handleEvent(None)(evt)
    case RecoveryCompleted => log.debug("Recovery completed!")
  }

  private def handleEvent(responder: Option[ActorRef])(evt: CombatEvent): Unit = evt match {
    case CombatInitialized(id, actors) =>
      val (newState, _) = Combat.addActor(actors).run(Combat.empty).value
      state = state + (id -> newState)
      respondWithCombat(responder, id)
    case ActorsAdded(id, actors) =>
      val (newState, _) = Combat.addActor(actors).run(getCombat(id)).value
      state = state + (id -> newState)
      respondWithCombat(responder, id)
    case ActorsRemoved(id, actorsToBeRemoved) =>
      val (newState, _) = Combat.removeActors(actorsToBeRemoved).run(getCombat(id)).value
      state = state + (id -> newState)
      respondWithCombat(responder, id)
  }

  private def respondWithCombat(responder: Option[ActorRef], id: String): Unit = responder match {
    case Some(sender) => sender ! returnCombat(id)
    case None =>
  }

  private def returnCombat(id: String): GetCombatResponse = {
    GetCombatResponse(id, getCombat(id))
  }

  override def receiveCommand: Receive = {
    case InitCombat(id, actors) => persist(CombatInitialized(id, actors))(handleEvent(sender().some))
    case AddActors(id, actors) => persist(ActorsAdded(id, actors))(handleEvent(sender().some))
    case RemoveActors(id, actors) => persist(ActorsRemoved(id, actors))(handleEvent(sender().some))
    case GetCombat(id) => sender ! returnCombat(id)
  }

  private def getCombat(id: String): Combat = {
    state.getOrElse(id, Combat.empty)
  }
}

object CombatHandler {
  def props(actorId: String): Props = Props(new CombatHandler(actorId))

  sealed trait CombatEvent

  case class CombatInitialized(id: String, actors: List[CombatActor]) extends CombatEvent

  case class ActorsAdded(id: String, actors: List[CombatActor]) extends CombatEvent

  case class ActorsRemoved(id: String, actors: List[CombatActor]) extends CombatEvent

  sealed trait CombatCommand {
    def id: String
  }

  case class InitCombat(id: String, actors: List[CombatActor]) extends CombatCommand

  case class AddActors(id: String, actors: List[CombatActor]) extends CombatCommand

  case class RemoveActors(id: String, actors: List[CombatActor]) extends CombatCommand

  case class GetCombat(id: String)

  case class GetCombatResponse(id: String, combat: Combat)

}

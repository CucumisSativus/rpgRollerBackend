package net.cucumbersome.rpgRoller.warhammer.combat

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import cats.syntax.option._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

import scala.concurrent.Promise

class CombatHandler(actorId: String) extends PersistentActor with ActorLogging {

  import CombatHandler._

  override def persistenceId: String = actorId

  private var state: Map[String, Combat] = Map()

  override def receiveRecover: Receive = {
    case evt: CombatEvent => handleEvent(None)(evt)
    case RecoveryCompleted => log.debug("Recovery completed!")
  }

  private def handleEvent(responder: Option[Promise[GetCombatResponse]])(evt: CombatEvent): Unit = evt match {
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

  private def respondWithCombat(responder: Option[Promise[GetCombatResponse]], id: String): Unit = responder match {
    case Some(r) => r.success(returnCombat(id))
    case None =>
  }

  private def returnCombat(id: String): GetCombatResponse = {
    GetCombatResponse(id, getCombat(id))
  }

  override def receiveCommand: Receive = {
    case c: CombatCommand => handleCommand(c, None)
    case WrappedCommand(c, r) => handleCommand(c, r.some)
    case GetCombat(id) => sender ! returnCombat(id)
  }

  private def getCombat(id: String): Combat = {
    state.getOrElse(id, Combat.empty)
  }

  private def handleCommand(command: CombatCommand, response: Option[Promise[GetCombatResponse]]): Unit = command match {
    case InitCombat(id, actors) => persist(CombatInitialized(id, actors))(handleEvent(response))
    case AddActors(id, actors) => persist(ActorsAdded(id, actors))(handleEvent(response))
    case RemoveActors(id, actors) => persist(ActorsRemoved(id, actors))(handleEvent(response))
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

  case class WrappedCommand(command: CombatCommand, responsePromise: Promise[GetCombatResponse])

  case class GetCombat(id: String)

  case class GetCombatResponse(id: String, combat: Combat)

}

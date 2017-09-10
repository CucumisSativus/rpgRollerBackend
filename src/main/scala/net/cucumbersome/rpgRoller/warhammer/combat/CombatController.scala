package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.InitCombat
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository.FilterExpression

import scala.concurrent.{ExecutionContext, Future}

class CombatController(combatHandler: ActorRef, actorRepository: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator)
                      (implicit val ec: ExecutionContext) {

  def route: Route = pathPrefix("combat") {
    path("new") {
      get {
        entity(as[CreateCombatParameters]) { combat =>
          completeOrRecoverWith(createCombat(combat)) { ex =>
            failWith(ex)
          }
        }
      }
    }
  }

  private def createCombat(params: CreateCombatParameters): Future[NewCombat] = {
    val actorIds = params.actorIds
    val newId = idGenerator.generateId

    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
    } yield {
      combatHandler ! InitCombat(newId, actors)
      NewCombat(newId)
    }
  }
}

object CombatController {

  trait CombatIdGenerator {
    def generateId: String
  }

  case object DefaultIdGenerator extends CombatIdGenerator {
    override def generateId: String = UUID.randomUUID().toString
  }
}

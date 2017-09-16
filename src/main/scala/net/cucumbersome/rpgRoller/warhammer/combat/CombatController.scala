package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.{AddActors, GetCombatResponse, InitCombat}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository.FilterExpression

import scala.concurrent.{ExecutionContext, Future}

class CombatController(commandGateway: ActorRef, actorRepository: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator)
                      (implicit val ec: ExecutionContext) {

  implicit val timeout: Timeout = Timeout(2, TimeUnit.SECONDS)

  def route: Route = pathPrefix("combat") {
    path("new") {
      get {
        entity(as[CreateCombatParameters]) { combat =>
          completeOrRecoverWith(createCombat(combat)) { ex =>
            failWith(ex)
          }
        }
      }
    } ~ pathPrefix(Segment) { combatId =>
      path("add-actors") {
        patch {
          entity(as[AddActorsToCombatParameters]) { params =>
            completeOrRecoverWith(addActorsToCombat(params)) { ex =>
              failWith(ex)
            }
          }
        }
      }
    }
  }

  private def createCombat(params: CreateCombatParameters): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    val newId = idGenerator.generateId

    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      response <- (commandGateway ? InitCombat(newId, actors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
    }
  }

  private def addActorsToCombat(params: AddActorsToCombatParameters): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      response <- (commandGateway ? AddActors(params.combatId, actors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
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

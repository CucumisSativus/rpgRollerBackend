package net.cucumbersome.rpgRoller.warhammer.combat.domain

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.combat.CombatPresenter
import net.cucumbersome.rpgRoller.warhammer.combat.domain.CombatHandler.{AddActors, GetCombatResponse, InitCombat, RemoveActors}
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

import scala.concurrent.{ExecutionContext, Future}

trait CombatService {
  def createCombat(params: CreateCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter]
  def removeActorsFromCombat(combatId: String, params: RemoveActorsFromCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter]
  def addActorsToCombat(combatId: String, params: AddActorsToCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter]
}

private[domain] class ActorBasedCombatService(commandGateway: ActorRef, actorRepository: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator) extends CombatService {
  implicit val timeout: Timeout = Timeout(2, TimeUnit.SECONDS)

  def createCombat(params: CreateCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    val newId = idGenerator.generateId

    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      convertedActors = convertCombatActorsToInCombatActor(actors)
      response <- (commandGateway ? InitCombat(newId, convertedActors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
    }
  }

  def removeActorsFromCombat(combatId: String, params: RemoveActorsFromCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter] = {
    val actorIds = params.actorIds.map(InCombatActor.Id.apply)
    (commandGateway ? RemoveActors(combatId, actorIds.toList)).mapTo[GetCombatResponse].
      map(r => CombatPresenter.combatToCombatPresenter(r.id, r.combat))
  }

  def addActorsToCombat(combatId: String, params: AddActorsToCombatParameters)(implicit ec: ExecutionContext): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      convertedActors = convertCombatActorsToInCombatActor(actors)
      response <- (commandGateway ? AddActors(combatId, convertedActors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
    }
  }

  private def convertCombatActorsToInCombatActor(combatActors: List[CombatActor]): List[InCombatActor] = {
    val actorIdGenerator = () => InCombatActor.Id(idGenerator.generateId)
    combatActors.map(a => InCombatActor.buildFromCombatActor(a, idGenerator = actorIdGenerator))
  }
}


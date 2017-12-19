package net.cucumbersome.rpgRoller.warhammer.combat.domain

import akka.actor.{ActorRef, ActorSystem}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository

object CombatInitializer {
  def initializeCombatService(commandGateway: ActorRef, actorRepository: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator): CombatService = {
    new ActorBasedCombatService(commandGateway, actorRepository, idGenerator)
  }

  def initializeCombatHandler(implicit system: ActorSystem) = {
    system.actorOf(CombatHandler.props("combatHandler"), "combatHandler")
  }
}

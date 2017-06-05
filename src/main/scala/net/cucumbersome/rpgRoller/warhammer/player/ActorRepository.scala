package net.cucumbersome.rpgRoller.warhammer.player

import scala.concurrent.{ExecutionContext, Future}

trait ActorRepository {
  def all(implicit ec: ExecutionContext): Future[List[CombatActor]]
}

class InMemoryActorRepository(initialActors: List[CombatActor]) extends ActorRepository{
  var actors: List[CombatActor] = initialActors

  override def all(implicit ec: ExecutionContext): Future[List[CombatActor]] = {
    Future.successful(actors)
  }
}

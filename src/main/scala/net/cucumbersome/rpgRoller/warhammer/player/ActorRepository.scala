package net.cucumbersome.rpgRoller.warhammer.player

import akka.Done

import scala.concurrent.{ExecutionContext, Future}

trait ActorRepository {
  def all(implicit ec: ExecutionContext): Future[List[CombatActor]]
  def find(id: CombatActor.Id)(implicit ec: ExecutionContext): Future[Option[CombatActor]]
  def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done]
}

class InMemoryActorRepository(initialActors: List[CombatActor]) extends ActorRepository{
  var actors: List[CombatActor] = initialActors

  override def all(implicit ec: ExecutionContext): Future[List[CombatActor]] = {
    Future.successful(actors)
  }

  override def find(id: CombatActor.Id)(implicit ec: ExecutionContext): Future[Option[CombatActor]] = {
    Future.successful(actors.find(_.id == id))
  }

  override def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done] = {
    actors = actors ++ List(combatActor)
    Future.successful(Done)
  }

  def clear() : Unit = {
    actors = List()
  }
}

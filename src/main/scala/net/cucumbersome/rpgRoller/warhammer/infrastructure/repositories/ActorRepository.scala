package net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories

import akka.Done
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

import scala.concurrent.{ExecutionContext, Future}
trait ActorRepository {
  def all(implicit ec: ExecutionContext): Future[List[CombatActor]]

  def find(id: CombatActor.Id)(implicit ec: ExecutionContext): Future[Option[CombatActor]]
  def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done]

  def filter(expression: FilterExpression)(implicit ec: ExecutionContext): Future[List[CombatActor]]
}

class InMemoryActorRepository(initialActors: List[CombatActor]) extends ActorRepository {
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

  def clear(): Unit = {
    actors = List()
  }

  override def filter(expression: FilterExpression)(implicit ec: ExecutionContext): Future[List[CombatActor]] = expression match {
    case ByName(value) => Future.successful(actors.filter(_.name.data.contains(value)))
    case ByHealth(value) => Future.successful(actors.filter(_.hp.data == value))
    case ByIds(ids) => Future.successful(actors.filter(a => ids.contains(a.id.data)))
  }
}

object ActorRepository {

  sealed trait FilterExpression
  object FilterExpression {

    case class ByName(value: String) extends FilterExpression

    case class ByHealth(value: Int) extends FilterExpression

    case class ByIds(ids: Seq[String]) extends FilterExpression
  }
}

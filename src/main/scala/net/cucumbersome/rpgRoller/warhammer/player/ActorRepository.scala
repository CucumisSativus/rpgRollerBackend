package net.cucumbersome.rpgRoller.warhammer.player

import akka.Done
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.player.ActorRepository.FilterExpression._

import scala.concurrent.{ExecutionContext, Future}
trait ActorRepository {
  def all(implicit ec: ExecutionContext): Future[List[CombatActor]]
  def find(id: CombatActor.Id)(implicit ec: ExecutionContext): Future[Option[CombatActor]]
  def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done]

  def filter[A <: FilterExpression.Column](expression: FilterExpression[A])(implicit ec: ExecutionContext): Future[List[CombatActor]]
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

  override def filter[A <: FilterExpression.Column](expression: FilterExpression[A])(implicit ec: ExecutionContext): Future[List[CombatActor]] = expression.column match {
    case ByName(value) => Future.successful(actors.filter(_.name.data.contains(value)))
    case ByHealth(value) => Future.successful(actors.filter(_.hp.data == value))
  }
}

object ActorRepository {

  case class FilterExpression[A <: FilterExpression.Column](column: A)

  object FilterExpression {

    sealed trait Column {
      type Value

      def value: Value
    }

    case class ByName(value: String) extends Column {
      type Value = String
    }

    case class ByHealth(value: Int) extends Column {
      type Value = Int
    }

  }
}

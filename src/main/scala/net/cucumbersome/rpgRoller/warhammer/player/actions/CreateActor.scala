package net.cucumbersome.rpgRoller.warhammer.player.actions

import cats.data.Validated.{Invalid, Valid}
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorValidator.validate
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorPresenter

import scala.concurrent.{ExecutionContext, Future}
object CreateActor {
  def createActor(repository: ActorRepository)(toBeSaved: CombatActorPresenter)(implicit ec: ExecutionContext): Future[CommandResult] =
    validate(toBeSaved) match {
      case Invalid(errors) => Future.successful(CommandFailed(errors.map(_.error).toList))
      case Valid(ca) => repository.add(ca).map(_ => Ok)
    }

}

sealed trait CommandResult
case object Ok extends CommandResult
case class CommandFailed(errors: List[String]) extends CommandResult
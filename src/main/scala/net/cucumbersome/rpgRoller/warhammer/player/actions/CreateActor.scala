package net.cucumbersome.rpgRoller.warhammer.player.actions

import akka.Done
import cats.data.Validated.{Invalid, Valid}
import net.cucumbersome.rpgRoller.warhammer.player.{ActorRepository, CombatActor, CombatActorValidator}

import scala.concurrent.{ExecutionContext, Future}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActorValidator.validate
object CreateActor {
  def createActor(repository: ActorRepository)(toBeSaved: CombatActor)(implicit ec: ExecutionContext): Future[CommandResult] =
    validate(toBeSaved) match {
      case Invalid(errors) => Future.successful(CommandFailed(errors.map(_.error).toList))
      case Valid(ca) => repository.add(ca).map(_ => Ok)
    }

}

trait CommandResult
case object Ok extends CommandResult
case class CommandFailed(errors: List[String]) extends CommandResult
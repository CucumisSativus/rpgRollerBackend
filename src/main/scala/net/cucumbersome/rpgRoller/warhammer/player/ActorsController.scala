package net.cucumbersome.rpgRoller.warhammer.player

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.cucumbersome.rpgRoller.warhammer.player.actions.{CommandFailed, CreateActor, Ok}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
class ActorsController(actorRepository: ActorRepository)(implicit executionContext: ExecutionContext)
  extends CombatActorJsonFormats with SprayJsonSupport {
  private val saveActor = CreateActor.createActor(actorRepository) _
  private val convertActor = CombatActorPresenter.fromCombatActor.get _
  private val convertActors = (l: List[CombatActor]) => l.map(convertActor)
  def route: Route = {
    path("actors"){
      get{
        val actors = actorRepository.all
        onComplete(actors){
          case Success(fetched) => complete(convertActors(fetched))
          case _ => complete(StatusCodes.InternalServerError)
        }
      } ~
      post{
        entity(as[CombatActorPresenter]) { ca =>
          onComplete(saveActor(ca)){
            case Success(Ok) => complete("ok")
            case Success(CommandFailed(errors)) => complete(StatusCodes.BadRequest)
            case _ => complete(StatusCodes.InternalServerError)
          }
        }
      }
    } ~
    pathPrefix("actor" / Segment){ id =>
      get {
        val actor = actorRepository.find(new CombatActor.Id(id))
        onComplete(actor){
          case Success(Some(fetched)) => complete(convertActor(fetched))
          case Success(None) => complete(StatusCodes.NotFound)
          case Failure(_) => complete(StatusCodes.InternalServerError)
        }
      }
    }
  }
}

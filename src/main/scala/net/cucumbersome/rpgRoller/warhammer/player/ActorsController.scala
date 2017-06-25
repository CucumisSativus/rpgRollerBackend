package net.cucumbersome.rpgRoller.warhammer.player
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.actions.{CommandFailed, CreateActor, Ok}
class ActorsController(actorRepository: ActorRepository)(implicit executionContext: ExecutionContext)
  extends JsonFormats with SprayJsonSupport{
  val saveActor = CreateActor.createActor(actorRepository) _
  def route: Route = {
    path("actors"){
      get{
        val actors = actorRepository.all
        onComplete(actors){
          case Success(fetched) => complete(fetched)
          case _ => complete(StatusCodes.InternalServerError)
        }
      } ~
      post{
        entity(as[CombatActor]) { ca =>
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
          case Success(Some(fetched)) => complete(fetched)
          case Success(None) => complete(StatusCodes.NotFound)
          case Failure(_) => complete(StatusCodes.InternalServerError)
        }
      }
    }
  }
}

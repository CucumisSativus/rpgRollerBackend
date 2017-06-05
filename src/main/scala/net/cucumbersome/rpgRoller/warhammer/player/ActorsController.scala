package net.cucumbersome.rpgRoller.warhammer.player
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext
import scala.util.Success
import spray.json._
class ActorsController(actorRepository: ActorRepository)(implicit executionContext: ExecutionContext){
  def route: Route = {
    path("actors"){
      get{
        val actors = actorRepository.all
        onComplete(actors){
          case Success(fetched) => complete(HttpEntity(fetched.map(_.name.data).mkString(" ")))
          case _ => complete(StatusCodes.InternalServerError)
        }
      }
    }
  }
}

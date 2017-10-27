package net.cucumbersome.rpgRoller.warhammer.player

import javax.ws.rs.Path

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.cucumbersome.rpgRoller.warhammer.player.actions.{CommandFailed, CreateActor, Ok}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import io.swagger.annotations._
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository

@Api(value = "/actors", produces = "application/json")
@Path("/actors")
class ActorsController(actorRepository: ActorRepository)(implicit executionContext: ExecutionContext)
  extends CombatActorJsonFormats with SprayJsonSupport {
  private val saveActor = CreateActor.createActor(actorRepository) _
  private val convertActor = CombatActorPresenter.fromCombatActor.get _
  private val convertActors = (l: List[CombatActor]) => l.map(convertActor)
  def route: Route = getActorsList ~
    createActor ~
    getOneActor

  @ApiOperation(value = "Get list of possible  comat actors", nickname = "getActorList", httpMethod = "GET", response = classOf[Array[CombatActorPresenter]])
  @ApiResponses(Array(
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getActorsList = path("actors"){
    get{
      val actors = actorRepository.all
      onComplete(actors){
        case Success(fetched) => complete(convertActors(fetched))
        case _ => complete(StatusCodes.InternalServerError)
      }
    }
  }

  @ApiOperation(value = "Add actor", nickname = "addActor", httpMethod = "POST", response = classOf[CombatActorPresenter])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "actor", value = "actor to be added", required = true,
      dataTypeClass = classOf[CombatActorPresenter], paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def createActor = path("actors"){
    post{
      entity(as[CombatActorPresenter]) { ca =>
        onComplete(saveActor(ca)){
          case Success(Ok) => complete(ca)
          case Success(CommandFailed(_)) => complete(StatusCodes.BadRequest)
          case Failure(ex) => failWith(ex)
        }
      }
    }
  }

  @Path("/{id}")
  @ApiOperation(value = "Get one specific actor", nickname = "showActor", httpMethod = "GET", response = classOf[CombatActorPresenter])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "actor id", required = true,
      dataType = "string", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 500, message = "Internal server error"),
    new ApiResponse(code = 404, message = "actor with given id is not found")
  ))
  def getOneActor = path("actor" / Segment){ id =>
    get {
      val actor = actorRepository.find(new CombatActor.Id(id))
      onComplete(actor){
        case Success(Some(fetched)) => complete(convertActor(fetched))
        case Success(None) => complete(StatusCodes.NotFound)
        case Failure(ex) => failWith(ex)
      }
    }
  }
}

package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID
import javax.ws.rs.Path

import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import io.swagger.annotations.{Api, ApiImplicitParam, ApiImplicitParams, ApiOperation}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.combat.domain.CombatService

import scala.concurrent.ExecutionContext

@Api(value = "/combat", produces = "application/json")
@Path("/combat")
class CombatController(service: CombatService)(implicit val ec: ExecutionContext) {

  def route: Route = pathPrefix("combat") {
    createCombatRoute ~
    addActorsRoute ~
    removeActorsRoute
  }

  @ApiOperation(value = "Create new combat", nickname = "addCombat", httpMethod = "POST", response = classOf[CombatPresenter])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "combat", value = "combat parameters", required = true,
      dataTypeClass = classOf[CreateCombatParameters], paramType = "body")
  ))
  def createCombatRoute: Route = pathEndOrSingleSlash {
    logRequest(s"create new combat", Logging.InfoLevel) {
      post {
        entity(as[CreateCombatParameters]) { combat =>
          completeOrRecoverWith(service.createCombat(combat)) { ex =>
            failWith(ex)
          }
        }
      }
    }
  }

  @Path("/{combatId}/add-actors")
  @ApiOperation(value = "Add actors to combat", nickname = "addActorsToCombat", httpMethod = "PATCH", response = classOf[CombatPresenter])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "actorIds", value = "list of actor ids to be added", required = true,
      dataTypeClass = classOf[AddActorsToCombatParameters], paramType = "body"),
    new ApiImplicitParam(name = "combatId", value = "combat id", required = true,
      dataType = "string", paramType = "path")
  ))
  def addActorsRoute: Route = path(Segment / "add-actors"){ combatId =>
    logRequest(s"add actor to combat ${combatId}", Logging.InfoLevel) {
      patch {
        entity(as[AddActorsToCombatParameters]) { params =>
          completeOrRecoverWith(service.addActorsToCombat(combatId, params)) { ex =>
            failWith(ex)
          }
        }
      }
    }
  }

  @Path("/{combatId}/remove-actors")
  @ApiOperation(value = "Remove actors from combat", nickname = "removeActorsFromCombat", httpMethod = "PATCH", response = classOf[CombatPresenter])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "combat", value = "list of actor ids to be removed", required = true,
      dataTypeClass = classOf[RemoveActorsFromCombatParameters], paramType = "body"),
    new ApiImplicitParam(name = "combatId", value = "combat id", required = true,
      dataType = "string", paramType = "path")
  ))
  def removeActorsRoute: Route = path(Segment / "remove-actors") { combatId =>
    logRequest(s"remove actor from combat ${combatId}", Logging.InfoLevel) {
      patch {
        entity(as[RemoveActorsFromCombatParameters]) { params =>
          completeOrRecoverWith(service.removeActorsFromCombat(combatId, params)) { ex =>
            failWith(ex)
          }
        }
      }
    }
  }
}

object CombatController {

  trait CombatIdGenerator {
    def generateId: String
  }

  case object DefaultIdGenerator extends CombatIdGenerator {
    override def generateId: String = UUID.randomUUID().toString
  }

}

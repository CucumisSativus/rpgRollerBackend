package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout
import io.swagger.annotations.{Api, ApiImplicitParam, ApiImplicitParams, ApiOperation}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.{CombatIdGenerator, DefaultIdGenerator}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.{AddActors, GetCombatResponse, InitCombat, RemoveActors}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatJsonSerializer._
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

import scala.concurrent.{ExecutionContext, Future}

@Api(value = "/combat", produces = "application/json")
@Path("/combat")
class CombatController(commandGateway: ActorRef, actorRepository: ActorRepository, idGenerator: CombatIdGenerator = DefaultIdGenerator)
                      (implicit val ec: ExecutionContext) {

  implicit val timeout: Timeout = Timeout(2, TimeUnit.SECONDS)


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
    post {
      entity(as[CreateCombatParameters]) { combat =>
        completeOrRecoverWith(createCombat(combat)) { ex =>
          failWith(ex)
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
    patch {
      entity(as[AddActorsToCombatParameters]) { params =>
        completeOrRecoverWith(addActorsToCombat(combatId, params)) { ex =>
          failWith(ex)
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
    patch {
      entity(as[RemoveActorsFromCombatParameters]) { params =>
        completeOrRecoverWith(removeActorsFromCombat(combatId, params)) { ex =>
          failWith(ex)
        }
      }
    }
  }

  private def createCombat(params: CreateCombatParameters): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    val newId = idGenerator.generateId

    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      convertedActors = convertCombatActorsToInCombatActor(actors)
      response <- (commandGateway ? InitCombat(newId, convertedActors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
    }
  }

  private def removeActorsFromCombat(combatId: String, params: RemoveActorsFromCombatParameters): Future[CombatPresenter] = {
    val actorIds = params.actorIds.map(InCombatActor.Id.apply)
    (commandGateway ? RemoveActors(combatId, actorIds.toList)).mapTo[GetCombatResponse].
      map(r => CombatPresenter.combatToCombatPresenter(r.id, r.combat))

  }

  private def addActorsToCombat(combatId: String, params: AddActorsToCombatParameters): Future[CombatPresenter] = {
    val actorIds = params.actorIds
    for {
      actors <- actorRepository.filter(FilterExpression.ByIds(actorIds))
      convertedActors = convertCombatActorsToInCombatActor(actors)
      response <- (commandGateway ? AddActors(combatId, convertedActors)).mapTo[GetCombatResponse]
    } yield {
      CombatPresenter.combatToCombatPresenter(response.id, response.combat)
    }
  }

  private def convertCombatActorsToInCombatActor(combatActors: List[CombatActor]): List[InCombatActor] = {
    val actorIdGenerator = () => InCombatActor.Id(idGenerator.generateId)
    combatActors.map(a => InCombatActor.buildFromCombatActor(a, idGenerator = actorIdGenerator))
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

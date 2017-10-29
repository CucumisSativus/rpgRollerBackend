package net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories

import akka.Done
import cats.Functor
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import net.cucumbersome.rpgRoller.warhammer.player.Statistics

import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId
import cats.implicits._
class MongoDbActorRepository(collection: MongoCollection[DatabaseActor]) extends ActorRepository {

  import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository._

  override def all(implicit ec: ExecutionContext): Future[List[CombatActor]] =
    collection
      .find()
      .map(databaseActorToComatActor)
      .toFuture()
      .map(_.toList)

  override def find(id: CombatActor.Id)(implicit ec: ExecutionContext): Future[Option[CombatActor]] =
    collection.
      find(Document("actorId" -> id.data))
      .first()
      .head()
      .map(Option.apply)
      .map(Functor[Option].lift(databaseActorToComatActor))

  override def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done] = ???

  override def filter(expression: FilterExpression)(implicit ec: ExecutionContext): Future[List[CombatActor]] = ???
}

object MongoDbActorRepository {

  import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._

  case class DatabaseActor(_id: ObjectId,
                           actorId: String,
                           actorName: String,
                           hp: Int,
                           weaponSkill: Int,
                           ballisticSkill: Int,
                           strength: Int,
                           toughness: Int,
                           agility: Int,
                           intelligence: Int,
                           perception: Int,
                           willPower: Int,
                           fellowship: Int,
                           influence: Int
                          )

  object DatabaseActor {
    def apply(actorId: String,
              actorName: String,
              hp: Int,
              weaponsSkill: Int,
              ballisticSkill: Int,
              strength: Int,
              toughness: Int,
              agility: Int,
              intelligence: Int,
              perception: Int,
              willPower: Int,
              fellowship: Int,
              influence: Int
             ): DatabaseActor = new DatabaseActor(new ObjectId(),
      actorId,
      actorName,
      hp,
      weaponsSkill,
      ballisticSkill,
      strength,
      toughness,
      agility,
      intelligence,
      perception,
      willPower,
      fellowship,
      influence
    )
  }

  private[repositories] def databaseActorToComatActor(db: DatabaseActor): CombatActor =
    CombatActor(
      id = db.actorId.toId,
      name = db.actorName.toName,
      hp = db.hp.toHp,
      statistics = Statistics(
        weaponSkill = db.weaponSkill.toWs,
        ballisticSkill = db.ballisticSkill.toBs,
        strength = db.strength.toStr,
        toughness = db.toughness.toTg,
        agility = db.agility.toAg,
        intelligence = db.intelligence.toIt,
        perception = db.perception.toPer,
        willPower = db.willPower.toWp,
        fellowship = db.fellowship.toFel,
        influence = db.influence.toInfl
      )

    )
}

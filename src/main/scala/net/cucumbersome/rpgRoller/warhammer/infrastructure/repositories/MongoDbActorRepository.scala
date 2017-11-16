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
import org.mongodb.scala.model.Filters._
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

  override def add(combatActor: CombatActor)(implicit ec: ExecutionContext): Future[Done] =
    collection
      .insertOne(combatActorToDatabaseActor(combatActor))
      .head()
      .map(_ => Done)


  override def filter(expression: FilterExpression)(implicit ec: ExecutionContext): Future[List[CombatActor]] = expression match{
    case FilterExpression.ByIds(ids) => filterByIds(ids)
    case expr => throw new Exception(s"${expr} is not supported yet")
  }

  private def filterByIds(ids: Seq[String])(implicit ec: ExecutionContext) = {
    collection
        .find()
          .filter(in("actorId", ids : _*))
          .toFuture()
          .map(_.map(databaseActorToComatActor).toList)
  }
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

  private[repositories] def combatActorToDatabaseActor(ac: CombatActor): DatabaseActor =
    DatabaseActor(
      actorId = ac.id.data,
      actorName = ac.name.data,
      hp = ac.hp.data,
      weaponsSkill = ac.statistics.weaponSkill.data,
      ballisticSkill = ac.statistics.ballisticSkill.data,
      strength = ac.statistics.strength.data,
      toughness = ac.statistics.toughness.data,
      agility = ac.statistics.agility.data,
      intelligence = ac.statistics.intelligence.data,
      perception = ac.statistics.perception.data,
      willPower = ac.statistics.willPower.data,
      fellowship = ac.statistics.fellowship.data,
      influence = ac.statistics.influence.data
    )
}

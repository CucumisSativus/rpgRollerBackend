package net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories

import akka.Done
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.ActorRepository.FilterExpression
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import net.cucumbersome.rpgRoller.warhammer.player
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import net.cucumbersome.test.MongoDbMockDatabase
import org.mongodb.scala.MongoCollection
import net.cucumbersome.test.ActorMatchers._
import org.mongodb.scala.bson.collection.immutable.Document

import scala.util.{Failure, Success}
class MongoDbActorRepositorySpec extends UnitSpec with MongoDbMockDatabase with RandomDataGenerator{
  import scala.concurrent.ExecutionContext.Implicits.global
  import net.cucumbersome.test.DatabaseActorGenerator._
  "Mongodb repository" when {
    "querying for all actors" should {
      "return all actors if present in the database" in withActorCollection{ col =>
        val databaseActors = random[DatabaseActor](2).sortBy(_.actorId)
        futureValue(col.insertMany(databaseActors).toFuture())

        val repository = new MongoDbActorRepository(col)
        val obtainedActors = futureValue(repository.all).sortBy(_.id.data)

        obtainedActors(0) must beTheSameAsDatabaseActor(databaseActors(0))
        obtainedActors(1) must beTheSameAsDatabaseActor(databaseActors(1))
      }

      "return empty list if nothing is in the database" in withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        futureValue(repository.all) mustBe empty
      }
    }

    "quering for one specific actor" should {
      "return one actor if present in the database" in withActorCollection{ col =>
        val databaseActors = random[DatabaseActor](3).sortBy(_.actorId)
        futureValue(col.insertMany(databaseActors).toFuture())

        val repository = new MongoDbActorRepository(col)
        val obtainedActor = futureValue(repository.find(new CombatActor.Id(databaseActors(1).actorId)))

        obtainedActor.get must beTheSameAsDatabaseActor(databaseActors(1))
      }
      "return none if actor is not present in the database" in withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        val obtainedActor = futureValue(repository.find(new CombatActor.Id("notExisting")))

        obtainedActor mustBe None
      }
    }

    "addning new actor to the database" should {
      "add new actor if no actor with this given id exist" in withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        val combatActorToBeAdded = random[CombatActor]

        futureValue(repository.add(combatActorToBeAdded)) must be(Done)

        val obtainedActor = futureValue(col.find(Document("actorId" -> combatActorToBeAdded.id.data)).head())

        combatActorToBeAdded must beTheSameAsDatabaseActor(obtainedActor)
      }

      "do not add new actor if the id is aready taken" ignore withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        val dbActor = random[DatabaseActor]

        futureValue(col.insertOne(dbActor).toFuture())

        val combatActorToBeAdded = random[CombatActor].copy(id = CombatActor.Id(dbActor.actorId))
        futureValue(repository.add(combatActorToBeAdded)) mustBe a[Failure[_]]
      }
    }

    "filtering by ids" should {
      "return empty list if no actor is found" in withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        val databaseActors = random[DatabaseActor](3)
        futureValue(col.insertMany(databaseActors).toFuture())

        val obtainedActors = futureValue(repository.filter(FilterExpression.ByIds(Seq("not existing"))))

        obtainedActors mustBe Seq()
      }

      "return list of actors if they are found" in withActorCollection{ col =>
        val repository = new MongoDbActorRepository(col)
        val databaseActors = random[DatabaseActor](3).sortBy(_.actorId)
        futureValue(col.insertMany(databaseActors).toFuture())

        val obtainedActors = futureValue(repository.filter(FilterExpression.ByIds(databaseActors.map(_.actorId)))).sortBy(_.id.data)

        obtainedActors(0) must beTheSameAsDatabaseActor(databaseActors(0))
        obtainedActors(1) must beTheSameAsDatabaseActor(databaseActors(1))
        obtainedActors(2) must beTheSameAsDatabaseActor(databaseActors(2))
      }
    }
  }
}

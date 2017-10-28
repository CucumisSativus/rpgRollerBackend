package net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.UnitSpec
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import net.cucumbersome.test.MongoDbMockDatabase
import org.mongodb.scala.MongoCollection
import net.cucumbersome.test.ActorMatchers._
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
    }
  }
}

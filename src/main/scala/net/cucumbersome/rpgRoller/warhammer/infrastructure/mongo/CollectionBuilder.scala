package net.cucumbersome.rpgRoller.warhammer.infrastructure.mongo

import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.{Document, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

object CollectionBuilder {
  def buildActorsCollection(db: MongoDatabase): MongoCollection[DatabaseActor] = {
    val registry = fromRegistries(fromProviders(classOf[DatabaseActor]), DEFAULT_CODEC_REGISTRY)
    val col = db.withCodecRegistry(registry).getCollection[DatabaseActor]("actors")
    col.createIndex(Document("actorId" ->1 ), IndexOptions().unique(true) )
    col
  }
}

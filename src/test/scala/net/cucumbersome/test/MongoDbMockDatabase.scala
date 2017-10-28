package net.cucumbersome.test

import com.github.fakemongo.async.FongoAsync
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.{Document, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.scalatest.{Suite, SuiteMixin}

import scala.reflect.ClassTag
trait MongoDbMockDatabase extends SuiteMixin{ this: Suite =>
  def withCollection[T](c: Class[T])(f: MongoCollection[T] => Unit): Unit ={
    val fongo = new FongoAsync("akka-http-mongodb-microservice")
    val _db = fongo.getDatabase("mongo-database")
    val registry = fromRegistries(fromProviders(classOf[DatabaseActor]), DEFAULT_CODEC_REGISTRY)
    val col = MongoCollection[T](_db.withCodecRegistry(registry).getCollection("col", c))
    try{
      f(col)
    } finally {
    }
  }

  def withActorCollection = withCollection(classOf[DatabaseActor]) _
}

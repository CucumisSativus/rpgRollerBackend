package net.cucumbersome.test

import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import org.scalatest.matchers.{MatchResult, Matcher}

object ActorMatchers {

  class TheSameAsDatabaseActor(actor: DatabaseActor) extends Matcher[CombatActor] {
    override def apply(left: CombatActor): MatchResult = {
      val actorFields = combatActorToMap(left)
      val databaseFields = databaseActorToMap(actor)

      val differences = actorFields.toSet.diff(databaseFields.toSet).toMap

      MatchResult(
        differences.isEmpty,
        s"Combat actor and database actor does not match, differences ${differences.keys.toSeq}",
        "Actors matches"
      )
    }

  }

  private def combatActorToMap(ac: CombatActor): Map[String, Any] = {
    Map(
      "id" -> ac.id.data,
      "name" -> ac.name.data,
      "hp" -> ac.hp.data,
      "ws" -> ac.statistics.weaponSkill.data,
      "bs" -> ac.statistics.ballisticSkill.data,
      "str" -> ac.statistics.strength.data,
      "tg" -> ac.statistics.toughness.data,
      "ag" -> ac.statistics.agility.data,
      "int" -> ac.statistics.intelligence.data,
      "per" -> ac.statistics.perception.data,
      "wp" -> ac.statistics.willPower.data,
      "fel" -> ac.statistics.fellowship.data,
      "infl" -> ac.statistics.influence.data
    )
  }

  private def databaseActorToMap(ac: DatabaseActor): Map[String, Any] = {
    Map(
      "id" -> ac.actorId,
      "name" -> ac.actorName,
      "hp" -> ac.hp,
      "ws" -> ac.weaponSkill,
      "bs" -> ac.ballisticSkill,
      "str" -> ac.strength,
      "tg" -> ac.toughness,
      "ag" -> ac.agility,
      "int" -> ac.intelligence,
      "per" -> ac.perception,
      "wp" -> ac.willPower,
      "fel" -> ac.fellowship,
      "infl" -> ac.influence
    )
  }

  def beTheSameAsDatabaseActor(expected: DatabaseActor) = new TheSameAsDatabaseActor(expected)
}

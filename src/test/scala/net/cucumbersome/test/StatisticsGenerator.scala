package net.cucumbersome.test

import net.cucumbersome.rpgRoller.warhammer.combat.domain.InCombatActor
import net.cucumbersome.rpgRoller.warhammer.infrastructure.repositories.MongoDbActorRepository.DatabaseActor
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, Statistics}
import org.scalacheck.{Arbitrary, Gen}

object StatisticsGenerator {
  implicit val arbitraryStatistics: Arbitrary[Statistics] = Arbitrary {
    for{
      ws <- Gen.choose(0 ,100)
      bs <- Gen.choose(0 ,100)
      s <- Gen.choose(0, 100)
      t <- Gen.choose(0, 100)
      ag <- Gen.choose(0, 100)
      int <- Gen.choose(0, 100)
      per <- Gen.choose(0, 100)
      wp <- Gen.choose(0, 100)
      fel <- Gen.choose(0, 100)
      inf <- Gen.choose(0, 100)
    } yield Statistics(
      new Statistics.WeaponSkill(ws),
      new Statistics.BallisticSkill(bs),
      new Statistics.Strength(s),
      new Statistics.Toughness(t),
      new Statistics.Agility(ag),
      new Statistics.Intelligence(int),
      new Statistics.Perception(per),
      new Statistics.WillPower(wp),
      new Statistics.Fellowship(fel),
      new Statistics.Influence(inf)
    )
  }
}

object CombatActorGenerator {
  import StatisticsGenerator.arbitraryStatistics

  implicit val arbitraryCombatActor: Arbitrary[CombatActor] = Arbitrary{
    for {
      id <- Gen.uuid
      stats <- Arbitrary.arbitrary[Statistics](arbitraryStatistics)
      health <- Gen.choose(1, 20)
      name <- Gen.alphaNumStr
    } yield {
      CombatActor(CombatActor.Id(id.toString), CombatActor.Name(name), stats, CombatActor.Health(health))
    }
  }
}



object DatabaseActorGenerator {
  implicit val arbitraryDatabaseActor : Arbitrary[DatabaseActor] = Arbitrary {
    for{
      actorId <- Gen.uuid
      actorName <- Gen.alphaStr
      hp <- Gen.choose(1, 20)
      ws <- Gen.choose(1, 100)
      bs <- Gen.choose(1, 100)
      str <- Gen.choose(1, 100)
      tg <- Gen.choose(1, 100)
      ag <- Gen.choose(1, 100)
      int <- Gen.choose(1, 100)
      per <- Gen.choose(1, 100)
      wp <- Gen.choose(1, 100)
      fel <- Gen.choose(1, 100)
      infl <- Gen.choose(1, 100)
    } yield DatabaseActor(
      actorId = actorId.toString,
      actorName = actorName,
      hp = hp,
      weaponsSkill = ws,
      ballisticSkill = bs,
      strength = str,
      toughness = tg,
      agility = ag,
      intelligence = int,
      perception = per,
      willPower = wp,
      fellowship = fel,
      influence = infl
    )
  }
}
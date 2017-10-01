package net.cucumbersome.test

import net.cucumbersome.rpgRoller.warhammer.combat.InCombatActor
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, Statistics}
import org.scalacheck.{Arbitrary, Gen}

object StatisticsGenerator {
  implicit val arbitraryPlayer: Arbitrary[Statistics] = Arbitrary {
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
  import StatisticsGenerator.arbitraryPlayer

  implicit val arbitraryCombatActor: Arbitrary[CombatActor] = Arbitrary{
    for {
      id <- Gen.uuid
      stats <- Arbitrary.arbitrary[Statistics]
      health <- Gen.choose(1, 20)
      name <- Gen.alphaNumStr
    } yield {
      CombatActor(CombatActor.Id(id.toString), CombatActor.Name(name), stats, CombatActor.Health(health))
    }
  }
}

object InCombatActorGenerator {

  import CombatActorGenerator._
  import cats.syntax.option._

  implicit val arbitraryInCombatActor: Arbitrary[InCombatActor] = Arbitrary {
    for {
      id <- Gen.uuid
      name <- Gen.alphaStr
      initiative <- Gen.choose(1, 20)
      actor <- Arbitrary.arbitrary[CombatActor]
    } yield InCombatActor(
      InCombatActor.Id(id.toString),
      InCombatActor.Name(name),
      actor.hp,
      InCombatActor.Initiative(initiative).some,
      actor
    )

  }
}
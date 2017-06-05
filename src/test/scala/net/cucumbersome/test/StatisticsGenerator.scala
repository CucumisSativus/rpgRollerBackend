package net.cucumbersome.test

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
      new Statistics.BalisticSkill(bs),
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
      CombatActor(new CombatActor.Id(id.toString), new CombatActor.Name(name), stats, new CombatActor.Health(health))
    }
  }
}
package net.cucumbersome.rpgRoller.warhammer.combat

import net.cucumbersome.rpgRoller.warhammer.combat.domain.InCombatActor
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import net.cucumbersome.test.CombatActorGenerator
import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor
import org.scalacheck.{Arbitrary, Gen}

object InCombatActorGenerator {

  import CombatActorGenerator._
  import cats.syntax.option._

  implicit val arbitraryInCombatActor: Arbitrary[InCombatActor] = Arbitrary {
    for {
      id <- Gen.uuid
      name <- Gen.alphaStr
      initiative <- Gen.choose(1, 20)
      actor <- Arbitrary.arbitrary[CombatActor](arbitraryCombatActor)
    } yield InCombatActor(
      InCombatActor.Id(id.toString),
      InCombatActor.Name(name),
      actor.hp,
      InCombatActor.Initiative(initiative).some,
      actor
    )

  }
}
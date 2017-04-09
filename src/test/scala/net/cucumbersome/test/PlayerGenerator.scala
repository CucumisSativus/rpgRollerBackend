package net.cucumbersome.test

import net.cucumbersome.rpgRoller.warhammer.Player
import org.scalacheck.{Arbitrary, Gen}

object PlayerGenerator {
  implicit val arbitraryPlayer: Arbitrary[Player] = Arbitrary {
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
    } yield Player(
      new Player.WeaponSkill(ws),
      new Player.BalisticSkill(bs),
      new Player.Strength(s),
      new Player.Toughness(t),
      new Player.Agility(ag),
      new Player.Intelligence(int),
      new Player.Perception(per),
      new Player.WillPower(wp),
      new Player.Fellowship(fel),
      new Player.Influence(inf)
    )
  }
}

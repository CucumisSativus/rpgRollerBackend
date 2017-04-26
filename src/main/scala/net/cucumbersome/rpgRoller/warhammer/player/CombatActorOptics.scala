package net.cucumbersome.rpgRoller.warhammer.player
import monocle.Lens
import monocle.macros.GenLens
import net.cucumbersome.rpgRoller.warhammer.player.Statistics.Agility

object CombatActorOptics {
  val statistics : Lens[CombatActor, Statistics] = GenLens[CombatActor](_.statistics)
  val agility : Lens[Statistics, Agility] = GenLens[Statistics](_.agility)

  val modifyAgility = statistics composeLens agility
}

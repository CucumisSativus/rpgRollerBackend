package net.cucumbersome.rpgRoller.warhammer.combat.domain

import java.util.UUID

import monocle.Lens
import monocle.macros.GenLens
import net.cucumbersome.rpgRoller.warhammer.combat.domain.InCombatActor.{Id, Initiative, Name}
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor.Health
import net.cucumbersome.rpgRoller.warhammer.player.{CombatActor, CombatActorOptics, Statistics}

private[combat] case class InCombatActor(id: Id, name: Name, currentHealth: Health, initiative: Option[Initiative], actor: CombatActor)

private[combat] object InCombatActor {

  type IdGenerator = () => Id
  lazy val defaultIdGenerator: IdGenerator = () => Id(UUID.randomUUID().toString)
  val actor: Lens[InCombatActor, CombatActor] = GenLens[InCombatActor](_.actor)
  val modifyAgility: Lens[InCombatActor, Statistics.Agility] = actor composeLens CombatActorOptics.modifyAgility

  def buildFromCombatActor(combatActor: CombatActor, name: Option[Name] = None,
                           idGenerator: IdGenerator = defaultIdGenerator): InCombatActor = {
    val actorName: Name = name.getOrElse(Name(s"${combatActor.name.data}/${idGenerator().data}"))
    InCombatActor(
      id = idGenerator(),
      name = actorName,
      currentHealth = combatActor.hp,
      initiative = None,
      actor = combatActor
    )
  }

  case class Id(data: String) extends AnyVal

  case class Initiative(data: Int) extends AnyVal

  case class Name(data: String) extends AnyVal

}
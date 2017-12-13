package net.cucumbersome.rpgRoller.warhammer

import java.util.UUID

import io.swagger.client.model.{CombatActorPresenter, Statistics}

import scala.util.Random

object ActorGenerator {
  def generate: CombatActorPresenter = {
    CombatActorPresenter(
      id = UuidGenerator.generate,
      name = NameGenerator.generate,
      hp = StatisticGenerator.generate,
      statistics = Statistics(
        weaponSkill = StatisticGenerator.generate,
        ballisticSkill = StatisticGenerator.generate,
        strength = StatisticGenerator.generate,
        toughness = StatisticGenerator.generate,
        agility = StatisticGenerator.generate,
        intelligence = StatisticGenerator.generate,
        perception = StatisticGenerator.generate,
        willPower = StatisticGenerator.generate,
        fellowship = StatisticGenerator.generate,
        influence = StatisticGenerator.generate
      )
    )
  }
}

object UuidGenerator{
  def generate: String = UUID.randomUUID().toString
}

object NameGenerator{
  def generate: String = s"Actor-${UuidGenerator.generate}"
}

object StatisticGenerator{
  def generate: Int = Random.nextInt(100)
}
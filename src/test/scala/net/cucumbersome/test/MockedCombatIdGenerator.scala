package net.cucumbersome.test

import net.cucumbersome.rpgRoller.warhammer.combat.CombatController.CombatIdGenerator

case class MockedCombatIdGenerator(expectedId: String) extends CombatIdGenerator {
  override def generateId: String = expectedId
}

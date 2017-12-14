package net.cucumbersome.rpgRoller.warhammer

import io.swagger.client.api.{ActorsApi, CombatApi}
import org.scalatest.{FeatureSpec, GivenWhenThen, MustMatchers}

class FunctionalTest extends FeatureSpec with GivenWhenThen with MustMatchers{
  private val apiEndpoint = "http://localhost:8080"
  val actorsApi = new ActorsApi(apiEndpoint)
  val combatApi = new CombatApi(apiEndpoint)
}

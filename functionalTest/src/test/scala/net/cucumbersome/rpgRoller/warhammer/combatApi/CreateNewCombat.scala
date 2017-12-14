package net.cucumbersome.rpgRoller.warhammer.combatApi

import io.swagger.client.model.CreateCombatParameters
import net.cucumbersome.rpgRoller.warhammer.{ActorSteps, FunctionalTest}

class CreateNewCombat extends FunctionalTest with ActorSteps{
  feature("Creating new combat"){
    scenario("creating new combat without actors"){
      When("Api call to create new combat is made")
      val possibleCombat = combatApi.addCombat(CreateCombatParameters(List()))

      Then("New combat must be returned")
      possibleCombat mustBe defined
      val newCombat = possibleCombat.get

      newCombat.actors mustBe empty
      newCombat.id must not be null
    }

    scenario("creating new combat with actor"){
      val actor = actorInTheDatabase

      When("Api call to create new combat is made")
      val possibleCombat = combatApi.addCombat(CreateCombatParameters(List(actor.id)))

      Then("New combat is returned")
      possibleCombat mustBe defined
      val newCombat = possibleCombat.get

      newCombat.actors must not be empty
      newCombat.id must not be null

      And("Freshly added actor is the one in the database")
      val actorInCombat = newCombat.actors.head
      actorInCombat.actor mustBe actor

      And("Freshly added actor is added with full health")
      actorInCombat.currentHealth mustBe actor.hp

      And("Freshly added actor is without initiative")
      actorInCombat.initiaive mustBe None

    }
  }
}

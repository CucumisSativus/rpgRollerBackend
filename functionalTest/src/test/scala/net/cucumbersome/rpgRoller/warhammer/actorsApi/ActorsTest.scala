package net.cucumbersome.rpgRoller.warhammer.actorsApi

import io.swagger.client.api.ActorsApi
import net.cucumbersome.rpgRoller.warhammer.{ActorGenerator, FunctionalTest}

class ActorsTest extends FunctionalTest{
  feature("Actors api"){
    val actorsApi = new ActorsApi("http://localhost:8080")
    scenario("Adding new actor"){
      Given("Actor represented in json")
      val actor = ActorGenerator.generate

      When("Api call to add new actor is called")
      val createdActor = actorsApi.addActor(actor)

      Then("System should return proper actor")
      createdActor must not be None
      createdActor.get mustBe actor

      And("Actor can be accessed with api call")
      val actorFromApi = actorsApi.showActor(createdActor.get.id)
      actorFromApi must not be None
      actorFromApi.get mustBe actor
    }
  }

}

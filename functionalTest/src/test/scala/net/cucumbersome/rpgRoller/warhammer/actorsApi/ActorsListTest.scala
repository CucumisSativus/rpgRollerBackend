package net.cucumbersome.rpgRoller.warhammer.actorsApi

import io.swagger.client.api.ActorsApi
import net.cucumbersome.rpgRoller.warhammer.{ActorGenerator, ActorSteps, FunctionalTest}

class ActorsListTest extends FunctionalTest with ActorSteps{
  feature("actors list"){
    scenario("actors list returns added actor"){
      val actor = actorInTheDatabase

      When("Api call for actor list is performed")
      val actorsList = actorsApi.getActorList()

      Then("Freshly added actor is returned in list")
      actorsList.get must contain(actor)
    }
  }
}

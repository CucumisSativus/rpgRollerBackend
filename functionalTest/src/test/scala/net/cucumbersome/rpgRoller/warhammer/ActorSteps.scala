package net.cucumbersome.rpgRoller.warhammer

import io.swagger.client.model.CombatActorPresenter

trait ActorSteps { this: FunctionalTest =>
  def actorInTheDatabase: CombatActorPresenter = {
    Given("Actor in a database")
    val actor = ActorGenerator.generate
    val res = actorsApi.addActor(actor)
    res must not be None
    actor
  }
}

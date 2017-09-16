package net.cucumbersome

import akka.actor.ActorSystem
import akka.testkit.TestKit
import net.cucumbersome.test.DefaultTimeouts
import org.scalatest.{MustMatchers, WordSpecLike}

class ActorSpec extends TestKit(ActorSystem("CombatHandler"))
  with WordSpecLike with MustMatchers with DefaultTimeouts {


}

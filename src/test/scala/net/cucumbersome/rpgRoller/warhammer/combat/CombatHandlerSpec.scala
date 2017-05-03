package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.{GetCombat, GetCombatResponse}
import net.cucumbersome.test.DefaultTimeouts
import org.scalatest.{MustMatchers, WordSpecLike}
import akka.pattern.ask

class CombatHandlerSpec extends TestKit(ActorSystem("CombatHandler"))
  with WordSpecLike with MustMatchers with DefaultTimeouts{

  "A combat handler" when {
    "Asking for state" should {
      "return the state" in {
        val sender = TestProbe()
        val worker = buildWorker

        sender.send(worker, GetCombat())
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(combat) => combat
        }

        combat mustBe Combat.empty
      }
    }
  }

  def buildWorker: ActorRef = {
    val id = UUID.randomUUID().toString
    system.actorOf(CombatHandler.props(id), id)
  }
}

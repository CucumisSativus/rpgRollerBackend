package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler.{GetCombat, GetCombatResponse, InitCombat}
import net.cucumbersome.test.DefaultTimeouts
import org.scalatest.{MustMatchers, WordSpecLike}
import akka.pattern.ask
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatHandlerSpec extends TestKit(ActorSystem("CombatHandler"))
  with WordSpecLike with MustMatchers with DefaultTimeouts with RandomDataGenerator{
  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  "A combat handler" when {
    "Asking for state" should {
      "return the state" in {
        val sender = TestProbe()
        val worker = buildWorker

        sender.send(worker, GetCombat())
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(cmb) => cmb
        }

        combat mustBe Combat.empty
      }
    }

    "Initializing the combat" should {
      "initialize with empty combat" in {
        val sender = TestProbe()
        val worker = buildWorker

        sender.send(worker, InitCombat(List()))
        sender.send(worker, GetCombat())
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(cmb) => cmb
        }

        combat mustBe Combat.empty
      }

      "initialize combat with actors" in {
        val sender = TestProbe()
        val worker = buildWorker

        val actor = random[CombatActor]
        val expectedCombat = Combat(List(actor))

        sender.send(worker, InitCombat(List(actor)))
        sender.send(worker, GetCombat())
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(cmb) => cmb
        }

        combat mustBe expectedCombat
      }
    }
  }

  def buildWorker: ActorRef = {
    val id = UUID.randomUUID().toString
    system.actorOf(CombatHandler.props(id), id)
  }
}

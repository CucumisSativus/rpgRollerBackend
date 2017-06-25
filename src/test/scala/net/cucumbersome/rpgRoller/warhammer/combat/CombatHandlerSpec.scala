package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler._
import net.cucumbersome.test.DefaultTimeouts
import org.scalatest.{MustMatchers, WordSpecLike}
import akka.pattern.ask
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor

class CombatHandlerSpec extends TestKit(ActorSystem("CombatHandler"))
  with WordSpecLike with MustMatchers with DefaultTimeouts with RandomDataGenerator{
  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  "A combat handler" when {
    "asking for state" should {
      "return the state" in {
        val sender = TestProbe()
        val worker = buildWorker
        val id = generateId

        sender.send(worker, GetCombat(id))
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(_, cmb) => cmb
        }

        combat mustBe Combat.empty
      }
    }

    "initializing the combat" should {
      "initialize with empty combat" in {
        val sender = TestProbe()
        val worker = buildWorker
        val id = generateId

        sender.send(worker, InitCombat(id, List()))
        sender.send(worker, GetCombat(id))
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(_, cmb) => cmb
        }

        combat mustBe Combat.empty
      }

      "initialize combat with actors" in {
        val sender = TestProbe()
        val worker = buildWorker
        val id = generateId

        val actor = random[CombatActor]
        val expectedCombat = Combat(List(actor))

        sender.send(worker, InitCombat(id, List(actor)))
        sender.send(worker, GetCombat(id))
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(_, cmb) => cmb
        }

        combat mustBe expectedCombat
      }
    }

    "addning new combat actors" should {
      "add them properly" in {
        val sender = TestProbe()
        val worker = buildWorker
        val id = generateId

        val (initialActor, newActor) = random[CombatActor](2) match {
          case f :: s :: Nil => (f, s)
        }

        sender.send(worker, InitCombat(id, List(initialActor)))
        sender.send(worker, AddActors(id, List(newActor)))
        sender.send(worker, GetCombat(id))
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(_, cmb) => cmb
        }

        val expectedCombat = Combat(List(initialActor, newActor))

        combat mustBe expectedCombat
      }
    }

    "removing actors" should {
      "remove actors " in {
        val sender = TestProbe()
        val worker = buildWorker
        val id = generateId

        val (firstActor, secondActor) = random[CombatActor](2) match {
          case f :: s :: Nil => (f, s)
        }

        sender.send(worker, InitCombat(id, List(firstActor, secondActor)))
        sender.send(worker, RemoveActors(id, List(secondActor)))
        sender.send(worker, GetCombat(id))
        val combat = sender.expectMsgPF(defaultTimeout){
          case GetCombatResponse(_, cmb) => cmb
        }

        val expectedCombat = Combat(List(firstActor))

        combat mustBe expectedCombat
      }
    }
  }

  def generateId: String = {
    UUID.randomUUID().toString
  }

  def buildWorker: ActorRef = {
    val id = UUID.randomUUID().toString
    system.actorOf(CombatHandler.props(id), id)
  }
}

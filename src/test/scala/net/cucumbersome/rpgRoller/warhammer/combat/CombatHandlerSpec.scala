package net.cucumbersome.rpgRoller.warhammer.combat

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestKit, TestProbe}
import akka.util.Timeout
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.rpgRoller.warhammer.combat.CombatHandler._
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor
import net.cucumbersome.test.DefaultTimeouts
import org.scalatest.{MustMatchers, WordSpecLike}

class CombatHandlerSpec extends TestKit(ActorSystem("CombatHandler"))
  with WordSpecLike with MustMatchers with DefaultTimeouts with RandomDataGenerator{
  import net.cucumbersome.test.CombatActorGenerator.arbitraryCombatActor

  implicit val timeout: Timeout = Timeout(2, TimeUnit.SECONDS)
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

        val expectedCombat = Combat(List(initialActor, newActor))
        sender.send(worker, InitCombat(id, List(initialActor)))
        sender.send(worker, AddActors(id, List(newActor)))
        sender.fishForMessage() {
          case GetCombatResponse(oid, oCombat) if oid == id && oCombat == expectedCombat => true
          case _ => false
        }
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

        val expectedCombat = Combat(List(firstActor))
        sender.send(worker, InitCombat(id, List(firstActor, secondActor)))
        sender.send(worker, RemoveActors(id, List(secondActor)))

        sender.fishForMessage() {
          case GetCombatResponse(oid, oCombat) if oid == id && oCombat == expectedCombat => true
          case _ => false
        }
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

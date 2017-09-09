package net.cucumbersome

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import net.cucumbersome.rpgRoller.warhammer.player.JsonFormats

class RouteSpec extends UnitSpec with ScalatestRouteTest with RandomDataGenerator with JsonFormats

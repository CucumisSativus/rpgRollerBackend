package net.cucumbersome

import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
class UnitSpec extends WordSpec with MustMatchers{
  def futureValue[A](f: Future[A]): A = Await.result(f, 5 seconds)
}

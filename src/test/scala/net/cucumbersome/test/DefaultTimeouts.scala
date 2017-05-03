package net.cucumbersome.test
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
trait DefaultTimeouts {
  val defaultTimeout: FiniteDuration = 5 seconds
  def futureResult[A](f: Future[A]): A = Await.result(f, defaultTimeout)
}

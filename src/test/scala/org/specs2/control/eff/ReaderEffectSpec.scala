package org.specs2.control.eff

import org.specs2.Specification
import Eff._, Effects._, ReaderEffect._
import cats.data._
import cats.syntax.all._

class ReaderEffectSpec extends Specification { def is = s2"""

 local can be used to "zoom" on a configuration $localEffect

"""

  def localEffect = {
    type R[A] = Reader[Config, A]
    type S = R |: NoEffect

    implicit def rm: Member[R, S] =
      Member.infer

    val action: Eff[S, (Int, String)] = for {
      f <- local[S, Config, Int]((_:Config).factor)
      h <- local[S, Config, String]((_:Config).host)
    } yield (f, h)

    run(runReader(Config(10, "www.me.com"))(action)) ==== ((10, "www.me.com"))
  }

  case class Config(factor: Int, host: String)

}

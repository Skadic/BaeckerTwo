package moe.skadic.baeckertwo

import cats.effect.IOApp

object Main extends IOApp.Simple {
  val run = BaeckerTwoServer.run
}

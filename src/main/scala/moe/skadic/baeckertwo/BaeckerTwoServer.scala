package moe.skadic.baeckertwo

import cats.effect.IO
import cats.syntax.all._
import com.comcast.ip4s._
import moe.skadic.baeckertwo.data.Recipe
import moe.skadic.baeckertwo.endpoints.{RecipeEndpoint, RecipeListEndpoint}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

import scala.collection.mutable.ArrayBuffer

object BaeckerTwoServer {

  val recipes: ArrayBuffer[Recipe] = ArrayBuffer[Recipe]();

  def run: IO[Nothing] = {
    for {
      _ <- EmberClientBuilder.default[IO].build
      recipeEndpoint = RecipeEndpoint.impl
      recipeListEndpoint = RecipeListEndpoint.impl

      httpApp = (
          BaeckerTwoRoutes.recipeRoutes(recipeEndpoint) <+>
          BaeckerTwoRoutes.recipeListRoutes(recipeListEndpoint)
      ).orNotFound

      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- 
        EmberServerBuilder.default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}

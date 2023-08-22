package moe.skadic.baeckertwo

import cats.effect._
import io.circe.generic.auto._
import moe.skadic.baeckertwo.data.Recipe
import moe.skadic.baeckertwo.endpoints.{RecipeEndpoint, RecipeListEndpoint}
import org.http4s.HttpRoutes
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.twirl._;

object BaeckerTwoRoutes {

  def recipeRoutes(recipes: RecipeEndpoint): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    implicit val enc = jsonEncoderOf[IO, List[Recipe]];
    HttpRoutes.of[IO] {
      case req@POST -> Root / "recipe" =>
        for {
          v <- req.as[Recipe]
          _ <- recipes.addRecipe(v)
          resp <- Ok(v)
        } yield resp
      case GET -> Root / "recipe" =>
        for {
          recipes <- recipes.getRecipes()
          recipeJsonList = recipes.toList
          res <- Ok(recipeJsonList)
        } yield res
      case GET -> Root / "recipe" / IntVar(i) =>
        for {
          html <- recipes.viewRecipe(i)
          res <- Ok(html)
        } yield res
      case GET -> Root / "recipe" / "edit" =>
        for {
          html <- recipes.editRecipe(None, None)
          res <- Ok(html)
        } yield res
      case GET -> Root / "recipe" / "edit" / IntVar(i) =>
        val recipe = BaeckerTwoServer.recipes.lift(i)
        for {
          html <- recipes.editRecipe(recipe, recipe.map(_ => i))
          res <- Ok(html)
        } yield res
    }
  }

  def recipeListRoutes(viewer: RecipeListEndpoint): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._;
    HttpRoutes.of[IO] {
      case GET -> Root / "recipe" / "list" =>
        for {
          template <- viewer.listRecipes(BaeckerTwoServer.recipes)
          res <- Ok(template)
        } yield res
    }
  }
}
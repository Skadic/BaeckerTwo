package moe.skadic.baeckertwo.endpoints

import cats.effect.IO
import cats.implicits._
import moe.skadic.baeckertwo.BaeckerTwoServer
import moe.skadic.baeckertwo.data.Recipe
import play.twirl.api.Html

trait RecipeEndpoint {
  def addRecipe(recipe: Recipe): IO[Unit];

  def getRecipes(): IO[Iterator[Recipe]]

  def viewRecipe(id: Int): IO[Html]

  def editRecipe(recipe: Option[Recipe], id: Option[Int]): IO[Html]
}

object RecipeEndpoint {
  def impl: RecipeEndpoint = new RecipeEndpoint {
    override def addRecipe(recipe: Recipe): IO[Unit] = {
      if (recipe.id.isDefined) {
        BaeckerTwoServer.recipes(recipe.id.get) = recipe
      } else {
        BaeckerTwoServer.recipes.addOne(recipe);
      }
      println(recipe.ingredients.toString() + "\n" + recipe.steps.toString()).pure[IO]
    }

    override def getRecipes(): IO[Iterator[Recipe]] =
      BaeckerTwoServer.recipes.iterator.pure[IO]

    override def viewRecipe(id: Int): IO[Html] = {
      html.RecipeViewer(BaeckerTwoServer.recipes(id)).pure[IO]
    }

    override def editRecipe(recipe: Option[Recipe], id: Option[Int]): IO[Html] = {
      html.RecipeEditor(recipe, id).pure[IO]
    }
  }
}

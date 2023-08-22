package moe.skadic.baeckertwo.endpoints

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import html.RecipeList
import moe.skadic.baeckertwo.data.Recipe
import play.twirl.api.Html

trait RecipeListEndpoint {
  def listRecipes(recipes: Iterable[Recipe]): IO[Html]
}

object RecipeListEndpoint {
  def impl: RecipeListEndpoint = (recipes: Iterable[Recipe]) => RecipeList(recipes).pure[IO]

}

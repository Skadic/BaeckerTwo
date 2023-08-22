package moe.skadic.baeckertwo.data

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

final case class Recipe(id: Option[Int], name: String, ingredients: List[Ingredient], steps: List[String])

final case class Ingredient(name: String, amount: Double, unit: String)

object Recipe {
  implicit def dec: EntityDecoder[IO, Recipe] =
    jsonOf[IO, Recipe]

  implicit def enc: EntityEncoder[IO, Recipe] =
    jsonEncoderOf[IO, Recipe]
}

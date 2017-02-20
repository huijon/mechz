package controllers

/**
  * Created by jono on 16/02/2017.
  */

import org.http4s.dsl.{->, Root, _}
import domain.GameStartDto
import util.Http4sJsonHelpers._
import org.http4s.HttpService
import org.http4s.dsl._
import services.GameService

case class GameController(gameService: GameService) {

  val service: HttpService = HttpService {
    case req @ POST -> Root / "start" => req.decode[GameStartDto] { dto =>
      println(s"dto: $dto")
      println(req.body)

      Ok("Game started")
    }
  }
}


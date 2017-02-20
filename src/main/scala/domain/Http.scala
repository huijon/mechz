package domain

import argonaut._, Argonaut._

//trait HeroClass {}
//case object Warrior extends HeroClass
//case object Hunter extends HeroClass
//case object Mage extends HeroClass

case class HeroRequest(name: String, `class`: String)

//object HeroRequest {
//  implicit def HeroRequestJson: CodecJson[HeroRequest] =
//    casecodec2(HeroRequest.apply, HeroRequest.unapply)("name", "class")
//}


object HeroRequest {
  implicit def decodeJson: DecodeJson[HeroRequest] =
    DecodeJson(c => for {
      name <- (c --\ "name").as[String]
      _class <- (c --\ "class").as[String]
    } yield HeroRequest(name, _class))

  implicit def encodeJson: EncodeJson[HeroRequest] =
    jencode2L((heroReq: HeroRequest) => (heroReq.name, heroReq.`class`))("name", "class")

  implicit val codecJson: CodecJson[HeroRequest] =
    CodecJson(
      encodeJson.encode,
      decodeJson.decode
    )
}

//case class EventId(id: String) {
//  override def toString = id
//}
//
//object EventId {
//  implicit val codecJson: CodecJson[EventId] = CodecJson (
//    _.toString.asJson,
//    c => {
//      c.as[String].flatMap(s => {
//        DecodeResult.ok(EventId(s))
//      })
//    })
//}


case class GameStartDto(h1: HeroRequest, h2: HeroRequest)
//case class GameStartDto(eventId: EventId)

object GameStartDto {
  implicit def decodeJson: DecodeJson[GameStartDto] =
    DecodeJson(c => for {
      h1 <- (c --\ "h1").as[HeroRequest]
      h2 <- (c --\ "h2").as[HeroRequest]
    } yield GameStartDto(h1, h2))

  implicit def encodeJson: EncodeJson[GameStartDto] =
    jencode2L((dto: GameStartDto) => (dto.h1, dto.h2))("h1", "h2")

  implicit val codecJson: CodecJson[GameStartDto] =
    CodecJson(
      encodeJson.encode,
      decodeJson.decode
    )
}

//object GameStartDto {
//
//  implicit def GameStartDtoDecodeJson: DecodeJson[GameStartDto] =
//    DecodeJson(c => for {
//      h1 <- (c --\ "h1").as[HeroRequest]
//      h2 <- (c --\ "h2").as[HeroRequest]
//    } yield {
//      GameStartDto(h1, h2)
//    })
//
//  implicit def GameStartDtoEncodeJson: EncodeJson[GameStartDto] =
//    EncodeJson((c: GameStartDto) =>
//      ("h1" := c.h1) ->:
//        ("h2" := c.h2) ->:
//        jEmptyObject)
//
//  implicit def GameStartDtoCodecJson: CodecJson[GameStartDto] =
//    CodecJson(
//      GameStartDtoEncodeJson.encode,
//      GameStartDtoCodecJson.decode
//    )
//}

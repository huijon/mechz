package util

/**
  * Created by jono on 20/02/2017.
  */

import argonaut.Argonaut._
import argonaut._
import errors.UnableToDecodeJson
import org.http4s.{DecodeResult, _}
import org.http4s.headers._
import scodec.bits.ByteVector

import scala.language.implicitConversions
import scalaz._, Scalaz._

object Http4sJsonHelpers extends ArgonautInstances {

  implicit def FailureEncodeJson: EncodeJson[NonEmptyList[String]] =
    EncodeJson((errors: NonEmptyList[String]) => Json("errors" -> jArray(errors.list.map(jString))))

  implicit def decoderFromDecodeJson[A](implicit codec: DecodeJson[A]): EntityDecoder[A] =
    fromJsonCodec(codec)

  implicit def encoderFromEncodeJson[A](implicit codec: CodecJson[A]): EntityEncoder[A] = {
    val hdr = `Content-Type`(MediaType.`application/json`).withCharset(Charset.`UTF-8`)
    EntityEncoder.simple[A](hdr)(a => ByteVector(a.asJson.nospaces.getBytes))
  }

  implicit def listEncoderFromEncodeJson[A](implicit codec: CodecJson[A]): EntityEncoder[List[A]] = {
    val hdr = `Content-Type`(MediaType.`application/json`).withCharset(Charset.`UTF-8`)
    EntityEncoder.simple[List[A]](hdr)(a => ByteVector(a.asJson.nospaces.getBytes))
  }

  def fromJsonCodec[A](implicit decoder: DecodeJson[A]): EntityDecoder[A] =
    json.flatMapR { json =>
      decoder.decodeJson(json).fold(
        (message, history) =>
          DecodeResult.failure(ParseFailure(
            ErrorResponse(UnableToDecodeJson(history)).asJson.nospaces,
            s"json: $json, error: $message, cursor: $history")
          ),
        DecodeResult.success(_)
      )
    }

  implicit def SuccessResponseEncoder[A: EncodeJson]: EntityEncoder[SuccessResponse[A]] =
    EntityEncoder.stringEncoder(Charset.`UTF-8`).contramap[SuccessResponse[A]] { successResponse: SuccessResponse[A] =>
      Argonaut.nospace.pretty(successResponse.asJson)
    }.withContentType(`Content-Type`(MediaType.`application/json`))

  implicit def ErrorResponseEncoder: EntityEncoder[ErrorResponse] =
    EntityEncoder.stringEncoder(Charset.`UTF-8`).contramap[ErrorResponse] { errorResponse =>
      Argonaut.nospace.pretty(errorResponse.asJson)
    }.withContentType(`Content-Type`(MediaType.`application/json`))

}
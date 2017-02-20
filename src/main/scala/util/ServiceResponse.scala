package util

/**
  * Created by jono on 20/02/2017.
  */

import argonaut._
import Argonaut._
import errors.ApiServiceError

sealed trait Meta
case class SuccessMeta(data: Map[String, String] = Map()) extends Meta
case class ErrorMeta(error: ApiServiceError) extends Meta

object Meta {
  implicit def SuccessMetaCodecJson: EncodeJson[SuccessMeta] =
    EncodeJson((m: SuccessMeta) => ("data" := m.data) ->: jEmptyObject)

  implicit def ErrorMetaCodecJson: EncodeJson[ErrorMeta] =
    EncodeJson((e: ErrorMeta) =>
      ("error_type" := e.error.name) ->: ("error_detail" := e.error.render) ->: jEmptyObject)

  implicit def MetaEncodeJson: EncodeJson[Meta] = EncodeJson((e: Meta) => e match {
    case e: SuccessMeta => e.asJson
    case e: ErrorMeta => e.asJson
  })
}

sealed trait ApiResponse {
  def meta: Meta
}

object ApiResponse {
  implicit def SuccessResponseEncodeJson[A : EncodeJson]: EncodeJson[SuccessResponse[A]] =
    jencode2L((p: SuccessResponse[A]) => (p.response, p.meta))("response", "meta")

  implicit def ErrorResponseEncodeJson: EncodeJson[ErrorResponse] =
    EncodeJson((e: ErrorResponse) =>
      ("meta" := e.meta.asJson) ->: ("response" := jNull) ->: jEmptyObject)
}

case class SuccessResponse[A : EncodeJson](response: A, meta: SuccessMeta = SuccessMeta()) extends ApiResponse
case class ErrorResponse(error: ApiServiceError) extends ApiResponse {
  def meta: Meta = ErrorMeta(error)
}

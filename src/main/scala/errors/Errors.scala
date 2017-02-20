package errors

/**
  * Created by jono on 20/02/2017.
  */

import argonaut.CursorHistory

sealed trait ApiServiceError {
  def render: String = this match {
    //case EventerServiceError(error) => error.reason
    case UnableToDecodeJson(cursorHistory) => s"Can't decode JSON: ${cursorHistory.toString}"
    case EventDoesNotExist(message) => message
    case InvalidRequest(message) => message
    case ApplicationError(message) => message
  }

  def name: String = this match {
    case error => error.getClass.getSimpleName
  }
}

//case class EventerServiceError(error: EventerError) extends ApiServiceError
case class UnableToDecodeJson(cursorHistory: CursorHistory) extends ApiServiceError
case class EventDoesNotExist(message: String) extends ApiServiceError
case class InvalidRequest(message: String) extends ApiServiceError
case class ApplicationError(message: String) extends ApiServiceError


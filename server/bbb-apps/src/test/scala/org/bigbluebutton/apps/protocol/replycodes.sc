package org.bigbluebutton.apps.protocol

object replycodes {

  object ResponseStatus extends Enumeration {
    type StatusType = Value
    val FAILED = Value("FAILED")
    val OK = Value("OK")
  }
  
  case class ResponseCode(message: String, code: Int)
  case class Response(status: ResponseStatus.StatusType, codes: Seq[ResponseCode])
  
  val r = ResponseCode("Hello", 20)               //> r  : org.bigbluebutton.apps.protocol.replycodes.ResponseCode = ResponseCode(
                                                  //| Hello,20)
}
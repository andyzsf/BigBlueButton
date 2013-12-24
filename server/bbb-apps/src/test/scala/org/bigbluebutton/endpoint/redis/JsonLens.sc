package org.bigbluebutton.endpoint.redis

import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.json.lenses.JsonLenses._
import spray.json.lenses._

object JsonLens {
 val json = """
{ "store": {
    "book": [
      { "category": "reference",
        "author": "Nigel Rees",
        "title": "Sayings of the Century",
        "price": 8.95
      },
      { "category": "fiction",
        "author": "Evelyn Waugh",
        "title": "Sword of Honour",
        "price": 12.99,
        "isbn": "0-553-21311-3"
      }
    ],
    "bicycle": {
      "color": "red",
      "price": 19.95
    }
  }
}""".asJson                                       //> json  : spray.json.JsValue = {"store":{"book":[{"category":"reference","auth
                                                  //| or":"Nigel Rees","title":"Sayings of the Century","price":8.95},{"category":
                                                  //| "fiction","author":"Evelyn Waugh","title":"Sword of Honour","price":12.99,"i
                                                  //| sbn":"0-553-21311-3"}],"bicycle":{"color":"red","price":19.95}}}


val allAuthors = 'store / 'book / * / 'author     //> allAuthors  : spray.json.lenses.Lens[Seq] = spray.json.lenses.JsonLenses$$an
                                                  //| on$1@6c22811e
val authorNames = json.extract[String](allAuthors)//> authorNames  : Seq[String] = List(Nigel Rees, Evelyn Waugh)
}
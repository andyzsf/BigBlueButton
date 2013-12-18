package org.bigbluebutton.apps.protocol

import spray.json._
import spray.json.JsonParser
import spray.json.DefaultJsonProtocol

object whiteboardmsgws {
  val wbmsg = """
{

        "id": "user1-shape-1",
        "correlationId": "q779ogycfmxk-13-1383262166102",
        "type": "text",
        "data": {
            "coordinate": {
                "firstX": 0.016025641025641028,
                "firstY": 0.982905982905983,
                "lastX": 1.33,
                "lastY": 2.45
            },
            "font": {
                "color": 0,
                "size": 18
            },
            "background": true,
            "backgroundColor": 16777215,
            "text": "He"
        },
        "by": {
            "id": "user1",
            "name": "Guga"
        }

}
  """                                             //> wbmsg  : String = "
                                                  //| {
                                                  //| 
                                                  //|         "id": "user1-shape-1",
                                                  //|         "correlationId": "q779ogycfmxk-13-1383262166102",
                                                  //|         "type": "text",
                                                  //|         "data": {
                                                  //|             "coordinate": {
                                                  //|                 "firstX": 0.016025641025641028,
                                                  //|                 "firstY": 0.982905982905983,
                                                  //|                 "lastX": 1.33,
                                                  //|                 "lastY": 2.45
                                                  //|             },
                                                  //|             "font": {
                                                  //|                 "color": 0,
                                                  //|                 "size": 18
                                                  //|             },
                                                  //|             "background": true,
                                                  //|             "backgroundColor": 16777215,
                                                  //|             "text": "He"
                                                  //|         },
                                                  //|         "by": {
                                                  //|             "id": "user1",
                                                  //|             "name": "Guga"
                                                  //|         }
                                                  //| 
                                                  //| }
                                                  //|   "
  
  case class Shape(id: String, correlationId: String, data: Map[String, String], by: Map[String, String])

	object ShapeJsonProtocol extends DefaultJsonProtocol {
	  implicit val shapeFormat = jsonFormat4(Shape)
	}
	
	import ShapeJsonProtocol._
	
	val jsonAst = JsonParser(wbmsg)           //> jsonAst  : spray.json.JsValue = {"id":"user1-shape-1","correlationId":"q779
                                                  //| ogycfmxk-13-1383262166102","type":"text","data":{"coordinate":{"firstX":0.0
                                                  //| 16025641025641028,"firstY":0.982905982905983,"lastX":1.33,"lastY":2.45},"fo
                                                  //| nt":{"color":0,"size":18},"background":true,"backgroundColor":16777215,"tex
                                                  //| t":"He"},"by":{"id":"user1","name":"Guga"}}
  val color = jsonAst.convertTo[Shape]            //> spray.json.DeserializationException: Expected String as JsString, but got {
                                                  //| "firstX":0.016025641025641028,"firstY":0.982905982905983,"lastX":1.33,"last
                                                  //| Y":2.45}
                                                  //| 	at spray.json.package$.deserializationError(package.scala:23)
                                                  //| 	at spray.json.BasicFormats$StringJsonFormat$.read(BasicFormats.scala:117
                                                  //| )
                                                  //| 	at spray.json.BasicFormats$StringJsonFormat$.read(BasicFormats.scala:113
                                                  //| )
                                                  //| 	at spray.json.JsValue.convertTo(JsValue.scala:32)
                                                  //| 	at spray.json.CollectionFormats$$anon$3$$anonfun$read$3.apply(Collection
                                                  //| Formats.scala:59)
                                                  //| 	at spray.json.CollectionFormats$$anon$3$$anonfun$read$3.apply(Collection
                                                  //| Formats.scala:58)
                                                  //| 	at scala.collection.TraversableLike$$anonfun$map$1.apply(TraversableLike
                                                  //| .scala:244)
                                                  //| 	at scala.collection.TraversableLike$$anonfun$map$1.apply(TraversableLike
                                                  //| .scala:244)
                                                  //| 	at scala.collection.Iterator$class.foreach(Iterator.scala:727)
                                                  //| 	at scala.collection.AbstractIterator.foreach(It
                                                  //| Output exceeds cutoff limit.
}
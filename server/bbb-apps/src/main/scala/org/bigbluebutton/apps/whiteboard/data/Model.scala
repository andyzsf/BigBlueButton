package org.bigbluebutton.apps.whiteboard.data

import org.bigbluebutton.apps.users.data.UserIdAndName

case class Foo1(bar: String) 

case class ShapeDescriptor(id: String, correlationId: String, 
                     shapeType: ShapeTypes.ShapeType, by: UserIdAndName,
                     order: Int = 0)
                     
 
object ShapeTypes extends Enumeration {
	type ShapeType     = Value
	val LINE           = Value("LINE")
	val SCRIBBLE       = Value("SCRIBBLE")
	val TEXT           = Value("TEXT")
	val RECTANGLE      = Value("RECTANGLE")
	val ELLIPSE        = Value("ELLIPSE")
	val TRIANGLE       = Value("TRIANGLE")	
}

object LineTypes extends Enumeration {
	type LineType      = Value
	val SOLID          = Value("SOLID")
	val DASH           = Value("DASHED")
	val DOT            = Value("DOTTED")
	val DASHDOT        = Value("DASHDOT")
}

case class Shape(descriptor: ShapeDescriptor, child: WbShape)

sealed trait WbShape
class PerimeteredShape(coordinate: Coordinate, background: Background,
                     border: LineDescriptor)
case class Point(x: Double, y: Double)
                  
case class Scribble(line: LineDescriptor,
                    points: Seq[Point]) 
                     extends WbShape

case class Background(visible: Boolean, color: Int)

case class LineDescriptor(weight: Int, color: Int, lineType: LineTypes.LineType)

case class Coordinate(firstX: Double, firstY: Double, lastX: Double, lastY: Double)
case class Font(style: String, color: Int, size: Int)
case class Text(coordinate: Coordinate, 
                 background: Background, 
                border: LineDescriptor, font: Font, text: String) extends WbShape
                
case class Rectangle(coordinate: Coordinate, 
                     background: Background, border: LineDescriptor,
                     square: Boolean) extends WbShape
                     
case class Ellipse(
                     circle: Boolean) extends WbShape
                     
case class Triangle(coordinate: Coordinate, background: Background,
                     border: LineDescriptor) extends WbShape
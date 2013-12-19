package org.bigbluebutton.apps.whiteboard.data

import org.bigbluebutton.apps.users.data.UserIdAndName

case class Foo1(bar: String) 

case class ShapeDescriptor(whiteboardId: String, shapeId: String, 
                           shapeType: ShapeTypes.ShapeType, 
                           by: UserIdAndName)
                     
 
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

case class Shape(descriptor: ShapeDescriptor, 
                 shape: WhiteboardShape, order: Int)

sealed trait WhiteboardShape

class Container(coordinate: Coordinate, background: Background, 
                border: LineDescriptor)

case class Point(x: Double, y: Double)
                  
case class Scribble(line: LineDescriptor, points: Seq[Point]) 
                          extends WhiteboardShape

case class Background(visible: Boolean, color: Int, alpha: Int)

case class LineDescriptor(weight: Int, color: Int, lineType: LineTypes.LineType)

case class Coordinate(firstX: Double, firstY: Double, lastX: Double, lastY: Double)
case class Font(style: String, color: Int, size: Int)
case class Text(container: Container, font: Font, text: String) extends WhiteboardShape
                
case class Rectangle(container: Container, square: Boolean) extends WhiteboardShape                     
case class Ellipse(container: Container, circle: Boolean) extends WhiteboardShape                  
case class Triangle(container: Container) extends WhiteboardShape
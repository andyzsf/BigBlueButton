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

sealed abstract class Shape(descriptor: ShapeDescriptor)

case class Point(x: Double, y: Double)
                  
case class Scribble(descriptor: ShapeDescriptor, line: LineDescriptor,
                    points: Seq[Point]) 
                     extends Shape(descriptor)

case class Background(visible: Boolean, color: Int)

case class LineDescriptor(weight: Int, color: Int, lineType: LineTypes.LineType)

case class Coordinate(firstX: Double, firstY: Double, lastX: Double, lastY: Double)
case class Font(style: String, color: Int, size: Int)
case class Text(descriptor: ShapeDescriptor, coordinate: Coordinate, 
                font: Font, background: Background, 
                border: LineDescriptor, text: String) extends Shape(descriptor)
                
case class Rectangle(descriptor: ShapeDescriptor, coordinate: Coordinate, 
                     background: Background, border: LineDescriptor,
                     square: Boolean) extends Shape(descriptor)
                     
case class Ellipse(descriptor: ShapeDescriptor, coordinate: Coordinate, 
                     border: LineDescriptor,
                     circle: Boolean) extends Shape(descriptor)
                     
case class Triangle(descriptor: ShapeDescriptor, coordinate: Coordinate, 
                     border: LineDescriptor) extends Shape(descriptor)
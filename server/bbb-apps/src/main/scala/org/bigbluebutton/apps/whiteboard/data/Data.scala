package org.bigbluebutton.apps.whiteboard.data

import org.bigbluebutton.apps.users.data.UserIdAndName




 



/*                     
class Whiteboard(id: String) {
  private var shapes = Seq[Shape]()
  private var order = 0
  
  def addShape(shape: Shape) = {
    order += 1
//    shape.descriptor.order = order
    val newShapes = shapes :+ shape
  }
  
  def getShape(id: String):Option[Shape] = {
    shapes find {s => s.descriptor.id == id}
  }
  
  def removeShape(id: String) = {
    val shape = getShape(id)
    val newShapes = shapes.filterNot {s => s.descriptor.id == id }
  }
}
*/
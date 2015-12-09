package tgle.events

import scala.collection.mutable.ListBuffer

class Event[TArgs] {
  
  private val listeners: ListBuffer[Listener[TArgs]] = new ListBuffer()
  
  def add(listener: Listener[TArgs]){
    if (!listeners.exists(_ == listener)){ 
      listeners += listener
    }
  }
  
  def remove(listener: Listener[TArgs]){
    listeners -= listener    
  }
  
  def invoke(sender: Any, e: TArgs){
    listeners foreach(_.invoke(sender, e))
  }
  
  def +=(that: Listener[TArgs]): Unit = {
    add(that)
  }
  
  def -=(that: Listener[TArgs]): Unit = {
    remove(that)
  }
}
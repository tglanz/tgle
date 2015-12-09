package tgle.events

trait Listener[TArgs]
{
  def invoke(sender: Any, e: TArgs): Unit
}

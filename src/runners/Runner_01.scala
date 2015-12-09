package runners

import tgle.application.Application

import tgle.Color

object Runner_01 extends App {
  
  println("Hello from Runners.Runner_01")
  
  val app = new Application()

  var isContinue = true
  
  if (isContinue)
  {
    app.initialize(width = 300, height = 300, windowX = 100, windowY = 100)
  }
  
  try
  {
    
    while (isContinue && app.continue()){
      val time = app.getTime()
    }
  }
  finally{
    app.terminate()
  }
  
  println("dicontinued")
}
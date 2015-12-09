package tgle.programs

import org.lwjgl.opengl._
import javax.management.OperationsException

abstract class Shader(val shaderType: Int, val source: String) {
  
  private var _shaderId: Int = -1
  def shaderId = _shaderId
  
  def isCreated: Boolean = _shaderId != -1
  
  def createAndCompile(): Unit = {
    
    if (isCreated){
      throw new OperationsException("Shader already created")
    }
    
    _shaderId = GL20.glCreateShader(shaderType)
    GL20.glShaderSource(shaderId, source)
    GL20.glCompileShader(shaderId)
    
    val intBuffer: java.nio.IntBuffer = org.lwjgl.BufferUtils.createIntBuffer(1)
    GL20.glGetShaderiv(shaderId, GL20.GL_COMPILE_STATUS, intBuffer)
    if (intBuffer.get(0) == GL11.GL_FALSE)
    { 
      try
      {
        val shaderInfoLog = GL20.glGetShaderInfoLog(shaderId)
        throw new Exception("Failed compiling shader, info log: " + shaderInfoLog)
      }
      finally
      {
        GL20.glDeleteShader(shaderId)
      }
    }
  }
  
  def deleteShader(): Unit = {
    GL20.glDeleteShader(shaderId)
  }
  
}
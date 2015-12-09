package tgle.programs

import org.lwjgl.opengl._
import javax.activity.InvalidActivityException

class Program (val vertexShader: Shader, val fragmentShader: Shader,
    val geometryShader: Shader = null, val tesselationShader: Shader = null, val computeShader: Shader = null) {
  
  private var _programId: Int = -1
  def programId = _programId
  
  def isCreated: Boolean = _programId != -1
  
  def createAndCompile(): Unit = {
    
    if (vertexShader == null) throw new NullPointerException("Vertex shader cannot be null")
    if (vertexShader.isCreated == false) throw new InvalidActivityException("Vertex shader hasn't been created")
    
    if (fragmentShader == null) throw new NullPointerException("Fragment shader cannot be null")
    if (fragmentShader.isCreated == false) throw new InvalidActivityException("Fragment shader hasn't been created")
    
    _programId = GL20.glCreateProgram()
    
    GL20.glAttachShader(programId, vertexShader.shaderId)
    GL20.glAttachShader(programId, fragmentShader.shaderId)
    
    def ifShaderValid(shader: Shader, body: Shader => Unit): Unit = {
      if (shader != null && shader.isCreated){
        body(shader)
      }
    }
    
    def attachIfShaderValid(shader: Shader): Unit = {
      ifShaderValid(shader, (x: Shader) => GL20.glAttachShader(programId, x.shaderId) )
    }
    
    attachIfShaderValid(geometryShader)
    attachIfShaderValid(tesselationShader)
    attachIfShaderValid(computeShader)
    
    GL20.glLinkProgram(programId)
    
    val isLinked = GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS)
    if (isLinked == GL11.GL_FALSE)
    { 
      try
      {
        val programInfoLog = GL20.glGetProgramInfoLog(programId)
        throw new Exception("Failed linking the program, info log: " + programInfoLog)
      }
      finally
      {
        GL20.glDeleteProgram(programId)
      }
    }
  }
}
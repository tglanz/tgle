import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.system.MemoryUtil._
import tgle._
import java.nio.FloatBuffer

package tgle.application
{
  class Application() {
    
    private var backcolorFloatBuffer = BufferUtils.createFloatBuffer(4)
    
    def setBackColor(color: Color): Unit = {
      backcolorFloatBuffer.clear()
      backcolorFloatBuffer.put(Array[Float](color.r, color.g, color.b, color.b))
      backcolorFloatBuffer.flip()
    }
    
    private[Application] class ErrorCallbackEventHandler(val callback: () => Unit) extends GLFWErrorCallback {
      def invoke(i1: Int, l1: Long): Unit = {
        println(s"ErrorCallbackEventHandler: An error has ocurred: $i1, $l1")
        callback()
      }
    }
    
    private[Application] class KeyCallbackEventHandler extends GLFWKeyCallback {
      def invoke(l1: Long, i1: Int, i2: Int, i3: Int, i4: Int): Unit = { 
        
      }
    }
    
    private val errorCallbackEventHandler = new ErrorCallbackEventHandler(() => {
      hasErrors = true
    })
    
    private val keyCallbackEventHandler = new KeyCallbackEventHandler

    private var hasErrors = false
    private var window: Long = -1
    private var primaryWindow: Long = -1
    
    private var lastIterationTime = 0.0
    
    private var program: tgle.programs.Program = null
  
    def continue(): Boolean = {
      if (hasErrors || glfwWindowShouldClose(window) == GLFW_TRUE){
        return false
      }
      
      iteration()
      
      return true
    }
    
    def terminate(): Unit = {
      glfwTerminate()
    }
    
    private def iteration(): Unit = {
      
      var currentIterationTime = getTime()
      
      update(currentIterationTime - lastIterationTime)
      
      glfwSwapBuffers(window)
      glfwPollEvents()
      
      lastIterationTime = currentIterationTime
    }
    
    def getTime(): Double = {
      return glfwGetTime()
    }
    
    private def update(delta: Double): Unit = {
      GL30.glClearBufferfv(GL11.GL_COLOR, 0, backcolorFloatBuffer)
      
      GL11.glPointSize(40.0f)
      
      GL20.glUseProgram(program.programId)
      GL11.glDrawArrays(GL11.GL_POINTS, 0, 1) // mode, first, count
    }
    
    def initialize(windowX: Int = 0, windowY: Int = 0, title: String = "TGLE Window", width: Int = 0, height: Int = 0): Unit = {
      
      def rangeDefault(value: Int, min: Int, max: Int, default: Int): Int = {
        if (value < min || value > max){
          return default
        }
        return value
      }
      
      glfwSetErrorCallback(errorCallbackEventHandler)
      
      if (glfwInit() != GLFW_TRUE){
        throw new IllegalStateException("UNABLE TO INITIALIZE GLFW")
      }
      
      glfwDefaultWindowHints()
      glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE)
      glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
      
      primaryWindow = glfwGetPrimaryMonitor()
      val vidMode = glfwGetVideoMode(primaryWindow)
      
      window = glfwCreateWindow(
          rangeDefault(width, 1, vidMode.width(), vidMode.width()),
          rangeDefault(height, 1, vidMode.height(), vidMode.height()),
          title,
          0,
          0)
      
      if (window == 0){
        throw new RuntimeException("Failed init glfw window")
      }
      
      glfwSetKeyCallback(window, keyCallbackEventHandler)
      
      glfwSetWindowPos(window, windowX, windowY)
      
      glfwMakeContextCurrent(window)
      
      // vsync
      glfwSwapInterval(1)
      
      glfwShowWindow(window)
      
      GL.createCapabilities()
      
      setBackColor(new Color(0x7E5e5e))
      
      
      tmp_setup()
    }
    
    var vao: Int = 0
    def tmp_setup(): Unit = {
      tmp_initialize_program()
      val prog = program.programId
      
      vao = GL30.glGenVertexArrays()
      GL30.glBindVertexArray(vao)
    }
    
    def tmp_initialize_program(): Unit = {
      
      var vertexShader: tgle.programs.Shader = null
      var fragmentShader: tgle.programs.Shader = null
      
      try
      {
        vertexShader = new tgle.programs.VertexShader(
            io.Source.fromFile("./resources/shaders/01.vert").mkString
        )
        
        fragmentShader = new tgle.programs.FragmentShader(
            io.Source.fromFile("./resources/shaders/01.frag").mkString
        )
        
        vertexShader.createAndCompile()
        fragmentShader.createAndCompile()
        
        program = new tgle.programs.Program(vertexShader, fragmentShader)
        program.createAndCompile()
      }
      finally
      {
        if (vertexShader != null) vertexShader.deleteShader() 
        if (fragmentShader != null) fragmentShader.deleteShader() 
      }
    }
  }
}
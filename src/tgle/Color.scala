package tgle

object Color {
  
  val MIN = 0
  val I_MAX = 255.0f
  val F_MAX = 1.0f
  
  private[Color] def clamp(value: Float): Float = {
    if (value < MIN)
      return MIN
      
    if (value > F_MAX)
      return F_MAX
    
    return value
  }
  
  private[Color] def normalize(value: Int): Float = {
    return value.toFloat / I_MAX
  }
}

class Color(valueNoAlpha: Long) {
  
  private var _r: Float = .0f
  private var _g: Float = .0f
  private var _b: Float = .0f
  private var _a: Float = 1.0f
  
  ri_=(((valueNoAlpha & 0xFF0000L) >> 16).toInt)
  gi_=(((valueNoAlpha & 0x00FF00L) >> 8).toInt)
  bi_=(((valueNoAlpha & 0x0000FFL) >> 0).toInt)
  
  def this(red: Float, green: Float, blue: Float, alpha: Float) {
    this(0)
    
    r_=(red)
    g_=(green)
    b_=(blue)
    a_=(alpha)
  }
  
  def this(red: Float, green: Float, blue: Float) = this(red, green, blue, 1.0f)
  
  def this(red: Int, green: Int, blue: Int, alpha: Int){
    this(0)
    
    ri_=(red)
    gi_=(green)
    bi_=(blue)
    ai_=(alpha)
  }
  
  def this(red: Int, green: Int, blue: Int) = this(red, green, blue, 255)
  
  // Float Getters (0.0 - 1.0)
  def r = _r
  def g = _g
  def b = _b
  def a = _a
  
  // Float Setters (0.0 - 1.0)
  def r_= (value: Float): Unit = {
    _r = Color.clamp(value)
  }
  
  def g_= (value: Float): Unit = {
    _g = Color.clamp(value)
  }
  
  def b_= (value: Float): Unit = {
    _b = Color.clamp(value)
  }
  
  def a_= (value: Float): Unit = {
    _a = Color.clamp(value)
  }
  
  // Int Getters  (0 - 255)
  def ri = (Color.F_MAX * _r).toInt
  def gi = (Color.F_MAX * _g).toInt
  def bi = (Color.F_MAX * _b).toInt
  def ai = (Color.F_MAX * _a).toInt
  
  // Int Setters (0 - 255)
  def ri_= (value: Int): Unit = {
    r_=(Color.normalize(value))
  }
  
  def gi_= (value: Int): Unit = {
    g_=(Color.normalize(value))
  }
  
  def bi_= (value: Int): Unit = {
    b_=(Color.normalize(value))
  }
  
  def ai_= (value: Int): Unit = {
    a_=(Color.normalize(value))
  }
}
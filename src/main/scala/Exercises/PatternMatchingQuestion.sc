

def sqrt(n: Double): Double =
  n match {
    case x if x < 0 => Double.NaN
    case 0 => 0
    case 1 => 1
    case _ =>
      var guess = 1.0
      while ((guess * guess - n).abs >= 0.0001) {
        guess = (guess + n / guess) / 2
      }

      guess
  }

//recursion

sqrt(25)

sqrt(20)


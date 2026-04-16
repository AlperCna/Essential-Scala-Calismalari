

def sqrt(n: Double): Double =
  n match {
    case x if x < 0 => Double.NaN
    case 0          => 0
    case 1          => 1
    case _ =>
      def iterate(guess: Double): Double =
        if ((guess * guess - n).abs < 0.0001) guess
        else iterate((guess + n / guess) / 2)

      iterate(1.0)
  }

//recursion

sqrt(25)

sqrt(20)


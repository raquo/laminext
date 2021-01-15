## `.shifted`

Transforms a `Signal[Option[Signal[A]]]` into a `Signal[Option[A]]`

```scala
import com.raquo.laminar.api.L._
import io.laminext.syntax.all._

val signal: Signal[Option[Signal[String]]] = ???

val shifted: Signal[Option[String]] = signal.shiftOption
```

See [example](/core/example-signal-shift-option).
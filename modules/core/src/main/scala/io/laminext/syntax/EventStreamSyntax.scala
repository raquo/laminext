package io.laminext.syntax

import com.raquo.laminar.api.L._
import io.laminext.core.ops.stream.EventStreamOps

trait EventStreamSyntax {

  implicit def syntaxEventStream[A](s: EventStream[A]): EventStreamOps[A] = new EventStreamOps[A](s)

}

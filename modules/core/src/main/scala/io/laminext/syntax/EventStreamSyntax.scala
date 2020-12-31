package io.laminext.syntax

import com.raquo.airstream.eventstream.EventStream
import io.laminext.ops.stream.EventStreamOps

trait EventStreamSyntax {

  implicit def syntaxEventStream[A](s: EventStream[A]): EventStreamOps[A] =
    new EventStreamOps[A](s)

}
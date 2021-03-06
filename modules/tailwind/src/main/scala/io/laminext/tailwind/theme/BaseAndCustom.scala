package io.laminext.tailwind.theme

import io.laminext.util.ClassJoin

final case class BaseAndCustom(
  base: String,
  custom: String = ""
) {

  @inline def reset(base: String): BaseAndCustom = BaseAndCustom(base = base)

  def custom(custom: String): BaseAndCustom =
    if (custom == this.custom) {
      this
    } else {
      this.copy(custom = custom)
    }

  val classes: String = ClassJoin(base, custom)

}

object BaseAndCustom {

  val empty: BaseAndCustom = BaseAndCustom(base = "")

}

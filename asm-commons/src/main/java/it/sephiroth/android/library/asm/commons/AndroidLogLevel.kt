package it.sephiroth.android.library.asm.commons

import java.io.Serializable

enum class AndroidLogLevel(val value: Int) : Serializable {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    ASSERT(7);
}

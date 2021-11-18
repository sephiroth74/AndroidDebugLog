package it.sephiroth.android.library.debuglog

import java.io.Serializable

enum class DebugArguments(val value: Int) : Serializable {
    None(0), Short(1), Full(2)
}

package it.sephiroth.android.library.asm.runtime.debuglog.annotations

object DebugArguments {
    /**
     * None of the method's params will be printed
     */
    const val NONE = 0

    /**
     * Print a short summary of the method's params
     */
    const val SHORT = 1

    /**
     * Fully print all the method's params
     */
    const val FULL = 2
}

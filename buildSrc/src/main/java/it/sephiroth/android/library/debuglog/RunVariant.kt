package it.sephiroth.android.library.debuglog

enum class RunVariant {
    // always run
    Always,

    // disabled
    Never,

    // run only on debug buildType
    Debug,

    // run only on release buildType
    Release
}

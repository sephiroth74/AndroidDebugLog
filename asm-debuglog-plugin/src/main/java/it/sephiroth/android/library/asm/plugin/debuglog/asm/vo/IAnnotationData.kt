package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

import it.sephiroth.android.library.asm.commons.utils.StringUtils
import it.sephiroth.android.library.asm.plugin.debuglog.Constants
import java.io.Serializable

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
abstract class IAnnotationData(
    val className: String,
    var debugExit: Boolean = false,
    var debugEnter: Boolean = true,
    var logLevel: Int = Constants.DEFAULT_LOG_LEVEL.value,
    var debugArguments: Int = Constants.DEFAULT_DEBUG_ARGUMENTS.value,
    var tag: String? = null
) : Serializable {
    constructor(parent: IAnnotationData) :
            this(parent.className, parent.debugExit, parent.debugEnter, parent.logLevel, parent.debugArguments, parent.tag) {
        this.enabled = parent.enabled
    }

    val simpleClassName = StringUtils.getSimpleClassName(className)
    var enabled: Boolean = true

    open fun isEnabled(): Boolean = enabled && (debugEnter || debugExit)
}

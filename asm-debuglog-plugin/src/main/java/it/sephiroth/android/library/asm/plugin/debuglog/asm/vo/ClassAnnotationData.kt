package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
class ClassAnnotationData(className: String) : IAnnotationData(className) {
    constructor(className: String, pluginData: DebugLogPluginParameters) : this(className) {
        this.debugEnter = pluginData.debugEnter.get()
        this.debugExit = pluginData.debugExit.get()
        this.logLevel = pluginData.logLevel.get().value
        this.debugArguments = pluginData.debugArguments.get().value
    }

    override fun toString(): String {
        return "ClassAnnotationData(" +
                "className='$simpleClassName', " +
                "exit=$debugExit, " +
                "enter=$debugEnter, " +
                "level=$logLevel, " +
                "arguments=$debugArguments, " +
                "tag=$tag, " +
                "enabled=$enabled)"
    }

    companion object {
        fun from(input: DebugLogPluginParameters, simpleClassName: String): ClassAnnotationData {
            return ClassAnnotationData(simpleClassName).apply {
                debugExit = input.debugExit.get()
                debugEnter = input.debugEnter.get()
                logLevel = input.logLevel.get().value
                debugArguments = input.debugArguments.get().value
            }
        }
    }
}

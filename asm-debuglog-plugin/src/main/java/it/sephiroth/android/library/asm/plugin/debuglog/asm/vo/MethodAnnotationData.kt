package it.sephiroth.android.library.asm.plugin.debuglog.asm.vo

/**
 * DebugLog
 *
 * @author Alessandro Crugnola on 18.11.21 - 14:01
 */
class MethodAnnotationData(
    parentData: IAnnotationData,
    val methodName: String,
    val descriptor: String,
) : IAnnotationData(parentData) {

    constructor(parentData: DebugLogPluginParameters, className: String, methodName: String, descriptor: String) :
            this(ClassAnnotationData(className, parentData), methodName, descriptor)

    var shouldSkip = false

    override fun isEnabled(): Boolean {
        return super.isEnabled() && !shouldSkip
    }

    val methodParams: MutableList<MethodParameter> = mutableListOf()

    val uniqueKey = generateUniqueKey(methodName, descriptor)

    val finalTag: String
        get() = tag?.takeIf { it.isNotBlank() } ?: simpleClassName

    override fun toString(): String {
        return "MethodAnnotationData(" +
                "className='$simpleClassName', " +
                "methodName='$methodName', " +
                "exit=$debugExit, " +
                "enter=$debugEnter, " +
                "level=$logLevel, " +
                "arguments=$debugArguments, " +
                "tag=$tag, " +
                "skip=$shouldSkip, " +
                "enabled=${isEnabled()}, " +
                "params=$methodParams" +
                ")"
    }

    companion object {
        fun generateUniqueKey(name: String, descriptor: String) = name + descriptor
    }
}

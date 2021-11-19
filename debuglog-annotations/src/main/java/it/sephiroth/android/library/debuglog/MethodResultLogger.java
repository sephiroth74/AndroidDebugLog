package it.sephiroth.android.library.debuglog;

import java.util.Arrays;

@SuppressWarnings("unused")
public class MethodResultLogger {
    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, byte returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, char returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, short returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, int returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, boolean returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, long returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, float returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, double returnVal) {
        DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
    }

    public static void print(int level, int printArguments, String className, String methodName, long costedMillis, Object returnVal) {
        if (printArguments == DebugArguments.NONE || returnVal == null) {
            DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, null);
        } else {
            if (returnVal.getClass().isArray()) { // array
                if (printArguments == DebugArguments.FULL) {
                    DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, arrayToString(returnVal));
                } else {
                    DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, ParamsLogger.arrayToHashCode(returnVal));
                }
            } else if (returnVal instanceof String || returnVal instanceof Enum) { // String, Enum
                DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
            } else { // everything else
                if (printArguments == DebugArguments.FULL) {
                    DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, returnVal + "");
                } else {
                    DebugLogger.DEFAULT_IMPL.logExit(level, className, methodName, costedMillis, ParamsLogger.objectToHashCode(returnVal) + "");
                }
            }
        }
    }


    private static String arrayToString(Object val) {
        if (!(val instanceof Object[])) {
            if (val instanceof int[]) {
                return Arrays.toString((int[]) val);
            } else if (val instanceof char[]) {
                return Arrays.toString((char[]) val);
            } else if (val instanceof boolean[]) {
                return Arrays.toString((boolean[]) val);
            } else if (val instanceof byte[]) {
                return Arrays.toString((byte[]) val);
            } else if (val instanceof long[]) {
                return Arrays.toString((long[]) val);
            } else if (val instanceof double[]) {
                return Arrays.toString((double[]) val);
            } else if (val instanceof float[]) {
                return Arrays.toString((float[]) val);
            } else if (val instanceof short[]) {
                return Arrays.toString((short[]) val);
            } else {
                return "Unknown type array";
            }
        } else {
            return Arrays.deepToString((Object[]) val);
        }
    }

}

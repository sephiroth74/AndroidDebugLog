package it.sephiroth.android.library.debuglog;

import java.lang.reflect.Array;
import java.util.Arrays;

@SuppressWarnings("unused")
public class ParamsLogger {
    private int paramIndex = 0;
    private final StringBuilder paramsList = new StringBuilder();
    private final String methodName;
    private final String divider = ", ";
    private final String tag;
    private final int debugArguments;
    private final boolean printArguments;

    public ParamsLogger(String tag, String methodName, int debugType) {
        this.tag = tag;
        this.methodName = methodName;
        this.debugArguments = debugType;
        this.printArguments = true; //debugArguments != DebugArguments.NONE;
    }

    public ParamsLogger(String tag, String methodName) {
        this(tag, methodName, DebugArguments.FULL);
    }

    public ParamsLogger append(String name) {
        if (!printArguments) return this;
        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(name);
        return this;
    }

    public ParamsLogger append(String name, int val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, boolean val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, short val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, byte val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, char val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, long val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, double val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, float val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }
        paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
        return this;
    }

    public ParamsLogger append(String name, Object val) {
        if (!printArguments) return this;

        if (paramIndex++ != 0) {
            paramsList.append(divider);
        }

        if (null != val) {
            if (debugArguments == DebugArguments.FULL) {
                if (val.getClass().isArray()) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, arrayToString(val)));
                } else {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
                }
            } else if (debugArguments == DebugArguments.SHORT) {
                if (val.getClass().isArray()) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, arrayToHashCode(val)));
                } else if (val instanceof String || val instanceof Enum) {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, val));
                } else {
                    paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, objectToHashCode(val)));
                }
            }
        } else {
            paramsList.append(String.format(PARAMETER_PRINT_FORMAT, name, "null"));
        }

        return this;
    }

    static String objectToHashCode(Object val) {
        if (val == null) return "null";
        return val.getClass().getName() + "#" + System.identityHashCode(val);
    }

    static String arrayToHashCode(Object val) {
        if (val == null) return "null";
        return val.getClass().getCanonicalName() + "[size=" + Array.getLength(val) + "]";
    }

    private String arrayToString(Object val) {
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

    public void print(int level) {
        DebugLogger.DEFAULT_IMPL.logEnter(level, tag, methodName, paramsList.toString());
    }

    static final String PARAMETER_PRINT_FORMAT = "%s=\"%s\"";
}




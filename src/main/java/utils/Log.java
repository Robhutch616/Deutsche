package utils;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Formatter;

/**
 * Simple helper class to aid logging
 * <p>
 * Note there will be more logging than usual.
 */
public class Log {
    /**
     * Debug level of Log
     */
    private static int debug = 0;

    public static void setDebug(int level) {
        debug = level;
    }

    /**
     * Check minimum Debug level agains
     */
    public static boolean isMinDebug(int level) {
        return debug >= level;
    }

    // Normal log printing
    public static void l(String message) {
        System.out.print(message + "\r\n");
    }

    public static void l(String format, Object... args) {
        l(new Formatter().format(format, args).toString());
    }

    // Error log printing - prefixed by [ERROR]
    public static void error(String message) {
        l("[ERROR] " + message + "\n");
    }

    public static void error(String format, Object... args) {
        error(new Formatter().format((format), args).toString());
    }

    public static void printError(@NotNull String context, @Nullable Exception error) {
        Log.error("%s : %s", context, error == null ? "null" : error.getMessage() == null ? "null" : error.getMessage());
        if (error != null) {
            error.printStackTrace();
        }
    }
}



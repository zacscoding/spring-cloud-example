package demo.search.helper;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

/**
 * Helpers for current thread's stack trace
 */
public final class StackTrace {

    public static String getStackTrace() {
        return getStackTrace(s -> true);
    }

    public static String getNetflixStackTrace() {
        return getStackTrace(s -> StringUtils.startsWith(s, "com.netflix"));
    }

    public static String getStackTrace(Predicate<String> predicate) {
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        final StringBuilder sb = new StringBuilder();

        for (StackTraceElement elt : trace) {
            final String v = elt.toString();
            if (predicate.test(v)) {
                sb.append(v).append('\n');
            }
        }
        return sb.toString();
    }

    private StackTrace() {}
}

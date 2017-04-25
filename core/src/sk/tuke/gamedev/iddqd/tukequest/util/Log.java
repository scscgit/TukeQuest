package sk.tuke.gamedev.iddqd.tukequest.util;

import com.badlogic.gdx.ApplicationLogger;

import java.util.function.Consumer;

/**
 * Created by Steve on 02.04.2017.
 */
public class Log implements ApplicationLogger {

    public static boolean trace;
    public static boolean debug = true;

    public static void t(Object context, String message) {
        if (!trace) {
            return;
        }
        log(System.out::println, "TRACE> ", context, message);
    }

    public static void d(Object context, String message) {
        if (!debug) {
            return;
        }
        log(System.out::println, "D> ", context, message);
    }

    /**
     * Main preferred way of logging messages by default.
     *
     * @param context Object which is responsible for the message, usually "this".
     * @param message Informative message.
     */
    public static void i(Object context, String message) {
        log(System.out::println, "", context, message);
    }

    public static void w(Object context, String message) {
        log(System.err::println, "WARNING> ", context, message);
    }

    public static void e(Object context, String message) {
        log(System.err::println, "ERROR> ", context, message);
    }

    private static void log(Consumer<String> logging, String metaContextPrefix, Object context, String message) {
        String contextString;
        if (context instanceof Class) {
            contextString = ((Class) context).getSimpleName();
        } else {
            contextString = context.getClass().getSimpleName();
        }
        if ("".equals(contextString)) {
            Log.e(Log.class, "Empty context. Most likely logged using \"this\" from Anonymous Class.");
        }
        logging.accept(metaContextPrefix + contextString + ": " + message);
    }

    @Override
    public void log(String tag, String message) {
        i(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        i(tag, message + " :" + exception.getMessage());
    }

    @Override
    public void error(String tag, String message) {
        e(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        e(tag, message + " :" + exception.getMessage());
    }

    @Override
    public void debug(String tag, String message) {
        d(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        d(tag, message + " :" + exception.getMessage());
    }
}

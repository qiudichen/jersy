/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author tlark
 * @version "%I%, %G%"
 */
package com.iex.tv.core.framework;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import sun.reflect.Reflection;

/**
 * This class wraps the Log4j functionality-makes it possible for us to change implementations without serious code
 * refactoring. Usage Example: private static final TvLogger logger = new TvLogger(SomeClass.class);
 * 
 * @author tlark
 * @since Apr 11, 2006
 */
@com.iex.Ident("$Id: TvLogger.java 93950 2013-06-13 11:54:41Z swilliams $")
public class TvLogger
{
    private static final Marker NOTICE_MARKER = MarkerFactory.getMarker("NOTICE");

    private final Logger logger;
    private final Package pkg;

    /**
     * If true, do not show web station package exceptions unless level is set to DEBUG.
     */
    private final boolean dropWebstationExceptions;
    private final boolean isWebstationLogger;
    
    public TvLogger(Class<?> ctx)
    {
        logger = (ctx != null) ? LoggerFactory.getLogger(ctx) : LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        if (ctx != null) {
            pkg = ctx.getPackage();            
            isWebstationLogger = (pkg == null) ? false : pkg.getName().matches("com.iex.tv.ws.*");
        }
        else {
            pkg = null;
            isWebstationLogger = false;
        }
        String prop = System.getProperty("drop.webstation.exceptions");
        dropWebstationExceptions = prop == null || prop.equals("true");
    }

    public TvLogger()
    {
        this(Reflection.getCallerClass(2));
    }

    /**
     * If DEBUG is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void debug(Object... args)
    {
        if (isDebugEnabled())
        {
            logger.debug(buildMsg(args));
        }
    }

    /**
     * If DEBUG is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void debug(Throwable t, Object... args)
    {
        if (isDebugEnabled())
        {
            logger.debug(buildMsg(args), t);
        }
    }

    /**
     * @return true if DEBUG is enabled.
     */
    public final boolean isDebugEnabled()
    {
        return logger.isDebugEnabled();
    }

    /**
     * If INFO is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void info(Object... args)
    {
        if (isInfoEnabled())
        {
            logger.info(buildMsg(args));
        }
    }

    /**
     * If INFO is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void info(Throwable t, Object... args)
    {
        if (isInfoEnabled())
        {
            if (!isDebugEnabled() && dropWebstationExceptions && isWebstationLogger) {
                logger.info(buildExceptMsg(t, args));
            }
            else {
                logger.info(buildMsg(args), t);
            }
        }
    }

    /**
     * @return true if INFO is enabled.
     */
    public final boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    /**
     * Logs the specified args (appended to form a string) to the logging system at an INFO level. Use this to log an
     * event at an INFO level, regardless of the current level.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void notice(Object... args)
    {
        logger.info(NOTICE_MARKER, buildMsg(args));
    }

    /**
     * If WARN is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void warn(Object... args)
    {
        if (isWarnEnabled())
        {
            logger.warn(buildMsg(args));
        }
    }

    /**
     * If WARN is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void warn(Throwable t, Object... args)
    {
        if (isWarnEnabled()) 
        {
            if (!isDebugEnabled() && dropWebstationExceptions && isWebstationLogger) {
                logger.warn(buildExceptMsg(t, args));
            }        
            else {
                logger.warn(buildMsg(args), t);
            }
        }
    }

    /**
     * @return true if WARN is enabled.
     */
    public final boolean isWarnEnabled()
    {
        return logger.isWarnEnabled();
    }

    /**
     * If ERROR is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void error(Object... args)
    {
        if (isErrorEnabled())
        {
            logger.error(buildMsg(args));
        }
    }

    /**
     * If ERROR is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void error(Throwable t, Object... args)
    {
        if (isErrorEnabled()) 
        {
            if (!isDebugEnabled() && dropWebstationExceptions && isWebstationLogger) {
                logger.error(buildExceptMsg(t, args));
            }
            else {
                logger.error(buildMsg(args), t);
            }
        }
    }

    /**
     * @return true if ERROR is enabled.
     */
    public final boolean isErrorEnabled()
    {
        return logger.isErrorEnabled();
    }

    /**
     * If FATAL is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     * @deprecated As of 2010-10-07, use error(Object... args) instead.
     */
    @Deprecated
    public final void fatal(Object... args)
    {
        // FATAL has been deprecated in slf4j, so just do ERROR
        error(args);
    }

    /**
     * If FATAL is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     * @deprecated As of 2010-10-07, use error(Throwable t, Object... args) instead.
     */
    @Deprecated
    public final void fatal(Throwable t, Object... args)
    {
        // FATAL has been deprecated in slf4j, so just do ERROR
        error(t, args);
    }

    /**
     * @return true if FATAL is enabled.
     * @deprecated As of 2010-10-07, use isErrorEnabled() instead.
     */
    @Deprecated
    public final boolean isFatalEnabled()
    {
        // FATAL has been deprecated in slf4j, so just do ERROR
        return isErrorEnabled();
    }

    /**
     * If TRACE is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void trace(Object... args)
    {
        if (isTraceEnabled())
        {
            logger.trace(buildMsg(args));
        }
    }

    /**
     * If TRACE is enabled, logs the specified args (appended to form a string) to the logging system.
     * 
     * @param t
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void trace(Throwable t, Object... args)
    {
        if (isTraceEnabled()) 
        {
            if (!isDebugEnabled() && dropWebstationExceptions && isWebstationLogger) {
                logger.trace(buildExceptMsg(t, args));
            }        
            else {
                logger.trace(buildMsg(args), t);
            }
        }
    }

    /**
     * If TRACE is enabled, logs the specified args (appended to form a string) to the logging system, along with a dump
     * of the call stack, beginning with the caller, and showing the number of caller elements specified by depth.
     * 
     * @param depth Number of stack elements to be shown.
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void traceWithStackDump(int depth, Object... args)
    {
        traceWithStackDumpHelper(depth, args);
    }

    /**
     * Convenience overload that logs the specified args (appended to form a string) to the logging system, with a
     * default number of stack elements (20).
     * 
     * @param args 0 or more Objects. If enabled, these will be appended to form a string.
     */
    public final void traceWithStackDump(Object... args)
    {
        traceWithStackDumpHelper(20, args);
    }

    private void traceWithStackDumpHelper(int depth, Object... args)
    {
        if (isTraceEnabled())
        {
            logger.trace(buildMsg(args), getCurrentStackTrace(depth));
        }
    }

    /**
     * Creates a back trace of the current call stack for emission by the logger. TODO JackD 11/17/09- The current
     * output takes advantage of the fact that an exception can gather its current call stack. However, the output form
     * this is somewhat confusing, because it makes it appear as if there was an exception, though in reality none was
     * actually thrown. Need to try and just create a string that shows the stack elements, and omit logging of the
     * exception object used to create the stack elements.
     * 
     * @param depth
     * @return A Throwable that contains a stack trace to the requested depth.
     */
    private Throwable getCurrentStackTrace(int depth)
    {
        Throwable throwable = new Throwable().fillInStackTrace();

        // by starting at 3, this method and its internal callers are omitted from the top of the stack trace
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        throwable.setStackTrace(Arrays.copyOfRange(stackTrace, 3, Math.min(stackTrace.length, depth + 3)));
        return throwable;
    }

    /**
     * @return true if TRACE is enabled.
     */
    public final boolean isTraceEnabled()
    {
        return logger.isTraceEnabled();
    }

    /**
     * Builds a StringBuilder out of the specified args by simply appending them.
     * 
     * @param args
     * @return a StringBuilder
     */
    private final String buildMsg(Object... args)
    {
        if (args == null)
        {
            return "";
        }

        StringBuilder buf = new StringBuilder(16 * args.length);
        for (Object object : args)
        {
            // Make sure we get "readable" array entries
            if ((object != null) && object.getClass().isArray())
            {
                //Utils.buildDelimitedString((Object[]) object, ",", buf);
            }
            else
            {
                buf.append(object);
            }
        }

        return buf.toString();
    }

    /**
     * Builds a StringBuilder out of the specified args by simply appending them.
     * 
     * @param args
     * @return a StringBuilder
     */
    private final String buildExceptMsg(Throwable t, Object... args)
    {
        if (args == null)
        {
            return "";
        }

        StringBuilder buf = new StringBuilder(16 * args.length);
        for (Object object : args)
        {
            // Make sure we get "readable" array entries
            if ((object != null) && object.getClass().isArray())
            {
                //Utils.buildDelimitedString((Object[]) object, ",", buf);
            }
            else
            {
                buf.append(object);
            }
        }
        if ( t != null ) {
            buf.append(" - " + t.getMessage());
        }

        return buf.toString();
    }

    /**
     * TODO Describe the purpose of this class/interface here
     * 
     * @author tlark
     * @since Feb 27, 2008
     */
    @com.iex.Ident("$Id: TvLogger.java 93950 2013-06-13 11:54:41Z swilliams $")//$NON-NLS-1$
    public static final class MappedContext
    {
        /*
         * Constants related to logging MappedContext data.
         */
        public static final String SESSION_ID_KEY = "tv.sessionId";
        public static final String TRANSACTION_ID_KEY = "tv.transactionId";
        public static final String CUSTOMER_NAME_KEY = "tv.customerName";
        public static final String CUSTOMER_ID_KEY = "tv.customerId";
        public static final String USER_NAME_KEY = "tv.userName";
        public static final String UNKNOWN_TEXT = "UNKNOWN";

        /**
         * Clears all mapped contexts in the current thread.
         */
        public static final void clear()
        {
            MDC.clear();
        }

        public static final void put(String key, Object value)
        {
            if (value != null)
            {
                MDC.put(key, String.valueOf(value));
            }
            else
            {
                MDC.put(key, null);
            }
        }
        
        public static final String get(String key)
        {
            return MDC.get(key);
        }        

        public static final void remove(String... keys)
        {
            for (String key : keys)
            {
                MDC.remove(key);
            }
        }
    }
}

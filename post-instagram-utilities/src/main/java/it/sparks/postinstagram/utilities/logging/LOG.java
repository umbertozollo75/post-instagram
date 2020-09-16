package it.sparks.postinstagram.utilities.logging;

import org.apache.log4j.Logger;

public class LOG {

  private static final Logger LOGGER = Logger.getLogger(it.sparks.postinstagram.utilities.logging.LOG.class);

  public static void debug(String msg) {

    LOGGER.debug(getCallerName() + " " + msg);
  }

  public static void debug(String msg, Exception e) {

    LOGGER.debug(getCallerName() + " " + msg, e);
  }

  public static void error(long errorCode) {

    error(null, errorCode, null);
  }

  public static void error(long errorCode, String errorDescription) {

    error(null, errorCode, errorDescription);
  }

  public static void error(String msg) {

    error(msg, 0, null);
  }

  public static void error(String msg, Exception e) {

    LOGGER.error(getCallerName() + msg, e);
  }

  public static void error(String msg, long errorCode) {

    error(msg, errorCode, null);
  }

  public static void error(String msg, long errorCode, String errorDescription) {
    String msg1 = null;
    
    msg1 = msg;
    
    if (msg == null) {
      msg1 = new String();
    }
    if (errorCode != 0) {
      msg1 += " - errorCode=" + errorCode;
    }
    if (errorDescription != null) {
      msg1 += " - errorDescription=" + errorDescription;
    }
    LOGGER.error(getCallerName() + msg1);
  }

  public static void fatal(String msg) {

    LOGGER.fatal(getCallerName() + msg);
  }

  public static void info(String msg) {

    LOGGER.info(getCallerName() + msg);
  }

  public static void traceIn() {

    LOGGER.info(">> " + getCallerName());
  }

  public static void traceIn(String msg) {

    LOGGER.info(">> " + getCallerName() + msg);
  }

  public static void traceOut() {

    LOGGER.info("<< " + getCallerName());
  }

  public static void traceOut(String msg) {

    LOGGER.info("<< " + getCallerName() + msg);
  }

  public static void warn(String msg) {

    LOGGER.warn(getCallerName() + msg);
  }

  /*
   * @return thread id and class name with package
   */
  private static String getCallerName() {
    String callerMethodName = "";
    String callerClassName = "";
    long tid = 0;
    String line = "";
    try {
      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      tid = Thread.currentThread().getId();
      boolean found = false;
      int id = 1;
      StackTraceElement caller = null;
      while (!found && id < 10) {
        caller = stackTraceElements[id];
        if (caller.getClassName().equals(it.sparks.postinstagram.utilities.logging.LOG.class.getCanonicalName())) {
          id++;
        } else {
          found = true;
        }
      }
      callerMethodName = caller.getMethodName();
      callerClassName = caller.getClassName();
      line = caller.getLineNumber() + "";
    } catch (Exception ex) {
      error(ex.getMessage(), ex);
      callerMethodName = "<exception in getCallerName()>";
    }
    return "TID:" + tid + " - " + callerClassName + "." + callerMethodName + "() - line " + line + " : ";
  }

  private LOG() {
  }

}

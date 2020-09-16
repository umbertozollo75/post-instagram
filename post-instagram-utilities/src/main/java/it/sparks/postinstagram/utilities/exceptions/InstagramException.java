package it.sparks.postinstagram.utilities.exceptions;

import it.sparks.postinstagram.utilities.costants.InstagramConst;

/**
 * 
 * @author Umberto Zollo
 *
 */
public class InstagramException extends Exception {

  private static final long ERRORCODE        = 1L;

  private static final long serialVersionUID = 1L;

  public InstagramException() {
    super();
  }

  public InstagramException(long errorCode) {
    this(null, errorCode);
  }

  public InstagramException(long errorCode, Throwable rootCause) {
    this(null, errorCode, rootCause);
  }

  public InstagramException(String message) {
    this(message, InstagramConst.GENERIC_ERROR);
  }

  public InstagramException(String message, long errorCode) {
    super(message);
    this.initCause(getCause());
  }

  public InstagramException(String message, long errorCode, Throwable rootCause) {
    super(message, rootCause);
  }

  public InstagramException(String message, Throwable rootCause) {
    this(message, InstagramConst.GENERIC_ERROR, rootCause);
  }

  public InstagramException(Throwable rootCause) {
    super(rootCause);
  }

  /**
   * @return the errorCode
   */
  public long getErrorCode() {
    return ERRORCODE;
  }

}

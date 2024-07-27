package br.com.process.integration.database.core.exception;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class ExceptionFactory {

	private static final Log LOG = LogFactory.getLog(ExceptionFactory.class);

	public static final BaseException createException(int id, Throwable t) {
		return new BaseException(id, t);
	}

	public static final BaseException createException(int id, String message) {
		return new BaseException(id, message);
	}

	public static final BaseException createException(String codigo, String message) {
		return new BaseException(codigo, message);
	}

	public static final BaseException createException(Throwable t) {
		return new BaseException(t);
	}

	public static final BaseException createException(String message, Throwable t) {
		return new BaseException(message, t);
	}

	public static final BaseException createException(int id, String message, Throwable t) {
		return new BaseException(id, message, t);
	}

	public static final BaseException createException(String codigo, String message, Throwable t) {
		return new BaseException(codigo, message, t);
	}

	public static final BaseException createException(int id, String codigo, String message, Throwable t) {
		return new BaseException(id, codigo, message, t);
	}
	
	public static final <T extends BaseException> T createException(Class<T> targetExceptionType, Throwable error) {
		if (targetExceptionType.isInstance(error)) {
			return (T) error;
		} else {
			Class<?>[] intArgsClass = new Class[] { Throwable.class };
			try {
				Constructor<T> c = targetExceptionType.getConstructor(intArgsClass);
				return c.newInstance(error);
			} catch (Exception e) {
				LOG.error("Erro no tratamento ExceptionFactory " + targetExceptionType + " Throwable: " + error);
				LOG.error(e);
				try {
					T returnValue = targetExceptionType.getDeclaredConstructor().newInstance();
					return returnValue;
				} catch (Exception e1) {
					return (T) error;
				}
			}
		}
	}

	public static final <T extends BaseException> T createException(Class<T> targetExceptionType, int id, Throwable error) {
		if (targetExceptionType.isInstance(error)) {
			((T) error).setId(id);
			return (T) error;
		} else {
			Class<?>[] intArgsClass = new Class[] { Throwable.class };
			try {
				Constructor<T> c = targetExceptionType.getConstructor(intArgsClass);
				T returnValue = c.newInstance(error);
				((T) error).setId(id);
				return returnValue;
			} catch (Exception e) {
				LOG.error("Erro no tratamento ExceptionFactory " + targetExceptionType + "  id: " + id + " Throwable: "
						+ error);
				LOG.error(e);
				try {
					T returnValue = targetExceptionType.getDeclaredConstructor().newInstance();
					return returnValue;
				} catch (Exception e1) {
					return (T) error;
				}
			}
		}
	}

}

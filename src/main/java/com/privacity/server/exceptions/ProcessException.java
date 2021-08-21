package com.privacity.server.exceptions;

import com.privacity.common.enumeration.ExceptionReturnCode;

public class ProcessException extends PrivacityException {

	public ProcessException(ExceptionReturnCode userUserNotLogger) {
		super(userUserNotLogger.getCode());
	}

	public ProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProcessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProcessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}

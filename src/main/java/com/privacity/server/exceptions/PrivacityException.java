package com.privacity.server.exceptions;

import com.privacity.common.enumeration.ExceptionReturnCode;

public class PrivacityException extends Exception {
	public PrivacityException(ExceptionReturnCode userUserNotLogger) {
		super(userUserNotLogger.getCode());
	}

	public PrivacityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public PrivacityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PrivacityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PrivacityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}

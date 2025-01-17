package org.programmers.kdtboard.controller.response;

import java.time.LocalDateTime;

public class ErrorResponse {

	private final LocalDateTime timestamp = LocalDateTime.now();
	private final int status;
	private final String error;
	private final String code;
	private final String message;

	public ErrorResponse(ErrorCodeMessage errorCodeMessage) {
		this.status = errorCodeMessage.getStatusCode().value();
		this.error = errorCodeMessage.getStatusCode().name();
		this.code = errorCodeMessage.name();
		this.message = errorCodeMessage.getMessage();
	}
}

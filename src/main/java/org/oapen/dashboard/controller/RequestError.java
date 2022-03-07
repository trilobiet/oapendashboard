package org.oapen.dashboard.controller;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RequestError {
	
	private final Integer errorCode;
	private final String message;
	private final LocalDateTime timestamp = LocalDateTime.now();
	// private final String preach = "[Some quote here]";

}

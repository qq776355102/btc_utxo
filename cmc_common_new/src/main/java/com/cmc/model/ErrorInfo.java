package com.cmc.model;

import java.io.Serializable;

public class ErrorInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4772943502999014194L;

	private String code;

	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorInfo [code=" + code + ", message=" + message + "]";
	}

}

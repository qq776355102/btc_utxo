package com.cmc.utxo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//@JsonInclude(Include.NON_NULL)
public class Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1347165047453323921L;
	private boolean success;
	private String message; // 消息
	@SuppressWarnings("rawtypes")
	private List data; // json数据
	private String status;

	public Result() {
	}

	public Result(boolean success) {
		this.success = success;
	}

	public Result(String message) {
		super();
		this.message = message;
	}

	public Result(boolean success, String message) {
		super();
		this.success = success;
		if (success) {
			this.status = "110";
		}else {
			this.status = "111";
		}
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



	public List getData() {
		return data;
	}

	/**
	 * @param obj
	 * @retur
	 * @author mc
	 */
	public static Result setData(Object obj) {
		Result rs = new Result();
		rs.data = new ArrayList();
		rs.status = "110";
		rs.setSuccess(true);
		if (obj != null) {
			if (obj instanceof Collection) {
				rs.data.addAll((Collection) obj);
			} else {
				rs.data.add(obj);
			}
		} else {
			rs.data = Collections.emptyList();
		}
		return rs;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message + ", data=" + data + ", state=" + status + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

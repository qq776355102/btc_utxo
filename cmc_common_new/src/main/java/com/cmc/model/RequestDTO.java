package com.cmc.model;

import net.sf.json.JSONObject;

public class RequestDTO {
	@SuppressWarnings("unused")
	private interface Basic{};
	private String v;
	/**
	 * 访问的方法名:8位
	 */
	private String a;
	/**
	 * 测试的时候用,正式环境勿用
	 */
	private String token;
	/**
	 * token
	 */
	private String t;
	private JSONObject jsonParam;
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public JSONObject getJsonParam() {
		return jsonParam;
	}
	public void setJsonParam(JSONObject jsonParam) {
		this.jsonParam = jsonParam;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	
	
}

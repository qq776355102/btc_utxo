package com.cmc.utxo.rpc;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmc.core.SysProperties;

import net.sf.json.JSONObject;

public class MvsRpc {

Logger log = LoggerFactory.getLogger(Rpc.class);
	private MvsRpc() {
	};
	private static String APPLICATION_JSON = "application/json;charset=utf-8";
//	= "123456.ylkj";// rpc接口密码
	private static String url; 
	//= "http://39.105.60.120:8820/rpc/v3:8820";
	static {
		url = SysProperties.getProperty("mvs_rpc_mainnet_url");
	}
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();
	// 发送 post请求
	public static String sendHttpPost(String params) {
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType(APPLICATION_JSON);
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	private static String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
	// 广播交易
	public static String sendRawtransaction(String signedhex) {
		Object[] str = new Object[] {signedhex};
		JSONObject json = new JSONObject();
		json.put("method", "sendrawtx");
		json.put("params", str);
		json.put("jsonrpc", "2.0");
		return sendHttpPost(json.toString());
	}
	// 根据账户获取原生交易
	public static String getRawtransaction(String txid) {
		Object[] str = new Object[] {txid};
		JSONObject json = new JSONObject();
		json.put("method", "gettx");
		json.put("params", str);
		json.put("jsonrpc", "2.0");
		String sendPostWithJson = sendHttpPost(json.toString());
		System.err.println(json.toString());
		return sendPostWithJson;
	}
}

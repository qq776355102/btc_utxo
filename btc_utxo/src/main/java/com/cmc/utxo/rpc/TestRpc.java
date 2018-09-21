package com.cmc.utxo.rpc;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.cmc.core.SysProperties;

import net.sf.json.JSONObject;

public class TestRpc {

	private TestRpc() {
	};

	private static String APPLICATION_JSON = "application/json;charset=utf-8";
	//test
	private static String test_user;
//	= "test";// rpc接口用户名
	private static String test_password;
//	= "123456";// rpc接口密码
	private static String test_url;
//	= "http://192.168.2.189:18332/";
	private static String test_cred = null;
	
	static {
		test_user = SysProperties.getProperty("btc_rpc_testnet_username");
		test_password = SysProperties.getProperty("btc_rpc_testnet_password");
		test_url = SysProperties.getProperty("btc_rpc_testnet_url");
		test_cred = DatatypeConverter.printBase64Binary((test_user + ":" + test_password).getBytes());
	}
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();

	// 发送 post请求
	public static String sendHttpPost(String params) {
		HttpPost httpPost = new HttpPost(test_url);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			httpPost.setHeader("Authorization", "Basic " + test_cred);
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

	// 根据账户获取一个新的地址
	public static String sendRawtransaction(String signedhex) {
		Object[] str = new Object[] {signedhex};
		JSONObject json = new JSONObject();
		json.put("method", "sendrawtransaction");
		json.put("params", str);
		json.put("id", Object.class);
		return sendHttpPost(json.toString());
	}
	
	// 根据账户获取一个新的地址
	public static String getRawtransaction(String txid) {
		Object[] str = new Object[] {txid,true};
		JSONObject json = new JSONObject();
		json.put("method", "getrawtransaction");
		json.put("params", str);
		json.put("id", Object.class);
		String sendPostWithJson = sendHttpPost(json.toString());
		return sendPostWithJson;
	}
	
}

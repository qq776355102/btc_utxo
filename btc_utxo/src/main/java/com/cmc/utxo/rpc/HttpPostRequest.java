package com.cmc.utxo.rpc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cmc.core.SysProperties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/**
 * 向远程服务器发送post请求获取返回值方法 类修改者 修改日期 修改说明
 * <p>
 * Title: HttpPost.java
 * </p >
 * <p>
 * Description:语联网
 * </p >
 * 
 * @author mc
 * @date Feb 21, 2014 2:16:54 PM
 * @version V1.0
 * 
 */

public class HttpPostRequest {

	private static MultiThreadedHttpConnectionManager connectionManager = null;

	private static HttpClient httpclient = null;

	private static String url ;

	static {
		 url = SysProperties.getProperty("pcf_balance_url");
		connectionManager = new MultiThreadedHttpConnectionManager();
		httpclient = new HttpClient(connectionManager);
		// 链接超时
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(600 * 1000);
		// 读取超时
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(600 * 1000);
	}

	// 普通的http请求，post方法传送map数据（大量数据的传输）
	public static String sendPostRequest(Map<String, String> param) {
		String result = null;
		PostMethod httpost = new PostMethod(url);
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (param != null) {
				// 添加请求参数
				Iterator<String> keySetIterator = param.keySet().iterator();
				while (keySetIterator.hasNext()) {
					String key = keySetIterator.next();
					String value = param.get(key);
					// 加密
					String encode = value;
					nvps.add(new NameValuePair(key, encode));
				}
			}
			NameValuePair[] data = nvps.toArray(new NameValuePair[] {});
			// 设置参数
			httpost.setRequestBody(data);
			httpost.setRequestHeader("token", "y019ab530fbd14642bd7d956cee036954");
			// 设置编码格式统一为UTF_8
			httpost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 设置超时
			// httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
			// 3000);
			// 开始执行
			httpclient.executeMethod(httpost);
			// 如果状态为200，表示执行成功
			result = getStringFromStream(httpost);
			System.out.println("haha.............." + result);
			return result;
		} catch (Exception e) {
			System.out.println("HttpRequest请求出错,错误信息:" + e.getMessage());
			return null;
		} finally {
			httpost.releaseConnection();
			httpclient.getHttpConnectionManager().closeIdleConnections(0);
		}
	}

	// 将流转换成为字符串
	private static String getStringFromStream(PostMethod method) throws Exception {
		InputStream resStream = method.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(resStream, "UTF-8"));
		StringBuffer resBuffer = new StringBuffer();
		String resTemp = "";
		while ((resTemp = br.readLine()) != null) {
			resBuffer.append(resTemp);
		}
		String response = resBuffer.toString();
		return response;
	}

	// btc给彭朝方发送地址余额
	public static void sendBtcAddressBalance(String address, String balance,String contractId) {
		Map<String, String> map = new HashMap<String, String>();
		JSONArray param = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("address", address);
		jsonObject.put("value", balance);
		jsonObject.put("contractid", contractId);
		param.add(jsonObject);
		map.put("param", param.toString());
		sendPostRequest(map);
	}
	
	// mvs给彭朝方发送地址余额
	public static void sendMvsAddressBalance(String param) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("param", param);
		sendPostRequest(map);
	}

}

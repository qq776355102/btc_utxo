package com.cmc.utxo.rpc;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpGetForUnspent {

	
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();

	private static String APPLICATION_JSON = "application/json;charset=utf-8";
	
	//btc第三方服务
	private static String preUrl_btc = "https://chain.api.btc.com/v3/address/";
	// 发送 post请求
	public static String sendHttpPost(String address) {
		String url = preUrl_btc.concat(address).concat("/unspent");
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Content-Type",APPLICATION_JSON);
		return sendHttpsGet(httpGet);
	}
	
	
	
	 private static String sendHttpsGet(HttpGet httpGet) {  
	        CloseableHttpClient httpClient = null;  
	        CloseableHttpResponse response = null;  
	        HttpEntity entity = null;  
	        String responseContent = null;  
	        try {  
	            // 创建默认的httpClient实例.  
	            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new java.net.URL(httpGet.getURI().toString()));  
	            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);  
	            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();  
	            httpGet.setConfig(requestConfig);  
	            // 执行请求  
	            response = httpClient.execute(httpGet);  
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
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return responseContent;  
	    }  
	 public static void main(String[] args) {
		String sendHttpPost = sendHttpPost("14V4mGBxTE65Y7NiqP8UWe7Ga3zaTJfbDM");
		System.out.println(sendHttpPost);
	}
}

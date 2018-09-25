package com.cmc.controller.appservices;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cmc.json.JsonPluginsUtil;

import com.cmc.model.ResponseDTO;
import com.cmc.util.ByteUtils;
import com.cmc.util.Gzip;


public class BaseAction {
	private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);
	public static HttpServletRequest getRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	public static HttpServletResponse getResponse(){
		HttpServletResponse resp = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		return resp;
	}
	
	
	public static String getIP(){
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-real-ip");
		
		if (StringUtils.isBlank(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isBlank(ip)) {
			ip = request.getRemoteAddr();
		} else if (StringUtils.isBlank(ip)) {
			ip = "0.0.0.0";
		}
		return ip;
	}
	
	public void formatReponse(ResponseDTO responseDTO){
		try {
			HttpServletRequest request = getRequest();
			HttpServletResponse response = getResponse();
			
			String joStr = JsonPluginsUtil.beanToJson(responseDTO);
			logger.debug("response content:"+joStr);
			byte[] reponseBytes = joStr.getBytes("UTF-8");
			boolean contentTypeIsJson = true;
			//返回数据暂时不做加密
			/*	if (request.getHeader("cmb-ae") != null) {
				Aes aes = new Aes(AES_KEY);
				reponseBytes = aes.encrypt(reponseBytes);
				contentTypeIsJson = false;
			}*/
			if (request.getHeader("cmb-g") != null) {
				reponseBytes = Gzip.gzip(reponseBytes);
				contentTypeIsJson = false;
            }
			if(contentTypeIsJson){
				response.setContentType("application/Json");
			}
			ServletOutputStream writer = response.getOutputStream();
			writer.write(reponseBytes);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常,请稍后重试！",e);
		}
	}
	
	public String parseRequest(){
		try
        {
			HttpServletRequest request = getRequest();
			String requestStr = request.getParameter("requestStr");
			byte[] requestBytes = null;
			//1、读取数据
			if(StringUtils.isEmpty(requestStr)){
				requestBytes = ByteUtils.readBytesFromInStream(request.getInputStream());
			}else{
				requestBytes = requestStr.getBytes("UTF-8");
			}
			//解压
			if (request.getHeader("cmb-g") != null) {
				requestBytes = Gzip.ungzip(requestBytes);
            }
			//aes解密
			if (request.getHeader("cmb-ae") != null) 
            {
			
            }
			//验证
			return new String(requestBytes,"UTF-8");
        }
        catch (Exception e)
        {
            logger.error("parseRequest error:",e);
        }
		return null;
	}
	public static boolean isValidSignature(String address, String signature, String parameters) {
		try {
			return ECKey.signedMessageToKey(parameters, signature)
					.toAddress(Address.fromBase58(null, address).getParameters()).toString().equals(address);
		} catch (Exception e) {
			return false;
		}
	}
}

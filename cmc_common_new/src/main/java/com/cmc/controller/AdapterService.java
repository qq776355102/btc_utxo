package com.cmc.controller;


import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.exception.MedicalDebugException;
import com.cmc.exception.MedicalException;
import com.cmc.model.RequestDTO;
import com.cmc.model.ResponseDTO;

import net.sf.json.JSONObject;

@RestController(value = "adapterService")
public class AdapterService {
	private static final Logger logger = LoggerFactory.getLogger(AdapterService.class);

	@Resource
	public Map<String, BusinessService> mappingServices;

	// @Autowired
	// private TokenProxy tokenProxy;

	// @Autowired
	// private IUserService userService;

	public ResponseDTO invoke(RequestDTO requestDTO, HttpServletRequest request)
			throws MedicalDebugException, MedicalException {
		String a = requestDTO.getA();
		if (a == null || a.length() != 8) {
			throw new MedicalDebugException("参数a位数错误，请重试！");
		}
		BusinessService bs = mappingServices.get(a.substring(0, 6));
		if (bs == null) {
			throw new MedicalDebugException("参数a编码错误，请重试！");
		}
		JSONObject param = requestDTO.getJsonParam();
		param = JSONObject.fromObject(param);
		JSONObject postRequ = null;
		JSONObject devBase = null;
		if (!param.isEmpty()) {
			postRequ = param.getJSONObject("PostRequ");
			devBase = param.getJSONObject("DevBase");
		}
		if (devBase.isNullObject()) {
			throw new MedicalDebugException("参数DevBase数错误，请重试！");
		}

		devBase.put("reqIP", getIP(request));
		devBase.put("queryStr", request.getQueryString());
		devBase.put("a", a);
		String userAgent = request.getHeader("User-Agent");
		if (!StringUtils.isEmpty(userAgent)) {
			devBase.put("userAgent", userAgent.toLowerCase());
		}
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		devBase.put("basePath", basePath);

		boolean priFlag = false;
		// 判断接口是否需要权限
		Integer type = BusinessService.APP_PERMS.get(a);
		if (type != null && type == 1) {
			priFlag = true;
		}

		String address = null;
		if (!devBase.containsKey("address")) {
			throw new MedicalException("devBase must need address");
		}else {
			address = devBase.getString("address");
		}

		if (!devBase.containsKey("debug")){
			if (!postRequ.isEmpty()) {
				if (!devBase.containsKey("token")) {
					priFlag =false;
				}else {
					String token = devBase.getString("token");
					priFlag = isValidSignature(address, token, postRequ.toString());
					logger.debug("正式环境token是否验证通过:"+priFlag);
				}
			}
		}
		
		
		if (!priFlag) {
			throw new MedicalException("接口认证信息有误");
		}
		// TO-DO 认证处理
		// if (devBase.containsKey("userId"))
		// {
		// userIdDevBase = devBase.getInt("userId");
		// if (userIdDevBase != null && userIdDevBase.compareTo(0) != 0)
		// {
		// //认证登录的用户
		// User user = userService.findUserByPrimaryKey(userIdDevBase);
		// if (user == null)
		// {
		// throw new MedicalException("认证信息有误，请重新登录");
		// }
		//
		//
		// //存入本地线程
		// //TO_DOD
		// }
		// }

		Object obj = null;
		try {
			Class<? extends BusinessService> cls = bs.getClass();
			Method setMethod = cls.getDeclaredMethod(BusinessService.METHOD_MAP.get(a), JSONObject.class,
					JSONObject.class);
			obj = setMethod.invoke(bs, postRequ, devBase);
		} catch (Exception e) {
			logger.error("系统异常，invoke error,", e);
			Throwable cause = e.getCause();
			if (cause instanceof MedicalDebugException) {
				throw new MedicalDebugException(cause.getMessage());
			} else if (cause instanceof MedicalException) {
				throw new MedicalException(cause.getMessage());
			}

		}
		return (ResponseDTO) obj;
	}

	private String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isBlank(ip)) {
			ip = request.getRemoteAddr();
		} else if (StringUtils.isBlank(ip)) {
			ip = "0.0.0.0";
		}
		return ip;
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

package com.cmc.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.cmc.core.YunLianMethodAnnotation;


public abstract class BusinessService {
	
	public static Map<String,String> METHOD_MAP = new HashMap<String,String>();
	public static Map<String, Integer> APP_PERMS = new HashMap<String, Integer>();
	@PostConstruct
	public void init(){
		try {
			Method[] ms = this.getClass().getMethods();
			for(Method m : ms){
				if(m==null){
					continue;
				}
				YunLianMethodAnnotation ma = m.getAnnotation(YunLianMethodAnnotation.class);
				if(ma==null){
					continue;
				}
				METHOD_MAP.put(ma.getMethodSimpCode(), m.getName());
				APP_PERMS.put(ma.getMethodSimpCode(), ma.getAppPerm());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

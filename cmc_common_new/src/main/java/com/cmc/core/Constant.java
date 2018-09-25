package com.cmc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 暴露对外接口通用常量
 * 
 * @author Administrator
 */
public class Constant
{
    /**
     * 数据状态-否
     */
    public static final int STATUS_0 = 0;

    /**
     * 数据状态-是
     */
    public static final int STATUS_1 = 1;

    /**
     * 数据状态-删除状态
     */
    public static final int STATUS_2 = 2;

    /**
     * 数据状态-禁用状态
     */
    public static final int STATUS_3 = 3;

    
    public static final String SUCCESS="110";  				//成功
    public static final String ERROR="111";	   				//错误
    public static final String TIMEOUT="112";  			//token过期
    public static final String WRONGFUL="113"; 			//token不合法
    public static final String ABNORMAL="114"; 			//接口异常

    /**
     * 系统部署环境
     */
    public static String SYSTEM_DEPLOY_ENV = SysProperties.getProperty("2yuanlian_proj_status",
        "debug");
    
    // 平台登录
    public static final int LOGIN_TYPE_0 = 0;

    // 外部登录
    public static final int LOGIN_TYPE_1 = 1;
    
    //成熟未花费
    public static final int UTXO_STATUS_0 = 0;
    
    //已花费,并确认
    public static final int UTXO_STATUS_1 = 1;
    
    //现有成熟utxo状态0,已花费未确认更改状态为2
    //成熟已花费未确认
    public static final int UTXO_STATUS_2 = 2;
    
    
    //花费现有utxo(0,3),新产出的未确认utxo增加记录状态为3
    //新产出utxo未确认
    public static final int UTXO_STATUS_3 = 3;
    
    //花费状态为3的utxo,把状态为3的utxo改为状态4
    //未成熟已花费未确认
    public static final int UTXO_STATUS_4 = 4;
    
    public static List<String> PICTURE_FORMAT = new ArrayList<String>();
    static
    {
    	PICTURE_FORMAT.add("jpg");
    	PICTURE_FORMAT.add("gif");
    	PICTURE_FORMAT.add("png");
    	PICTURE_FORMAT.add("bmp");
    	PICTURE_FORMAT.add("jpeg");
    }
}

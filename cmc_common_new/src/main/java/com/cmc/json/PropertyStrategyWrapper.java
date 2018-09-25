/**
 * Project Name:2mobo_common
 * File Name:PropertyStrategyWrapper.java
 * Package Name:cn.mobo.thirdPage.json
 * Date:2017年6月29日下午7:17:47
 * Copyright (c) 2017, mjiang@2mobo.cn All Rights Reserved.
 *
*/

package com.cmc.json;

import net.sf.json.JSONException;
import net.sf.json.util.PropertySetStrategy;

/**
 * ClassName:PropertyStrategyWrapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年6月29日 下午7:17:47 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class PropertyStrategyWrapper extends PropertySetStrategy
{

    private PropertySetStrategy original;

    public PropertyStrategyWrapper(PropertySetStrategy original) {
        this.original = original;
    }

    @Override
    public void setProperty(Object o, String string, Object o1) throws JSONException {
        try {
            original.setProperty(o, string, o1);
        } catch (Exception ex) {
        }

    }

}


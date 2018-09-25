package com.cmc.core;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
 * 读取sys.properities
 */
public class SysProperties extends PropertyPlaceholderConfigurer
{
    private static Map<String, Object> ctxPropertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props)
        throws BeansException
    {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, Object>();
        for (Object key : props.keySet())
        {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    public static String getProperty(String name)
    {
        return (String)ctxPropertiesMap.get(name);
    }

    public static Integer getIntProperty(String name)
    {
        String value = (String)ctxPropertiesMap.get(name);
        return Integer.valueOf(value);
    }

    public static Boolean getBooleanProperty(String name)
    {
        String value = (String)ctxPropertiesMap.get(name);
        return Boolean.valueOf(value);
    }

    public static String getProperty(String name, String dv)
    {
        Object obj = ctxPropertiesMap.get(name);
        if (obj == null)
        {
            return dv;
        }
        return (String)obj;
    }

    public static Integer getIntProperty(String name, Integer dv)
    {
        Object obj = ctxPropertiesMap.get(name);
        if (obj == null)
        {
            return dv;
        }
        return Integer.valueOf(dv);
    }

    public static Boolean getBooleanProperty(String name, boolean dv)
    {
        Object obj = ctxPropertiesMap.get(name);
        if (obj == null)
        {
            return dv;
        }
        return Boolean.valueOf(dv);
    }

}

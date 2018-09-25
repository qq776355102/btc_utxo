package com.cmc.json;


import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.cmc.exception.MedicalDebugException;
import com.cmc.util.StringUtils;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;
import net.sf.json.util.PropertySetStrategy;


public class JsonPluginsUtil
{
    private static final Logger logger = LoggerFactory.getLogger(JsonPluginsUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();
    static
    {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static JsonConfig config = new JsonConfig();
    static
    {
        PropertyFilter filter = new PropertyFilter()
        {
            public boolean apply(Object object, String fieldName, Object fieldValue)
            {
                return null == fieldValue;
            }
        };
        config.setJsonPropertyFilter(filter);
        config.setAllowNonStringKeys(false);
        config.registerJsonValueProcessor(Date.class,
            new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.registerJsonValueProcessor(Timestamp.class,
            new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        config.setPropertySetStrategy(new PropertyStrategyWrapper(PropertySetStrategy.DEFAULT));

        String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
        MorpherRegistry mr = JSONUtils.getMorpherRegistry();
        mr.registerMorpher(new DateMorpher(formats));
        mr.registerMorpher(new TimestampToDateMorpher(formats));
    }

    public static <T> T jsonToBean(String jsonString, Class<T> beanCalss)
        throws MedicalDebugException
    {
        if (StringUtils.isEmpty(jsonString))
        {
            return null;
        }
        return jsonToBean(JSONObject.fromObject(jsonString), beanCalss);
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(JSONObject json, Class<T> beanCalss)
        throws MedicalDebugException
    {
        try
        {
            config.setRootClass(beanCalss);
            return (T)JSONObject.toBean(json, config);
        }
        catch (Exception e)
        {
            logger.error("jsonToBean error,", e);
            throw new MedicalDebugException("jsonToBean error" + e);
        }
    }

    public static <T> T jsonToBean(String jsonString, Class<T> beanCalss,
                                   Map<String, Class> classMap)
        throws MedicalDebugException
    {
        if (StringUtils.isEmpty(jsonString))
        {
            return null;
        }
        return jsonToBean(JSONObject.fromObject(jsonString), beanCalss,classMap);
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(JSONObject json, Class<T> beanCalss,
                                   Map<String, Class> classMap)
        throws MedicalDebugException
    {
        try
        {
            config.setRootClass(beanCalss);
            config.setClassMap(classMap);
            return (T)JSONObject.toBean(json, config);
        }
        catch (Exception e)
        {
            logger.error("jsonToBean error,", e);
            throw new MedicalDebugException("jsonToBean error" + e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanList(String jsonStr, Class<?> elementClasses,Map<String, Class> classMap)
        throws MedicalDebugException
    {
        config.setRootClass(elementClasses);
        config.setClassMap(classMap);
        return (T)JSONArray.toCollection(JSONArray.fromObject(jsonStr), config);
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanList(JSONArray jSONArray, Class<?> elementClasses,Map<String, Class> classMap)
        throws MedicalDebugException
    {
        config.setRootClass(elementClasses);
        config.setClassMap(classMap);
        return (T)JSONArray.toCollection(jSONArray, config);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanList(String jsonStr, Class<?> elementClasses)
        throws MedicalDebugException
    {
        config.setRootClass(elementClasses);
        return (T)JSONArray.toCollection(JSONArray.fromObject(jsonStr), config);
    }

    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanList(JSONArray jSONArray, Class<?> elementClasses)
        throws MedicalDebugException
    {
        config.setRootClass(elementClasses);
        return (T)JSONArray.toCollection(jSONArray, config);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] jsonArrayToArray(JSONArray jSONArray, Class<?> elementClasses)
        throws MedicalDebugException
    {
        List<T> k = JsonPluginsUtil.jsonToBeanList(jSONArray, elementClasses);
        T[] dest = (T[])Array.newInstance(elementClasses, k.size());
        return k.toArray(dest);
    }

    public static String beanToJson(Object bean)
        throws MedicalDebugException
    {
    	return beanToJson(bean, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static String beanToJson(Object bean, String dateFomart)
        throws MedicalDebugException
    {
    	if(StringUtils.isEmpty(dateFomart)){
    		dateFomart = "yyyy-MM-dd HH:mm:ss";
    	}
    	config.registerJsonValueProcessor(Date.class,
    		new DateJsonValueProcessor(dateFomart));
    	config.registerJsonValueProcessor(Timestamp.class,
    		new DateJsonValueProcessor(dateFomart));
        if (bean instanceof List)
        {
            return JSONArray.fromObject(bean, config).toString();
        }
        return JSONObject.fromObject(bean, config).toString();
    }

    public static void main(String[] args)
        throws MedicalDebugException
    {
        JSONArray jSONArray = new JSONArray();
        jSONArray.add(1);
        jSONArray.add(2);
        Integer[] ids = jsonArrayToArray(jSONArray, Integer.class);
        System.out.println(ids[0]);

    }

}

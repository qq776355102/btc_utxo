/**
 * Project Name:2mobo_common File Name:TimestampToDateMorpher.java Package
 * Name:cn.mobo.thirdPage.json Date:2017年6月29日下午7:58:39 Copyright (c) 2017, mjiang@2mobo.cn All
 * Rights Reserved.
 */

package com.cmc.json;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;


/**
 * ClassName:TimestampToDateMorpher <br/> Function: TODO ADD FUNCTION. <br/> Reason: TODO ADD
 * REASON. <br/> Date: 2017年6月29日 下午7:58:39 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.7
 * @see
 */
public class TimestampToDateMorpher extends AbstractObjectMorpher
{

    /*** 日期字符串格式 */
    private String[] formats;

    public TimestampToDateMorpher(String[] formats)
    {
        this.formats = formats;
    }

    public Object morph(Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (Timestamp.class.isAssignableFrom(value.getClass()))
        {
            return (Timestamp)value;
        }
        if (!supports(value.getClass()))
        {
            throw new MorphException(value.getClass() + " 是不支持的类型");
        }
        String strValue = (String)value;
        SimpleDateFormat dateParser = null;
        for (int i = 0; i < formats.length; i++ )
        {
            if (null == dateParser)
            {
                dateParser = new SimpleDateFormat(formats[i]);
            }
            else
            {
                dateParser.applyPattern(formats[i]);
            }
            try
            {
                return new Timestamp(dateParser.parse(strValue.toLowerCase()).getTime());
            }
            catch (ParseException e)
            {
                // e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Class morphsTo()
    {
        return Timestamp.class;
    }

    public boolean supports(Class clazz)
    {
        return String.class.isAssignableFrom(clazz);
    }

}

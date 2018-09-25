package com.cmc.controller;


import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cmc.core.YunLianMethodAnnotation;
import com.cmc.core.YunLianParamValidateAnnotation;
import com.cmc.exception.MedicalDebugException;
import com.cmc.exception.MedicalException;
import com.cmc.util.StringUtils;
import net.sf.json.JSONObject;


/**
 * @author mc
 * 
 */
@Aspect
@Component
//@Transactional(rollbackFor = Exception.class)
public class TreatServiceAspect
{
    private static final Logger logger = LoggerFactory.getLogger(TreatServiceAspect.class);

    @Pointcut("@annotation(com.cmc.core.YunLianMethodAnnotation),@execution(com.cmc.controller.treatService..*..*.*(..))")
    public void serviceAspect()
    {}

    @Before("serviceAspect()")
    public void doBefore(JoinPoint joinPoint)
        throws MedicalDebugException, ClassNotFoundException, MedicalException, Exception
    {
        JSONObject postRequ = null;
        JSONObject devBase = null;
        Object[] arguments = joinPoint.getArgs();
        if (arguments != null)
        {
            postRequ = (JSONObject)(arguments[0]);
            devBase = (JSONObject)(arguments[1]);
        }

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        YunLianMethodAnnotation yunLianMethodAnnotation = null;
        YunLianParamValidateAnnotation yunLianParamValidateAnnotation = null;
        String logDesc = "";
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length)
                {
                    yunLianMethodAnnotation = method.getAnnotation(YunLianMethodAnnotation.class);
                    yunLianParamValidateAnnotation = method.getAnnotation(
                        YunLianParamValidateAnnotation.class);
                    if (yunLianMethodAnnotation != null)
                    {
                        logDesc = yunLianMethodAnnotation.getMethodSimpDesc();
                    }

                    if (yunLianParamValidateAnnotation == null)
                    {
                        break;
                    }
                    String[] dNotNullNames = yunLianParamValidateAnnotation.getDNotNullNames().split(
                        ",");
                    if (dNotNullNames != null && dNotNullNames.length > 0)
                    {
                        for (String parmName : dNotNullNames)
                        {
                            if (!StringUtils.isEmpty(parmName) && !devBase.has(parmName))
                            {
                                throw new MedicalDebugException(
                                    "request param:" + parmName + " is a must!");
                            }
                        }
                    }
                    String[] pNotNullNames = yunLianParamValidateAnnotation.getPNotNullNames().split(
                        ",");
                    if (pNotNullNames != null && pNotNullNames.length > 0)
                    {
                        for (String parmName : pNotNullNames)
                        {
                            if (!StringUtils.isEmpty(parmName) && !postRequ.has(parmName))
                            {
                                throw new MedicalDebugException(
                                    "request param:" + parmName + " is a must!");
                            }
                        }
                    }
                }
            }
        }
        //操作日志  example
        //TO-DO
    }

    // 操作日志记录
    @Async
    private void doBeforAsyncHandle(JSONObject postRequ, JSONObject devBase, String logDesc)
        throws Exception
    {
    	//TO-DO
    }

    /**
     * 记录主要功能的核心数据，用于数据统计和分析 01/02/03 ---019999记录功能块下每一步操作日志
     * 
     * @param joinPoint
     * @throws MedicalDebugException
     * @throws ClassNotFoundException
     */
    @AfterReturning(pointcut = "serviceAspect()", returning = "retValue")
    public void doAfter(JoinPoint joinPoint, Object retValue)
    {
        //TO-DO
    }

    @Async
    private void doAfterAsyncHandle(JSONObject postRequ, JSONObject devBase, Object retValue)
        throws Exception
    {
    	//TO-DO
    }
}

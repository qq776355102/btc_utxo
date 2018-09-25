package com.cmc.controller.appservices;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.controller.AdapterService;
import com.cmc.core.Constant;
import com.cmc.exception.MedicalDebugException;
import com.cmc.exception.MedicalException;
import com.cmc.model.RequestDTO;
import com.cmc.model.ResponseDTO;

import net.sf.json.JSONObject;


@RestController
@RequestMapping(value = "/cmc/a")
public class AllInAction extends BaseAction
{
    private static final Logger logger = LoggerFactory.getLogger(AllInAction.class);

    @Autowired
    private AdapterService adapterService;

    @RequestMapping(value = "/i")
    public Object main(RequestDTO requestDTO, HttpServletRequest request,
                       HttpServletResponse response)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        try
        {
            // 解析请求内容
            String requestStr = parseRequest();
            logger.debug("request URI=:" + request.getQueryString() + ",content=" + requestStr);
            JSONObject requestJSON = JSONObject.fromObject(requestStr);
            requestDTO.setJsonParam(requestJSON);
            // 处理
            responseDTO = adapterService.invoke(requestDTO, request);
        }
        catch (MedicalDebugException e1)
        {
            logger.error("invoke error:" + request.getQueryString(), e1);
            String message = e1.getMessage();
            if (!"debug".equals(Constant.SYSTEM_DEPLOY_ENV))
            {
                message = "系统异常,请稍后重试！";
            }
            responseDTO.setErrorinfo(message);
        }
        catch (MedicalException e2)
        {
            logger.error("invoke error:" + request.getQueryString(), e2);
            responseDTO.setErrorinfo(e2.getMessage());
        }
        catch (Exception e3)
        {
            logger.error("invoke error:" + request.getQueryString(), e3);
            responseDTO.setErrorinfo("系统异常,请稍后重试！");
        }
        // 格式化响应内容
//        formatReponse(responseDTO);
        return responseDTO;
    }
}

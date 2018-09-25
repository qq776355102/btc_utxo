package com.cmc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

public class ResponseDTO {
	public interface Basic{};
	@JsonView(ResponseDTO.Basic.class)
	private boolean flag = false;
	

	@JsonView(ResponseDTO.Basic.class)
	private ErrorInfo errorinfo;
	
	@SuppressWarnings("rawtypes")
	@JsonView(ResponseDTO.Basic.class)
	private List result;
	
//	@JsonIgnore(true)
//	private Integer pageTotal;
	
	private String status;
	
	public ResponseDTO setErrorinfo(String message, String... code) {
		status = "0";
		errorinfo = new ErrorInfo();
		errorinfo.setMessage(message);
		if (code.length > 0) {
			errorinfo.setCode(code[0]);
		}
		return this;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseDTO setResult(Object data) {
		this.result = new ArrayList();
		flag = true;
		if (data != null) {
			if (data instanceof Collection) {
				this.result.addAll((Collection)data);
			} else {
				this.result.add(data);
			}
		}
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDTO setStaticResult(Object data) {
		ResponseDTO dto = new ResponseDTO();
		dto.result = new ArrayList();
		dto.flag = true;
		dto.status = "1";
		if (data != null) {
			if (data instanceof Collection) {
				dto.result.addAll((Collection)data);
			} else {
				dto.result.add(data);
			}
		}
		return dto;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ResponseDTO setStaticResult(Object data,Integer pageTotal) {
        ResponseDTO dto = new ResponseDTO();
//        dto.setPageTotal(pageTotal);
        dto.result = new ArrayList();
        dto.flag = true;
        if (data != null) {
            if (data instanceof Collection) {
                dto.result.addAll((Collection)data);
            } else {
                dto.result.add(data);
            }
        }
        return dto;
    }

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ErrorInfo getErrorinfo() {
		return errorinfo;
	}

	@SuppressWarnings("rawtypes")
	public List getResult() {
		return result;
	}


//	public Integer getPageTotal() {
//		return pageTotal;
//	}
//
//
//	public void setPageTotal(Integer pageTotal) {
//		this.pageTotal = pageTotal;
//	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
}

package com.cmc.controller.treatService.btc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.controller.BusinessService;

import com.cmc.core.YunLianMethodAnnotation;
import com.cmc.model.ResponseDTO;
import com.cmc.utxo.optimalutxo.BitUnpent;
import com.cmc.utxo.service.impl.UtxoServiceImpl;

import net.sf.json.JSONObject;

@RestController(value = "010102")
public class Balance extends BusinessService {

	/**
	 * 访问url:
	 * http://localhost:8080/cmc/a/i?v=5400&a=01010201&t
	 * =abc
	 * 
	 * 参数demo:
	 *    { 
	 *      "PostRequ":{
	 *      		...
	 *      }, 
	 *      "DevBase":{ 
	 *      	  "username":"mc",
	 *            "password":123456, 
	 *            "userId":1,
	 *            "token":"abc"
	 *            .... 
	 *      } 
	 *    }
	 * @param postRequ
	 * @param devBase
	 * @return
	 */
	
	@Autowired
	private UtxoServiceImpl utxoService;
	
	
	
	@YunLianMethodAnnotation(getMethodSimpCode = "01010201", getMethodSimpDesc = "查询余额", getAppPerm = 1)
	public Object post(JSONObject postRequ, JSONObject devBase) {
		String address = "";
		if (devBase.containsKey("address")) {
			 address = devBase.getString("address");
		}else {
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setErrorinfo("devbase缺少参数address", "4");
			return responseDTO;
		}
		String balance = utxoService.getAvailableBanlance(address);
		return ResponseDTO.setStaticResult(balance);
	}
	
	@YunLianMethodAnnotation(getMethodSimpCode = "01010202", getMethodSimpDesc = "花费余额", getAppPerm = 1)
	public Object get(JSONObject postRequ, JSONObject devBase) throws Exception {
		String address = postRequ.getString("address");
		String amount = postRequ.getString("amount");
		String rate = "2";
		if (postRequ.containsKey("rate")) {
			rate = postRequ.getString("rate");
		}else {
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setErrorinfo("缺少参数rate", "4");
			return responseDTO;
		} 
		List<BitUnpent> unpentList = utxoService.getOptimalUtxo(address, amount,rate);
		return ResponseDTO.setStaticResult(unpentList);
	}

}

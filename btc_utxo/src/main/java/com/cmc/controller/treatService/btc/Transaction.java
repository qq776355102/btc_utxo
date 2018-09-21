package com.cmc.controller.treatService.btc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.controller.BusinessService;
import com.cmc.core.YunLianMethodAnnotation;
import com.cmc.exception.MedicalException;
import com.cmc.model.ResponseDTO;
import com.cmc.utxo.rpc.Rpc;
import com.cmc.utxo.rpc.TestRpc;
import com.cmc.utxo.service.impl.UtxoServiceImpl;

import net.sf.json.JSONObject;

@RestController(value = "010101")
public class Transaction extends BusinessService{

	@Autowired
	private UtxoServiceImpl utxoService;
	
	
	@YunLianMethodAnnotation(getMethodSimpCode = "01010101", getMethodSimpDesc = "广播交易", getAppPerm = 1)
	public Object put(JSONObject postRequ, JSONObject devBase) throws MedicalException {
		String txid = "";	
		if (!postRequ.containsKey("contractId")) {
			throw new MedicalException("Missing parameters : <contractId>");
		}
		if (postRequ.containsKey("signedhex")) {
			JSONObject rs = null;
			//生产环境打开
			if (devBase.containsKey("debug")) {
				txid = TestRpc.sendRawtransaction(postRequ.getString("signedhex"));
			}else {
				
				txid = Rpc.sendRawtransaction(postRequ.getString("signedhex"));
			}
			rs = JSONObject.fromObject(txid);
			System.err.println(postRequ.getString("signedhex"));
			if (rs.containsKey("error")&& rs.get("error")!= null && rs.getString("result") == "null") {
				ResponseDTO responseDTO =	new ResponseDTO();
				responseDTO.setErrorinfo(rs.getJSONObject("error").getString("message"), rs.getJSONObject("error").getString("code"));
				return responseDTO;
			}else {
				
				txid = rs.getString("result");
			}
		} else {
			ResponseDTO responseDTO =	new ResponseDTO();
			responseDTO.setErrorinfo("Missing parameters : <signedhex>", "111");
			return responseDTO;
		}
		//修改数据状态
		if (txid != null && txid != "") {
			utxoService.updateUtxoStatusByBroadcastTransaction(txid,devBase.getString("address"),postRequ.getString("contractId"));
		}
		return ResponseDTO.setStaticResult(txid);
	}

}

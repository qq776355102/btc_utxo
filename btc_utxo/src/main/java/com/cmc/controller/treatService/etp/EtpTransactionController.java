package com.cmc.controller.treatService.etp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.controller.BusinessService;
import com.cmc.core.YunLianMethodAnnotation;
import com.cmc.exception.MedicalException;
import com.cmc.model.ResponseDTO;
import com.cmc.utxo.etp.service.MvsUtxoServiceImpl;
import com.cmc.utxo.rpc.MvsRpc;

import net.sf.json.JSONObject;

@RestController("020101")
public class EtpTransactionController extends BusinessService{

	@Autowired
	private MvsUtxoServiceImpl mvsUtxoService;
	
	
	@YunLianMethodAnnotation(getMethodSimpCode = "02010101", getMethodSimpDesc = "广播元界的交易", getAppPerm = 1)
	public Object broadcastTransaction(JSONObject postRequ, JSONObject devBase) throws MedicalException {
		String txid = "";		
		if (!postRequ.containsKey("contractId")) {
			throw new MedicalException("Missing parameters : <contractId>");
		}
		if (postRequ.containsKey("signedhex")) {
			JSONObject rs = null;
			txid = MvsRpc.sendRawtransaction(postRequ.getString("signedhex"));
			rs = JSONObject.fromObject(txid);
			System.err.println(postRequ.getString("signedhex"));
			if (rs.containsKey("error")&& rs.get("error")!= null ) {
				ResponseDTO responseDTO =	new ResponseDTO();
				responseDTO.setErrorinfo(rs.getJSONObject("error").getString("message"), rs.getJSONObject("error").getString("code"));
				System.err.println(txid);
				return responseDTO;
			}else {
				txid = rs.getString("result");
				System.err.println(txid);
			}
		} else {
			ResponseDTO responseDTO =	new ResponseDTO();
			responseDTO.setErrorinfo("Missing parameters : <signedhex>", "111");
			return responseDTO;
		}
		//修改数据状态
		if (txid != null && txid != "") {
			mvsUtxoService.updateUtxoStatusByBroadcastTransaction(txid,devBase.getString("address"),postRequ.getString("contractId"));
		}
		return ResponseDTO.setStaticResult(txid);
	}
}

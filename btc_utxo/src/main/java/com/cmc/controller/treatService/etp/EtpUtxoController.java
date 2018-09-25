package com.cmc.controller.treatService.etp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.controller.BusinessService;
import com.cmc.core.YunLianMethodAnnotation;
import com.cmc.exception.MedicalException;
import com.cmc.model.ResponseDTO;
import com.cmc.utxo.etp.model.vo.MvsTransUtxoVo;
import com.cmc.utxo.etp.service.MvsUtxoServiceImpl;

import net.sf.json.JSONObject;

@RestController("020102")
public class EtpUtxoController extends BusinessService {
	/*http://localhost:8080/cmc/a/i?v=2100&a=02010201
	{"DevBase":{"address":"MTQsZC8Tw6f4umbfTtXAAkZRpFnwDsJ33U","debug":true,"os":"android"},"PostRequ":{"symbol":"MVS.ZGC","amount":"0.0001","address":"MTQsZC8Tw6f4umbfTtXAAkZRpFnwDsJ33U"}}*/
	@Autowired
	private MvsUtxoServiceImpl mvsUtxoService;

	@YunLianMethodAnnotation(getMethodSimpCode = "02010201", getMethodSimpDesc = "花费余额", getAppPerm = 1)
	public Object huaFeiYue(JSONObject postRequ, JSONObject devBase) throws MedicalException {
		if (postRequ.getString("address") == null) {
			throw new MedicalException("缺少参数address");
		}
		if (postRequ.getString("amount") == null) {
			throw new MedicalException("缺少参数amount");
		}
		if (postRequ.getString("symbol") == null) {
			throw new MedicalException("缺少参数symbol");
		}
		List<MvsTransUtxoVo> mvsUtxos = mvsUtxoService.getMvsUtxosByAddress(postRequ.getString("address"),
				postRequ.getString("amount"), postRequ.getString("symbol"));
		return ResponseDTO.setStaticResult(mvsUtxos);
	}
}

//package com.cmc.utxo.controller;
//
//import java.util.List;
//
//import org.bitcoinj.core.Address;
//import org.bitcoinj.core.ECKey;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cmc.utxo.optimalutxo.BitUnpent;
//import com.cmc.utxo.rpc.Rpc;
//import com.cmc.utxo.service.impl.UtxoServiceImpl;
//import com.cmc.utxo.util.Result;
//
//import net.sf.json.JSONObject;
//
//@RestController
//@RequestMapping("appservice/v1/btc")
//public class BtcController {
//
//	@Autowired
//	private UtxoServiceImpl utxoService;
//
//	// 花费余额接口
//	/**
//	 * @param address
//	 * @param amount
//	 * @param signature
//	 * @param isDebug
//	 * @return
//	 * @throws Exception
//	 */
//	@PostMapping("spendingBal")
//	public Object getOptimalUtxo(@RequestBody JSONObject parameters) throws Exception {
//		// 参数
//		String address = null;
//		String amount = null;
//		List<BitUnpent> unpentList = null;
//		String signature = null;
//		boolean isDebug = false;
//		String parameterString = null;
//		try {
//			JSONObject params = parameters.getJSONObject("requestDTO");
//			parameterString = params.toString();
//			address = params.getString("address");
//			amount = params.getString("amount");
//
//		} catch (Exception e) {
//			return new Result(false, "The parameter is invalid");
//		}
//		boolean b = false;
//		// 测试环境
//		if (parameters.containsKey("isDebug")) {
//			isDebug = parameters.getBoolean("isDebug");
//			if (isDebug) {
//				unpentList = utxoService.getOptimalUtxo(address, amount);
//			}
//		} else {
//			// 正式环境
//			try {
//				signature = parameters.getString("signature");
//				b = isValidSignature(address, signature, parameterString);
//				if (b) {
//					unpentList = utxoService.getOptimalUtxo(address, amount);
//				} else {
//					return new Result(false, "Account is not secure");
//				}
//			} catch (Exception e) {
//				return new Result(false, "Missing signature");
//			}
//		}
//
//		return Result.setData(unpentList);
//
//	}
//
//	// 查询余额
//	@GetMapping("balance")
//	public Object getBalance(@RequestParam(name="address", required = true)String address) {
//			return Result.setData(utxoService.getAvailableBanlance(address));
//	}
//
//	// 广播交易
//	@PostMapping("broadcastTrans")
//	public Object broadcastTransaction(@RequestBody JSONObject parameters) {
//		String txid = "";
//		JSONObject requestDTO = parameters.getJSONObject("requestDTO");
//		if (requestDTO.containsKey("signedhex")) {
//			JSONObject rs = null;
//			txid = Rpc.sendRawtransaction(requestDTO.getString("signedhex"));
//			rs = JSONObject.fromObject(txid);
//			if (rs.containsKey("error")) {
//				return new Result(false, JSONObject.fromObject(txid).getJSONObject("error").getString("message"));
//			}else {
//				txid = rs.getString("result");
//			}
//		} else {
//			return new Result(false, "Missing parameters : <signedhex>");
//		}
//		//修改数据状态
//		if (!txid.isEmpty()&&txid != null) {
//			//生产环境打开
////			utxoService.updateUtxoStatusByBroadcastTransaction(txid,address);
//		}
//		return Result.setData(txid);
//	}
//
//	public static boolean isValidSignature(String address, String signature, String parameters) {
//		try {
//			return ECKey.signedMessageToKey(parameters, signature)
//					.toAddress(Address.fromBase58(null, address).getParameters()).toString().equals(address);
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	public static void main(String[] args) {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("fromAddress", "gLc8Dgj88nHWy7MmLjXA5y7yWF6FNwAEtn");
//		jsonObject.put("amount",900000 );
//		boolean validSignature = isValidSignature("1K8rTThJHVfVFHdvjAFYZj473EvY6S2oqM", "H93HyKDkRtCdo3H0n9gGk0XjegmgPekm878ADzQVn8wIOpnFins4lA899BvCPpPt/7NNM7t4tJKDTpNjQr+SyYs=", jsonObject.toString());
//		System.out.println(validSignature);
//	}
//
//}

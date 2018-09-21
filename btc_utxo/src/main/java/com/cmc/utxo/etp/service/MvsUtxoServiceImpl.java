package com.cmc.utxo.etp.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.print.attribute.standard.MediaName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmc.core.Constant;
import com.cmc.exception.MedicalException;
import com.cmc.utxo.etp.mapper.MvsUtxoMapper;
import com.cmc.utxo.etp.mapper.TokenUnitMapper;
import com.cmc.utxo.etp.model.po.MvsUtxo;
import com.cmc.utxo.etp.model.po.TokenUnit;
import com.cmc.utxo.etp.model.vo.MvsTransUtxoVo;
import com.cmc.utxo.etp.optimalMvs.MvsOptimalUtxo;
import com.cmc.utxo.etp.optimalMvs.MvsTokenOptimalUtxo;
import com.cmc.utxo.rpc.HttpPostRequest;
import com.cmc.utxo.rpc.MvsRpc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class MvsUtxoServiceImpl {

	@Autowired
	private MvsUtxoMapper mapper;

	@Autowired
	private TokenUnitMapper tokenUnitMapper;

	/**
	 * 
	 * 获取最佳utxo
	 * 
	 * @param address
	 * @throws MedicalException 
	 */
	public List<MvsTransUtxoVo> getMvsUtxosByAddress(String address, String amount, String symbol) throws MedicalException {
		MvsOptimalUtxo mvsOptimalUtxo = new MvsOptimalUtxo();
		// 未花费EtpUtxo输出
		List<MvsTransUtxoVo> mvsTransUtxoVoS = new ArrayList<MvsTransUtxoVo>();
		// 要花费的资产
		MvsUtxo toSendAmount = new MvsUtxo();
		toSendAmount.setAddress(address);
		// 可用余额状态
		Integer[] availableBanlance = { Constant.UTXO_STATUS_0, Constant.UTXO_STATUS_3 };
		ArrayList<MvsUtxo> mvsUtxos = mapper.selectByAddress(address, "ETP", availableBanlance);
		if (mvsUtxos != null) {
			mvsOptimalUtxo.setMvsUnSpentList(mvsUtxos);
		} else {
			return Collections.emptyList();
		}
		toSendAmount.setAmount(amount);
		boolean b = true;
		if (!symbol.toUpperCase().equals("ETP")) {// 非etp转账
			ArrayList<MvsUtxo> mu = mapper.selectByAddress(address, symbol, availableBanlance);
			MvsTokenOptimalUtxo mvsTokenOptimalUtxo = new MvsTokenOptimalUtxo();
			mvsTokenOptimalUtxo.setMvsUnSpentOfTokenList(mu);
			mvsTokenOptimalUtxo.setToSendAmountOfToken(toSendAmount);
			b = mvsTokenOptimalUtxo.getmvsTokenOptimalUtxo();
			if (b) {
				TokenUnit tokenUnit = tokenUnitMapper.selectBySymbol(symbol);
				if (tokenUnit == null) {
					tokenUnit = new TokenUnit();
					tokenUnit.setDecimals(8);
				}
				Integer unit = tokenUnit.getDecimals();
				List<MvsUtxo> toSendUnpentOfTokenList = mvsTokenOptimalUtxo.getToSendUnpentOfTokenList();
				for (MvsUtxo mvsu : toSendUnpentOfTokenList) {
					MvsTransUtxoVo mvsTransUtxoVo = new MvsTransUtxoVo();
					mvsTransUtxoVo.setAddress(address);
					mvsTransUtxoVo.setAmount(new BigDecimal(mvsu.getAmount())
							.multiply(BigDecimal.valueOf(Math.pow(10, unit))).toPlainString());
					mvsTransUtxoVo.setSymbol(symbol);
					mvsTransUtxoVo.setTokenUnit(unit.toString());
					mvsTransUtxoVo.setTxid(mvsu.getHash());
					mvsTransUtxoVo.setVout(mvsu.getOutindex());
					mvsTransUtxoVoS.add(mvsTransUtxoVo);
				}
			}
			toSendAmount.setAmount("0");
		}
		mvsOptimalUtxo.setToSendAmount(toSendAmount);
		if (!b) return mvsTransUtxoVoS = Collections.emptyList();
		b = mvsOptimalUtxo.getmvsOptimalUtxo();
		if (b) {
			List<MvsUtxo> toSendUnpentList = mvsOptimalUtxo.getToSendUnpentList();
			for (MvsUtxo mvsUtxo : toSendUnpentList) {
				MvsTransUtxoVo mvsTransUtxoVo = new MvsTransUtxoVo();
				mvsTransUtxoVo.setAddress(address);
				mvsTransUtxoVo.setAmount(
						new BigDecimal(mvsUtxo.getAmount()).multiply(BigDecimal.valueOf(1e8)).toPlainString());
				mvsTransUtxoVo.setTxid(mvsUtxo.getHash());
				mvsTransUtxoVo.setVout(mvsUtxo.getOutindex());
				mvsTransUtxoVo.setSymbol("ETP");
				mvsTransUtxoVo.setTokenUnit("8");
				mvsTransUtxoVoS.add(mvsTransUtxoVo);
			}
		} else {
			mvsTransUtxoVoS = Collections.emptyList();
		}
		return mvsTransUtxoVoS;
		// 可用余额列表
	}

	// 广播交易之后数据库数据状态
	public void updateUtxoStatusByBroadcastTransaction(String txid, String address, String contractId) {
		String decodeTransaction = MvsRpc.getRawtransaction(txid);
		JSONObject decodeTransactionJson = JSONObject.fromObject(decodeTransaction).getJSONObject("result");
		JSONArray inputsJson = decodeTransactionJson.getJSONArray("inputs");
		for (Object inputObject : inputsJson) {
			JSONObject vinJosn = JSONObject.fromObject(inputObject);
			JSONObject previous_output = vinJosn.getJSONObject("previous_output");
			MvsUtxo mvsUtxo = mapper.selectByHashAndIndex(previous_output.getString("hash"),
					previous_output.getString("index"));
			if (mvsUtxo.getStatus() == 0) {
				mvsUtxo.setStatus(2);
			}
			if (mvsUtxo.getStatus() == 3) {
				mvsUtxo.setStatus(4);
			}
			mapper.updateByPrimaryKeySelective(mvsUtxo);
		}
		JSONArray outputsJson = decodeTransactionJson.getJSONArray("outputs");
		JSONArray adressAndSymbolBalance = new JSONArray();
		for (Object outputObject : outputsJson) {
			JSONObject outJson = JSONObject.fromObject(outputObject);
			MvsUtxo mvsUtxo = new MvsUtxo();
			String outAddress = outJson.getString("address");
			String outType = outJson.getJSONObject("attachment").getString("type");
			if (outAddress.equals(address)) {
				mvsUtxo.setHash(decodeTransactionJson.getString("hash"));
				mvsUtxo.setAddress(address);
				mvsUtxo.setOutindex(outJson.getInt("index"));
				mvsUtxo.setStatus(3);
				mvsUtxo.setScript(outJson.getString("script"));
				if (outType.equals("etp")) {
					String amount = new BigDecimal(outJson.getString("value")).divide(BigDecimal.valueOf(1e8))
							.toPlainString();
					mvsUtxo.setSymbol("ETP");
					mvsUtxo.setAmount(amount);
					mvsUtxo.setType("etp");
					adressAndSymbolBalance.add("ETP");
				} else if (outType.equals("asset-transfer")) {// 代币资产转账交易
					String symbol = outJson.getJSONObject("attachment").getString("symbol");
					TokenUnit tokenUnit = tokenUnitMapper.selectBySymbol(symbol);
					int unit = tokenUnit.getDecimals();
					if (symbol.equals("MVS.CMC")) {
						unit = 4;
					}
					String quantity = outJson.getJSONObject("attachment").getString("quantity");
					System.err.println("金额之前 =" + quantity);
					String amount = new BigDecimal(quantity).divide(BigDecimal.valueOf(Math.pow(10, unit)))
							.toPlainString();
					System.err.println("金额除以10e8之后" + amount);
					mvsUtxo.setAmount(amount);
					mvsUtxo.setSymbol(symbol);
					mvsUtxo.setType("asset-transfer");
					adressAndSymbolBalance.add(symbol);
				} else if (outType.equals("asset-issue")) { // 资产发布
					String symbol = outJson.getJSONObject("attachment").getString("symbol");
					int unit = outJson.getJSONObject("attachment").getInt("decimal_number");
					String quantity = outJson.getJSONObject("attachment").getString("quantity");
					String amount = new BigDecimal(quantity).divide(BigDecimal.valueOf(Math.pow(10, unit)))
							.toPlainString();
					mvsUtxo.setSymbol(symbol);
					mvsUtxo.setAmount(amount);
					mvsUtxo.setType("asset-issue");
					adressAndSymbolBalance.add(symbol);
				}
				mapper.insertSelective(mvsUtxo);
			}
		}
		TopcfMvsBanlance(address, contractId, adressAndSymbolBalance);
	}

	@Async
	private void TopcfMvsBanlance(String address, String contractId, JSONArray adressAndSymbolBalance) {
		// 这个发给pcf
		JSONArray balances = new JSONArray();
		for (Object symbolObject : adressAndSymbolBalance) {
			System.err.println("符号" +symbolObject.toString());
			JSONObject tokenBalance = new JSONObject();
			tokenBalance.put("address", address);
			BigDecimal totalAmount = BigDecimal.ZERO;
			Integer[] isused = { Constant.UTXO_STATUS_0, Constant.UTXO_STATUS_3 };
			ArrayList<MvsUtxo> selectByAddress = mapper.selectByAddress(address, symbolObject.toString(), isused);
			if (symbolObject.toString().equals("ETP")) {
				tokenBalance.put("contractid", "ed35e5c2-aedc-409c-aaa3-b2947537e7eb");
				for (MvsUtxo mu : selectByAddress) {
					totalAmount = totalAmount.add(new BigDecimal(mu.getAmount()));
				}
				tokenBalance.put("value", totalAmount.toPlainString());
			} else {
				tokenBalance.put("contractid", contractId);
				for (MvsUtxo mu : selectByAddress) {
					totalAmount = totalAmount.add(new BigDecimal(mu.getAmount()));
				}
				tokenBalance.put("value", totalAmount.toPlainString());
			}
			balances.add(tokenBalance);
		}
		System.err.println("pcf 余额"+balances.toString());
		HttpPostRequest.sendMvsAddressBalance(balances.toString());
	}

}

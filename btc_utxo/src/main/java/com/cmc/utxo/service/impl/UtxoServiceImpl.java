package com.cmc.utxo.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmc.core.Constant;
import com.cmc.exception.MedicalException;
import com.cmc.utxo.mapper.BtcutxoMapper;
import com.cmc.utxo.model.po.Btcutxo;
import com.cmc.utxo.optimalutxo.BitUnpent;
import com.cmc.utxo.optimalutxo.OptimalUtxo;
import com.cmc.utxo.rpc.HttpGetForUnspent;
import com.cmc.utxo.rpc.Rpc;
import com.cmc.utxo.util.ToPcf;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class UtxoServiceImpl {

	@Autowired
	private BtcutxoMapper mapper;
	
	@Autowired
	private ToPcf toPcf;
	
	public List<Btcutxo> selectByAddress(String address,Integer isused) {
		return mapper.selectByAddress(address,isused);
	}
	
	public int insertSelective(Btcutxo btcutxo) {
		return mapper.insert(btcutxo);
	}
	
	public void updateUtxoStatus(String txid,Integer outindex,Integer isused) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("txid", txid);
		param.put("outindex", outindex);
		param.put("isused", isused);
		mapper.updateUtxoStatus(param);
	}
	
	//查询余额
	public String getAvailableBanlance(String address) {
		Integer[] availableBanlance = {Constant.UTXO_STATUS_0,Constant.UTXO_STATUS_3};
		//查询到所有余额
		List<Btcutxo> btcutxoList = mapper.getSpendingBanlance(address, availableBanlance);
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (Btcutxo btcutxo : btcutxoList) {
			BigDecimal value = new BigDecimal(btcutxo.getValue());
			totalAmount = totalAmount.add(value);
		}
		return totalAmount.toString();
	}
	
	//获取最佳utxo
	@SuppressWarnings("deprecation")
	public List<BitUnpent> getOptimalUtxo(String address,String amount,String rate) throws MedicalException  {
		//可用余额状态
		Integer[] availableBanlance = {Constant.UTXO_STATUS_0,Constant.UTXO_STATUS_3};
		//查询到所有余额
		List<Btcutxo> btcutxoList = mapper.getSpendingBanlance(address, availableBanlance);
		//取最优算法的未花费余额
		ArrayList<BitUnpent> bitUnpentList = new ArrayList<BitUnpent>();
//		if (btcutxoList == null || btcutxoList.isEmpty()) {
			//走第三方接口
			try {
				String sendHttpPost = HttpGetForUnspent.sendHttpPost(address);
				JSONObject unspentJson = JSONObject.fromObject(sendHttpPost);
				JSONArray jsonArray = unspentJson.getJSONObject("data").getJSONArray("list");
				if (btcutxoList != null && !btcutxoList.isEmpty()) {
					
					if (jsonArray.size() > btcutxoList.size()) {
						
						for (Object unspentobject : jsonArray) {
							JSONObject unspentJ = JSONObject.fromObject(unspentobject);
							BitUnpent bitUnpent = new BitUnpent();
							String money = unspentJ.getString("value");
							BigDecimal btcAmount = new BigDecimal(money).divide(BigDecimal.valueOf(1e8), 8, RoundingMode.HALF_UP);
							bitUnpent.setAmount(btcAmount.toPlainString());
							bitUnpent.setAddress(address);
							bitUnpent.setTxid(unspentJ.getString("tx_hash"));
							bitUnpent.setVout(unspentJ.getInt("tx_output_n"));
							bitUnpentList.add(bitUnpent);
						}
						
					}else {
						for (Btcutxo btcutxo : btcutxoList) {
							BitUnpent bitUnpent = new BitUnpent();
							bitUnpent.setAmount(btcutxo.getValue());
							bitUnpent.setAddress(address);
							bitUnpent.setTxid(btcutxo.getTxid());
							bitUnpent.setVout(btcutxo.getOutindex());
							bitUnpentList.add(bitUnpent);
						}
					}
					
				}else {
					for (Object unspentobject : jsonArray) {
						JSONObject unspentJ = JSONObject.fromObject(unspentobject);
						BitUnpent bitUnpent = new BitUnpent();
						String money = unspentJ.getString("value");
						BigDecimal btcAmount = new BigDecimal(money).divide(BigDecimal.valueOf(1e8), 8, RoundingMode.HALF_UP);
						bitUnpent.setAmount(btcAmount.toPlainString());
						bitUnpent.setAddress(address);
						bitUnpent.setTxid(unspentJ.getString("tx_hash"));
						bitUnpent.setVout(unspentJ.getInt("tx_output_n"));
						bitUnpentList.add(bitUnpent);
					}
				}
				
				
			} catch (Exception e) {
				
				if (btcutxoList != null && !btcutxoList.isEmpty()) {
					for (Btcutxo btcutxo : btcutxoList) {
						BitUnpent bitUnpent = new BitUnpent();
						bitUnpent.setAmount(btcutxo.getValue());
						bitUnpent.setAddress(address);
						bitUnpent.setTxid(btcutxo.getTxid());
						bitUnpent.setVout(btcutxo.getOutindex());
						bitUnpentList.add(bitUnpent);
					}
				}else {
					return Collections.emptyList();
				}
				
			}
//		}

		

		
		//最优算法
		OptimalUtxo optimalUtxo = new OptimalUtxo();
		optimalUtxo.setRate(rate);
		optimalUtxo.setBitUnpentList(bitUnpentList);
		BitUnpent toSendAmount = new BitUnpent();
		toSendAmount.setAddress(address);
		toSendAmount.setAmount(amount);
		optimalUtxo.setToSendAmount(toSendAmount);
		boolean b = optimalUtxo.getOptimalUtxo();
		List<BitUnpent> toSendUnpentList = Collections.emptyList();
		if (b) {
			List<BitUnpent> bitUnpents  = optimalUtxo.getToSendUnpentList();
			for (BitUnpent bitUnpent : bitUnpents) {
				bitUnpent.setAmount(new BigDecimal(bitUnpent.getAmount()).multiply(BigDecimal.valueOf(1e8)).toPlainString());
			}
			toSendUnpentList = bitUnpents;
		}
		return toSendUnpentList;
	}
	
	//通过广播交易修改utxo数据状态
	@Async
	public void updateUtxoStatusByBroadcastTransaction(String txid,String address,String contractId){
		String decodeTransaction = Rpc.getRawtransaction(txid);
		JSONObject decodeTransactionJson = JSONObject.fromObject(decodeTransaction);
		JSONArray vinArray = decodeTransactionJson.getJSONObject("result").getJSONArray("vin");
		JSONArray voutArray = decodeTransactionJson.getJSONObject("result").getJSONArray("vout");
		for (Object object : voutArray) {
			JSONObject voutJson = JSONObject.fromObject(object);
			JSONObject scriptPubKeyJson = voutJson.getJSONObject("scriptPubKey");
			JSONArray addresses = scriptPubKeyJson.getJSONArray("addresses");
			//判断是否是找零地址
			boolean contains = addresses.contains(address);
			if (contains) {
				Btcutxo btcutxo = new Btcutxo();
				btcutxo.setAddress(address);
				btcutxo.setHash(txid);
				btcutxo.setIsused(3);//新的utxo,未确认
				btcutxo.setTxid(txid);
				btcutxo.setOutindex(voutJson.getInt("n"));
				btcutxo.setValue(voutJson.getString("value"));
				Btcutxo iscunzaiutxo = mapper.selectByTxidAndVout(txid, voutJson.getInt("n"));
				if (iscunzaiutxo == null) {
					mapper.insertSelective(btcutxo);
				}
			}
		}
		for (Object object : vinArray) {
			JSONObject vinJson = JSONObject.fromObject(object);
			Btcutxo btcutxo = mapper.selectByTxidAndVout(vinJson.getString("txid"), vinJson.getInt("vout"));
			if (btcutxo.getIsused()==0) {  
				String td = vinJson.getString("txid");
				int v = vinJson.getInt("vout");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("txid", td);
				param.put("outindex", v);
				param.put("isused", Constant.UTXO_STATUS_2);
				mapper.updateUtxoStatus(param);
			}
			if (btcutxo.getIsused()==3) {  
				String td = vinJson.getString("txid");
				int v = vinJson.getInt("vout");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("txid", td);
				param.put("outindex", v);
				param.put("isused", Constant.UTXO_STATUS_4);
				mapper.updateUtxoStatus(param);
			}
		}
		//提供给彭朝方
		toPcf.SendPcfBtcBalance(address,contractId);
	}
	
}
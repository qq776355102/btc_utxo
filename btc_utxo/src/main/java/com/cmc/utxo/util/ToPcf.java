package com.cmc.utxo.util;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cmc.core.Constant;
import com.cmc.utxo.mapper.BtcutxoMapper;
import com.cmc.utxo.model.po.Btcutxo;
import com.cmc.utxo.rpc.HttpPostRequest;

@Component
public class ToPcf {

	@Autowired
	private BtcutxoMapper mapper;
	
	@Async
	public void SendPcfBtcBalance(String address,String contractId) {
		Integer[] isused = {Constant.UTXO_STATUS_0,Constant.UTXO_STATUS_3};
		List<Btcutxo> btcutxoList = mapper.getSpendingBanlance(address, isused);
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (Btcutxo btcutxo : btcutxoList) {
			String value = btcutxo.getValue();
			totalAmount = totalAmount.add(new BigDecimal(value));
		}
		HttpPostRequest.sendBtcAddressBalance(address, totalAmount.toPlainString(),contractId);
	}
	
}

package com.cmc.utxo.etp.optimalMvs;

import java.math.BigDecimal;
import java.util.Comparator;

import com.cmc.utxo.etp.model.po.MvsUtxo;
import com.cmc.utxo.etp.model.vo.MvsTransUtxoVo;

public class MvsComparatorConfig implements Comparator<MvsUtxo>{

	public int compare(MvsUtxo o1, MvsUtxo o2) {
		return new BigDecimal(o1.getAmount()).compareTo(new BigDecimal(o2.getAmount()));
	}
	
}

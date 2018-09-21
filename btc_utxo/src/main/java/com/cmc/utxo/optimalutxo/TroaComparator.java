package com.cmc.utxo.optimalutxo;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author mc
 *
 * 比特币系列交易优化算法
 */
public class TroaComparator implements Comparator<BitUnpent>{

	public int compare(BitUnpent o1, BitUnpent o2) {
		BigDecimal multiply = new BigDecimal(o1.getAmount()).multiply(BigDecimal.valueOf(1e8));
		BigDecimal multiply2 = new BigDecimal(o2.getAmount()).multiply(BigDecimal.valueOf(1e8));
		return multiply.compareTo(multiply2);
	}
}

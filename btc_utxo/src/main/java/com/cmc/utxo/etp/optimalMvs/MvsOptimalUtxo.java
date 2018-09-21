package com.cmc.utxo.etp.optimalMvs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cmc.utxo.etp.model.po.MvsUtxo;

public class MvsOptimalUtxo {

	// 所有未花费的utxo
	public ArrayList<MvsUtxo> mvsUnSpentList = new ArrayList<MvsUtxo>();
	// 要发送的金额
	private MvsUtxo toSendAmount;
	// 代币单位
	private String tokenUnit;
	// 用于发送资产的utxo
	private List<MvsUtxo> toSendUnpentList = new ArrayList<MvsUtxo>();
	// 旷工费
	private final BigDecimal fee = new BigDecimal("10000");

	public boolean getmvsOptimalUtxo(){
		if (mvsUnSpentList.isEmpty()) {
			return false;
		}
		Collections.sort(mvsUnSpentList, new MvsComparatorConfig());
		BigDecimal targetAmount = new BigDecimal(toSendAmount.getAmount());
		int indexOf = mvsUnSpentList.indexOf(toSendAmount);
		// list中可用utox的index
		// 没有可用的,直接结束
		int size = mvsUnSpentList.size();
		if (indexOf == 1 && size == 2) {
			return false;
		}
		BigDecimal totalRight = BigDecimal.ZERO;
		// 找零
		BigDecimal change = BigDecimal.valueOf(-1);
		if (size > indexOf + 1) {
			for (int j = 1; size > (indexOf + j); j++) {
				toSendUnpentList.add(mvsUnSpentList.get(indexOf + j));
				totalRight = totalRight.add(new BigDecimal(mvsUnSpentList.get(indexOf + j).getAmount()));
				BigDecimal bigFee = totalRight.subtract(targetAmount);
				change = bigFee.multiply(BigDecimal.valueOf(1e8)).subtract(fee);
				if (change.compareTo(BigDecimal.ZERO) < 0) {
					continue;
				} else { // 预留旷工费大于实际扣除的
					// 找零
					if (change.compareTo(BigDecimal.ZERO) >= 0) {
						break;
					}
					continue;
				}
			}
			if (change.compareTo(BigDecimal.ZERO) < 0) {
				toSendUnpentList = Collections.emptyList();
			}
		}else {
			if (indexOf == 0) {
				toSendUnpentList = Collections.emptyList();
				return false;
			}
			BigDecimal totalLeft = new BigDecimal(mvsUnSpentList.get(indexOf-1).getAmount());
			toSendUnpentList.add(mvsUnSpentList.get(indexOf-1));
			for ( int k = 0; k < (indexOf-1) ; k++) {
				MvsUtxo mvsUtxo = mvsUnSpentList.get(k);
				totalLeft = totalLeft.add(new BigDecimal(mvsUtxo.getAmount()));
				toSendUnpentList.add(mvsUnSpentList.get(k));
				BigDecimal bigFee = totalLeft.subtract(targetAmount);
				change = bigFee.multiply(BigDecimal.valueOf(1e8)).subtract(fee);
				if (change.compareTo(BigDecimal.ZERO) < 0) {
					continue;
				} else { // 预留旷工费大于实际扣除的
					// 找零
					if (change.compareTo(BigDecimal.ZERO) >= 0) {
						break;
					}
					continue;
				}
			}
			if (change.compareTo(BigDecimal.ZERO) < 0) {
				toSendUnpentList = Collections.emptyList();
			}
		}
		return true;
	}

	public ArrayList<MvsUtxo> getMvsUnSpentList() {
		return mvsUnSpentList;
	}

	public void setMvsUnSpentList(ArrayList<MvsUtxo> mvsUnSpentList) {
		this.mvsUnSpentList = mvsUnSpentList;
	}

	public MvsUtxo getToSendAmount() {
		return toSendAmount;
	}

	public void setToSendAmount(MvsUtxo toSendAmount) {
		this.toSendAmount = toSendAmount;
		if (mvsUnSpentList!=null) {
			mvsUnSpentList.add(toSendAmount);
		}
	}

	public String getTokenUnit() {
		return tokenUnit;
	}

	public void setTokenUnit(String tokenUnit) {
		this.tokenUnit = tokenUnit;
	}

	public List<MvsUtxo> getToSendUnpentList() {
		return toSendUnpentList;
	}

	public void setToSendUnpentList(ArrayList<MvsUtxo> toSendUnpentList) {
		this.toSendUnpentList = toSendUnpentList;
	}

	@Override
	public String toString() {
		return "MvsOptimalUtxo [mvsUnSpentList=" + mvsUnSpentList + ", toSendAmount=" + toSendAmount + ", tokenUnit="
				+ tokenUnit + ", toSendUnpentList=" + toSendUnpentList + "]";
	}

}

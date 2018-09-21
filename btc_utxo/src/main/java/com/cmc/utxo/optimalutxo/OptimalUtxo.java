package com.cmc.utxo.optimalutxo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cmc.exception.MedicalException;

public class OptimalUtxo {
	//[1] 指定地址所有未花费的utxo 
	public ArrayList<BitUnpent> bitUnpentList = new ArrayList<BitUnpent>();
	//[2] 发件人,必要参数发件地址,发送资产金额
	private  BitUnpent toSendAmount;
	//[3]比特币费率
	private String rate;
	//[4] 用于支付发送资产金额的utxo
	public List<BitUnpent> toSendUnpentList = new ArrayList<BitUnpent>();;
	
	
	public boolean getOptimalUtxo() throws MedicalException{
		if (bitUnpentList.isEmpty()) {
			return false;
		}
		Collections.sort(bitUnpentList, new TroaComparator());
		BigDecimal targetAmount = new BigDecimal(toSendAmount.getAmount());
		int indexOf = bitUnpentList.indexOf(toSendAmount);
		//list中可用utox的index
		//没有可用的,直接结束
		int size = bitUnpentList.size();
		if (indexOf == 1 && size == 2 ) {
			return false;
		}

		
		BigDecimal totalRight = BigDecimal.ZERO;
		//准备支出的零钱减去要花费的金额必须大于旷工费
		BigDecimal change = BigDecimal.valueOf(-1);
		//旷工费
		Integer fee = 0;
		if (size>indexOf+1) {
			for ( int j = 1; size > (indexOf+j); j++) {
				toSendUnpentList.add(bitUnpentList.get(indexOf+j));
				totalRight = totalRight.add(new BigDecimal(bitUnpentList.get(indexOf+j).getAmount()));
				BigDecimal bigFee = totalRight.subtract(targetAmount);
				fee = (148 *(toSendUnpentList.size()) + 78)*Integer.valueOf(rate);
				change = bigFee.multiply(BigDecimal.valueOf(1e8)).subtract(BigDecimal.valueOf(fee));
				//预留看旷工费小于实际要扣除的
				if (change.compareTo(BigDecimal.ZERO) < 0) {
					continue;
				}else {   //预留旷工费大于实际扣除的
					//找零
					if (change.compareTo(BigDecimal.valueOf(452)) >0) {
						break;
					}
					continue;
				}
			}
			if (change.compareTo(BigDecimal.valueOf(452)) <0) {
				toSendUnpentList = Collections.emptyList();
			}
			
			if (change.compareTo(BigDecimal.ZERO) < 0) {
				toSendUnpentList = Collections.emptyList();
			}
		}else {
				if (indexOf == 0) {
					toSendUnpentList = Collections.emptyList();
					return false;
				}
				BigDecimal totalLeft = new BigDecimal(bitUnpentList.get(indexOf-1).getAmount());
				toSendUnpentList.add(bitUnpentList.get(indexOf-1));
				for ( int k = 0; k < (indexOf-1) ; k++) {
					BitUnpent bitUnpentListL = bitUnpentList.get(k);
					totalLeft = totalLeft.add(new BigDecimal(bitUnpentListL.getAmount()));
					toSendUnpentList.add(bitUnpentList.get(k));
					BigDecimal bigFee = totalLeft.subtract(targetAmount);
					fee = (148 *(toSendUnpentList.size())+ 78)*Integer.valueOf(rate);
					change = bigFee.multiply(BigDecimal.valueOf(1e8)).subtract(BigDecimal.valueOf(fee));
					//预留看旷工费小于实际要扣除的
					if (change.compareTo(BigDecimal.ZERO) < 0) {
						continue;
					}else {   //找零金额
						if (change.compareTo(BigDecimal.valueOf(452)) >0) {
							break;
						}
						continue;
					}
				}
				
				if (change.compareTo(BigDecimal.valueOf(452)) <0) {
					toSendUnpentList = Collections.emptyList();
				}
				
				if (change.compareTo(BigDecimal.ZERO) < 0) {
					toSendUnpentList = Collections.emptyList();
				}
		}
		System.err.println("big="+change);
		return true;
	}

	public BitUnpent getToSendAmount() {
		return toSendAmount;
	}

	public void setToSendAmount(BitUnpent toSendAmount) {
		this.toSendAmount = toSendAmount;
		if (!bitUnpentList.isEmpty()) {
			bitUnpentList.add(toSendAmount);
		}
	}

	public ArrayList<BitUnpent> getBitUnpentList() {
		return bitUnpentList;
	}

	public void setBitUnpentList(ArrayList<BitUnpent> bitUnpentList) {
		this.bitUnpentList = bitUnpentList;
		
	}

	public List<BitUnpent> getToSendUnpentList() {
		return toSendUnpentList;
	}

	public void setToSendUnpentList(List<BitUnpent> toSendUnpentList) {
		this.toSendUnpentList = toSendUnpentList;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
	
}

package com.cmc.utxo.etp.optimalMvs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cmc.exception.MedicalException;
import com.cmc.utxo.etp.model.po.MvsUtxo;

public class MvsTokenOptimalUtxo {

	// 所有未花费的utxo ----token
	public ArrayList<MvsUtxo> mvsUnSpentOfTokenList = new ArrayList<MvsUtxo>();
	// 要发送的金额----
	private MvsUtxo toSendAmountOfToken;
	// 代币单位
	private String tokenUnitOfToken;
	// 用于发送资产的utxo
	private List<MvsUtxo> toSendUnpentOfTokenList = new ArrayList<MvsUtxo>();

	public boolean getmvsTokenOptimalUtxo() throws MedicalException {
		if (mvsUnSpentOfTokenList.isEmpty()) {
			return false;
		}
		Collections.sort(mvsUnSpentOfTokenList, new MvsComparatorConfig());
		BigDecimal targetAmount = new BigDecimal(toSendAmountOfToken.getAmount());
		int indexOf = mvsUnSpentOfTokenList.indexOf(toSendAmountOfToken);
		// list中可用utox的index
		// 没有可用的,直接结束
		int size = mvsUnSpentOfTokenList.size();
		if (indexOf == 1 && size == 2 && new BigDecimal(mvsUnSpentOfTokenList.get(0).getAmount())
				.compareTo(new BigDecimal(mvsUnSpentOfTokenList.get(1).getAmount())) == 0) {
			toSendUnpentOfTokenList.add(mvsUnSpentOfTokenList.get(0));
			return true;
		}
		BigDecimal totalRight = BigDecimal.ZERO;
		BigDecimal change = BigDecimal.valueOf(-1);
		if (size > indexOf + 1) {
			for (int j = 1; size > (indexOf + j); j++) {
				toSendUnpentOfTokenList.add(mvsUnSpentOfTokenList.get(indexOf + j));
				totalRight = totalRight.add(new BigDecimal(mvsUnSpentOfTokenList.get(indexOf + j).getAmount()));
				change = totalRight.subtract(targetAmount);
				if (change.compareTo(BigDecimal.ZERO) >= 0) {
					break;
				}
			}

			if (change.compareTo(BigDecimal.ZERO) < 0) {
				toSendUnpentOfTokenList = Collections.emptyList();
			}
		} else {
			if (indexOf == 0) {
				toSendUnpentOfTokenList = Collections.emptyList();
				return false;
			}
			BigDecimal totalLeft = new BigDecimal(mvsUnSpentOfTokenList.get(indexOf - 1).getAmount());
			toSendUnpentOfTokenList.add(mvsUnSpentOfTokenList.get(indexOf - 1));
			for (int k = 0; k < (indexOf - 1); k++) {
				MvsUtxo mvsUtxo = mvsUnSpentOfTokenList.get(k);
				totalLeft = totalLeft.add(new BigDecimal(mvsUtxo.getAmount()));
				toSendUnpentOfTokenList.add(mvsUnSpentOfTokenList.get(k));
				change = totalLeft.subtract(targetAmount);
				if (change.compareTo(BigDecimal.ZERO) >= 0) {
					break;
				}
			}
			if (change.compareTo(BigDecimal.ZERO) < 0) {
				throw new MedicalException("余额不足!");
			}
		}
		return true;
	}

	public ArrayList<MvsUtxo> getMvsUnSpentOfTokenList() {
		return mvsUnSpentOfTokenList;
	}

	public void setMvsUnSpentOfTokenList(ArrayList<MvsUtxo> mvsUnSpentOfTokenList) {
		this.mvsUnSpentOfTokenList = mvsUnSpentOfTokenList;
	}

	public MvsUtxo getToSendAmountOfToken() {
		return toSendAmountOfToken;
	}

	public void setToSendAmountOfToken(MvsUtxo toSendAmountOfToken) {
		this.toSendAmountOfToken = toSendAmountOfToken;
		if (mvsUnSpentOfTokenList != null) {
			mvsUnSpentOfTokenList.add(toSendAmountOfToken);
		}
	}

	public String getTokenUnitOfToken() {
		return tokenUnitOfToken;
	}

	public void setTokenUnitOfToken(String tokenUnitOfToken) {
		this.tokenUnitOfToken = tokenUnitOfToken;
	}

	public List<MvsUtxo> getToSendUnpentOfTokenList() {
		return toSendUnpentOfTokenList;
	}

	public void setToSendUnpentOfTokenList(List<MvsUtxo> toSendUnpentOfTokenList) {
		this.toSendUnpentOfTokenList = toSendUnpentOfTokenList;
	}

}

package com.cmc.utxo.etp.model.vo;

import java.io.Serializable;

public class MvsTransUtxoVo implements Serializable{

	private static final long serialVersionUID = 1929607810296176579L;

	private String address;
	
	private String amount;
	
	private String txid;
	
	private int vout;
	
	private String symbol;
	
	private String tokenUnit;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public int getVout() {
		return vout;
	}

	public void setVout(int vout) {
		this.vout = vout;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "MvsTransUtxoVo [address=" + address + ", amount=" + amount + ", txid=" + txid + ", vout=" + vout
				+ ", symbol=" + symbol + "]";
	}

	public String getTokenUnit() {
		return tokenUnit;
	}

	public void setTokenUnit(String tokenUnit) {
		this.tokenUnit = tokenUnit;
	}
	
}

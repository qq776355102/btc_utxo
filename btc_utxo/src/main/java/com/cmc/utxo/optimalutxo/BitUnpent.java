package com.cmc.utxo.optimalutxo;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author mc
 * 
 *         接收listUnspent命令返回result
 */
public class BitUnpent implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 4140845597635975300L;

	/**
	 * 账户
	 */
	@JSONField(serialize = false, deserialize = false)
	private String account;

	private String address;

	/**
	 * 金额
	 */
	private String amount;

	@JSONField(serialize = false, deserialize = false)
	private int confirmations;

	@JSONField(serialize = false, deserialize = false)
	private boolean safe;

	@JSONField(serialize = false, deserialize = false)
	private String scriptPubKey;

	@JSONField(serialize = false, deserialize = false)
	private boolean solvable;

	@JSONField(serialize = false, deserialize = false)
	private boolean spendablel;

	/**
	 * 交易hash
	 */
	private String txid;

	/**
	 * UTXO输出索引
	 */
	private int vout;

	public String getAccount() {
		return account;
	}

	public String getAddress() {
		return address;
	}

	public String getAmount() {
		return amount;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public String getScriptPubKey() {
		return scriptPubKey;
	}

	public String getTxid() {
		return txid;
	}

	public int getVout() {
		return vout;
	}

	public boolean isSafe() {
		return safe;
	}

	public boolean isSolvable() {
		return solvable;
	}

	public boolean isSpendablel() {
		return spendablel;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}

	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	public void setScriptPubKey(String scriptPubKey) {
		this.scriptPubKey = scriptPubKey;
	}

	public void setSolvable(boolean solvable) {
		this.solvable = solvable;
	}

	public void setSpendablel(boolean spendablel) {
		this.spendablel = spendablel;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public void setVout(int vout) {
		this.vout = vout;
	}

	@Override
	public String toString() {
		return "BitParameter [txid=" + txid + ", vout=" + vout + ", address=" + address + ", account=" + account
				+ ", scriptPubKey=" + scriptPubKey + ", amount=" + amount + ", confirmations=" + confirmations
				+ ", spendablel=" + spendablel + ", solvable=" + solvable + ", safe=" + safe + "]";
	}

}

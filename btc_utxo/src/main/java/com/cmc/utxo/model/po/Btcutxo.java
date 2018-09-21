package com.cmc.utxo.model.po;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;


public class Btcutxo {
    private BigDecimal id;

    private String txid;

    @JSONField(serialize=false)
    private String hash;

    private String address;
    
    @JSONField(format="amount")
    private String value;


    private Integer outindex;

    @JSONField(serialize=false)
    private int isused;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid == null ? null : txid.trim();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOutindex() {
		return outindex;
	}

	public void setOutindex(Integer outindex) {
		this.outindex = outindex;
	}

	public int getIsused() {
		return isused;
	}

	public void setIsused(int isused) {
		this.isused = isused;
	}

	@Override
	public String toString() {
		return "Btcutxo [id=" + id + ", txid=" + txid + ", hash=" + hash + ", address=" + address + ", value=" + value
				+ ", outindex=" + outindex + ", isused=" + isused + "]";
	}
    
}
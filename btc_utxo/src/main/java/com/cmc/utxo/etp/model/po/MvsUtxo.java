package com.cmc.utxo.etp.model.po;

import java.io.Serializable;

public class MvsUtxo implements Serializable{
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 787974653748095169L;


	private int id;

  
    private String hash;

 
    private String address;

  
    private String script;


    private String amount;


    private int outindex;

    private String symbol;

  
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getScript() {
		return script;
	}


	public void setScript(String script) {
		this.script = script;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public int getOutindex() {
		return outindex;
	}


	public void setOutindex(int outindex) {
		this.outindex = outindex;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	private int status;

  
    private String type;


}
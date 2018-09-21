package com.cmc.transaction.model.po;

import java.math.BigDecimal;

public class BtcTransaction {
    /**
     * null
     */
    private BigDecimal id;

    /**
     * null
     */
    private String txid;

    /**
     * null
     */
    private String hash;

    /**
     * null
     */
    private String fromadd;

    /**
     * null
     */
    private String toadd;

    /**
     * null
     */
    private BigDecimal value;

    /**
     * null
     */
    private BigDecimal minerfee;

    /**
     * null
     */
    private BigDecimal blocknumber;

    /**
     * null
     */
    private String blockhash;

    /**
     * null
     */
    private String time;

    /**
     * null
     * @return ID null
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * null
     * @param id null
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * null
     * @return TXID null
     */
    public String getTxid() {
        return txid;
    }

    /**
     * null
     * @param txid null
     */
    public void setTxid(String txid) {
        this.txid = txid == null ? null : txid.trim();
    }

    /**
     * null
     * @return HASH null
     */
    public String getHash() {
        return hash;
    }

    /**
     * null
     * @param hash null
     */
    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    /**
     * null
     * @return FROMADD null
     */
    public String getFromadd() {
        return fromadd;
    }

    /**
     * null
     * @param fromadd null
     */
    public void setFromadd(String fromadd) {
        this.fromadd = fromadd == null ? null : fromadd.trim();
    }

    /**
     * null
     * @return TOADD null
     */
    public String getToadd() {
        return toadd;
    }

    /**
     * null
     * @param toadd null
     */
    public void setToadd(String toadd) {
        this.toadd = toadd == null ? null : toadd.trim();
    }

    /**
     * null
     * @return VALUE null
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * null
     * @param value null
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * null
     * @return MINERFEE null
     */
    public BigDecimal getMinerfee() {
        return minerfee;
    }

    /**
     * null
     * @param minerfee null
     */
    public void setMinerfee(BigDecimal minerfee) {
        this.minerfee = minerfee;
    }

    /**
     * null
     * @return BLOCKNUMBER null
     */
    public BigDecimal getBlocknumber() {
        return blocknumber;
    }

    /**
     * null
     * @param blocknumber null
     */
    public void setBlocknumber(BigDecimal blocknumber) {
        this.blocknumber = blocknumber;
    }

    /**
     * null
     * @return BLOCKHASH null
     */
    public String getBlockhash() {
        return blockhash;
    }

    /**
     * null
     * @param blockhash null
     */
    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash == null ? null : blockhash.trim();
    }

    /**
     * null
     * @return TIME null
     */
    public String getTime() {
        return time;
    }

    /**
     * null
     * @param time null
     */
    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }
}
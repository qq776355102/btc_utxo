package com.cmc.utxo.etp.model.po;

import java.math.BigDecimal;

public class TokenUnit {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOKEN_ADDRESS.ID
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    private int id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOKEN_ADDRESS.TOKEN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    private String token;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOKEN_ADDRESS.ADDRESS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    private String address;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOKEN_ADDRESS.DECIMALS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    private int decimals;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOKEN_ADDRESS.CHAIN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    private Short chain;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOKEN_ADDRESS.ID
     *
     * @return the value of TOKEN_ADDRESS.ID
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public int getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOKEN_ADDRESS.ID
     *
     * @param id the value for TOKEN_ADDRESS.ID
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOKEN_ADDRESS.TOKEN
     *
     * @return the value of TOKEN_ADDRESS.TOKEN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public String getToken() {
        return token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOKEN_ADDRESS.TOKEN
     *
     * @param token the value for TOKEN_ADDRESS.TOKEN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOKEN_ADDRESS.ADDRESS
     *
     * @return the value of TOKEN_ADDRESS.ADDRESS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOKEN_ADDRESS.ADDRESS
     *
     * @param address the value for TOKEN_ADDRESS.ADDRESS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOKEN_ADDRESS.DECIMALS
     *
     * @return the value of TOKEN_ADDRESS.DECIMALS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOKEN_ADDRESS.DECIMALS
     *
     * @param decimals the value for TOKEN_ADDRESS.DECIMALS
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOKEN_ADDRESS.CHAIN
     *
     * @return the value of TOKEN_ADDRESS.CHAIN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public Short getChain() {
        return chain;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOKEN_ADDRESS.CHAIN
     *
     * @param chain the value for TOKEN_ADDRESS.CHAIN
     *
     * @mbggenerated Mon Sep 17 16:51:26 CST 2018
     */
    public void setChain(Short chain) {
        this.chain = chain;
    }
}
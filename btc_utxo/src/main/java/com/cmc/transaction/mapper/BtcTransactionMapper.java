package com.cmc.transaction.mapper;

import java.util.List;

import com.cmc.transaction.model.po.BtcTransaction;

public interface BtcTransactionMapper {
    /**
     *
     * 	@mbg.generated 2018-08-27
     * 
     */
    int insert(BtcTransaction record);

    /**
     *
     * 	@mbg.generated 2018-08-27
     * 
     */
    int insertSelective(BtcTransaction record);
    
    List<BtcTransaction> selectByAddress(String address);
}
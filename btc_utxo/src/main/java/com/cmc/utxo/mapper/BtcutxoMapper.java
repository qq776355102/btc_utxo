package com.cmc.utxo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmc.utxo.model.po.Btcutxo;

public interface BtcutxoMapper {
    int insert(Btcutxo record);

    int insertSelective(Btcutxo record);
    
    List<Btcutxo> selectByAddress(@Param("address")String address,@Param("isused")Integer isused);
    
    List<Btcutxo> getSpendingBanlance(@Param("address")String address,@Param("isused")Integer[] isused);
    
    
    void updateUtxoStatus(Map<String, Object> param);
    
    Btcutxo selectByTxidAndVout(@Param("txid")String txid,@Param("vout")Integer vout);
}
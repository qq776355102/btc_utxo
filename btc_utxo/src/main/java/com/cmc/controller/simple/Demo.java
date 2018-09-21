package com.cmc.controller.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmc.utxo.service.impl.UtxoServiceImpl;

@RestController
@RequestMapping("test")
public class Demo {

	@Autowired
	private UtxoServiceImpl utxoService;
	
	@RequestMapping("1")
	public Object rcp() {
		String availableBanlance = utxoService.getAvailableBanlance("1L7dTUuhd2rF1qiAFQCmsfUo5FUSskARr1");
		return	availableBanlance;
	}
}

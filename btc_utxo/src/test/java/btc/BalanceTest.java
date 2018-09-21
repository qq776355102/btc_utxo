package btc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cmc.utxo.mapper.BtcutxoMapper;
import com.cmc.utxo.model.po.Btcutxo;
import com.cmc.utxo.service.impl.UtxoServiceImpl;

public class BalanceTest {

	@SuppressWarnings("resource")
	@Test
	public void  getAvailableBanlance() {
		
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
		BtcutxoMapper bean = applicationContext.getBean(BtcutxoMapper.class);

	}
}

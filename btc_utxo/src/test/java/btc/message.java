package btc;

import java.security.SignatureException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import net.sf.json.JSONObject;

public class message {

		
		public static void main(String[] args) throws AddressFormatException, SignatureException {
			String message = "02000000018a6a7e5e08aba491ee9ed5bc95fd83a69b651cb95c61de654007cdea97f61c2d010000006b483045022100eb943a2474de8fc4281e9cfcddcc55ce2724fc584ea5c27938f574d8705f03a702201fd88337de81d57013add3165daa2a5f5cc14d83ec01bb3bb504cbff087bade9012102f61f4b38102b71786c797e4188011af9f83d30a3f04fed8bb230723f1cf261d0ffffffff02e8030000000000001976a91426372a640abfd9e61e5d6ca7fbe48e3a66771efb88ace0980500000000001976a914d1ab516d73b38a6b82e06775b2be4c45763983bc88ac00000000";
	    	String signature = "ICEMQGuY2ATSot+FoJbkf8XiCNsx0KDT9FBpbt8wHbuNMSWK3AznagFc0N0loKklRHb3tDB87piQWJzNRKX85lE=";
	    	String address = "1L7dTUuhd2rF1qiAFQCmsfUo5FUSskARr1";
	    	String wif = "L5m9Vbg44JyUFfwoApAuvBZ7VwGnGP6c71WJjrV1Yn29C7CUhoWc";
	    	//ICEMQGuY2ATSot+FoJbkf8XiCNsx0KDT9FBpbt8wHbuNMSWK3AznagFc0N0loKklRHb3tDB87piQWJzNRKX85lE=
	    	String signMsg = signMsg(message, wif);
	    	System.out.println(signMsg);
//	    	boolean validSignature = isValidSignature(address, signMsg, message);
//	    	System.err.println(validSignature);
		}
	    /**
	     * @param msg 要签名的信息
	     * @param privateKey 私钥
	     * @return
	     */
	    public static String signMsg(String msg,String privateKey) {
	        NetworkParameters networkParameters = null;
//	        if (!BTC_TEST_NET)
	            networkParameters = MainNetParams.get();
	            //比特币测试网络
	            //        else
//	            networkParameters = TestNet3Params.get();
	        DumpedPrivateKey priKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey);
	        ECKey ecKey = priKey.getKey();
	        return ecKey.signMessage(msg);
	    }
	    public static boolean isValidSignature(String address, String signature, String message) {
	        try {
	            return ECKey.signedMessageToKey(message, signature).toAddress(Address.fromBase58(null, address).getParameters()).toString().equals(address);
	        } catch (Exception e) {
	            return false;
	        }
	    }
}

package dev.mars.util;


/**
 * Created by ma.xuanwei on 2017/2/20.
 */

public class SecretUtilImp2 implements ISecretUtil{

    @Override
    public String decrypt(String encryptedStr){
        return encryptedStr;
    }


    @Override
    public String encrypt(String source){
	       return source;
    }

}

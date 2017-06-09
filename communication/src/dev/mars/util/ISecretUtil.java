package dev.mars.util;

/**
 * Created by ma.xuanwei on 2017/2/28.
 */

public interface ISecretUtil {
    /**
     * 加密
     * @param src UTF-8格式的字符串
     * @return UTF-8格式的字符串
     */
    public abstract String encrypt(String src);

    /**
     * 解密
     * @param src UTF-8格式的字符串
     * @return UTF-8格式的字符串
     */
    public abstract String decrypt(String src);
}

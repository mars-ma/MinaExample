package dev.mars.util;


/**
 * Created by ma.xuanwei on 2016/12/6.
 */

public class TextUtils {
    /**
     * 判断是否为空字符串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str==null||"".equals(str)||str.length()==0){
            return true;
        }
        return false;
    }

}

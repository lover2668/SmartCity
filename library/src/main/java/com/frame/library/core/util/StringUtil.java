package com.frame.library.core.util;

import android.text.TextUtils;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年09月25日13:52
 * @Email: 971613168@qq.com
 */
public class StringUtil {
    private static final String PHONE_START = "1";
    private static final int PHONE_LENGTH = 11;

    private static final int LENGTH_ID_CARD = 18;
    /**
     * 温度符号
     */
    public static final String SYMBOL_TEMP = "°";
    public static final String LINE_HORIZONTAL = "--";
    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == PHONE_LENGTH && number.startsWith(PHONE_START);
    }


    public static boolean isIdCard(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return number.length() == LENGTH_ID_CARD ;
    }

    public static String  getNotNullValue(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        return number;
    }


    public static boolean isCarnumberNo(String carNumber) {
   /*
   1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
   2.武警车牌：允许前两位为大写英文字母，后面可录入五个或六个字符，由大写英文字母和阿拉伯数字组成，其中第三位可录汉字也可录大写英文字母及阿拉伯数字，第三位也可空，如：WJ警00081、WJ京1234J、WJ1234X。
   3.最后一个为汉字的车牌：允许以汉字开头，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字，汉字包括“挂”、“学”、“警”、“军”、“港”、“澳”。如：粤Z1234港。
   4.新军车牌：以两位为大写英文字母开头，后面以5位阿拉伯数字组成。如：BA12345。
       */
        String carNumRegex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        if (TextUtils.isEmpty(carNumber)) return false;
        else return carNumber.matches(carNumRegex);
    }


}

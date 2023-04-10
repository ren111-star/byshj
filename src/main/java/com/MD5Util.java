package com;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * 对字符串进行MD5摘要加密，返回结果与MySQL的MD5函数一致
     *
     * @param input
     * @return 返回值中的字母为小写
     */
    public static String md5(String input) {
        if (null == input) {
            input = "";
        }
        String result = "";
        try {
            // MessageDigest类用于为应用程序提供信息摘要算法的功能，如MD5或SHA算法
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 获取输入
            md.update(input.getBytes());
            // 获得产出（有符号的哈希值字节数组，包含16个元素）
            byte output[] = md.digest();

            // 32位的加密字符串
            StringBuilder builder = new StringBuilder(32);
            // 下面进行十六进制的转换
            for (int offset = 0; offset < output.length; offset++) {
                // 转变成对应的ASSIC值
                int value = output[offset];
                // 将负数转为正数（最终返回结果是无符号的）
                if (value < 0) {
                    value += 256;
                }
                // 小于16，转为十六进制后只有一个字节，左边追加0来补足2个字节
                if (value < 16) {
                    builder.append("0");
                }
                // 将16位byte[]转换为32位无符号String
                builder.append(Integer.toHexString(value));
            }
            result = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String md5Mix(String input) {//e10adc3949ba59abbe56e057f20f883e
        String result = md5(md5(input));
        return result;
    }

    // 测试
    public static void main(String[] args) {
        String m1 = md5("123456");
//        String m2 = md5("124");
//        String m3 = md5("");
        System.out.println("m1=" + m1);
//        System.out.println("m2=" + m2);
//        System.out.println("m3=" + m3);
        String m4 = md5Mix("e10adc3949ba59abbe56e057f20f883e");//前端传过来的123456对应的js加密后的md5码
        System.out.println("m4=" + m4);
    }
}

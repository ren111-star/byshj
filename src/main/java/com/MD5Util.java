package com;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * ���ַ�������MD5ժҪ���ܣ����ؽ����MySQL��MD5����һ��
     *
     * @param input
     * @return ����ֵ�е���ĸΪСд
     */
    public static String md5(String input) {
        if (null == input) {
            input = "";
        }
        String result = "";
        try {
            // MessageDigest������ΪӦ�ó����ṩ��ϢժҪ�㷨�Ĺ��ܣ���MD5��SHA�㷨
            MessageDigest md = MessageDigest.getInstance("MD5");
            // ��ȡ����
            md.update(input.getBytes());
            // ��ò������з��ŵĹ�ϣֵ�ֽ����飬����16��Ԫ�أ�
            byte output[] = md.digest();

            // 32λ�ļ����ַ���
            StringBuilder builder = new StringBuilder(32);
            // �������ʮ�����Ƶ�ת��
            for (int offset = 0; offset < output.length; offset++) {
                // ת��ɶ�Ӧ��ASSICֵ
                int value = output[offset];
                // ������תΪ���������շ��ؽ�����޷��ŵģ�
                if (value < 0) {
                    value += 256;
                }
                // С��16��תΪʮ�����ƺ�ֻ��һ���ֽڣ����׷��0������2���ֽ�
                if (value < 16) {
                    builder.append("0");
                }
                // ��16λbyte[]ת��Ϊ32λ�޷���String
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

    // ����
    public static void main(String[] args) {
        String m1 = md5("123456");
//        String m2 = md5("124");
//        String m3 = md5("");
        System.out.println("m1=" + m1);
//        System.out.println("m2=" + m2);
//        System.out.println("m3=" + m3);
        String m4 = md5Mix("e10adc3949ba59abbe56e057f20f883e");//ǰ�˴�������123456��Ӧ��js���ܺ��md5��
        System.out.println("m4=" + m4);
    }
}

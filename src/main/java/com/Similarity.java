/*
 LCS(Longest Common Subsequence)�㷨ʵ�ֵ��ı����ƶȷ�����
�㷨ԭ��

(1) �������ַ����ֱ����к�����ɾ���
(2) ����ÿ���ڵ������ַ��Ƿ���ͬ������ͬ��Ϊ 1��
(3) ͨ���ҳ�ֵΪ 1 ����Խ��߼��ɵõ�������Ӵ���

   �� ��  �� �� ʱ ��
�� 0, 0, 0, 0, 0, 0
�� 0, 0, 0, 0, 0, 0
�� 1, 0, 0, 0, 0, 0
�� 0, 1, 0, 0, 0, 0
�� 0, 0, 1, 0, 0, 0
�� 0, 0, 0, 1, 0, 0
�� 0, 0, 0, 0, 0, 0

Ϊ��һ���������㷨�����ǿ��Խ��ַ���ͬ�ڵ�(1)��ֵ�������Ͻ�(d[i-1, j-1])��ֵ��
�������ɻ��������Ӵ��ĳ��ȡ����һ��ֻ�����кź����ֵΪ�������ɽ�ȡ����Ӵ���

   �� ��  �� �� ʱ ��
�� 0, 0, 0, 0, 0, 0
�� 0, 0, 0, 0, 0, 0
�� 1, 0, 0, 0, 0, 0
�� 0, 2, 0, 0, 0, 0
�� 0, 0, 3, 0, 0, 0
�� 0, 0, 0, 4, 0, 0
�� 0, 0, 0, 0, 0, 0
 * */
package com;

public class Similarity {
    private final String content_regex = "(?i)[^a-zA-Z0-9\u4E00-\u9FA5]";

    /**
     * �ж������������ƶ�
     */
    public static Float calculatesimilar(String str1, String str2) {
        //���������ַ����ĳ��ȡ�
        int len1 = str1.length();
        int len2 = str2.length();
        //��������˵�����飬���ַ����ȴ�һ���ռ�
        int[][] dif = new int[len1 + 1][len2 + 1];
        //����ֵ������B��
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //���������ַ��Ƿ�һ�����������ϵ�ֵ
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //ȡ����ֵ����С��
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        /*���dif��ά����*/
        String teststr = "";
        for (int i = 0; i <= len1; i++) {
            teststr = "";
            for (int j = 0; j <= len2; j++) {
                teststr = teststr + " " + dif[i][j];
            }
            //System.out.println(i+"�У�"+teststr);
        }

        /**/
        //�������ƶ�
        float similarity = 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        return Float.valueOf(similarity);
    }

    //�õ���Сֵ
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }
}

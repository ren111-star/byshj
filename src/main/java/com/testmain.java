package com;

import java.util.Date;

public class testmain {

    public static void main(String[] args) throws Exception {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            //endDate = format.parse("2014-1-19");
            endDate = new Date();
            beginDate = format.parse("2014-1-16");
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            System.out.println("���������=" + day);
            long weeknum = day / 7 + 1;
            System.out.println("��ǰΪ��" + weeknum + "��");
        } catch (Exception e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        }
    }

}

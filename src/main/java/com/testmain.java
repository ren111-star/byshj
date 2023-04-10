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
            System.out.println("相隔的天数=" + day);
            long weeknum = day / 7 + 1;
            System.out.println("当前为第" + weeknum + "周");
        } catch (Exception e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
    }

}

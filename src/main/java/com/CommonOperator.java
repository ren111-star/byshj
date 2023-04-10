package com;

import org.junit.Test;

public class CommonOperator {
	
	static public String MD5Modify(String md5str){
		
		String result1=md5str.substring(0, 9);//È¡Ç°9¸ö×Ö·û
		String result2=md5str.substring(10, 19);
		String result3=md5str.substring(19,md5str.length()-1);
		
		String newresult=result2+result3+result1;
		
		System.out.println(newresult);
		
		return newresult;
	}
	@Test
	public void test(){
		
		System.out.println(MD5Modify("e10adc3949ba59abbe56e057f20f883e"));
		
	}
}

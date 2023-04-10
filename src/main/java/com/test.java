package com;

import bean.ProgressBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Type type = new TypeToken<List<ProgressBean>>(){}.getType();  
		Gson gson = new Gson();
		String subprogs="";
		List<ProgressBean> processes=gson.fromJson(subprogs,type);
	}

}

package com;

import bean.SubSimBean;

import java.util.Comparator;

public class SimiComparator implements Comparator{
	public int compare(Object o1,Object o2){
		SubSimBean t1=(SubSimBean)o1;
		SubSimBean t2=(SubSimBean)o2;
		return t2.getSimilard().compareTo(t1.getSimilard());
	}
}

package com;

import bean.TeacherSubNumBean;

import java.util.Comparator;

/**������Ŀ����������*/
public class TeaSubComparator implements Comparator<TeacherSubNumBean>{
	public int compare(TeacherSubNumBean o1,TeacherSubNumBean o2){
		int subsum1=o1.getSubsum();
		int subsum2=o2.getSubsum();
		return subsum2-subsum1;
	}
}

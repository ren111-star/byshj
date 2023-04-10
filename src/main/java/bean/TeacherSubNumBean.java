/**教师申报的总题目数*/
package bean;

import java.util.ArrayList;

public class TeacherSubNumBean {
	String tid;//教师编号
	int subsum;//总题目数
	ArrayList<String> subjects;//所有题目
	
	
	public TeacherSubNumBean() {
		subsum = 0;
		subjects = new ArrayList<String>();
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getSubsum() {
		return subsum;
	}
	public void setSubsum(int subsum) {
		this.subsum = subsum;
	}
	public ArrayList<String> getSubjects() {
		return subjects;
	}
	
}

/**��ʦ�걨������Ŀ��*/
package bean;

import java.util.ArrayList;

public class TeacherSubNumBean {
	String tid;//��ʦ���
	int subsum;//����Ŀ��
	ArrayList<String> subjects;//������Ŀ
	
	
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

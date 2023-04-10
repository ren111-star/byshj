/*仅是为相似度计算结果定义的结构*/
package bean;

public class SubSimBean {
	private String subname;//课题名
	private String tutorname;//指导教师名
	private Float similard;//相似度
	private String usedyear;//被使用的年份
	
	public String getUsedyear() {
		return usedyear;
	}
	public void setUsedyear(String usedyear) {
		this.usedyear = usedyear;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public Float getSimilard() {
		return similard;
	}
	public void setSimilard(Float similard) {
		this.similard = similard;
	}
	public String getTutorname() {
		return tutorname;
	}
	public void setTutorname(String tutorname) {
		this.tutorname = tutorname;
	}
	
}

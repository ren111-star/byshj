package bean;

public class ReviewPaperBaseInfoBean {
	String classname;//班级
	String stuid;//学号
	String sname;//姓名
	String subid;//课题号
	String subname;//课题名称
	String tutornames;//指导教师名
	String reviewername;//评阅人名
	String submitflag;//提交标志
	
	public String getSubmitflag() {
		return submitflag;
	}
	public void setSubmitflag(String submitflag) {
		this.submitflag = submitflag;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public String getTutornames() {
		return tutornames;
	}
	public void setTutornames(String tutornames) {
		this.tutornames = tutornames;
	}
	public String getReviewername() {
		return reviewername;
	}
	public void setReviewername(String reviewername) {
		this.reviewername = reviewername;
	}
}

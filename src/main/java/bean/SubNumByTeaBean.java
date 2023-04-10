package bean;

public class SubNumByTeaBean {
	String tid;//教师编号
	String tname;//教师名
	int submitsubnum;//提交的课题数目
	int reviewsubnum;//需审核的课题数目
	int unrevnum;//未被审核的课题数目
	
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public int getUnrevnum() {
		return unrevnum;
	}
	public void setUnrevnum(int unrevnum) {
		this.unrevnum = unrevnum;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getSubmitsubnum() {
		return submitsubnum;
	}
	public void setSubmitsubnum(int submitsubnum) {
		this.submitsubnum = submitsubnum;
	}
	public int getReviewsubnum() {
		return reviewsubnum;
	}
	public void setReviewsubnum(int reviewsubnum) {
		this.reviewsubnum = reviewsubnum;
	}
}

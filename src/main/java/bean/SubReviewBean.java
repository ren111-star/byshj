package bean;

public class SubReviewBean {
	String tid;//审核教师号
	String subid;//课题编号
	String subname;//课题名称
	Integer simsubcount;//相似的历史课题数（与自己的课题比较）
	String reviewopinion;//审核意见（盲审）
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
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
	public String getReviewopinion() {
		return reviewopinion;
	}
	public void setReviewopinion(String reviewopinion) {
		this.reviewopinion = reviewopinion;
	}
	/**wxh 2016.12.17*/
	public Integer getSimsubcount() {
		return simsubcount;
	}
	public void setSimsubcount(Integer simsubcount) {
		this.simsubcount = simsubcount;
	}
}

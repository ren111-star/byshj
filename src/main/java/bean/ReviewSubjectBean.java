package bean;

public class ReviewSubjectBean {
	
	private String subid;//课题编号，自动分配
	private String subname;//课题名
	private String tutornames;//指导教师名[已拼装好的]
	private String reviewername;//课题审核人名
	private String reviewopinion;//审核意见（课题盲审）
	
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
	public String getReviewopinion() {
		return reviewopinion;
	}
	public void setReviewopinion(String reviewopinion) {
		this.reviewopinion = reviewopinion;
	}
}

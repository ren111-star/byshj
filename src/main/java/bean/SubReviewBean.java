package bean;

public class SubReviewBean {
	String tid;//��˽�ʦ��
	String subid;//������
	String subname;//��������
	Integer simsubcount;//���Ƶ���ʷ�����������Լ��Ŀ���Ƚϣ�
	String reviewopinion;//��������ä��
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

package bean;

public class SubNumByTeaBean {
	String tid;//��ʦ���
	String tname;//��ʦ��
	int submitsubnum;//�ύ�Ŀ�����Ŀ
	int reviewsubnum;//����˵Ŀ�����Ŀ
	int unrevnum;//δ����˵Ŀ�����Ŀ
	
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

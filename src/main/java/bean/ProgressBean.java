package bean;

public class ProgressBean {
	
	private String progid;//����id,�Զ����
	private String subid;//������
	private String inorder;//�ڲ�˳��ͬһ���������ɽ�����Ŀ
	private String content;//��������
	private String startenddate;//��ʼ����ʱ��
	private String checkopinion;//������
	
	public String getStartenddate() {
		return startenddate;
	}
	public void setStartenddate(String startenddate) {
		this.startenddate = startenddate;
	}
	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInorder() {
		
		return inorder;
	}
	public void setInorder(String inorder) {
		this.inorder = inorder;
	}
	public String getProgid() {
		return progid;
	}
	public void setProgid(String progid) {
		this.progid = progid;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getCheckopinion() {
		if(checkopinion==null) checkopinion="";
		return checkopinion;
	}
	public void setCheckopinion(String checkopinion) {
		this.checkopinion = checkopinion;
	}
}

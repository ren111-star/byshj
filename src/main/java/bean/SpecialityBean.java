package bean;

public class SpecialityBean {
	
	private String specid;//רҵ��ţ�3λ�ַ�
	private String specname;//רҵ����
	private String specmagtid;//רҵ������id
	private String specmagtname;//רҵ��������
	public String getSpecmagtname() {
		return specmagtname;
	}
	public void setSpecmagtname(String specmagtname) {
		this.specmagtname = specmagtname;
	}
	public String getSpecid() {
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
	public String getSpecmagtid() {
		if(specmagtid==null) specmagtid="";
		return specmagtid;
	}
	public void setSpecmagtid(String specmagtid) {
		this.specmagtid = specmagtid;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}
	public void setSpecname(String specname) {
		this.specname = specname;
	}
	
	

}

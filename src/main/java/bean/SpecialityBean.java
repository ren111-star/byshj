package bean;

import lombok.Data;

@Data
public class SpecialityBean {
	
	private String specid;//רҵ��ţ�3λ�ַ�
	private String specname;//רҵ����
	private String specmagtid;//רҵ������id
	private String specmagtname;//רҵ��������
	public String getSpecmagtid() {
		if(specmagtid==null) specmagtid="";
		return specmagtid;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}

}

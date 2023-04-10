package bean;

import lombok.Data;

@Data
public class SpecialityBean {
	
	private String specid;//专业编号，3位字符
	private String specname;//专业名称
	private String specmagtid;//专业负责人id
	private String specmagtname;//专业负责人名
	public String getSpecmagtid() {
		if(specmagtid==null) specmagtid="";
		return specmagtid;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}

}

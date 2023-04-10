package bean;

import lombok.Data;

@Data
public class RoleBean {
	private String roleid;
	private String rolename;
	private String roledesc;
	
	public String getRoledesc() {
		if(roledesc==null)roledesc="";
		return roledesc;
	}
	public String getRolename() {
		if(rolename==null)rolename="";
		return rolename;
	}
	
	
}

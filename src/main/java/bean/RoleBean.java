package bean;

public class RoleBean {
	private String roleid;
	private String rolename;
	private String roledesc;
	
	public String getRoledesc() {
		if(roledesc==null)roledesc="";
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		if(rolename==null)rolename="";
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	
}

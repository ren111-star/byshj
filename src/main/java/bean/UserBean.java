package bean;

public class UserBean {
	private String usertype;//�û�����
	private String userid;//�û���
	private String userpwd;//�û�����
	private String username;//�û���ʵ����
	public String getUsertype() {
		if(usertype==null) usertype="";
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getUserid() {
		if(userid==null) userid="";
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		if(username==null) username="";
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpwd() {
		if(userpwd==null) userpwd="";
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
}

package bean;

import lombok.Data;

@Data
public class UserBean {
	private String usertype;//用户类型
	private String userid;//用户名
	private String userpwd;//用户密码
	private String username;//用户真实姓名
	public String getUsertype() {
		if(usertype==null) usertype="";
		return usertype;
	}

	public String getUserid() {
		if(userid==null) userid="";
		return userid;
	}
	public String getUsername() {
		if(username==null) username="";
		return username;
	}
	public String getUserpwd() {
		if(userpwd==null) userpwd="";
		return userpwd;
	}
}

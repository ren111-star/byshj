package bean;

import lombok.Data;

@Data
public class UserBean {
	private String usertype;//�û�����
	private String userid;//�û���
	private String userpwd;//�û�����
	private String username;//�û���ʵ����
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

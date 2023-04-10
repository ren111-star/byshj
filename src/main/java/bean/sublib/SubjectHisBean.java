package bean.sublib;

import base.SubjectBasePojo;
import lombok.Data;

/**¿ÎÌâÀúÊ·bean*/
@Data
public class SubjectHisBean extends SubjectBasePojo{
	private String usedyear;
	private String stuid;
	private String sname;
	private String subprogs;
	
	public String getSubprogs() {
		if(subprogs==null) subprogs="";
		return subprogs;
	}
}

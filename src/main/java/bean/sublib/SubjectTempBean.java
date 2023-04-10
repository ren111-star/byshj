package bean.sublib;

import base.SubjectBasePojo;
import lombok.Data;

/**¿ÎÌâÀúÊ·bean*/
@Data
public class SubjectTempBean extends SubjectBasePojo{
	private String subprogs;
	private String remark;
	
	public String getSubprogs() {
		if(subprogs==null) subprogs="";
		return subprogs;
	}
	public String getRemark() {
		if(remark==null) remark="";
		return remark;
	}
	
}

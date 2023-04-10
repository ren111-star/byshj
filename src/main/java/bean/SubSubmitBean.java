package bean;

import lombok.Data;

@Data
public class SubSubmitBean {
	String stuid;//学号
	String sname;
	String classname;
	String paperblindstatus;//状态文本信息，下同
	String paperstatus;
	String translationstatus;
	String sourcecodestatus;
	String validweekupnum;//已完成的周总结数
}

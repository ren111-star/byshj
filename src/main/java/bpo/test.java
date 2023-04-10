package bpo;

import bean.SubjectBean;
import bean.TeacherBean;
import org.junit.Test;

import java.util.List;

public class test {
	@Test
	public void exportalltask() throws Exception {
		ExcelBpo exbpo=new ExcelBpo();
		TeacherBpo tbpo=new TeacherBpo();
		SubjectBpo subbpo=new SubjectBpo();
		List<TeacherBean> teachers=tbpo.getAllinfo();
		for(TeacherBean teacher:teachers) {
			String tid=teacher.getTid();
			List<SubjectBean> subjects=subbpo.getAllinfo(tid);
			if(subjects.size()==0) continue;
			exbpo.exporttaskbook(tid);
		}
	}
}

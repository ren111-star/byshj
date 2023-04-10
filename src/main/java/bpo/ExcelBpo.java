package bpo;

import bean.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelBpo {
	private String filepath;
	public ExcelBpo() throws Exception{
		SysarguBpo sysbpo=new SysarguBpo();
		filepath=sysbpo.getSysargu("tempfilepath").getArguvalue();
		if(filepath.equals("")) throw new Exception("出错了：tempfilepath参数未配置！");
	}
	//导入学生基本信息
	public void importstudents(InputStream stream)throws Exception{
		Workbook workbook =null;
		try{
	        //读取文件输入流中的内容
		    workbook = Workbook.getWorkbook(stream);
		    int count=workbook.getNumberOfSheets();
		    //System.out.println("表单数："+String.valueOf(count));
		    List<StudentBean> students=new ArrayList<StudentBean>();
		    for(int m=0;m<count;m++){
		    	Sheet sheet = workbook.getSheet(m); 
	            //int col=sheet.getColumns();
	            int row=sheet.getRows();
	            String sheetname=sheet.getName();
	            for(int i=1;i<row;i++){
	        		String stuid=sheet.getCell(0,i).getContents().trim();
	        		String sname=sheet.getCell(1,i).getContents();
	        		String classname=sheet.getCell(2,i).getContents();
	        		if(stuid.equals("")) break;
	        		//检查读入信息的有效性
	        		//if(!(stuid.length()==10))throw new Exception("表单<"+sheetname+">中学号"+stuid+"出错,应为10位字符！");
	        		StudentBean temp=new StudentBean();
	        		temp.setStuid(stuid);
	        		temp.setSname(sname);
	        		temp.setClassname(classname);
	        		students.add(temp);
	        		//System.out.println(sheetname+":"+stuid+","+sname+","+classname);
	            }
		    }
		    //将学生维护到数据库
		    StudentBpo studentbpo=new StudentBpo();
		    studentbpo.addinfoBatch(students);
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//导出学生课题明细表
	public void exportstusublist(String specid,String classname)throws Exception{
		//System.out.println(System.getProperty("user.dir"));//显示servlet路径D:\apache-tomcat-5.5.26\bin
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"output.xls")); 
			//获得专业列表
			SpecialityBpo specbpo=new SpecialityBpo();
			List<SpecialityBean> specs=new ArrayList<SpecialityBean>();
			specs=specbpo.getAllinfo(specid);
			Iterator it = specs.iterator();
			//按专业写入表单信息，
			int sheetcount=0;
			SubjectBpo subjectbpo=new SubjectBpo();
			while (it.hasNext()) {
				SpecialityBean spec=(SpecialityBean) it.next();
				String specname=spec.getSpecname();
				String specid0=spec.getSpecid();
				WritableSheet sheet = workbook.createSheet(specname+"课题明细表", sheetcount);
				
				//前两行显示标题
				sheet.addCell(new Label(0, 0, "序号")); //(col,row)
				sheet.mergeCells(0, 0, 0, 1);
				sheet.addCell(new Label(1, 0, "学号"));
				sheet.mergeCells(1, 0, 1, 1);
				sheet.addCell(new Label(2, 0, "姓名"));
				sheet.mergeCells(2, 0, 2, 1);
				sheet.addCell(new Label(3, 0, "专业"));
				sheet.mergeCells(3, 0, 3, 1);
				sheet.addCell(new Label(4, 0, "班级"));
				sheet.mergeCells(4, 0, 4, 1);
				sheet.addCell(new Label(5, 0, "毕业设计（论文）题目"));
				sheet.mergeCells(5, 0, 5, 1);
				sheet.addCell(new Label(6, 0, "类别"));
				sheet.mergeCells(6, 0, 6, 1);
				sheet.addCell(new Label(7, 0, "题目类型"));
				sheet.mergeCells(7, 0, 7, 1);
				sheet.addCell(new Label(8, 0, "题目性质"));
				sheet.mergeCells(8, 0,8, 1);
				sheet.addCell(new Label(9, 0, "题目来源"));
				sheet.mergeCells(9, 0, 9, 1);
				sheet.addCell(new Label(10, 0, "指导教师"));
				sheet.mergeCells(10, 0, 11, 0);
				sheet.addCell(new Label(10, 1, "姓名"));
				sheet.addCell(new Label(11, 1, "职称/学位"));
				sheet.addCell(new Label(12, 1, "是否校外"));
//					第3行往后显示数据
				List<StudentBean> students=new ArrayList<StudentBean>();
				students=subjectbpo.getStusBySpec(specid0, classname, "");
				Iterator it0 = students.iterator();
				int row=2;//从第3行开始写入数据
				while (it0.hasNext()) {
					StudentBean temp=(StudentBean) it0.next();
					String tname=temp.getSubject().getTutor().getTname();
					String tpostdegree=temp.getSubject().getTutor().getTpostname()+"/"+temp.getSubject().getTutor().getTdegreename();
					String othertname=temp.getSubject().getOthertutor().getTname();
					String othertpostdegree=temp.getSubject().getOthertutor().getTpostname()+"/"+temp.getSubject().getOthertutor().getTdegreename();
					if(!othertname.equals("")){
						tname=tname+"、"+othertname;
						tpostdegree=tpostdegree+"、"+othertpostdegree;
					}
					sheet.addCell(new Label(0, row, String.valueOf(row-1))); 
					sheet.addCell(new Label(1, row, temp.getStuid())); 
					sheet.addCell(new Label(2, row, temp.getSname())); 
					sheet.addCell(new Label(3, row, specname)); 
					sheet.addCell(new Label(4, row, temp.getClassname())); 
					sheet.addCell(new Label(5, row, temp.getSubject().getSubname())); 
					sheet.addCell(new Label(6, row, temp.getSubject().getSubsortname())); 
					sheet.addCell(new Label(7, row, temp.getSubject().getSubtypename())); 
					sheet.addCell(new Label(8, row, temp.getSubject().getSubkindname())); 
					sheet.addCell(new Label(9, row, temp.getSubject().getSubsourcename())); 
					sheet.addCell(new Label(10, row, tname)); 
					sheet.addCell(new Label(11, row, tpostdegree)); 
					int isoutschool=temp.getSubject().getIsoutschool();
					if(isoutschool==1){
						sheet.addCell(new Label(12, row,"是" ));
					}else{
						sheet.addCell(new Label(12, row, ""));
					}
					row=row+1;
				}
				//表单数加1
				sheetcount=sheetcount+1;
			}
			//写入文件
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//导出任务书（根据课题编号导出一份任务书）
	public void exporttaskbook(String subid)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+subid+"taskbookoutput.xls")); 
			taskbookcontent(workbook,0,subid);
			//写入文件
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
//	导出任务书（根据教师编号导出该教师的所有任务书-一个excel文件）
	public void exporttaskbooksBytid(String tid)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		//System.out.println(System.getProperty("user.dir"));
		try{
			workbook = Workbook.createWorkbook(new File(filepath+tid+"taskbookoutput.xls")); 
			SubjectBpo subjectbpo=new SubjectBpo();
			List<SubjectBean> subjects=subjectbpo.getAllinfo(tid);
			Iterator it=subjects.iterator();
			String subid="";
			int sheetnum=0;
			while(it.hasNext()){
				subid=((SubjectBean)it.next()).getSubid();
				taskbookcontent(workbook,sheetnum,subid);
				sheetnum=sheetnum+1;
			}
			//写入文件
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**
	 * @param workbook //excel文件
	 * @param sheetnum //sheet序号
	 * @param subid //任务对应课题编号，一个任务书对应一个sheet
	 * @throws Exception
	 */
	private void taskbookcontent(WritableWorkbook workbook,int sheetnum,String subid)throws Exception{
//		获得课题基本信息
		SubjectBpo subjectbpo=new SubjectBpo();
		SubjectBean subject=subjectbpo.getBysubid(subid);
		StudentBean student=subjectbpo.getStudentBysubid(subid);
		//创建任务书表单
		WritableSheet sheet = workbook.createSheet(subid+"课题任务书", sheetnum);
		//共6列，设置每列列宽
		sheet.setColumnView( 0,10 );//设置列宽
		sheet.setColumnView( 1,14 );//设置列宽
		sheet.setColumnView( 2,14 );//设置列宽
		sheet.setColumnView( 3,16 );//设置列宽
		sheet.setColumnView( 4,14 );//设置列宽
		sheet.setColumnView( 5,18 );//设置列宽
		//合并第一行所有列，显示题头。表格共6列
		sheet.mergeCells(0, 0, 5, 0);//(col,row)
		//设置第一行，行高为800
		sheet.setRowView(0, 800);
		//第1行填写标题
		WritableFont wfont=new WritableFont(WritableFont.createFont("宋体"),14);//设置单元格字体(宋体、四号、加粗）
		WritableCellFormat wc=new WritableCellFormat(wfont);
		wc.setAlignment(Alignment.CENTRE);//设置水平居中
		wc.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label label=new Label(0,0,"山东建筑大学毕业设计（论文）任务书",wc);
		sheet.addCell(label);
		//设置表中每个单元格格式
		//标题文本内容格式
		WritableCellFormat wctitle=new WritableCellFormat();
		wctitle.setAlignment(Alignment.CENTRE);//标题居中
		wctitle.setVerticalAlignment(VerticalAlignment.CENTRE);
		wctitle.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//边框为细线,默认为none
		wctitle.setBorder(Border.TOP, BorderLineStyle.THIN);//边框为细线
		wctitle.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wctitle.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wctitle.setWrap(true);//支持自动换行
		//表格内容格式
		WritableCellFormat wccontent=new WritableCellFormat();
		wccontent.setAlignment(Alignment.LEFT);//内容居左
		wccontent.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontent.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//边框为细线,默认为none
		wccontent.setBorder(Border.TOP, BorderLineStyle.THIN);//边框为细线
		wccontent.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontent.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontent.setWrap(true);//支持自动换行
		
		//第2行填写基本信息
		sheet.setRowView(1, 400,false);//行高
		sheet.addCell(new Label(0,1, "班级",wctitle)); //(col,row)
		sheet.addCell(new Label(1,1, student.getClassname(),wctitle)); 
		sheet.addCell(new Label(2,1, "学生姓名",wctitle)); 
		sheet.addCell(new Label(3,1, student.getSname(),wctitle)); 	
		sheet.addCell(new Label(4,1, "指导教师",wctitle)); 
		sheet.addCell(new Label(5,1, " "+subject.getTutor().getTname()+" "+subject.getOthertutor().getTname(),wctitle));
		//第3行论文题目
	    sheet.setRowView(2, 400,false);//行高
		sheet.mergeCells(0,2,1,2);
		sheet.addCell(new Label(0,2, "设计（论文）题目",wctitle));
		sheet.mergeCells(2,2,5,2);
		sheet.addCell(new Label(2,2, subject.getSubname(),wccontent));
		//第4行设计（论文）原始参数
		sheet.setRowView(3, this.evaluateRowHeight(40, subject.getOldargu()));
		//System.out.println("原始参数行高："+String.valueOf(this.evaluateRowHeight(40, subject.getOldargu())));
		//sheet.setRowView(3, 4000,false);//行高
		sheet.mergeCells(1,3,5,3);
		sheet.addCell(new Label(0,3, "设计（论文）概述",wctitle));
		sheet.addCell(new Label(1,3, subject.getOldargu(),wccontent));
		
		//第5行设计（论文）工作内容4000
		
		//sheet.setRowView(4, 4000,false);//行高
		sheet.setRowView(4, this.evaluateRowHeight(40, subject.getContent()));
		//System.out.println("工作内容行高："+String.valueOf(this.evaluateRowHeight(40, subject.getContent())));
		sheet.mergeCells(1,4,5,4);
		sheet.addCell(new Label(0,4, "设计（论文）工作内容",wctitle));
		sheet.addCell(new Label(1,4, subject.getContent(),wccontent));
	
		//第6行设计（论文）工作基本要求3000
		//sheet.setRowView(5,3000,false);//行高
		sheet.setRowView(5, this.evaluateRowHeight(40, subject.getRequirement()));
		sheet.mergeCells(1,5,5,5);
		sheet.addCell(new Label(0,5, "设计（论文）工作基本要求",wctitle));
		sheet.addCell(new Label(1,5, subject.getRequirement(),wccontent));
		
		//第7行设计（论文）工作日程
		
		//sheet.setRowView(6, 3000,false);//行高
		sheet.setRowView(6, this.evaluateRowHeight(40, subject.getSubprog()));
		sheet.mergeCells(1,6,5,6);
		sheet.addCell(new Label(0,6, "设计（论文）工作日程",wctitle));
		sheet.addCell(new Label(1,6, subject.getSubprog(),wccontent));
		
		//第8行主要参考资料及文献
		//sheet.setRowView(7, 3000,false);//行高
		sheet.setRowView(7, this.evaluateRowHeight(40, subject.getRefpapers()));
		sheet.mergeCells(1,7,5,7);
		sheet.addCell(new Label(0,7, "主要参考资料及文献",wctitle));
		sheet.addCell(new Label(1,7, subject.getRefpapers(),wccontent));
		
		//第9行 签字
		sheet.setRowView(8,500);//行高
		sheet.mergeCells(0,8,5,8);
		sheet.addCell(new Label(0,8,"指导教师（签字）：                  教研室主任（签字）：                  院系主任（签字）："));
		/*
		//第10行 签字
		sheet.setRowView(9,400);//行高
		sheet.mergeCells(0,9,5,9);
		sheet.addCell(new Label(0,9,"院系主任（签字）："));
		*/
	}
	
	/**
	 * @param stream 
	 * @throws Exception
	 * 导入教师基本信息
	 */
	public void importteachers(InputStream stream)throws Exception{
		Workbook workbook =null;
		try{
	        //读取文件输入流中的内容
		    workbook = Workbook.getWorkbook(stream);
		    int count=workbook.getNumberOfSheets();
		    //System.out.println("表单数："+String.valueOf(count));
		    List<TeacherBean> teachers=new ArrayList<TeacherBean>();
		    for(int m=0;m<count;m++){
		    	Sheet sheet = workbook.getSheet(m); 
	            //int col=sheet.getColumns();
	            int row=sheet.getRows();
	            String sheetname=sheet.getName();
	            for(int i=1;i<row;i++){
	        		String tid=sheet.getCell(0,i).getContents();
	        		String tname=sheet.getCell(1,i).getContents();
	        		String tdept=sheet.getCell(2,i).getContents();
	        		String tpost=sheet.getCell(3,i).getContents();
	        		String tdegree=sheet.getCell(4,i).getContents();
	        		if(tid.equals("")) continue;
	        		//检查读入信息的有效性
	        		if(tid.length()!=6)throw new Exception("表单<"+sheetname+">中职工号“"+tid+"”出错,应为6位字符！");
	        		TeacherBean temp=new TeacherBean();
	        		temp.setTid(tid);
	        		temp.setTname(tname);
	        		temp.setTdept(tdept);
	        		temp.setTpost(tpost);
	        		temp.setTdegree(tdegree);
	        		teachers.add(temp);
	        		//System.out.println(sheetname+":"+tid+","+tname+","+tdept);
	            }
		    }
		    //将教师维护到数据库
		    TeacherBpo teacherbpo=new TeacherBpo();
		    teacherbpo.addinfoBatch(teachers);
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**估算行高
	 * countperrow-每行字数。任务书大约每行为40个。
	 * text-显示的文本
	 * */
	public int evaluateRowHeight(int countperrow, String text)throws Exception{
		int rowcounts=0;//行数
		String[] textarr=text.split("\n");
		for(int i=0;i<textarr.length;i++){
			int rowtemp=textarr[i].length()/countperrow+1;
			rowcounts=rowcounts+rowtemp;
		}
		//excel中默认行高为300
		return rowcounts*300;
	}
	/**导出评阅意见（盲审）（批量）,按专业一个专业生成一个excel文件*/
	public void exportBlindReviewContentBySpec(String specid,String classname,String sname)throws Exception{
		//String specname=specbpo.getByspecid(specid).getSpecname();//获得专业名
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"reviewpaper.xls")); 
			StudentBpo studentbpo=new StudentBpo();
			SubjectBpo subjectbpo=new SubjectBpo();
			ReviewPaperBpo reviewbpo=new ReviewPaperBpo();
			
			List<StudentBean> students=studentbpo.getAllinfo(specid,classname, sname);
			if(students.size()==0) throw new Exception("没有满足条件的学生！");
			Iterator<StudentBean> it=students.iterator();
			int sheetnum=0;
			while(it.hasNext()){
				StudentBean temp=it.next();
				SubjectBean subject=subjectbpo.getSubjectByStuPicked(temp.getStuid());
				if(subject==null) continue;
				ReviewPaperBean reviewpaper=reviewbpo.getReviewOpinion(subject.getSubid());
				if(reviewpaper==null) continue;
				reviewpaper.setSubname(subject.getSubname());
				blindReviewContent(workbook,sheetnum,temp,reviewpaper);
				sheetnum=sheetnum+1;
			}
			//写入文件
			if(sheetnum==0) throw new Exception("满足条件的学生，还没有确定相应的课题或还有没有进行论文评阅，所以无法生成任何盲审表！！");
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**导出评阅意见（盲审）（单个）*/
	private void blindReviewContent(WritableWorkbook workbook,int sheetnum,StudentBean student,ReviewPaperBean reviewpaper)throws Exception{
		WritableSheet sheet = workbook.createSheet(reviewpaper.getSubid()+"盲审表", sheetnum);
		/**设置各种字体*/
		WritableFont wfont12=new WritableFont(WritableFont.createFont("楷体"),12);//设置单元格字体(宋体、小四号、加粗）
		
		WritableFont wfont22bold=new WritableFont(WritableFont.createFont("楷体"),22);//设置单元格字体(宋体、22号、加粗）
		wfont22bold.setBoldStyle(WritableFont.BOLD);//字体加粗
		
		WritableFont wfont14bold=new WritableFont(WritableFont.createFont("楷体"),14);//设置单元格字体(宋体、四号、加粗)
		wfont14bold.setBoldStyle(WritableFont.BOLD);
		
		WritableFont wfont14=new WritableFont(WritableFont.createFont("楷体"),14);//设置单元格字体(宋体、四号)
		/**共4列，设置每列列宽*/
		sheet.setColumnView( 0,18 );//设置列宽
		sheet.setColumnView( 1,50 );//设置列宽
		sheet.setColumnView( 2,11 );//设置列宽
		sheet.setColumnView( 3,11 );//设置列宽
		/**标题1-4*/
		//合并第1行所有列，显示题头。
		sheet.mergeCells(0, 0, 3, 0);//(col,row)
		//设置第1行，行高为400.填写标题1
		sheet.setRowView(0, 400);
		Label label=new Label(0,0,"山东建筑大学计算机科学与技术学院",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont14bold));
		sheet.addCell(label);
		//设置第2行，行高为800，填写标题2
		sheet.mergeCells(0, 1, 3, 1);
		sheet.setRowView(1, 800);
		sheet.addCell(new Label(0,1,"毕业设计评阅人评审表（盲审）",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont22bold)));
		//设置第3行，行高为400，填写论文编号
		sheet.mergeCells(0, 2, 3, 2);
		sheet.setRowView(2, 400);
		
		sheet.addCell(new Label(0,2,"班级："+student.getClassname()+"  学号："+student.getStuid()+"  姓名："+student.getSname(),this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont12)));
		//设置第4行，行高为800，填写论文题目
		sheet.mergeCells(0, 3, 3, 3);
		sheet.setRowView(3, 800);
		sheet.addCell(new Label(0,3,"题目："+reviewpaper.getSubname(),this.setCellFormatWithoutBorder(Alignment.LEFT, wfont14)));
		
		/**表格内容5-14*/
		//第5行填写基本信息表格标题
		sheet.setRowView(4, 400,false);//行高
		sheet.addCell(new Label(0,4, "评审内容",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); //(col,row)
		sheet.addCell(new Label(1,4, "具 体 要 求",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 
		sheet.addCell(new Label(2,4, "分值",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 
		sheet.addCell(new Label(3,4, "评分",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 	
		//第6行
	    sheet.setRowView(5, 800,false);//行高
	    sheet.addCell(new Label(0,5, "选题意义",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,5, "选题的理论意义或实际应用价值，选题的专业性与新颖性。",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,5, "0-2",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,5, String.valueOf(reviewpaper.getSignificance()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第7行
	    sheet.setRowView(6, 1600,false);//行高
	    sheet.addCell(new Label(0,6, "毕业设计内容",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,6, "设计内容的难易程度、工作量；方案设计的科学性和合理性；实验真实性和正确性；结论合理性、结果的应用价值；学生掌握基础理论、专业知识、基本工程方法和技能情况；学生分析问题和解决问题的能力。",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,6, "0-10",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,6, String.valueOf(reviewpaper.getDesigncontent()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第8行
	    sheet.setRowView(7, 1200,false);//行高
	    sheet.addCell(new Label(0,7, "设计说明书撰写能力",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,7, "概念清晰与分析、设计严谨的程度；术语准确性，文字通顺程度；写作规范性及文字表达能力。",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,7, "0-3",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,7, String.valueOf(reviewpaper.getComposeability()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第9行
	    sheet.setRowView(8, 1200,false);//行高
	    sheet.addCell(new Label(0,8, "文献查阅外文翻译",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,8, "文献查阅的广度和深度，对学科或行业领域知识的了解程度，对文献资料的掌握及综述能力；外文翻译准确性、表述水平及写作规范性。",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,8, "0-3",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,8, String.valueOf(reviewpaper.getTranslationlevel()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第10行
	    sheet.setRowView(9, 400,false);//行高
	    sheet.addCell(new Label(0,9, "创新性",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,9, "对前人工作有一定的改进或突破，或有独特见解。",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,9, "0-2",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,9, String.valueOf(reviewpaper.getInnovative()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第11行
		sheet.mergeCells(0, 10, 2, 10);//合并前3列
		sheet.setRowView(10, 400,false);//行高
		sheet.addCell(new Label(0,10, "评阅人评分（保留1位小数）合计：",this.setCellFormatWithThinLine(Alignment.RIGHT, wfont12))); //(col,row)
		sheet.addCell(new Label(3,10, String.valueOf(reviewpaper.getSumgrade()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//System.out.println(reviewpaper.getSumgrade());
		//第12行
		String reviewresult="";
		if(reviewpaper.getSumgrade()>=16) {
			reviewresult="√评审通过[16,20]   修改后通过[12,16)   不通过[0,12)";
		}
		if(reviewpaper.getSumgrade()<16&&reviewpaper.getSumgrade()>=12) {
			reviewresult="评审通过[16,20]    √修改后通过[12,16)   不通过[0,12)";
		}
		if(reviewpaper.getSumgrade()<12) {
			reviewresult="评审通过[16,20]    修改后通过[12,16)    √不通过[0,12)";
		}
		sheet.mergeCells(1, 11, 3, 11);//合并前2列
		sheet.setRowView(11, 1000,false);//行高
		sheet.addCell(new Label(0,11, "评阅结果（请在相应项上划 √）",this.setCellFormatWithThinLine(Alignment.RIGHT, wfont12))); //(col,row)
		sheet.addCell(new Label(1,11, reviewresult,this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//第13行
		sheet.mergeCells(0, 12, 3, 12);//合并所有列
	    sheet.setRowView(12, 400,false);//行高
	    sheet.addCell(new Label(0,12, "评阅意见：",this.setCellFormatWithoutButtom(Alignment.LEFT, wfont12)));
		//第14行
	    sheet.mergeCells(0, 13, 3, 13);//合并所有列
	    sheet.setRowView(13, 4800);//行高
	    sheet.addCell(new Label(0,13, reviewpaper.getReviewopinion(),this.setCellFormatWithoutTopButtom(Alignment.LEFT, wfont12)));
	    //第15行(时间)
	    sheet.mergeCells(0, 14, 3, 14);//合并所有列
	    sheet.setRowView(13, 500);//行高
	    sheet.addCell(new Label(0,14, reviewpaper.getReviewtime()+"  ",this.setCellFormatWithoutTop(Alignment.RIGHT, wfont12)));
	    //第16行(尾部)
	    sheet.mergeCells(0, 15, 3, 15);
	    sheet.addCell(new Label(0,15, "计算机科学与技术学院制",this.setCellFormatWithoutBorder(Alignment.RIGHT, wfont12)));
	}
	//导出进程表（每个学生一份）
	public void exportProgressTableByStu(String stuid)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+stuid+"progresstable.xls")); 
			StudentBpo studentbpo=new StudentBpo();
			SubjectBpo subjectbpo=new SubjectBpo();
			
			StudentBean student=studentbpo.getBystuid(stuid);
			SubjectBean subject=subjectbpo.getSubjectByStuPicked(student.getStuid());
			progessTableContent(workbook,0,student,subject);
			//写入文件
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//导出进程表（每个学生一份）指定路径
		public void exportProgressTableByStu0(String path,String stuid)throws Exception{
			//String filepath="../webapps/computer_byshjMag/uploadfiles/";
			WritableWorkbook workbook =null;
			try{
				workbook = Workbook.createWorkbook(new File(path+stuid+"progresstable.xls")); 
				StudentBpo studentbpo=new StudentBpo();
				SubjectBpo subjectbpo=new SubjectBpo();
				
				StudentBean student=studentbpo.getBystuid(stuid);
				SubjectBean subject=subjectbpo.getSubjectByStuPicked(student.getStuid());
				progessTableContent(workbook,0,student,subject);
				//写入文件
				workbook.write();
			}catch(Exception e){
				throw e;
			}finally{
				if(workbook!=null) workbook.close();
			}
		}
	//导出进程表（批量）
	public void exportProgressTableBySpec(String specid,String classname,String sname)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"progresstable.xls")); 
			StudentBpo studentbpo=new StudentBpo();
			SubjectBpo subjectbpo=new SubjectBpo();
			
			List<StudentBean> students=studentbpo.getAllinfo(specid,classname, sname);
			if(students.size()==0) throw new Exception("没有满足条件的学生！");
			Iterator<StudentBean> it=students.iterator();
			int sheetnum=0;
			while(it.hasNext()){
				StudentBean temp=it.next();
				SubjectBean subject=subjectbpo.getSubjectByStuPicked(temp.getStuid());
				if(subject==null) continue;
				progessTableContent(workbook,sheetnum,temp,subject);
				sheetnum=sheetnum+1;
			}
			if(sheetnum==0) throw new Exception("满足条件的学生，还没有确定相应的课题，所以无法生成任何进程表！");
			//写入文件
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
		
	/**导出进程表（单个）*/
	private void progessTableContent(WritableWorkbook workbook,int sheetnum,StudentBean student,SubjectBean subject)throws Exception{
		WritableSheet sheet = workbook.createSheet(subject.getSubid()+"进程表", sheetnum);
		/**设置各种字体*/
		WritableFont wfont12=new WritableFont(WritableFont.createFont("宋体"),12);//设置单元格字体(宋体、小四号、加粗）
		
		WritableFont wfont14bold=new WritableFont(WritableFont.createFont("宋体"),14);//设置单元格字体(宋体、四号、加粗)
		wfont14bold.setBoldStyle(WritableFont.BOLD);
		
		WritableFont wfont11=new WritableFont(WritableFont.createFont("宋体"),11);//设置单元格字体(宋体、四号)
		/**共6列，设置每列列宽*/
		sheet.setColumnView( 0,10 );//设置列宽
		sheet.setColumnView( 1,14 );//设置列宽
		sheet.setColumnView( 2,14 );//设置列宽
		sheet.setColumnView( 3,16 );//设置列宽
		sheet.setColumnView( 4,14 );//设置列宽
		sheet.setColumnView( 5,18 );//设置列宽
		/**标题1*/
		sheet.mergeCells(0, 0, 5, 0);//(col,row)
		//设置第1行，行高为600.填写标题1
		sheet.setRowView(0, 800);
		Label label=new Label(0,0,"山东建筑大学毕业设计（论文）工作进程表",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont14bold));
		sheet.addCell(label);
		/**标题2-4*/
		//第2行
		sheet.setRowView(1, 400);
		sheet.addCell(new Label(0,1,"班级",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(1,1,student.getClassname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(2,1,"学生姓名",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(3,1,student.getSname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(4,1,"指导教师",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		String tutorname=subject.getTutor().getTname();
		if(subject.getOthertutor()!=null){
			tutorname=tutorname+" "+subject.getOthertutor().getTname();
		}
		sheet.addCell(new Label(5,1,tutorname,this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		//第3行
		sheet.setRowView(2, 400);
		sheet.mergeCells(0, 2, 1, 2);//(col,row)合并前两列
		sheet.addCell(new Label(0,2,"设计（论文）题目",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.mergeCells(2, 2, 5, 2);//(col,row)合并前两列
		sheet.addCell(new Label(2,2,subject.getSubname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		//第4行
		sheet.setRowView(3, 400);
		sheet.mergeCells(0, 3, 1, 3);//(col,row)合并前两列
		sheet.mergeCells(2, 3, 3, 3);
		sheet.mergeCells(4, 3, 5, 3);
		sheet.addCell(new Label(0,3,"时间（按两周填写）",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(2,3,"应完成的工作内容",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(4,3,"检查情况",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		/**内容(循环)5-10*/
		List<ProgressBean> progresses=subject.getProgress();
		int initrownum=4;//第5行开始
		int progsize=progresses.size();//进程数
		SysarguBpo sysargubpo=new SysarguBpo();
		for(int i=0;i<progsize;i++){
			ProgressBean progress=progresses.get(i);
			int rowheighttemp=this.evaluateRowHeight(11, progress.getContent());
			if(this.evaluateRowHeight(11, progress.getCheckopinion())+3*400>rowheighttemp) rowheighttemp=this.evaluateRowHeight(11, progress.getCheckopinion())+3*400;
			int currentrownum=initrownum+i;
			sheet.setRowView(currentrownum, rowheighttemp);
			sheet.mergeCells(0, currentrownum, 1, currentrownum);//(col,row)合并前两列
			sheet.mergeCells(2, currentrownum, 3, currentrownum);
			sheet.mergeCells(4, currentrownum, 5, currentrownum);
			
			sheet.addCell(new Label(0,currentrownum,progress.getStartenddate(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
			sheet.addCell(new Label(2,currentrownum,progress.getContent(),this.setCellFormatWithThinLine(Alignment.LEFT, wfont12)));
			sheet.addCell(new Label(4,currentrownum,progress.getCheckopinion()+"\n          "+tutorname+"\n                "+sysargubpo.getFillinDate(Integer.valueOf(progress.getInorder())),this.setCellFormatWithThinLine(Alignment.LEFT, wfont12)));
		}
		/**尾部11（教务处制）*/
		int tailrownum=initrownum+progsize;
		sheet.setRowView(tailrownum, 300);
		sheet.mergeCells(0, tailrownum, 5, tailrownum);
		sheet.addCell(new Label(0,tailrownum,"教务处制",this.setCellFormatWithoutBorder(Alignment.RIGHT, wfont11)));
		/**尾部12（注）*/
		sheet.setRowView(tailrownum+1, 600);
		sheet.mergeCells(0, tailrownum+1, 5, tailrownum+1);
		sheet.addCell(new Label(0,tailrownum+1,"注：“应完成的工作内容”、“检查情况”两栏应详细填写检查监督情况，并有指导教师签字及检查日期。",this.setCellFormatWithoutBorder(Alignment.LEFT, wfont11)));
	}
	/**设置单元格格式（带细边线）*/
	private WritableCellFormat setCellFormatWithThinLine(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//内容对齐方式
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//边框为细线,默认为none
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.THIN);//边框为细线
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//支持自动换行
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutButtom(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//内容对齐方式
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.NONE);//边框为细线,默认为none
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.THIN);//边框为细线
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//支持自动换行
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutTop(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//内容对齐方式
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//边框为细线,默认为none
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.NONE);//边框为细线
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//支持自动换行
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutTopButtom(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//内容对齐方式
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.NONE);//边框为细线,默认为none
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.NONE);//边框为细线
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//支持自动换行
		
		return wccontentcentre;
	}
	/**设置单元格格式（无边线）*/
	private WritableCellFormat setCellFormatWithoutBorder(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//内容对齐方式
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setWrap(true);//支持自动换行
		
		return wccontentcentre;
	}
}

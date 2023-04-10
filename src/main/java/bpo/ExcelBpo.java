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
		if(filepath.equals("")) throw new Exception("�����ˣ�tempfilepath����δ���ã�");
	}
	//����ѧ��������Ϣ
	public void importstudents(InputStream stream)throws Exception{
		Workbook workbook =null;
		try{
	        //��ȡ�ļ��������е�����
		    workbook = Workbook.getWorkbook(stream);
		    int count=workbook.getNumberOfSheets();
		    //System.out.println("������"+String.valueOf(count));
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
	        		//��������Ϣ����Ч��
	        		//if(!(stuid.length()==10))throw new Exception("��<"+sheetname+">��ѧ��"+stuid+"����,ӦΪ10λ�ַ���");
	        		StudentBean temp=new StudentBean();
	        		temp.setStuid(stuid);
	        		temp.setSname(sname);
	        		temp.setClassname(classname);
	        		students.add(temp);
	        		//System.out.println(sheetname+":"+stuid+","+sname+","+classname);
	            }
		    }
		    //��ѧ��ά�������ݿ�
		    StudentBpo studentbpo=new StudentBpo();
		    studentbpo.addinfoBatch(students);
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//����ѧ��������ϸ��
	public void exportstusublist(String specid,String classname)throws Exception{
		//System.out.println(System.getProperty("user.dir"));//��ʾservlet·��D:\apache-tomcat-5.5.26\bin
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"output.xls")); 
			//���רҵ�б�
			SpecialityBpo specbpo=new SpecialityBpo();
			List<SpecialityBean> specs=new ArrayList<SpecialityBean>();
			specs=specbpo.getAllinfo(specid);
			Iterator it = specs.iterator();
			//��רҵд�����Ϣ��
			int sheetcount=0;
			SubjectBpo subjectbpo=new SubjectBpo();
			while (it.hasNext()) {
				SpecialityBean spec=(SpecialityBean) it.next();
				String specname=spec.getSpecname();
				String specid0=spec.getSpecid();
				WritableSheet sheet = workbook.createSheet(specname+"������ϸ��", sheetcount);
				
				//ǰ������ʾ����
				sheet.addCell(new Label(0, 0, "���")); //(col,row)
				sheet.mergeCells(0, 0, 0, 1);
				sheet.addCell(new Label(1, 0, "ѧ��"));
				sheet.mergeCells(1, 0, 1, 1);
				sheet.addCell(new Label(2, 0, "����"));
				sheet.mergeCells(2, 0, 2, 1);
				sheet.addCell(new Label(3, 0, "רҵ"));
				sheet.mergeCells(3, 0, 3, 1);
				sheet.addCell(new Label(4, 0, "�༶"));
				sheet.mergeCells(4, 0, 4, 1);
				sheet.addCell(new Label(5, 0, "��ҵ��ƣ����ģ���Ŀ"));
				sheet.mergeCells(5, 0, 5, 1);
				sheet.addCell(new Label(6, 0, "���"));
				sheet.mergeCells(6, 0, 6, 1);
				sheet.addCell(new Label(7, 0, "��Ŀ����"));
				sheet.mergeCells(7, 0, 7, 1);
				sheet.addCell(new Label(8, 0, "��Ŀ����"));
				sheet.mergeCells(8, 0,8, 1);
				sheet.addCell(new Label(9, 0, "��Ŀ��Դ"));
				sheet.mergeCells(9, 0, 9, 1);
				sheet.addCell(new Label(10, 0, "ָ����ʦ"));
				sheet.mergeCells(10, 0, 11, 0);
				sheet.addCell(new Label(10, 1, "����"));
				sheet.addCell(new Label(11, 1, "ְ��/ѧλ"));
				sheet.addCell(new Label(12, 1, "�Ƿ�У��"));
//					��3��������ʾ����
				List<StudentBean> students=new ArrayList<StudentBean>();
				students=subjectbpo.getStusBySpec(specid0, classname, "");
				Iterator it0 = students.iterator();
				int row=2;//�ӵ�3�п�ʼд������
				while (it0.hasNext()) {
					StudentBean temp=(StudentBean) it0.next();
					String tname=temp.getSubject().getTutor().getTname();
					String tpostdegree=temp.getSubject().getTutor().getTpostname()+"/"+temp.getSubject().getTutor().getTdegreename();
					String othertname=temp.getSubject().getOthertutor().getTname();
					String othertpostdegree=temp.getSubject().getOthertutor().getTpostname()+"/"+temp.getSubject().getOthertutor().getTdegreename();
					if(!othertname.equals("")){
						tname=tname+"��"+othertname;
						tpostdegree=tpostdegree+"��"+othertpostdegree;
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
						sheet.addCell(new Label(12, row,"��" ));
					}else{
						sheet.addCell(new Label(12, row, ""));
					}
					row=row+1;
				}
				//������1
				sheetcount=sheetcount+1;
			}
			//д���ļ�
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//���������飨���ݿ����ŵ���һ�������飩
	public void exporttaskbook(String subid)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+subid+"taskbookoutput.xls")); 
			taskbookcontent(workbook,0,subid);
			//д���ļ�
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
//	���������飨���ݽ�ʦ��ŵ����ý�ʦ������������-һ��excel�ļ���
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
			//д���ļ�
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**
	 * @param workbook //excel�ļ�
	 * @param sheetnum //sheet���
	 * @param subid //�����Ӧ�����ţ�һ���������Ӧһ��sheet
	 * @throws Exception
	 */
	private void taskbookcontent(WritableWorkbook workbook,int sheetnum,String subid)throws Exception{
//		��ÿ��������Ϣ
		SubjectBpo subjectbpo=new SubjectBpo();
		SubjectBean subject=subjectbpo.getBysubid(subid);
		StudentBean student=subjectbpo.getStudentBysubid(subid);
		//�����������
		WritableSheet sheet = workbook.createSheet(subid+"����������", sheetnum);
		//��6�У�����ÿ���п�
		sheet.setColumnView( 0,10 );//�����п�
		sheet.setColumnView( 1,14 );//�����п�
		sheet.setColumnView( 2,14 );//�����п�
		sheet.setColumnView( 3,16 );//�����п�
		sheet.setColumnView( 4,14 );//�����п�
		sheet.setColumnView( 5,18 );//�����п�
		//�ϲ���һ�������У���ʾ��ͷ�����6��
		sheet.mergeCells(0, 0, 5, 0);//(col,row)
		//���õ�һ�У��и�Ϊ800
		sheet.setRowView(0, 800);
		//��1����д����
		WritableFont wfont=new WritableFont(WritableFont.createFont("����"),14);//���õ�Ԫ������(���塢�ĺš��Ӵ֣�
		WritableCellFormat wc=new WritableCellFormat(wfont);
		wc.setAlignment(Alignment.CENTRE);//����ˮƽ����
		wc.setVerticalAlignment(VerticalAlignment.CENTRE);
		Label label=new Label(0,0,"ɽ��������ѧ��ҵ��ƣ����ģ�������",wc);
		sheet.addCell(label);
		//���ñ���ÿ����Ԫ���ʽ
		//�����ı����ݸ�ʽ
		WritableCellFormat wctitle=new WritableCellFormat();
		wctitle.setAlignment(Alignment.CENTRE);//�������
		wctitle.setVerticalAlignment(VerticalAlignment.CENTRE);
		wctitle.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wctitle.setBorder(Border.TOP, BorderLineStyle.THIN);//�߿�Ϊϸ��
		wctitle.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wctitle.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wctitle.setWrap(true);//֧���Զ�����
		//������ݸ�ʽ
		WritableCellFormat wccontent=new WritableCellFormat();
		wccontent.setAlignment(Alignment.LEFT);//���ݾ���
		wccontent.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontent.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wccontent.setBorder(Border.TOP, BorderLineStyle.THIN);//�߿�Ϊϸ��
		wccontent.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontent.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontent.setWrap(true);//֧���Զ�����
		
		//��2����д������Ϣ
		sheet.setRowView(1, 400,false);//�и�
		sheet.addCell(new Label(0,1, "�༶",wctitle)); //(col,row)
		sheet.addCell(new Label(1,1, student.getClassname(),wctitle)); 
		sheet.addCell(new Label(2,1, "ѧ������",wctitle)); 
		sheet.addCell(new Label(3,1, student.getSname(),wctitle)); 	
		sheet.addCell(new Label(4,1, "ָ����ʦ",wctitle)); 
		sheet.addCell(new Label(5,1, " "+subject.getTutor().getTname()+" "+subject.getOthertutor().getTname(),wctitle));
		//��3��������Ŀ
	    sheet.setRowView(2, 400,false);//�и�
		sheet.mergeCells(0,2,1,2);
		sheet.addCell(new Label(0,2, "��ƣ����ģ���Ŀ",wctitle));
		sheet.mergeCells(2,2,5,2);
		sheet.addCell(new Label(2,2, subject.getSubname(),wccontent));
		//��4����ƣ����ģ�ԭʼ����
		sheet.setRowView(3, this.evaluateRowHeight(40, subject.getOldargu()));
		//System.out.println("ԭʼ�����иߣ�"+String.valueOf(this.evaluateRowHeight(40, subject.getOldargu())));
		//sheet.setRowView(3, 4000,false);//�и�
		sheet.mergeCells(1,3,5,3);
		sheet.addCell(new Label(0,3, "��ƣ����ģ�����",wctitle));
		sheet.addCell(new Label(1,3, subject.getOldargu(),wccontent));
		
		//��5����ƣ����ģ���������4000
		
		//sheet.setRowView(4, 4000,false);//�и�
		sheet.setRowView(4, this.evaluateRowHeight(40, subject.getContent()));
		//System.out.println("���������иߣ�"+String.valueOf(this.evaluateRowHeight(40, subject.getContent())));
		sheet.mergeCells(1,4,5,4);
		sheet.addCell(new Label(0,4, "��ƣ����ģ���������",wctitle));
		sheet.addCell(new Label(1,4, subject.getContent(),wccontent));
	
		//��6����ƣ����ģ���������Ҫ��3000
		//sheet.setRowView(5,3000,false);//�и�
		sheet.setRowView(5, this.evaluateRowHeight(40, subject.getRequirement()));
		sheet.mergeCells(1,5,5,5);
		sheet.addCell(new Label(0,5, "��ƣ����ģ���������Ҫ��",wctitle));
		sheet.addCell(new Label(1,5, subject.getRequirement(),wccontent));
		
		//��7����ƣ����ģ������ճ�
		
		//sheet.setRowView(6, 3000,false);//�и�
		sheet.setRowView(6, this.evaluateRowHeight(40, subject.getSubprog()));
		sheet.mergeCells(1,6,5,6);
		sheet.addCell(new Label(0,6, "��ƣ����ģ������ճ�",wctitle));
		sheet.addCell(new Label(1,6, subject.getSubprog(),wccontent));
		
		//��8����Ҫ�ο����ϼ�����
		//sheet.setRowView(7, 3000,false);//�и�
		sheet.setRowView(7, this.evaluateRowHeight(40, subject.getRefpapers()));
		sheet.mergeCells(1,7,5,7);
		sheet.addCell(new Label(0,7, "��Ҫ�ο����ϼ�����",wctitle));
		sheet.addCell(new Label(1,7, subject.getRefpapers(),wccontent));
		
		//��9�� ǩ��
		sheet.setRowView(8,500);//�и�
		sheet.mergeCells(0,8,5,8);
		sheet.addCell(new Label(0,8,"ָ����ʦ��ǩ�֣���                  ���������Σ�ǩ�֣���                  Ժϵ���Σ�ǩ�֣���"));
		/*
		//��10�� ǩ��
		sheet.setRowView(9,400);//�и�
		sheet.mergeCells(0,9,5,9);
		sheet.addCell(new Label(0,9,"Ժϵ���Σ�ǩ�֣���"));
		*/
	}
	
	/**
	 * @param stream 
	 * @throws Exception
	 * �����ʦ������Ϣ
	 */
	public void importteachers(InputStream stream)throws Exception{
		Workbook workbook =null;
		try{
	        //��ȡ�ļ��������е�����
		    workbook = Workbook.getWorkbook(stream);
		    int count=workbook.getNumberOfSheets();
		    //System.out.println("������"+String.valueOf(count));
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
	        		//��������Ϣ����Ч��
	        		if(tid.length()!=6)throw new Exception("��<"+sheetname+">��ְ���š�"+tid+"������,ӦΪ6λ�ַ���");
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
		    //����ʦά�������ݿ�
		    TeacherBpo teacherbpo=new TeacherBpo();
		    teacherbpo.addinfoBatch(teachers);
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**�����и�
	 * countperrow-ÿ���������������Լÿ��Ϊ40����
	 * text-��ʾ���ı�
	 * */
	public int evaluateRowHeight(int countperrow, String text)throws Exception{
		int rowcounts=0;//����
		String[] textarr=text.split("\n");
		for(int i=0;i<textarr.length;i++){
			int rowtemp=textarr[i].length()/countperrow+1;
			rowcounts=rowcounts+rowtemp;
		}
		//excel��Ĭ���и�Ϊ300
		return rowcounts*300;
	}
	/**�������������ä�󣩣�������,��רҵһ��רҵ����һ��excel�ļ�*/
	public void exportBlindReviewContentBySpec(String specid,String classname,String sname)throws Exception{
		//String specname=specbpo.getByspecid(specid).getSpecname();//���רҵ��
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"reviewpaper.xls")); 
			StudentBpo studentbpo=new StudentBpo();
			SubjectBpo subjectbpo=new SubjectBpo();
			ReviewPaperBpo reviewbpo=new ReviewPaperBpo();
			
			List<StudentBean> students=studentbpo.getAllinfo(specid,classname, sname);
			if(students.size()==0) throw new Exception("û������������ѧ����");
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
			//д���ļ�
			if(sheetnum==0) throw new Exception("����������ѧ������û��ȷ����Ӧ�Ŀ������û�н����������ģ������޷������κ�ä�����");
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	/**�������������ä�󣩣�������*/
	private void blindReviewContent(WritableWorkbook workbook,int sheetnum,StudentBean student,ReviewPaperBean reviewpaper)throws Exception{
		WritableSheet sheet = workbook.createSheet(reviewpaper.getSubid()+"ä���", sheetnum);
		/**���ø�������*/
		WritableFont wfont12=new WritableFont(WritableFont.createFont("����"),12);//���õ�Ԫ������(���塢С�ĺš��Ӵ֣�
		
		WritableFont wfont22bold=new WritableFont(WritableFont.createFont("����"),22);//���õ�Ԫ������(���塢22�š��Ӵ֣�
		wfont22bold.setBoldStyle(WritableFont.BOLD);//����Ӵ�
		
		WritableFont wfont14bold=new WritableFont(WritableFont.createFont("����"),14);//���õ�Ԫ������(���塢�ĺš��Ӵ�)
		wfont14bold.setBoldStyle(WritableFont.BOLD);
		
		WritableFont wfont14=new WritableFont(WritableFont.createFont("����"),14);//���õ�Ԫ������(���塢�ĺ�)
		/**��4�У�����ÿ���п�*/
		sheet.setColumnView( 0,18 );//�����п�
		sheet.setColumnView( 1,50 );//�����п�
		sheet.setColumnView( 2,11 );//�����п�
		sheet.setColumnView( 3,11 );//�����п�
		/**����1-4*/
		//�ϲ���1�������У���ʾ��ͷ��
		sheet.mergeCells(0, 0, 3, 0);//(col,row)
		//���õ�1�У��и�Ϊ400.��д����1
		sheet.setRowView(0, 400);
		Label label=new Label(0,0,"ɽ��������ѧ�������ѧ�뼼��ѧԺ",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont14bold));
		sheet.addCell(label);
		//���õ�2�У��и�Ϊ800����д����2
		sheet.mergeCells(0, 1, 3, 1);
		sheet.setRowView(1, 800);
		sheet.addCell(new Label(0,1,"��ҵ��������������ä��",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont22bold)));
		//���õ�3�У��и�Ϊ400����д���ı��
		sheet.mergeCells(0, 2, 3, 2);
		sheet.setRowView(2, 400);
		
		sheet.addCell(new Label(0,2,"�༶��"+student.getClassname()+"  ѧ�ţ�"+student.getStuid()+"  ������"+student.getSname(),this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont12)));
		//���õ�4�У��и�Ϊ800����д������Ŀ
		sheet.mergeCells(0, 3, 3, 3);
		sheet.setRowView(3, 800);
		sheet.addCell(new Label(0,3,"��Ŀ��"+reviewpaper.getSubname(),this.setCellFormatWithoutBorder(Alignment.LEFT, wfont14)));
		
		/**�������5-14*/
		//��5����д������Ϣ������
		sheet.setRowView(4, 400,false);//�и�
		sheet.addCell(new Label(0,4, "��������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); //(col,row)
		sheet.addCell(new Label(1,4, "�� �� Ҫ ��",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 
		sheet.addCell(new Label(2,4, "��ֵ",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 
		sheet.addCell(new Label(3,4, "����",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont14bold))); 	
		//��6��
	    sheet.setRowView(5, 800,false);//�и�
	    sheet.addCell(new Label(0,5, "ѡ������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,5, "ѡ������������ʵ��Ӧ�ü�ֵ��ѡ���רҵ������ӱ�ԡ�",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,5, "0-2",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,5, String.valueOf(reviewpaper.getSignificance()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��7��
	    sheet.setRowView(6, 1600,false);//�и�
	    sheet.addCell(new Label(0,6, "��ҵ�������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,6, "������ݵ����׳̶ȡ���������������ƵĿ�ѧ�Ժͺ����ԣ�ʵ����ʵ�Ժ���ȷ�ԣ����ۺ����ԡ������Ӧ�ü�ֵ��ѧ�����ջ������ۡ�רҵ֪ʶ���������̷����ͼ��������ѧ����������ͽ�������������",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,6, "0-10",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,6, String.valueOf(reviewpaper.getDesigncontent()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��8��
	    sheet.setRowView(7, 1200,false);//�и�
	    sheet.addCell(new Label(0,7, "���˵����׫д����",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,7, "�������������������Ͻ��ĳ̶ȣ�����׼ȷ�ԣ�����ͨ˳�̶ȣ�д���淶�Լ����ֱ��������",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,7, "0-3",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,7, String.valueOf(reviewpaper.getComposeability()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��9��
	    sheet.setRowView(8, 1200,false);//�и�
	    sheet.addCell(new Label(0,8, "���ײ������ķ���",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,8, "���ײ��ĵĹ�Ⱥ���ȣ���ѧ�ƻ���ҵ����֪ʶ���˽�̶ȣ����������ϵ����ռ��������������ķ���׼ȷ�ԡ�����ˮƽ��д���淶�ԡ�",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,8, "0-3",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,8, String.valueOf(reviewpaper.getTranslationlevel()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��10��
	    sheet.setRowView(9, 400,false);//�и�
	    sheet.addCell(new Label(0,9, "������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); //(col,row)
		sheet.addCell(new Label(1,9, "��ǰ�˹�����һ���ĸĽ���ͻ�ƣ����ж��ؼ��⡣",this.setCellFormatWithThinLine(Alignment.LEFT, wfont12))); 
		sheet.addCell(new Label(2,9, "0-2",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		sheet.addCell(new Label(3,9, String.valueOf(reviewpaper.getInnovative()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��11��
		sheet.mergeCells(0, 10, 2, 10);//�ϲ�ǰ3��
		sheet.setRowView(10, 400,false);//�и�
		sheet.addCell(new Label(0,10, "���������֣�����1λС�����ϼƣ�",this.setCellFormatWithThinLine(Alignment.RIGHT, wfont12))); //(col,row)
		sheet.addCell(new Label(3,10, String.valueOf(reviewpaper.getSumgrade()),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//System.out.println(reviewpaper.getSumgrade());
		//��12��
		String reviewresult="";
		if(reviewpaper.getSumgrade()>=16) {
			reviewresult="������ͨ��[16,20]   �޸ĺ�ͨ��[12,16)   ��ͨ��[0,12)";
		}
		if(reviewpaper.getSumgrade()<16&&reviewpaper.getSumgrade()>=12) {
			reviewresult="����ͨ��[16,20]    ���޸ĺ�ͨ��[12,16)   ��ͨ��[0,12)";
		}
		if(reviewpaper.getSumgrade()<12) {
			reviewresult="����ͨ��[16,20]    �޸ĺ�ͨ��[12,16)    �̲�ͨ��[0,12)";
		}
		sheet.mergeCells(1, 11, 3, 11);//�ϲ�ǰ2��
		sheet.setRowView(11, 1000,false);//�и�
		sheet.addCell(new Label(0,11, "���Ľ����������Ӧ���ϻ� �̣�",this.setCellFormatWithThinLine(Alignment.RIGHT, wfont12))); //(col,row)
		sheet.addCell(new Label(1,11, reviewresult,this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12))); 
		//��13��
		sheet.mergeCells(0, 12, 3, 12);//�ϲ�������
	    sheet.setRowView(12, 400,false);//�и�
	    sheet.addCell(new Label(0,12, "���������",this.setCellFormatWithoutButtom(Alignment.LEFT, wfont12)));
		//��14��
	    sheet.mergeCells(0, 13, 3, 13);//�ϲ�������
	    sheet.setRowView(13, 4800);//�и�
	    sheet.addCell(new Label(0,13, reviewpaper.getReviewopinion(),this.setCellFormatWithoutTopButtom(Alignment.LEFT, wfont12)));
	    //��15��(ʱ��)
	    sheet.mergeCells(0, 14, 3, 14);//�ϲ�������
	    sheet.setRowView(13, 500);//�и�
	    sheet.addCell(new Label(0,14, reviewpaper.getReviewtime()+"  ",this.setCellFormatWithoutTop(Alignment.RIGHT, wfont12)));
	    //��16��(β��)
	    sheet.mergeCells(0, 15, 3, 15);
	    sheet.addCell(new Label(0,15, "�������ѧ�뼼��ѧԺ��",this.setCellFormatWithoutBorder(Alignment.RIGHT, wfont12)));
	}
	//�������̱�ÿ��ѧ��һ�ݣ�
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
			//д���ļ�
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
	//�������̱�ÿ��ѧ��һ�ݣ�ָ��·��
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
				//д���ļ�
				workbook.write();
			}catch(Exception e){
				throw e;
			}finally{
				if(workbook!=null) workbook.close();
			}
		}
	//�������̱�������
	public void exportProgressTableBySpec(String specid,String classname,String sname)throws Exception{
		//String filepath="../webapps/computer_byshjMag/uploadfiles/";
		WritableWorkbook workbook =null;
		try{
			workbook = Workbook.createWorkbook(new File(filepath+"progresstable.xls")); 
			StudentBpo studentbpo=new StudentBpo();
			SubjectBpo subjectbpo=new SubjectBpo();
			
			List<StudentBean> students=studentbpo.getAllinfo(specid,classname, sname);
			if(students.size()==0) throw new Exception("û������������ѧ����");
			Iterator<StudentBean> it=students.iterator();
			int sheetnum=0;
			while(it.hasNext()){
				StudentBean temp=it.next();
				SubjectBean subject=subjectbpo.getSubjectByStuPicked(temp.getStuid());
				if(subject==null) continue;
				progessTableContent(workbook,sheetnum,temp,subject);
				sheetnum=sheetnum+1;
			}
			if(sheetnum==0) throw new Exception("����������ѧ������û��ȷ����Ӧ�Ŀ��⣬�����޷������κν��̱�");
			//д���ļ�
			workbook.write();
		}catch(Exception e){
			throw e;
		}finally{
			if(workbook!=null) workbook.close();
		}
	}
		
	/**�������̱�������*/
	private void progessTableContent(WritableWorkbook workbook,int sheetnum,StudentBean student,SubjectBean subject)throws Exception{
		WritableSheet sheet = workbook.createSheet(subject.getSubid()+"���̱�", sheetnum);
		/**���ø�������*/
		WritableFont wfont12=new WritableFont(WritableFont.createFont("����"),12);//���õ�Ԫ������(���塢С�ĺš��Ӵ֣�
		
		WritableFont wfont14bold=new WritableFont(WritableFont.createFont("����"),14);//���õ�Ԫ������(���塢�ĺš��Ӵ�)
		wfont14bold.setBoldStyle(WritableFont.BOLD);
		
		WritableFont wfont11=new WritableFont(WritableFont.createFont("����"),11);//���õ�Ԫ������(���塢�ĺ�)
		/**��6�У�����ÿ���п�*/
		sheet.setColumnView( 0,10 );//�����п�
		sheet.setColumnView( 1,14 );//�����п�
		sheet.setColumnView( 2,14 );//�����п�
		sheet.setColumnView( 3,16 );//�����п�
		sheet.setColumnView( 4,14 );//�����п�
		sheet.setColumnView( 5,18 );//�����п�
		/**����1*/
		sheet.mergeCells(0, 0, 5, 0);//(col,row)
		//���õ�1�У��и�Ϊ600.��д����1
		sheet.setRowView(0, 800);
		Label label=new Label(0,0,"ɽ��������ѧ��ҵ��ƣ����ģ��������̱�",this.setCellFormatWithoutBorder(Alignment.CENTRE, wfont14bold));
		sheet.addCell(label);
		/**����2-4*/
		//��2��
		sheet.setRowView(1, 400);
		sheet.addCell(new Label(0,1,"�༶",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(1,1,student.getClassname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(2,1,"ѧ������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(3,1,student.getSname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(4,1,"ָ����ʦ",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		String tutorname=subject.getTutor().getTname();
		if(subject.getOthertutor()!=null){
			tutorname=tutorname+" "+subject.getOthertutor().getTname();
		}
		sheet.addCell(new Label(5,1,tutorname,this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		//��3��
		sheet.setRowView(2, 400);
		sheet.mergeCells(0, 2, 1, 2);//(col,row)�ϲ�ǰ����
		sheet.addCell(new Label(0,2,"��ƣ����ģ���Ŀ",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.mergeCells(2, 2, 5, 2);//(col,row)�ϲ�ǰ����
		sheet.addCell(new Label(2,2,subject.getSubname(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		//��4��
		sheet.setRowView(3, 400);
		sheet.mergeCells(0, 3, 1, 3);//(col,row)�ϲ�ǰ����
		sheet.mergeCells(2, 3, 3, 3);
		sheet.mergeCells(4, 3, 5, 3);
		sheet.addCell(new Label(0,3,"ʱ�䣨��������д��",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(2,3,"Ӧ��ɵĹ�������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		sheet.addCell(new Label(4,3,"������",this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
		/**����(ѭ��)5-10*/
		List<ProgressBean> progresses=subject.getProgress();
		int initrownum=4;//��5�п�ʼ
		int progsize=progresses.size();//������
		SysarguBpo sysargubpo=new SysarguBpo();
		for(int i=0;i<progsize;i++){
			ProgressBean progress=progresses.get(i);
			int rowheighttemp=this.evaluateRowHeight(11, progress.getContent());
			if(this.evaluateRowHeight(11, progress.getCheckopinion())+3*400>rowheighttemp) rowheighttemp=this.evaluateRowHeight(11, progress.getCheckopinion())+3*400;
			int currentrownum=initrownum+i;
			sheet.setRowView(currentrownum, rowheighttemp);
			sheet.mergeCells(0, currentrownum, 1, currentrownum);//(col,row)�ϲ�ǰ����
			sheet.mergeCells(2, currentrownum, 3, currentrownum);
			sheet.mergeCells(4, currentrownum, 5, currentrownum);
			
			sheet.addCell(new Label(0,currentrownum,progress.getStartenddate(),this.setCellFormatWithThinLine(Alignment.CENTRE, wfont12)));
			sheet.addCell(new Label(2,currentrownum,progress.getContent(),this.setCellFormatWithThinLine(Alignment.LEFT, wfont12)));
			sheet.addCell(new Label(4,currentrownum,progress.getCheckopinion()+"\n          "+tutorname+"\n                "+sysargubpo.getFillinDate(Integer.valueOf(progress.getInorder())),this.setCellFormatWithThinLine(Alignment.LEFT, wfont12)));
		}
		/**β��11�������ƣ�*/
		int tailrownum=initrownum+progsize;
		sheet.setRowView(tailrownum, 300);
		sheet.mergeCells(0, tailrownum, 5, tailrownum);
		sheet.addCell(new Label(0,tailrownum,"������",this.setCellFormatWithoutBorder(Alignment.RIGHT, wfont11)));
		/**β��12��ע��*/
		sheet.setRowView(tailrownum+1, 600);
		sheet.mergeCells(0, tailrownum+1, 5, tailrownum+1);
		sheet.addCell(new Label(0,tailrownum+1,"ע����Ӧ��ɵĹ������ݡ�����������������Ӧ��ϸ��д���ල���������ָ����ʦǩ�ּ�������ڡ�",this.setCellFormatWithoutBorder(Alignment.LEFT, wfont11)));
	}
	/**���õ�Ԫ���ʽ����ϸ���ߣ�*/
	private WritableCellFormat setCellFormatWithThinLine(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//���ݶ��뷽ʽ
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.THIN);//�߿�Ϊϸ��
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//֧���Զ�����
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutButtom(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//���ݶ��뷽ʽ
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.NONE);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.THIN);//�߿�Ϊϸ��
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//֧���Զ�����
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutTop(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//���ݶ��뷽ʽ
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.THIN);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.NONE);//�߿�Ϊϸ��
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//֧���Զ�����
		
		return wccontentcentre;
	}
	private WritableCellFormat setCellFormatWithoutTopButtom(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//���ݶ��뷽ʽ
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setBorder(Border.BOTTOM, BorderLineStyle.NONE);//�߿�Ϊϸ��,Ĭ��Ϊnone
		wccontentcentre.setBorder(Border.TOP, BorderLineStyle.NONE);//�߿�Ϊϸ��
		wccontentcentre.setBorder(Border.LEFT, BorderLineStyle.THIN);
		wccontentcentre.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		wccontentcentre.setWrap(true);//֧���Զ�����
		
		return wccontentcentre;
	}
	/**���õ�Ԫ���ʽ���ޱ��ߣ�*/
	private WritableCellFormat setCellFormatWithoutBorder(Alignment alignment,WritableFont wfont) throws Exception{
		WritableCellFormat wccontentcentre=new WritableCellFormat(wfont);
		wccontentcentre.setAlignment(alignment);//���ݶ��뷽ʽ
		wccontentcentre.setVerticalAlignment(VerticalAlignment.CENTRE);
		wccontentcentre.setWrap(true);//֧���Զ�����
		
		return wccontentcentre;
	}
}

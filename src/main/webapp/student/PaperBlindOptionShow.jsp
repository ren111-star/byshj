<%@page import="bean.SubjectBean"%>
<%@page import="bean.ReviewPaperBean"%>
<%@page import="bpo.ReviewPaperBpo"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>
<%
	//检查用户是否合法 2015.6.10
  	String userid=(String)session.getAttribute("userid");
  	String usertype=(String)session.getAttribute("usertype");
  	if(userid==null||"".equals(userid)) {
  		response.sendRedirect("../UserLogin.jsp");
  	}
  	
  	/////
	String stuid=request.getParameter("stuid");
	String subid="";
	String errmsg="";
	SubjectBean subject=null;
	ReviewPaperBean reviewpaper=null;
	float totalgrade;
	try{
		bpo.StusubBpo stusubbpo=new bpo.StusubBpo();
		subid=stusubbpo.getSubidForStu(stuid);
		ReviewPaperBpo reviewpaperbpo=new ReviewPaperBpo();
		reviewpaper=reviewpaperbpo.getReviewOpinion(subid);
		if(reviewpaper==null) reviewpaper=new ReviewPaperBean();
		bpo.SubjectBpo subjectbpo=new bpo.SubjectBpo();
		subject=subjectbpo.getBysubid(subid);
		totalgrade=reviewpaper.getSignificance()+reviewpaper.getDesigncontent()+reviewpaper.getComposeability()+reviewpaper.getTranslationlevel()+reviewpaper.getInnovative();
	}catch(Exception e){
		throw e;
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>查看说明书（论文）盲审意见[<%=stuid %>]</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
		<link rel="stylesheet" type="text/css" href="../css/common.css" />
		<script src="../js/jquery-1.4.2.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="wrapper">
			<div id="header">
				<div class="logo">
				</div>
			</div>
			<div id="content">
				<table class="mtable2">
					<caption>
					    <span>毕业设计评阅人评审表（盲审）</span>&nbsp;&nbsp;&nbsp;&nbsp;[
						<span id="subname">课题名：<%=subject.getSubname()%></span>
						<input id="subid" type="hidden" value=<%=subid%>></input>&nbsp;&nbsp; 
						指导教师：<span id=tutorname><%=subject.getTutor().getTname()%><%=subject.getOthertutor().getTname()%> </span>
							<img src="../images/loading.gif" name="loadingimg" style="display:none">
							<span name="errmsg0" class="flag"></span>]
					 </caption>
					<tr class="caption"><td align="center" style="width:180px">评审内容</td><td align="center" style="width:400px">具 体 要 求</td><td align="center">分值</td><td align="center">评分</td></tr>
		 			<tr>
		 				<td align="center">选题意义</td>
		 				<td align="center">选题的理论意义或实际应用价值，选题的专业性与新颖性</td>
		 				<td align="center">0－2</td>
		 				<td align="center"><%=reviewpaper.getSignificance()%></td>
		 			</tr>
		 			<tr>
		 				<td align="center">毕业设计内容</td>
		 				<td align="center">设计内容的难易程度、工作量；方案设计的科学性和合理性；实验真实性和正确性；结论合理性、结果的应用价值；学生掌握基础理论、专业知识、基本工程方法和技能情况；学生分析问题和解决问题的能力</td>
		 				<td align="center">0－10</td>
		 				<td align="center"><%=reviewpaper.getDesigncontent()%></td>
		 			</tr>
		 			<tr>
		 				<td align="center">设计说明书撰写能力</td>
		 				<td align="center">概念清晰与分析、设计严谨的程度；术语准确性，文字通顺程度；写作规范性及文字表达能力</td>
		 				<td align="center">0－3</td>
		 				<td align="center"><%=reviewpaper.getComposeability()%></td>
		 			</tr>
		 			<tr>
		 				<td align="center">文献查阅外文翻译</td>
		 				<td align="center">文献查阅的广度和深度，对学科或行业领域知识的了解程度，对文献资料的掌握及综述能力；外文翻译准确性、表述水平及写作规范性</td>
		 				<td align="center">0－3</td>
		 				<td align="center"><%=reviewpaper.getTranslationlevel()%></td>
		 			</tr>
		 			<tr>
		 				<td align="center">创新性</td>
		 				<td align="center">对前人工作有一定的改进或突破，或有独特见解。</td>
		 				<td align="center">0－2</td>
		 				<td align="center"><%=reviewpaper.getInnovative()%></td>
		 			</tr>
		 			<tr><td colspan=3 align="right">评阅人评分（保留1位小数）合计：</td><td align="center"><%=totalgrade%></td></tr>
		 			<tr><td colspan=1 >评阅结果（请在相应项上划“√”）</td>
		 			    <td colspan=3>&nbsp;&nbsp;
			 			    <%if(totalgrade>=16&&totalgrade<20){%>
			 			    	  <span class="flag">√</span>
			 			     <%}%> 评审通过[16,20]
			 			     &nbsp;&nbsp;&nbsp;&nbsp;
			 			    <%if(totalgrade>=12&&totalgrade<16){%>
			 			    	  <span class="flag">√</span>
			 			     <%}%> 修改后通过[12,16) &nbsp;&nbsp;&nbsp;&nbsp;
			 			     <%if(totalgrade<12){%>
			 			    	  <span class="flag">√</span>
			 			     <%}%>不通过[0,12)
		 			     </td>
		 			</tr>
		 			<tr><td colspan=4>评阅意见：<br><%=reviewpaper.getReviewopinion().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>
		 			<tr><td colspan=4 align="right"><%=reviewpaper.getReviewtime()%></td></tr>
		 		</table>
			</div>
		</div>
	</body>
</html>
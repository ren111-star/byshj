<%@page import="java.util.Set,java.util.HashSet"%>
<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" 
import="bpo.SysarguBpo,bean.SysarguBean,java.util.Date" errorPage="../error.jsp"%>
<%
	SysarguBpo sysargubpo=new SysarguBpo();
	String startdate=sysargubpo.getSysargu("startdate").getArguvalue();
	int progressnum=Integer.parseInt(sysargubpo.getSysargu("graduateweeknum").getArguvalue());
	//2015-11-29 wxh
	String graduateweeknumstr=sysargubpo.getSysargu("graduateweeknum").getArguvalue();;//毕业设计周数
	int graduateweeknum=Integer.parseInt(graduateweeknumstr);
	
	Date currentdate=new Date();
	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	Date gradstartdate=format.parse(startdate);
	long day=(currentdate.getTime()-gradstartdate.getTime())/(24*60*60*1000);//相隔天数
	long weeknum=0;
	if(day>=0) weeknum=day/7+1;//周次
	if(weeknum>graduateweeknum) weeknum=graduateweeknum;//最多12周
	//处理需要修改的周总结周次
	String modifiedweekorders=sysargubpo.getSysargu("modifiedweekorders").getArguvalue();
	String[] weekorderarr=modifiedweekorders.split(",");
	Set<Integer> weekorderset=new HashSet<Integer>();
	int weeknumint=(int)weeknum;
	weekorderset.add(weeknumint);
	for(int i=0;i<weekorderarr.length;i++){
		int temp=Integer.parseInt(weekorderarr[i]);
		if(temp==0) {//当前周
		    if(weeknumint>1){
		    	weekorderset.add(weeknumint-1);//设置当前周和上一周的周总结可修改
		    }
			continue;
		}
		weekorderset.add(temp);
	}
%>
<div id="myweeksum">
<%
if(weeknum==0){%><span class="highlight">毕业设计还未开始，无法填写周总结！</span><br> <%}
for(int i=0;i<progressnum;i++){
    String weekorder=String.valueOf(i+1);
    if(i+1<=weeknum){
    	if(weekorderset.contains(i+1)){%>
    	 	<div class="weekwin">
    	 		<table width="100%">
    	 			<tr><td align="center"><img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
    	 				<label class="highlight weeksumwithcontent" <%if(i+1==weeknum){ %>name="currentweeksumlabel"<%}%>>第<%=weekorder%>周 周总结</label>
    	 				<input type="hidden" value="<%=weekorder%>"/></td></tr>
    	 			<tr><td>
	    	 			<table class="mtable1" id="weeksumtb<%=weekorder%>" <%if(i+1!=weeknum){ %>style="display:none"<%} %>>
				    	 	<tr><td width="80px">本周工作总结</td><td><textarea rows=8 name="thiscontent"></textarea></td></tr>
							<tr><td>要求的支持和工作建议</td><td><textarea rows=8 name="support"></textarea></td></tr>
							<tr><td>下周工作安排</td><td><textarea rows=8 name="nextcontent"></textarea></td></tr>
							<tr><td>教师对问题的处理</td><td class="readonlyborder"><span name="tutorreply"></span></td></tr>
							<!--<tr><td>教师评价考核</td><td class="readonlyborder"><span name="tutorreview"></span></td></tr> -->
							<tr><td colspan=2 align="right"><img alt="" src="../images/loading.gif" name="loadingimg" style="display:none"><input type="button" value="保存" name="savebtn"/><input type="hidden" value="<%=weekorder%>"/></td></tr>
						</table>
						</td>
    	 			</tr>
    	 		</table>
	    	 	
			</div>
    	<%
    	}else{
    	%>
	    	<div class="weekwin">
	    	<table style="width:100%">
	    		<tr><td align="center"><img src="../images/loading.gif" name="loadingimg" style="display:none"><label class="weeksumwithcontent">第<%=weekorder%>周 周总结</label><input type="hidden" value="<%=weekorder%>"/></td></tr>
	    		<tr><td>
		    		<table class="mtable1" id="weeksumtb<%=weekorder%>" style="display:none">
			    		<tr><td width="80px" class="readonlyborder">本周工作总结</td><td class="readonlyborder"><span name="thiscontent"></span></td></tr>
						<tr><td class="readonlyborder">要求的支持和工作建议</td><td class="readonlyborder"><span name="support"></span></td></tr>
						<tr><td class="readonlyborder">下周工作安排</td><td class="readonlyborder"><span name="nextcontent"></span></td></tr>
						<tr><td class="readonlyborder">教师对问题的处理</td><td class="readonlyborder"><span name="tutorreply"></span></td></tr>
						<!--<tr><td class="readonlyborder">教师评价考核</td><td class="readonlyborder"><span name="tutorreview"></span></td></tr> -->
					</table>
	    		</td></tr>
	    	</table>
			</div>
    	<%
    	}
    }else{%>
    	<label class="weeksumwithoutcontent">第<%=weekorder%>周 周总结</label>
    	<br/>
    <%
    }
}
%>
</div>
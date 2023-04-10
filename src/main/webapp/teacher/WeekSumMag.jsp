<%@page import="java.util.Set,java.util.HashSet"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"
		 import="bpo.SysarguBpo,bean.SysarguBean,java.util.Date,bpo.StusubBpo,bean.StusubBean,java.util.List,bpo.WeekSumBpo" errorPage="../error.jsp"%>
<%
	String tid=request.getParameter("userid");
	SysarguBpo sysargubpo=new SysarguBpo();
	String startdate=sysargubpo.getSysargu("startdate").getArguvalue();
	//2015-11-29 wxh
	String graduateweeknumstr=sysargubpo.getSysargu("graduateweeknum").getArguvalue();;//毕业设计周数
	int graduateweeknum=Integer.parseInt(graduateweeknumstr);
	
	Date currentdate=new Date();
	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	Date gradstartdate=format.parse(startdate);
	long day=(currentdate.getTime()-gradstartdate.getTime())/(24*60*60*1000);//相隔天数
	long weeknum=0;
	if(day>=0) weeknum=day/7+1;//周次
	if(weeknum>graduateweeknum) weeknum=graduateweeknum; //2015-11-29 wxh
	//处理需要修改的周总结周次
	String modifiedweekorders=sysargubpo.getSysargu("modifiedweekorders").getArguvalue();
	String[] weekorderarr=modifiedweekorders.split(",");
	Set<Integer> weekorderset=new HashSet<>();
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
	if(weeknum==0){%><span class="highlight">毕业设计还未开始，无任何周总结！</span><br> <%}
	else{//查询每个学生及当前周之前的周总结
		StusubBpo stusubBpo=new StusubBpo();
		List<StusubBean> stusubs=stusubBpo.getStusubBytid(tid);
		WeekSumBpo weeksumbpo=new WeekSumBpo();
	%>
<!-- 周总结管理div-->
<table class="mtable2" id="weeksummagtb">
	<caption>周总结管理</caption>
	<%
	   for(int i=0;i<stusubs.size();i++){
	   	  StusubBean stusub=stusubs.get(i);
	   %>
	   <tr>
	   	  <td width=60px><%=stusub.getSname()%></td>
	   	  <td>
	   	     <span><%=stusub.getSubname()%></span>&nbsp;&nbsp;
	   	     <img src="../images/loading.gif" name="loadingimg" style="display:none"><input type="button" value="管理周总结" name="weeksummag" title="显示/隐藏周总结界面"/>&nbsp;&nbsp;
	   	     <img src="../images/loading.gif" name="loadingimg" style="display:none"><input type="hidden" value="<%=stusub.getStuid()%>"/><input type="button" value="生成进程表" name="generateprogress"/>
	   	     <span name="weekupstatus" class="flag">已完成周总结数<%=weeksumbpo.getWeekupNum(stusub.getStuid()) %></span><span name="errmsg" class="flag"></span>
	   	     <div style="display:none"><hr/>
	   	     	<%
	   	     	for(int j=0;j<weeknum;j++){
   					String weekorder=String.valueOf(j+1);
   					if(weekorderset.contains(j+1)){%>
   						<div class="weekwin">
			    	 		<table width="100%">
			    	 			<tr><td align="center"><img src="../images/loading.gif" name="loadingimg" style="display:none">
			    	 				<label class="highlight weeksumwithcontent" <%if(j+1==weeknum){ %>name="currentweeksumlabel"<%}%>>第<%=weekorder%>周 周总结</label>
			    	 				<input type="hidden" value="<%=weekorder%>"/><input type="hidden" value="<%=stusub.getStuid()%>"/></td></tr>
			    	 			<tr><td>
				    	 			<table class="mtable1" name="weeksumtb<%=weekorder%>" style="display:none">
							    	 	<tr><td width="80px">本周工作总结</td><td class="readonlyborder"><span name="thiscontent"></span></td></tr>
										<tr><td>要求的支持和工作建议</td><td class="readonlyborder"><span name="support"></span></td></tr>
										<tr><td>下周工作安排</td><td class="readonlyborder"><span name="nextcontent"></span></td></tr>
										<tr><td>教师对问题的处理</td><td><textarea rows=4 name="tutorreply"></textarea></td></tr>
										<tr><td>教师评价考核</td><td><textarea rows=4 name="tutorreview"></textarea></td></tr>
										<tr><td colspan=2 align="right"><img src="../images/loading.gif" name="loadingimg" style="display:none"><input type="button" value="保存" name="savebtn"/><input type="hidden" value="<%=weekorder%>"/><input type="hidden" value="<%=stusub.getStuid()%>"/></td></tr>
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
				    		<tr><td align="center"><img src="../images/loading.gif" name="loadingimg" style="display:none"><label class="weeksumwithcontent">第<%=weekorder%>周 周总结</label><input type="hidden" value="<%=weekorder%>"/><input type="hidden" value="<%=stusub.getStuid()%>"/></td></tr>
				    		<tr><td>
					    		<table class="mtable1" name="weeksumtb<%=weekorder%>" style="display:none">
						    		<tr><td width="80px" class="readonlyborder">本周工作总结</td><td class="readonlyborder"><span name="thiscontent"></span></td></tr>
									<tr><td class="readonlyborder">要求的支持和工作建议</td><td class="readonlyborder"><span name="support"></span></td></tr>
									<tr><td class="readonlyborder">下周工作安排</td><td class="readonlyborder"><span name="nextcontent"></span></td></tr>
									<tr><td class="readonlyborder">教师对问题的处理</td><td class="readonlyborder"><span name="tutorreply"></span></td></tr>
									<tr><td class="readonlyborder">教师评价考核</td><td class="readonlyborder"><span name="tutorreview"></span></td></tr>
								</table>
				    		</td></tr>
				    	</table>
					</div>
			    	<%
   					}
   				}
	   	     	%>
	   	     </div>
	   	  </td>
	   </tr>
	<%   	  
	   } 
	%>
</table>
	<%	
	}
%>

	


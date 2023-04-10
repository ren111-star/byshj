<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"
		 errorPage="error.jsp"%>
<%
request.getSession().removeAttribute("userid"); 
request.getSession().removeAttribute("username"); 
//session.setMaxInactiveInterval(50); file://Sesion有效时长，以秒为单位 

//String usercount="";
String usercount=application.getAttribute("userCount").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<title>计算机学院毕业设计过程管理系统-用户登录</title>
		<link rel="stylesheet" type="text/css" href="css/common.css" />
		<script src="js/jquery-1.4.2.js" type="text/javascript"></script>
		<script src="js/jquery.form.js" type="text/javascript"></script>
		<script src="js/md5.js" type="text/javascript"></script>
		<script type="text/javascript">
			var vusertype;
			var vuserid;
			$(document).ready(function(){   
				//输入密码后，在隐藏域中设置加密后的密码	
				$("#olduserpwd").change(function(){
					$('#userpwd').val(hex_md5($("#olduserpwd").val()));
				});
	   		});
	   		function loginon(){
				$('#userform').ajaxSubmit({
					dataType:'json',
					beforeSubmit: validate,
					success:function(data){
	   					//若用户输入错误，则提示
	   					if(data!=""){   						
							$('#errflag').text(data);
							return;
	   					}
	   					//否则跳转到相应页面
	   					if(vusertype=="管理员"){
							 location.href="manager/ManagerMag.jsp";
							 return;
	   					}
	   					if(vusertype=="教师"){   						
							location.href="teacher/TeacherMag.jsp";
							return;
	   					}
	   					if(vusertype=="学生"){	   						
							location.href="student/StudentMag.jsp";
							return;
	   					}
	   				}
	   			});
	   		}
	   		function validate(formdata){
	   			vusertype=formdata[0].value;
	   			vuserid=formdata[1].value;
	   			var vuserpwd=$("#olduserpwd").val();
	   			if(vusertype==""){	   				
					$('#errflag').text("用户类型不能为空!");
					$('#usertype').focus();
	   				return false;
	   			}
	   			if(vuserid==""){	   				
					$('#errflag').text("用户名不能为空!");
					$('#userid').focus();
	   				return false;
	   			}
	   			if(vuserpwd==""){	   				
					$('#errflag').text("密码不能为空!");
					$('#olduserpwd').focus();
	   				return false;
	   			}
	   			return true;
	   		}
		</script>	
	</HEAD>
	<body background="images1/login_bg.gif" MS_POSITIONING="GridLayout">
	<div style="top:100">
	<form id="userform" action="LoginServlet" method="post">
			<TABLE id="Table1" cellSpacing="0" cellPadding="0" width="707" align="center" border="0">
				<TR>
					<TD colSpan="9"><IMG height="27" src="images1/login_01.gif" width="707"></TD>
				</TR>
				<TR>
					<TD style="HEIGHT: 68px" colSpan="9">
						<TABLE id="Table2" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<TR>
								<TD background="images1/wyx.JPG" height="60"><FONT face="宋体"></FONT></TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD colSpan="9"><IMG height="29" src="images1/login_03.gif" width="707"></TD>
				</TR>
				<TR>
					<TD width="170" rowSpan="6"><IMG styl="WIDTH: 170px; HEIGHT: 244px" height="244" src="images1/login_04.gif"
							width="170"></TD>
					<TD colSpan="7"><IMG height="47" src="images1/login_05.gif" width="432"></TD>
					<TD width="105" rowSpan="6"><IMG style="WIDTH: 105px; HEIGHT: 244px" height="244" src="images1/login_06.gif"
							width="105"></TD>
				</TR>
				<TR>
					<TD style="HEIGHT: 182px" width="53" rowSpan="4"><IMG style="WIDTH: 53px; HEIGHT: 182px" height="182" src="images1/login_07.gif"
							width="53"></TD>
					<TD width="53" background="images1/login_bg2.gif" height="142"><FONT face="宋体"></FONT></TD>
					<TD width="58" background="images1/login_bg2.gif" height="142"><IMG src="images1/wyx1.jpg"><FONT face="宋体"></FONT></TD>
					<TD vAlign="top" background="images1/login_bg2.gif" colSpan="3">
						<TABLE id="Table3" cellSpacing="0" cellPadding="0" width="100%" border="0">
							<TR bgcolor="#ffffff">
								<TD height="13"><FONT face="宋体"></FONT></TD>
							</TR>
						</TABLE>
						<TABLE id="Table4" cellSpacing="0" cellPadding="3" width="100%" border="0">
							<TR>
								<TD vAlign="bottom" height="30" bgcolor="#ffffff"><select id="usertype" name="usertype">
								<option value="">[请选择]</option>
								<option value="管理员">管理员</option>
								<option value="教师">教师</option>
								<option value="学生">学生</option>
							</select></TD>
							</TR>
							<TR>
								<TD vAlign="bottom" height="36" bgcolor="#ffffff"><input id="userid" name="userid" type="text" class="text" style="width:120px"></input></TD>
							</TR>
							<TR>
								<TD vAlign="bottom" height="36" bgcolor="#ffffff">
								    <input id="olduserpwd"  type="password" class="text" style="width:120px"></input>
								    <input id="userpwd" name="userpwd" type="hidden"></input>
									<span id="errflag" style="font-size:12px;color:red"></span></TD>
							</TR>
							<tr><td><span style="font-size:12px">当前在线&nbsp;<%=usercount%>&nbsp;人</span></td></tr>
						</TABLE>
					</TD>
					<TD style="HEIGHT: 182px" width="13" rowSpan="4" ><IMG style="WIDTH: 12px; HEIGHT: 172px" height="172" src="images1/login_11.gif"
							width="12"></TD>
				</TR>
				<TR>
					<TD colSpan="5"><IMG height="2" src="images1/login_12.gif" width="366"></TD>
				</TR>
				<TR background="images1/login_13.gif">
					<TD style="HEIGHT: 36px" background="images1/login_17.gif" colSpan="2" rowSpan="2"></TD>
					<TD width="57"><IMG height="11" src="images1/login_14.gif" width="57"></TD>
					<TD style="HEIGHT: 36px" width="6" rowSpan="2"><IMG height="36" src="images1/login_15.gif" width="6"></TD>
					<TD style="HEIGHT: 36px" vAlign="bottom" width="192" background="images1/login_17.gif"
						height="36" rowSpan="2"><input type="reset" value="重置" class="btn"></input></TD>
					</TR>
				<TR>
					<TD style="HEIGHT: 25px" vAlign="bottom" height="36"  background="images1/login_17.gif" >
					<input type="button" value="登录" onclick="loginon()" class="btn"></input></TD>
				</TR>
				<TR>
					<TD colSpan="8"><IMG height="15" src="images1/login_20.gif" width="432"></TD>
				</TR>
				<TR background="images1/login_bottom_bg.gif">
					<TD background="images1/login_bottom_bg.gif" colSpan="10" height="68">
						<DIV align="left">
							<TABLE id="Table5" cellSpacing="0" cellPadding="0" width="100%" border="0">
								<TR>
									<TD height="8"><FONT face="宋体"></FONT></TD>
								</TR>
							</TABLE>
						</DIV>
					</TD>
				</TR>
				<TR><td colSpan="10" align="center"><jsp:include page="footer.jsp"  flush="true"/></td></TR>
			</TABLE>
		</form>
		
		</div>
		
	</body>
</HTML>


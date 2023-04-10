<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="error.jsp"%>
<% 
	String userid=(String)session.getAttribute("userid");
  	if(!(userid==null||"".equals(userid))) {
  		throw new Exception("用户 "+userid+" 已登录。请退出后再用其他用户登录。");
  	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>计算机学院毕业设计过程管理系统-用户登录</title>
		<script src="js/jquery-1.4.2.js" type="text/javascript"></script>
		<script src="js/jquery.form.js" type="text/javascript"></script>
		<script src="js/md5.js" type="text/javascript"></script>
		<script type="text/javascript">
			var vusertype;
			var vuserid;
			$(document).ready(function(){   
				//输入密码后，在隐藏域中设置加密后的密码	
				$("#userpwd").change(function(){
					$('#md5userpwd').val(hex_md5($("#userpwd").val()));
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
	   			var vuserpwd=formdata[2].value;
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
					$('#userpwd').focus();
	   				return false;
	   			}
	   			return true;
	   		}
		</script>
	</head>
	<body>
		<div>
			<form id="userform" action="LoginServlet" method="post">
				<table>
					<tr>					
						<td>用户类型：</td>
						<td>		
							<select id="usertype" name="usertype">
								<option value="">[请选择]</option>
								<option value="管理员">管理员</option>
								<option value="教师">教师</option>
								<option value="学生">学生</option>
							</select>
						</td>
					</tr>
					<tr><td>用户名：</td><td><input id="userid" name="userid" type="text"></input></td></tr>
					<tr><td>密  码：</td><td><input id="userpwd" name="userpwd"  type="password"></input>
						<input id="md5userpwd" name="md5userpwd" type="hidden"></input></td></tr>
					<tr><td><input type="button" value="登录" onclick="loginon()"></input></td>
						<td><input type="reset" value="重置"></input></td>
					</tr>
					<tr><td colspan=2><span id="errflag"></span></td></tr>
				</table>
			</form>
		</div>
	</body>
</html>
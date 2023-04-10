<%@ page contentType="text/html; charset=gb2312" language="java"  isErrorPage="true"%>
<% 
	//exception = (Throwable)request.getAttribute("javax.servlet.error.exception"); 
%>
<html>
<head>
<title>Servlet错误页面</title>
</head>

<body>
   <div id="error">发生了以下错误：</div><br><hr>
   <%=exception.getMessage() %><br><hr>
   <br>
</body>
</html>
<%@ page contentType="text/html; charset=gb2312" language="java"  isErrorPage="true"%>
<% 
	//exception = (Throwable)request.getAttribute("javax.servlet.error.exception"); 
%>
<html>
<head>
<title>Servlet����ҳ��</title>
</head>

<body>
   <div id="error">���������´���</div><br><hr>
   <%=exception.getMessage() %><br><hr>
   <br>
</body>
</html>
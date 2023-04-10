<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="error.jsp"%>
<%
//清空session
	request.getSession().invalidate(); 
	response.sendRedirect("UserLogin.jsp");
%>

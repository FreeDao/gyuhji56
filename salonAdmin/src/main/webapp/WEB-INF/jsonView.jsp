<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*" %>
<%
System.out.println("in jsonView2"); 
	ObjectMapper mapper = new ObjectMapper(); 
	JsonGenerator gen = mapper.getFactory().createGenerator(out);

	gen.writeObject(request.getAttribute("msg"));
	
	%>

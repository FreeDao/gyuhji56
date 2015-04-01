<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*" %>
<%
	ObjectMapper mapper = new ObjectMapper(); 
	JsonGenerator gen = mapper.getFactory().createGenerator(out);
%>

	<%
	WebScanDaoImpl imp = new WebScanDaoImpl();
	/**for (int x = 0; x < 10; x++) {
		SourceDetectVo vo = imp.selectSourceDetectedById(x);
		out.println(vo);
	}**/
	Object obj = imp.selectSourceDetectedList(0,100);
	gen.writeObject(obj);
	
	%>

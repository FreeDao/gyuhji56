<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<html>
<head>
    <title>案例列表</title>
<style>
a{
	text-decoration: none;
}
</style>
</head>

<body>
<h2>案例列表</h2>

<div style="width:600px">
<c:forEach items="${msg.list}" var="art"> 
<div><c:out value="${art.host}" />
 <a href="anliArtDetail.html?anliArtId=${art.host}">${art.host } &nbsp;</a></div> 

</c:forEach>
</div>

<div style="background-color:#eee; margin:8px;width:900px">
共${msg.pageSplit.totalItem}项
&nbsp; <a href='?pi=1'>首页</a>&nbsp;

<c:forEach items="${msg.pageSplit.pageIndexes}" var="pi"> 
&nbsp; <a href='?pi=${pi }'>${pi }</a>
</c:forEach>
&nbsp; <a href='?pi=1'>尾页/${msg.pageSplit.totalPageNum}</a>&nbsp;
</div>
</body>
</html>

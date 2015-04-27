<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="java.util.*,rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Map msg = ((Map) request.getAttribute("msg"));
	boolean watching = (Boolean) msg.get("watching");
%>

<div class="row">
	<div class="col-md-4">
		<div
			class="portlet box <c:choose>
					<c:when test="${msg.watching }">green</c:when>
					<c:when test="${msg.audited }">grey</c:when>
					<c:otherwise>red</c:otherwise>
					</c:choose>">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-comments"></i> <span id=hostName><c:out
							value="${msg.host }" /></span> （
					<c:choose>
						<c:when test="${msg.watching }">
					已经监控
					</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${msg.audited }">已忽略</c:when>
								<c:otherwise>未审核</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					）
				</div>

			</div>

			<div class="portlet-body ">
				<div class="clearfix">
					<button type="button" id="watchHostButton" class="btn btn-success">增加到监控列表</button>
					<button type="button" id="ignoreHostButton" class="btn btn-warning">忽略</button>
					<c:if test="${msg.audited }">
						<a href="auditNextHost.html">审核下一条</a>
					</c:if>
				</div>
				<div class="dd" id="nestable_list_1">
					<ol class="dd-list">
						<li class="dd-item" data-id="1">
							<div class="dd-handle" style="height: 100%">
								共<b><c:out value="${fn:length(msg.vos) }" /></b>次检测, 扫描<b><c:out
										value="${msg.level_0  }" /></b>个页面<br>
								<button type="button" class="btn green">
									<c:out value="${msg.level_3 }" />
								</button>
								<button type="button" class="btn yellow">
									<c:out value="${msg.level_2 }" />
								</button>
								<button type="button" class="btn blue">
									<c:out value="${msg.level_1 }" />
								</button>
								<button type="button" class="btn purple">
									<c:out value="${msg.level_0 }" />
								</button>
							</div>

						</li>
						<li class="dd-item" data-id="2">

							<div class="dd-handle">页面列表</div>
							<ol class="dd-list">
								<c:forEach items="${msg.pages }" var="thePage">
									<li class="dd-item" data-id="3">
										<div class="dd-handle">
											<a
												href="?host=<c:out value='${msg.host }'/>&filePath=<c:out value='${thePage.file.absolutePath }'/>">
												<c:out value="${thePage.file.name }" />
											</a>
										</div>
									</li>
								</c:forEach>
							</ol>
						</li>

					</ol>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-8">
		<div class="portlet box green">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-comments"></i><c:out value="${ msg.curPage.file}" />
				</div>
			</div>
			<div class="portlet-body">
				<div class="dd" id="nestable_list_2">
					<ol class="dd-list">

						<li class="dd-item" data-id="15">
							<button data-action="expand" type="button" style="display: none;">Expand</button>
							<div class="dd-handle">
								
							</div>
							<!--  <ol class="dd-list">
								<li class="dd-item" data-id="16">
									<div class="dd-handle"></div>
								</li>
								
							</ol>-->
							<div style="height: 100%">
								<c:out value="${ msg.curPage.wrapedText}" escapeXml="false" />
							</div>


						</li>
					</ol>
				</div>
			</div>
		</div>
	</div>
</div>
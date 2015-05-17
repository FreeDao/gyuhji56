<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="java.util.*,rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="row">
	<div class="col-md-4">
		<div class="portlet box grey">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-comments"></i> <span id=hostName><c:out
							value="${msg.host }" /></span>
				</div>

			</div>

			<div class="portlet-body ">
				<div class="clearfix hide">
						<a href="...">审核下一条</a>
				</div>
				<div class="dd" id="nestable_list_1">
					<ol class="dd-list">
						<li class="dd-item" data-id="1">
							<!-- 共<b><c:out value="${fn:length(msg.vos) }" /></b>次检测,<b> -->
							需审核 <b>${fn:length(msg.vos)}</b> 个页面<br>


						</li>
					</ol>
					<div class="portlet-body">
						<div class="table-responsive">
							<table style="margin-bottom: 10px"
								class="table table-hover table-bordered table-striped">
								<!-- <thead>
									<tr>
										<th>分值</th>
										<th>标题</th>
									</tr>
								</thead> -->
								<tbody id="pages">
									<c:forEach var="detectedPage" items="${msg.vos }">
										<tr id='pageId-${detectedPage.id }' pageId='${detectedPage.id }'
											class=" pageTr leftAudit-${detectedPage.audit_status }">
											<td><c:out value="${detectedPage.score }" /></td>
											<td nowrap><span  
												title='${detectedPage.title }'> ${detectedPage.title } </span></td>
										</tr>
									</c:forEach>

								</tbody>
							</table>
						</div>


					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-8">
		<div class="portlet box blue" id="audit-head">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-comments"></i>
					<%-- 					<c:out value="${ msg.curPage.file}" /> --%>
					<span id="detectedPageTitle"></span>
				</div>
			</div>
			<div class="portlet-body">
				<div class="dd" id="nestable_list_2">

					<div class="hide" id="button-area">
						<button id="btn-prePass" type="button" class="btn green btn-circle btn-default">接受</button>
						
						<button type="button" id="btn-preIgnore" class="btn red  btn-circle btn-default ">忽略</button>
					</div>
					<ol class="dd-list">


						<li class="dd-item" data-id="15">
							<div class="col-md-2">
								ID：<span id="detectedPageId"></span>
							</div>
							<div class="col-md-2">
								分值：<b><span id="detectedPageScore"></span></b>
							</div>
							<div class="col-md-4">
								获取时间: <span id="detectedPageTime" style="display:none"></span>
							</div>
							<div class="col-md-4">
								原址: <a id="detectedPageOrgUrl" target="_blank">打开</a>
							</div>
						</li>

						<li class="dd-item" data-id="15">
							<div class="dd-handle">
								<span id="detectedPageContent"></span>
							</div>
						</li>

					</ol>
				</div>
			</div>
		</div>
	</div>
</div>
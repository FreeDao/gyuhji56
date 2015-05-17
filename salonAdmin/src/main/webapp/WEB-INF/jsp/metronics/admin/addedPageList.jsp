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
					<i class="fa fa-comments"></i> <b>${msg.statusName} :
						${msg.pageSplit.totalItem}</b> 个
				</div>

			</div>

			<div class="portlet-body ">

				<div class="dd" id="nestable_list_1">

					<div class="portlet-body">
						<div class="table-responsive">
							<table class="table table-hover table-bordered table-striped">
								<!-- <thead>
									<tr>
										<th>分值</th>
										<th>标题</th>
									</tr>
								</thead> -->
								<tbody id="pages">
									<c:forEach var="detectedPage" items="${msg.vos }">
										<tr id='pageId-${detectedPage.id }'
											pageId='${detectedPage.id }'
											class="pageTr leftAudit-${detectedPage.audit_status }">
											<td><c:out value="${detectedPage.score }" /></td>
											<td nowrap><span title='${detectedPage.title }'>
													${detectedPage.title } </span></td>
										</tr>
									</c:forEach>

								</tbody>
							</table>
							<div class="dataTables_paginate paging_bootstrap_full_number"
								id="sample_1_paginate">
								<ul class="pagination" style="visibility: visible;">
									<c:set var="urlPrx"
										value="?status_p=${msg.status}&pageIndex_p=" />
									<c:choose>
										<c:when test="${msg.pageSplit.currentPage==1 }">
											<li class="prev disabled"><a><i
													class="fa fa-angle-double-left"></i></a></li>
											<li class="prev disabled"><a title="Prev"><i
													class="fa fa-angle-left"></i></a></li>
										</c:when>
										<c:otherwise>
											<li class="prev "><a href="${urlPrx}1" title="First"><i
													class="fa fa-angle-double-left"></i></a></li>
											<li class="prev "><a href="${urlPrx}${pi-1 }"
												title="Prev"><i class="fa fa-angle-left"></i></a></li>
										</c:otherwise>
									</c:choose>

									<c:forEach items="${msg.pageSplit.pageIndexes}" var="pi">

										<li
											<c:if test="${msg.pageSplit.currentPage==pi }"> class="active"</c:if>><a
											href="${urlPrx}${pi }">${pi }</a></li>


									</c:forEach>
									<c:choose>
										<c:when
											test="${msg.pageSplit.currentPage>=msg.pageSplit.totalPageNum }">
											<li class="next disabled"><a title="Next"><i
													class="fa fa-angle-right"></i></a></li>
											<li class="next disabled"><a title="Last"><i
													class="fa fa-angle-double-right"></i></a></li>
										</c:when>
										<c:otherwise>
											<li class="next"><a
												href="${urlPrx}${msg.pageSplit.currentPage+1 }" title="Next"><i
													class="fa fa-angle-right"></i></a></li>
											<li class="next"><a
												href="${urlPrx}${msg.pageSplit.totalPageNum }" title="Last"><i
													class="fa fa-angle-double-right"></i></a></li>
										</c:otherwise>
									</c:choose>


								</ul>
							</div>
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
						<button id="btn-prePass" type="button"
							class="btn green btn-circle btn-default">接受</button>

						<button type="button" id="btn-preIgnore"
							class="btn red  btn-circle btn-default ">忽略</button>
					</div>
					<ol class="dd-list">


						<li class="dd-item" data-id="15">

							<div class="col-md-2">
								分值：<b><span id="detectedPageScore"></span></b>
							</div>
							<div class="col-md-4" style="display:none">
								获取时间: <span id="detectedPageTime"></span>
							</div>
							<div class="col-md-4">
								原址: <a id="detectedPageOrgUrl" target="_blank">打开</a>
							</div>
						</li>



					</ol>
					<div class="portlet light" style="margin-bottom:0px">

						<div class="portlet-body">
							<div class="scroller" style="height: 350px" data-rail-visible="1"
								data-rail-color="yellow" data-handle-color="#a1b2bd">

								<span id="detectedPageContent"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	updatePageStatusAfterUrl = "${urlPrx}${msg.pageSplit.currentPage }";
	var scoreStr = ${msg.scoreStr};
	//alert(scoreStr[0]);
</script>
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="../../assets/global/plugins/respond.min.js"></script>
<script src="../../assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="../../assets/global/plugins/jquery.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/jquery-migrate.min.js"
	type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="../../assets/global/plugins/jquery-ui/jquery-ui.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/bootstrap/js/bootstrap.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/jquery.blockui.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/jquery.cokie.min.js"
	type="text/javascript"></script>
<script src="../../assets/global/plugins/uniform/jquery.uniform.min.js"
	type="text/javascript"></script>
<script
	src="../../assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js"
	type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script type="text/javascript"
	src="../../assets/global/plugins/select2/select2.min.js"></script>
<script type="text/javascript"
	src="../../assets/global/plugins/datatables/media/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="../../assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="../../assets/global/scripts/metronic.js"
	type="text/javascript"></script>
<script src="../../assets/admin/layout/scripts/layout.js"
	type="text/javascript"></script>
<script src="../../assets/admin/layout/scripts/quick-sidebar.js"
	type="text/javascript"></script>
<script src="../../assets/admin/layout/scripts/demo.js"
	type="text/javascript"></script>
<script src="../../assets/admin/pages/scripts/table-managed.js"></script>


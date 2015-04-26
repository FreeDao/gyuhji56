<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page
	import="java.util.*,rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*"%>
<div class="row">
	<div class="col-md-12">

		<div class="portlet grey-cascade box">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-cogs"></i>Shopping Cart
				</div>
				<div class="tools">
					<a id = "refresher" href="javascript:;" class="reload" data-original-title="" title="">
								</a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="table-responsive">
					<table style="margin-bottom: 10px"
						class="table table-hover table-bordered table-striped">
						<thead>
							<tr>
								<th>id</th>
								<th>网站</th>
								<th>level_3</th>
								<th>level_2</th>
								<th>level_1</th>
								<th>扫描数</th>
								<th>路径</th>
								<th>Discount Amount</th>
								<th>Total</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${msg.list}" var="art">

								<tr>
									<td></td>
									<td><a target=_blank
										href='detectedHostV2.html?host=<c:out value="${art.host}"/>'>
											${art.host} </a></td>
									<td>${art.count}</td>
									<td> </td>
									<td> </td>
									<td> </td>
									<td>-</td>
									<td></td>
									<td></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

				<div class="row">
					<div class="col-md-6 col-sm-12">
						<div class="dataTables_info" id="sample_1_info" role="status"
							aria-live="polite">共${msg.pageSplit.totalItem }项，显示${msg.pageSplit.itemStartIndex +1}到${msg.pageSplit.itemEndIndex }项</div>
					</div>
					<div class="col-md-6 col-sm-12">
						<div class="dataTables_paginate paging_bootstrap_full_number"
							id="sample_1_paginate"> 
							<ul class="pagination" style="visibility: visible;">
								<c:choose>
									<c:when test="${msg.pageSplit.currentPage==1 }">
										<li class="prev disabled"><a><i
												class="fa fa-angle-double-left"></i></a></li>
										<li class="prev disabled"><a title="Prev"><i
												class="fa fa-angle-left"></i></a></li>
									</c:when>
									<c:otherwise>
										<li class="prev "><a href="?pageIndex=1" title="First"><i
												class="fa fa-angle-double-left"></i></a></li>
										<li class="prev "><a href="?pageIndex=${pi-1 }"
											title="Prev"><i class="fa fa-angle-left"></i></a></li>
									</c:otherwise>
								</c:choose>

								<c:forEach items="${msg.pageSplit.pageIndexes}" var="pi">

									<li
										<c:if test="${msg.pageSplit.currentPage==pi }"> class="active"</c:if>><a
										href="?pageIndex=${pi }">${pi }</a></li>


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
											href="?pageIndex=${msg.pageSplit.currentPage+1 }"
											title="Next"><i class="fa fa-angle-right"></i></a></li>
										<li class="next"><a
											href="?pageIndex=${msg.pageSplit.totalPageNum }" title="Last"><i
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
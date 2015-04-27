<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- BEGIN SIDEBAR -->
<div class="page-sidebar-wrapper">
	<div class="page-sidebar navbar-collapse collapse">
		<!-- BEGIN SIDEBAR MENU -->
		<ul class="page-sidebar-menu page-sidebar-menu-closed" data-keep-expanded="false"
			data-auto-scroll="true" data-slide-speed="200">
			<!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->

			<!-- DOC: To remove the search box from the sidebar you just need to completely remove the below "sidebar-search-wrapper" LI element -->
			
			<li class="start "><a href="javascript:;"> <i
					class="icon-home"></i> <span class="title">Dashboard</span> <span
					class="arrow "></span>
			</a>
				<ul class="sub-menu">
					<li class="active"><a href="index.html"> <i
							class="icon-bar-chart"></i> Default Dashboard
					</a></li>
					<li><a href="index_2.html"> <i class="icon-bulb"></i> New
							Dashboard #1
					</a></li>

				</ul></li>

			<!-- END ANGULARJS LINK -->
			<li class="heading">
				<h3 class="uppercase">Features</h3>
			</li>
			<li class=""><a href="javascript:;"> <i
					class="icon-settings"></i> <span class="title">网站探针</span> <span
					class="selected"></span> <span class="arrow open"></span>
			</a>
				<ul class="sub-menu">
					<li><a href="detectedList.html">探针结果</a></li>
					<li><a href="detectedListV2.html">detectedListV2</a></li>
					<li><a href="detectedPageList.html">detectedPageList</a></li>
				</ul></li>

			<li class="active open">
			<a href="javascript:;">
					<i class="icon-docs"></i>
					<span class="title">页面审核</span>
					<span class="arrow  open"></span>
					</a>
			
				<ul class="sub-menu">

					<li><a href="detectedPageList.html?status_p=0">待审页面</a></li>
					<li><a href="detectedPageList.html?status_p=9">接受页面</a></li>
					<li><a href="detectedPageList.html?status_p=3">忽略页面</a></li>
					<li><a href="detectedPageList.html?status_p=9999">已审页面</a></li>
					<li><a href="detectedPageList.html">所有页面</a></li>
					<li><a href="detectedPageList.html?status_p=6">放弃页面</a></li>
				</ul></li>


			<li class="heading">
				<h3 class="uppercase">More</h3>
			</li>


			<li class="last "><a href="javascript:;"> <i
					class="icon-pointer"></i> <span class="title">Maps</span> <span
					class="arrow "></span>
			</a>
				<ul class="sub-menu">
					<li><a href="maps_google.html"> Google Maps</a></li>
					<li><a href="maps_vector.html"> Vector Maps</a></li>
				</ul></li>
		</ul>
		<!-- END SIDEBAR MENU -->
	</div>
</div>
<!-- END SIDEBAR -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="rays.web.rays.dao.mybatis.imp.*, rays.web.rays.vo.*, com.fasterxml.jackson.core.*,  com.fasterxml.jackson.databind.*"%>
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
<script>
	jQuery(document).ready(function() {
		Metronic.init(); // init metronic core components
		Layout.init(); // init current layout
		QuickSidebar.init(); // init quick sidebar
		Demo.init(); // init demo features
		TableManaged.init();
	});
	$(function() {
		$('#ignoreHostButton').click(function() {
			var hostName = $('#hostName').text();
			var statusCode = 'ignore';
			updateHostStatus(hostName, statusCode);
		});
	});
	$(function() {
		$('#watchHostButton').click(function() {
			var hostName = $('#hostName').text();
			var statusCode = 'watching';
			updateHostStatus(hostName, statusCode);
		});
	});
	updateHostStatus = function(hostName, statusCode) {

		$.ajax({
			type : "GET",
			url : "setHostAuditStatus.html",
			data : {
				host : hostName,
				status : statusCode
			},
			dataType : "json",
			success : function(retJson) {
				
				if(retJson.retCode == 200){
					
					if(confirm("命令成功执行！！是否执行下一条？")){
						window.location.href="auditNextHost.html";
					}else{
						alert("OK");
						window.location.reload();
					}
					
				}else{
					alert(JSON.stringify(retJson));
				}
			}
		});
	}
</script>

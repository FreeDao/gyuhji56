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
Date.prototype.format = function(format) {
		/*
		 * format="yyyy-MM-dd hh:mm:ss";
		 */
		var o = {
			"Y+" : this.getFullYear(),
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"h+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"S" : this.getMilliseconds()
		}
		if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}

		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	}

	jQuery(document).ready(function() {
		Metronic.init(); // init metronic core components
		Layout.init(); // init current layout
		QuickSidebar.init(); // init quick sidebar
		Demo.init(); // init demo features
		TableManaged.init();
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

				if (retJson.retCode == 200) {

					if (confirm("命令成功执行！！是否执行下一条？")) {
						window.location.href = "auditNextHost.html";
					} else {
						alert("OK");
						window.location.reload();
					}

				} else {
					alert(JSON.stringify(retJson));
				}
			}
		});
	}
	updatePageStatus = function(pageId, status) {
		
		$.ajax({
			type : "GET",
			url : "setPageAuditStatusV2.html",
			data : {
				pageId_p : pageId,
				status_p : status
			},
			dataType : "json",
			success : function(retJson) {
				
				if (retJson.retCode == 200) {
					
					if (confirm("命令成功执行！！是否执行下一条？")) {
						window.location.href = "auditNextHostV2.html";
					} else {
						window.location.reload();
					}
					
				} else {
					alert(JSON.stringify(retJson));
				}
			}
		});
	}
auditHeadColor = {red:'red', blue:'blue', grenn:'green'};
	setAuditHeadColor=function(color){
		for(var i in auditHeadColor){
			$("#audit-head").removeClass(auditHeadColor[i]);
		}
	
		$("#audit-head").addClass(color);
	}
	
	auditPage = function(pageId) {

		$.ajax({
			type : "GET",
			url : "getDetectedPageV2.html",
			data : {
				pageId : pageId
			},
			dataType : "json",
			success : function(retJson) {

				if (retJson.retCode == 200) {

					//detectedPageTitle
					//detectedPageContent
					//detectedPageTime
					//detectedPageScore
					//detectedPageOrgUrl
					var timeFormat = "MM/dd hh:mm";
					var vo = retJson.PageDetectedVo;
					var updateDate = new Date(vo.update_time);
					console.log(updateDate);
					var updateDateStr = updateDate.format(timeFormat);
					$("#detectedPageId").html(vo.id);
					$("#detectedPageTitle").html(vo.title);
					$("#detectedPageContent").html("vo.content");
					$("#detectedPageTime").html(updateDateStr);
					$("#detectedPageScore").html(vo.score);
					$("#detectedPageOrgUrl").attr("href", vo.url);
				
					if(vo.audit_status == 3){
						$("#btn-prePass").html("接受");
						$("#btn-preIgnore").html("已  忽略")
						setAuditHeadColor('red');
						
					}else if(vo.audit_status ==9){
						$("#btn-preIgnore").html("忽略")
						$("#btn-prePass").html("已 接受");
						setAuditHeadColor('green');
					}else if(vo.audit_status ==0){
						$("#btn-prePass").html("接受");
						$("#btn-preIgnore").html("忽略")
						setAuditHeadColor('blue');
					}
					$("#btn-prePass").unbind('click').click(function(){
						updatePageStatus(vo.id,9);
					});
					$("#btn-preIgnore").unbind('click').click(function(){
						updatePageStatus(vo.id,3);
					});
					$("#button-area").removeClass("hide")
				} else {
					alert(JSON.stringify(retJson));
				}
			}
		});
	};
	var nextToAudit=function(){
		var $kk = $("#pages tr");
		var pageId= null;
		if ($kk && $kk.length) {
			$kk.each(function(idx, ele){
				if($(ele).hasClass("leftAudit-0")){
					var idStr = ele.id;
					pageId = idStr.split("-")[1];
						
					return false;
				}else{
					console.log("skip " + $(ele))
				}
			});
		}
		return pageId;
	};
	$(function() {
		var trs = $(".pageTr");
		
		if ($(trs) ) {
				$(trs).click(function(){
					var pageId = $(this).attr("pageId");
					auditPage(pageId);
				});
		}
		$(".leftAudit-3").attr("style", "background-color:red");
		$(".leftAudit-9").attr("style", "background-color:green");
	});
	
	$(function() {
		pageId = nextToAudit();
		if(pageId){
			auditPage(pageId);
		}
	});
	
	
	
</script>

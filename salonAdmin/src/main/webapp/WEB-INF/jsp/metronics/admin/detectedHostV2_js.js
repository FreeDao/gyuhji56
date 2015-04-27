
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


updatePageStatus = function(pageId, status) {
	if(!updatePageStatusAfterUrl){
		updatePageStatusAfterUrl = "auditNextHostV2.html";
	}
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

				$("#pageId-"+pageId).attr("class", "leftAudit-" + status)
//				updateLeftColor();
				var newPageId=nextToAudit();
				if(newPageId){
					auditPage(newPageId)
				}else{
					if (confirm("命令成功执行！！是否执行下一条？")) {
						window.location.href = updatePageStatusAfterUrl;
					} 
				}

			} else {
				alert(JSON.stringify(retJson));
			}
		}
	});
}
auditHeadColor = {red:'red', blue:'blue', green:'green'};

var setAuditHeadColor=function(color){
	for(var i in auditHeadColor){
		$("#audit-head").removeClass(auditHeadColor[i]);
	}
	$("#audit-head").addClass(color);
}

var auditPage = function(pageId) {

	$.ajax({
		type : "GET",
		url : "getDetectedPageV2.html",
		data : {
			pageId : pageId
		},
		dataType : "json",
		success : function(retJson) {

			if (retJson.retCode == 200) {

				var timeFormat = "MM/dd hh:mm";
				var vo = retJson.PageDetectedVo;
				var updateDate = new Date(vo.update_time);
				console.log(updateDate);
				var updateDateStr = updateDate.format(timeFormat);
				$("#detectedPageTitle").html(vo.title);
				$("#detectedPageContent").html("vo.content");
				$("#detectedPageTime").html(updateDateStr);
				$("#detectedPageScore").html(vo.score);
				$("#detectedPageOrgUrl").attr("href", vo.url);
				$("#detectedPageOrgUrl").html(vo.host);
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
				$("#button-area").removeClass("hide");
				$(".isAuditing").removeClass("isAuditing");
				$("#pageId-"+vo.id).addClass("isAuditing");
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
//	updateLeftColor();

});

$(function() {
	pageId = nextToAudit();
	if(pageId){
		auditPage(pageId);
	}
});



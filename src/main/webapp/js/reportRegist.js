
function upsertInfo() {
	var param = new Object();
	var params = new Array();
	var checktype = false;
	var checkvalue = false;
	$("#paramTd div.tb_para").each(function(index, item){
		if($(this).find("select#paramType").val() == ""){
			checktype = true;
			return false;
		}
		if($(this).find("select#paramType").val() == "99" && $(this).find("input#paramValue").val() == ""){
			checkvalue = true;
			return false;
		}
		let data = new Object();
		data.paramNm = $(this).find("input#paramNm").val();
		data.paramType = $(this).find("select#paramType").val();
		data.paramValue = $(this).find("input#paramValue").val();
		params.push(data);
	});
	if(checktype){
		alert("파라미터 타입을 선택하세요.");
		return;
	}
	if(checkvalue){
		alert("파라미터 값을 입력하세요.");
		return;
	}
	param.params = params;
	$("#reportAttr").val(JSON.stringify(param));
	
	if($("#mgrInfoCotainer .mgrInfo").length == 0){
		alert("편집권한 소유자를 선택하세요.");
		return;
	}
	
	if($("#mainPicrId").val() == ""){
		alert("관리자(정)을 선택하세요.");
		return;
	}
	
	//json타입은 값이 없어도, json형태로 던져줘야 형변환 에러가 안남
	let pcptInfoJson = new Object();
	let pcptListJson = new Object();
	let pcptUserInfos = new Array();
	let pcptDeptInfos = new Array();
	let pcptGroupInfos = new Array();
	let pcptIds = new Array();
	$("#pcptInfoCotainer div").each(function(index, item){
		pcptIds.push($(this).attr("refId"));
		var refTy = $(this).attr("refTy");
		if(refTy == "1"){//사용자
			pcptUserInfos.push(JSON.parse($(this).find("pre.pcptInfo").text()));
		}else if(refTy == "2"){//부서
			pcptDeptInfos.push(JSON.parse($(this).find("pre.pcptInfo").text()));
		}else if(refTy == "3"){//그룹
			pcptGroupInfos.push(JSON.parse($(this).find("pre.pcptInfo").text()));
		}
	});
	pcptListJson.userList = pcptUserInfos;
	pcptListJson.deptList = pcptDeptInfos;
	pcptListJson.groupList = pcptGroupInfos;
	pcptInfoJson.list = pcptListJson; 
	pcptInfoJson.ids = pcptIds;
	
	$("#pcptInfo").text(JSON.stringify(pcptInfoJson));
	if($("#pcptInfoCotainer div").length == 0){
		$("#oldPcptInfo").text(JSON.stringify(pcptInfoJson));
	}
	
	//json타입은 값이 없어도, json형태로 던져줘야 형변환 에러가 안남
	let mgrInfoJson = new Object();
	let mgrListJson = new Object();
	let mgrUserInfos = new Array();
	let mgrDeptInfos = new Array();
	let mgrGroupInfos = new Array();
	let mgrIds = new Array();
	$("#mgrInfoCotainer div").each(function(index, item){
		mgrIds.push($(this).attr("refId"));
		var refTy = $(this).attr("refTy");
		if(refTy == "1"){//사용자
			mgrUserInfos.push(JSON.parse($(this).find("pre.mgrInfo").text()));
		}else if(refTy == "2"){//부서
			mgrDeptInfos.push(JSON.parse($(this).find("pre.mgrInfo").text()));
		}else if(refTy == "3"){//그룹
			mgrGroupInfos.push(JSON.parse($(this).find("pre.mgrInfo").text()));
		}
	});
	mgrListJson.userList = mgrUserInfos;
	mgrListJson.deptList = mgrDeptInfos;
	mgrListJson.groupList = mgrGroupInfos;
	mgrInfoJson.list = mgrListJson; 
	mgrInfoJson.ids = mgrIds;
	
	$("#mgrInfo").text(JSON.stringify(mgrInfoJson));
	if($("#mgrInfoCotainer div").length == 0){
		$("#oldMgrInfo").text(JSON.stringify(mgrInfoJson));
	}

	//json타입은 값이 없어도, json형태로 던져줘야 형변환 에러가 안남
	let deptInfoJson = new Object();
	let deptListJson = new Object();
	let deptUserInfos = new Array();
	let deptDeptInfos = new Array();
	let deptGroupInfos = new Array();
	let deptIds = new Array();
	$("#deptInfoCotainer div").each(function(index, item){
		deptIds.push($(this).attr("refId"));
		var refTy = $(this).attr("refTy");
		if(refTy == "1"){//사용자
			deptUserInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "2"){//부서
			deptDeptInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "3"){//그룹
			deptGroupInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}
	});
	
	$(".mainPicrId div").each(function(index, item){
		deptIds.push($(this).attr("refId"));
		var refTy = $(this).attr("refTy");
		if(refTy == "1"){//사용자
			deptUserInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "2"){//부서
			deptDeptInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "3"){//그룹
			deptGroupInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}
	});
	
	$(".subPicrId div").each(function(index, item){
		deptIds.push($(this).attr("refId"));
		var refTy = $(this).attr("refTy");
		if(refTy == "1"){//사용자
			deptUserInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "2"){//부서
			deptDeptInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}else if(refTy == "3"){//그룹
			deptGroupInfos.push(JSON.parse($(this).find("pre.deptInfo").text()));
		}
	});
	deptListJson.userList = deptUserInfos;
	deptListJson.deptList = deptDeptInfos;
	deptListJson.groupList = deptGroupInfos;
	deptInfoJson.list = deptListJson; 
	deptInfoJson.ids = deptIds;
	
	$("#deptInfo").text(JSON.stringify(deptInfoJson));
	if($("#deptInfoCotainer div").length == 0){
		$("#oldDeptInfo").text(JSON.stringify(deptInfoJson));
	}
	
	if($("input[name='reportTy']:checked").length == 0){
		alert("보고서 유형을 선택하세요.");
		return;
	}
	
	if ($('#reportId').val()) $('#reportForm').attr('action', '/project/report/update').submit();
	else $('#reportForm').attr('action', '/project/report/insert').submit();
	
}

function deleteInfo() {
	$('#reportForm').attr('action', '/project/report/delete').submit();
}

function search() {
	$('#reportForm')
	.attr('action', '/project/report')
	.attr('method', 'post')
	.submit();
}

function popupMember(gubun) {
	// 사용자 화면 호출 함수
	var pagetitle = $(this).attr("title");
	var page = "/system/member/popup";
	var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
		.html('<iframe id="memberPopup" style="border: 0px; " gubun="' + gubun + '" src="' + page + '" width="100%" height="100%"></iframe>')
		.dialog({
			autoOpen: false,
			modal: true,
			width: 800,
			height: 750,
			title: pagetitle,
			buttons: [
				{
					text: "선택",
					click: function() {
						var checkedMembers = $("#memberPopup").contents().find('input:checkbox[class="memberChk"]:checked');
						var addMemberIconMsg = addMemberIcon(checkedMembers, $("#memberPopup").attr('gubun'));
						if (addMemberIconMsg == 'success') {
							$('.ui-dialog').remove();
						} else {
							alert(addMemberIconMsg);
						}
					}
				},
				{
					text: "취소",
					click: function() {
						$('.ui-dialog').remove();
					}
				}
			]
		});
	$dialog.dialog('open');
}

// $('#_startDt').click(function() {
//	 $('#date_before').trigger('click');
// });
//
// $('#_endDt').click(function() {
//	 $('#date_after').trigger('click');
// });

	//팝업창에서 회원 선택시 부모창에 유저아이콘 add
	function addMemberIcon(chkObj, gubun) {
		var msg = 'success';
		var g = "2";
		if (chkObj.length == 0) {
			msg = '선택된 회원이 없습니다';
		}
		if (gubun == 'mgr') {
			g = "1";
//			if (chkObj.length > 1) {
//				msg = '관리권한소유자는 1명만 선택되어야 합니다.';
//			}
		}
		var jsonObj;
		for (i=0 ; i<chkObj.length ; i++) {
			jsonObj = JSON.parse(chkObj.eq(i).val());
			//부모창에 이미 선택된 회원이 추가되어 있으면 pass, 없으면 추가
			if ($("#" + gubun + "InfoCotainer").children('div[refId="'+jsonObj.userId+'"]').length == 1) {
				//do nothing	
			} else {
				//관리권한소유자는 empty처리후 append한다.
//				if (gubun == 'mgr') {
//					$("#" + gubun + "InfoCotainer").empty();
//				}
				$("#" + gubun + "InfoCotainer").append(getIconHtml(jsonObj, g));
			}
		}
		return msg;
	}

	function getIconHtml(jsonObj, gubun) {
		var returnHtml = "";
		if(jsonObj.refTy == "1"){
			returnHtml += "<div class='radio btn_delete_img' style='margin-right:15px;padding-top:3px;padding-bottom:3px;' refId='"+jsonObj.refId+"' refTy='"+jsonObj.refTy+"' refNm='"+jsonObj.refNm+"' deptCode='"+jsonObj.deptCode+"' deptNm='"+jsonObj.deptNm+"' pstnCode='"+jsonObj.pstnCode+"' pstnNm='"+jsonObj.pstnNm+"' tableauUserId='"+jsonObj.tableauUserId+"' fileUrl='"+jsonObj.fileUrl+"'>";
		}else if(jsonObj.refTy == "2"){
			returnHtml += "<div class='radio btn_delete_img' style='margin-right:15px;padding-top:3px;padding-bottom:3px;' refId='"+jsonObj.refId+"' refTy='"+jsonObj.refTy+"' refNm='"+jsonObj.refNm+"' deptCode='"+jsonObj.deptCode+"' deptNm='"+jsonObj.deptNm+"'>";
		}
		if(gubun == '1'){
			returnHtml += "	<pre class='hidden mgrInfo'>"+JSON.stringify(jsonObj)+"</pre>";
		}else if(gubun == '2'){
			returnHtml += "	<pre class='hidden pcptInfo'>"+JSON.stringify(jsonObj)+"</pre>";
		}else if(gubun == '3'){
			returnHtml += "	<pre class='hidden deptInfo'>"+JSON.stringify(jsonObj)+"</pre>";
		}
		returnHtml += "	<input type='radio' checked=''>";
		returnHtml += "	<label>";
//		returnHtml += "		<span class='icon'><img src='/images/minus.png'/></span>";
		returnHtml += "		<span class='txt'>"+jsonObj.refNm+"</span>";
		returnHtml += "	</label>";
		returnHtml += "</div>";
		
		return returnHtml;
	}
							
	function pcptInfoShow() {
		var pcptInfoVal = $("#pcptInfo").val().trim();
		try {
			var pcptInfoJson = JSON.parse(pcptInfoVal);
			var userList = pcptInfoJson.list.userList;
			var deptList = pcptInfoJson.list.deptList;
			var groupList = pcptInfoJson.list.groupList;
			var appendHtml = "";
			for (i=0 ; i<userList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(userList[i], 2);
			}
			for (i=0 ; i<deptList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(deptList[i], 2);
			}
			for (i=0 ; i<groupList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(groupList[i], 2);
			}
			$("#pcptInfoCotainer").empty().append(appendHtml);
		} catch(e) {
			console.log(e);
		}
	}
	
	function mgrInfoShow() {
		var mgrInfoVal = $("#mgrInfo").val().trim();
		try {
			var mgrInfoJson = JSON.parse(mgrInfoVal);
			var userList = mgrInfoJson.list.userList;
			var deptList = mgrInfoJson.list.deptList;
			var groupList = mgrInfoJson.list.groupList;
			var appendHtml = "";
			for (i=0 ; i<userList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(userList[i], 1);
			}
			for (i=0 ; i<deptList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(deptList[i], 1);
			}
			for (i=0 ; i<groupList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(groupList[i], 1);
			}
			$("#mgrInfoCotainer").empty().append(appendHtml);
		} catch(e) {
			console.log(e);
		}		
	}

	function deptInfoShow() {
		var deptInfoVal = $("#deptInfo").val();
		try {
			var deptInfoJson = JSON.parse(deptInfoVal);
			var userList = deptInfoJson.list.userList;
			var deptList = deptInfoJson.list.deptList;
			var groupList = deptInfoJson.list.groupList;
			var appendHtml = "";
			var mainHtml = "";
			var subHtml = "";
			for (i=0 ; i<userList.length ; i++) {
				if(i == 0){
					mainHtml = mainHtml + getIconHtml(userList[i], 3);
					$(".mainPicrId").empty().append(mainHtml);
				}else{
					subHtml = subHtml + getIconHtml(userList[i], 3);
					$(".subPicrId").empty().append(subHtml);
				}
			}
			for (i=0 ; i<deptList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(deptList[i], 3);
			}
			for (i=0 ; i<groupList.length ; i++) {
				appendHtml = appendHtml + getIconHtml(groupList[i], 3);
			}
			$("#deptInfoCotainer").empty().append(appendHtml);
		} catch(e) {
			console.log(e);
		}		
	}
	
	$(document).ready(function() {
		$('#reportPreview').on("click", function(){
			if($(this).attr("useYn") == 'N'){
				alert("삭제된 보고서 입니다.");
				return;
			}
			let reportId = $("#reportId").val();
			//let encReportUrl = btoa(encodeURIComponent(reportUrl));
			//http://127.0.0.1:8081/project/report/reportPreview/MDFfJTJGc2hlZXQwJUVDJTk4JTgx/popup
			let popupUrl = "/project/report/reportPreview/" + reportId + "/popup"
			window.open(popupUrl, '분석데이터 플랫폼 대시보드', 'width=1920px,height=1080px,scrollbars=yes,top=0,left=0');
		});
		
		//회원 삭제(-)버튼 클릭
		$(document).on("click", ".btn_delete_img", function(){
			//$(this).remove();
		});
		
		$("#plus").on("click",function(){
			$("#paramDiv").children().clone().appendTo("#paramTd");
		});

		$("#paramTd").on("click", "img" ,function(){
			$(this).parent().parent().parent().parent().remove();
		});

		$("#paramTd").on("change", "select", function(){
			if($(this).children("option:selected").val() == 99){
				$(this).parent().parent().find("#paramValue").attr("readonly", false);
			}else{
				$(this).parent().parent().find("#paramValue").attr("readonly", true);
				$(this).parent().parent().find("#paramValue").val("");
			}
		});
		
		pcptInfoShow();
		mgrInfoShow();
		deptInfoShow();
	});	
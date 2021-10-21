function aprvReq() {
	alert('준비중입니다');
}

function aprvReqCancle() {
	alert('준비중입니다');
}

function upsertInfo() {
	//프로젝트명 필수입력
	if ($("#projectNm").val().trim() == '') {
		alert('프로젝트명을 입력하세요.');
		$("#projectNm").focus();
		return false;	
	}
	if($("#projectNm").val().length > 50){
		alert("프로젝트명은 50자 이상 작성할 수 없습니다.");
		return;
	}
	var start = Number($("#startDt").val().replace(/-/gi,""));
	var end = Number($("#endDt").val().replace(/-/gi,""));
	//시작일은 필수값
	if (isNaN(start) || (start==0)) {
		alert('프로젝트 시작일을 선택하세요.');
		return false;
	}
	//시작일 <= 종료일 이어야함	
	if (!isNaN(start) && !isNaN(end)) {
		if (start > end) {
			alert('시작일은 종료일보다 이전이어야 합니다.');
			return false; 
		}
	} 

//	if($("#projectDsc").val().length > 2000){
//		alert("프로젝트 내용은 2000자 이상 작성할 수 없습니다.");
//		return;
//	}
	
    if ($('#projectId').val()) $('#projectForm').attr('action', '/project/project/update').submit();
    else $('#projectForm').attr('action', '/project/project/insert').submit();
}

function deleteInfo() {
    $('#projectForm').attr('action', '/project/project/delete').submit();
}

function search() {
    $('#projectForm')
    .attr('action', '/project/project')
    .attr('method', 'post')
    .submit();
}

// $('#_startDt').click(function() {
//     $('#date_before').trigger('click');
// });
//
// $('#_endDt').click(function() {
//     $('#date_after').trigger('click');
// });
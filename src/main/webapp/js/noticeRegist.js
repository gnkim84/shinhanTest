
function upsertInfo() {
	var sj = $("input[name='sj']");
	var cn = $("textarea[name='cn']");
	console.log(cn.val());
	console.log(cn.val().length);
	if(sj.val().length > 50){
		alert("제목은 50자 이상 작성할 수 없습니다.");
		return;
	}

//	if(cn.val().length > 2000){
//		alert("내용은 2000자 이상 작성할 수 없습니다.");
//		return;
//	}

	var startDtVal	= $("#startDt").val();
	var endDtVal	= $("#endDt").val();
	
	console.log('#startDtVal = ' + startDtVal);
	console.log('#endDtVal = ' + endDtVal);	

	if (startDtVal!='' && isDatePattern(startDtVal)==false) {
		alert('유효한 날짜가 아닙니다');
		$("#startDt").focus();
		return;
	}
	if (endDtVal!='' && isDatePattern(endDtVal)==false) {
		alert('유효한 날짜가 아닙니다');
		$("#endDt").focus();
		return;
	}
	if (startDtVal!='' && endDtVal!='') {
		if (startDtVal > endDtVal) {
			alert('시작일은 종료일보다 클수 없습니다.');
			$("#date_before").focus();
			return;
		}
	}

    if ($('#noticeId').val()) $('#noticeForm').attr('action', '/board/notice/update').submit();
    else $('#noticeForm').attr('action', '/board/notice/insert').submit();
}

function deleteInfo() {
    $('#noticeForm').attr('action', '/board/notice/delete').submit();
}
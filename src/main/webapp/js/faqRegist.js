
function upsertInfo() {
	var sj = $("input[name='qstn']");
	var cn = $("textarea[name='answ']");
	console.log(cn.val());
	console.log(cn.val().length);
	if(sj.val().length > 50){
		alert("질문은 50자 이상 작성할 수 없습니다.");
		return;
	}

//	if(cn.val().length > 2000){
//		alert("답변은 2000자 이상 작성할 수 없습니다.");
//		return;
//	}
    if ($('#faqId').val()) $('#faqForm').attr('action', '/board/faq/update').submit();
    else $('#faqForm').attr('action', '/board/faq/insert').submit();
}

function deleteInfo() {
    $('#faqForm').attr('action', '/board/faq/delete').submit();
}
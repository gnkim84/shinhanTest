
function upsertInfo() {
	var cn = $("textarea[name='answ']");
//	if(cn.val().length > 2000){
//		alert("답변은 2000자 이상 작성할 수 없습니다.");
//		return;
//	}
    $('#qnaForm').attr('action', '/board/qna/update').submit();
}

function deleteInfo() {
    $('#qnaForm').attr('action', '/board/qna/delete').submit();
}
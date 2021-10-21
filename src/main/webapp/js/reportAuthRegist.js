
function upsertInfo() {

	//테스트
	if (!$('#userId').val()) $('#userId').val('073137');
	//
	
    if ($('#aprvId').val()) $('#reportAuthForm').attr('action', '/project/reportAuth/update').submit();
    else $('#reportAuthForm').attr('action', '/project/reportAuth/insert').submit();
    
}

function deleteInfo() {
    $('#reportAuthForm').attr('action', '/project/reportAuth/delete').submit();
}

function searchReport() {
	
	console.log($('#reportId').val());
	//테스트
    if (!$('#reportId').val()) {
    	$('#reportId').val('RP2021_000000008');
    	$('#reportNm').val('test');
    	$('input[value="DEPT"]').attr('checked', '');
	}
    //
    console.log($('#reportId').val());
    
}

// $('#_startDt').click(function() {
//     $('#date_before').trigger('click');
// });
//
// $('#_endDt').click(function() {
//     $('#date_after').trigger('click');
// });
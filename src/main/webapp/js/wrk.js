$(document).ready(function(){

	$("#addWrk").on("click",function () {
	
	    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");
	
	    if(result) {
	        clearInputWrk();
	    }
	});
	
	$("#saveWrk").on("click",function () {
	
	    let validResult = validateWrk();
	
	    if(!validResult) return;
	
	    let result = confirm("항목을 저장하시겠습니까?");
	
	    if(result) {
	        const formData = $("form[name=wrkForm]").serialize();
	
	        $.ajax({
	            type: "post",
	            url: "/system/wrkCat/insert",
	            dataType: "text",
	            data: formData,
	            success: function (result) {
	
	                if(result == "Update") {
	                    alert("항목이 수정되었습니다.");
	                } else if(result == "Insert") {
	                    alert("신규 항목이 생성되었습니다.");
	                } else {
	                    alert("항목 저장이 실패했습니다. 다시 시도해 주세요");
	                    return;
	                }
	
	                location.href = '/system/wrkCat';
	            }
	        });
	    }
	})
	
	$('#deleteWrk').click(function () {
	
	    if($('#wrkId').val() == '') {
	        alert("삭제할 항목이 없습니다.");
	        return;
	    }
	
	    let result = confirm("선택한 항목을 삭제하시겠습니까?");
	
	    if(result) {
	        const formData = $("form[name=wrkForm]").serialize();
	
	        $.ajax({
	            type: "post",
	            url: "/system/wrkCat/delete",
	            dataType: "text",
	            data: formData,
	            success: function (result) {
	                if(result == "Delete") {
	                    alert("삭제가 완료되었습니다.");
	                    location.href = '/system/wrkCat';
	                } else {
	                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
	                    return;
	                }
	            }
	        });
	    }
	});
});

// 데이터를 요청하여 가져온다.
function getWrkData(wrkId) {

    if(wrkId != '') {
        $.ajax({
            type: "post",
            url: "/system/wrkCat/detail/" + wrkId,
            dataType: "text",
            success: function (result) {
                let wrk = JSON.parse(result);
                viewDetail(wrk);
            }
        });
    }
}

function viewDetail(wrk) {
    $('#wrkId').val(wrk.wrkId);
    $('#wrkNm').val(wrk.wrkNm);
    $('#wrkDsc').val(wrk.wrkDsc);
    $('#wrkId').attr('readonly', true);
    //$('#addWrk').hide();
    //$('#addWrk').attr('disabled', true);
    //$('#addWrk').removeClass("blue").addClass("grayb");
}

function clearInputWrk() {
    $('#wrkId').val('');
    $('#wrkNm').val('');
    $('#wrkDsc').val('');
    $('#wrkId').attr('readonly', false);
    //$('#addWrk').hide();
}

function validateWrk() {
//    if($('#wrkId').val() == '') {
//        alert('업무카테고리ID가 입력되어 있지 않습니다.');
//        $('#wrkId').focus();
//        return false;
//    }

    if($('#wrkNm').val() == '') {
        alert('업무카테고리명 입력되어 있지 않습니다.');
        $('#wrkNm').focus();
        return false;
    }

    return true;
}


$("#addExtrnl").click(function () {

    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");

    if(result) {
        clearInputExtrnl();
    }
});

$("#saveExtrnl").click(function () {

    let validResult = validateExtrnl();

    if(!validResult) return;

    let result = confirm("항목을 저장하시겠습니까?");

    if(result) {
        const formData = $("form[name=extrnlForm]").serialize();

        $.ajax({
            type: "post",
            url: "/external/extrnl/insert",
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

                location.href = '/external/extrnl';
            }
        });
    }

})

$('#deleteExtrnl').click(function () {

    if($('#extrnlId').val() == '') {
        alert("삭제할 항목이 없습니다.");
        return;
    }

    let result = confirm("선택한 항목을 삭제하시겠습니까?");

    if(result) {
        const formData = $("form[name=extrnlForm]").serialize();

        $.ajax({
            type: "post",
            url: "/external/extrnl/delete",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Delete") {
                    alert("삭제가 완료되었습니다.");
                    location.href = '/external/extrnl';
                } else {
                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
                    return;
                }
            }
        });
    }
});

// 데이터를 요청하여 가져온다.
function getExtrnlData(extrnlId) {

    if(extrnlId != '') {
        $.ajax({
            type: "post",
            url: "/external/extrnl/detail/" + extrnlId,
            dataType: "text",
            success: function (result) {
                let extrnl = JSON.parse(result);
                viewDetail(extrnl);
            }
        });
    }
}

function viewDetail(extrnl) {
    $('#extrnlId').val(extrnl.extrnlId);
    $('#extrnlNm').val(extrnl.extrnlNm);
    $('#extrnlUrl').val(extrnl.extrnlUrl);
    $('#extrnlDsc').val(extrnl.extrnlDsc);
    $('#extrnlId').attr('readonly', true);
    //$('#addExtrnl').hide();
}

function clearInputExtrnl() {
    $('#extrnlId').val('');
    $('#extrnlId').removeAttr('readonly')
    $('#extrnlNm').val('');
    $('#extrnlUrl').val('');
    $('#extrnlDsc').val('');
    //$('#addExtrnl').hide();
}


function validateExtrnl() {
    if($('#extrnlId').val() == '') {
        alert('외부시스템ID가 입력되어 있지 않습니다.');
        $('#extrnlId').focus();
        return false;
    }

    if($('#extrnlNm').val() == '') {
        alert('외부시스템명이 입력되어 있지 않습니다.');
        $('#extrnlNm').focus();
        return false;
    }

    if($('#extrnlUrl').val() == '') {
        alert('외부시스템URL이 입력되어 있지 않습니다.');
        $('#extrnlUrl').focus();
        return false;
    }

    return true;
}


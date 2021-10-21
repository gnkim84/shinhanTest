$("#addLicense").click(function () {

    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");

    if(result) {
        clearInputLicense();
    }
});

$("#saveLicense").click(function () {

    $('#authId').attr('disabled', false);

    let validResult = validateLicense();

    if(!validResult) {
        $('#authId').attr('disabled', true);
        return;
    }

    let result = confirm("항목을 저장하시겠습니까?");

    if(result) {
        const formData = $("form[name=licenseForm]").serialize();

        $.ajax({
            type: "post",
            url: "/system/license/insert",
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

                location.href = '/system/license';
            }
        });
    } else {
        $('#authId').attr('disabled', true);
    }
})

$('#deleteLicense').click(function () {

    $('#authId').attr('disabled', false);

    if($('#authId').val() == '' && $('#licenseId').val() == '') {
        alert("삭제할 항목이 없습니다.");
        return;
    }

    let result = confirm("선택한 항목을 삭제하시겠습니까?");

    if(result) {
        const formData = $("form[name=licenseForm]").serialize();

        $.ajax({
            type: "post",
            url: "/system/license/delete",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Delete") {
                    alert("삭제가 완료되었습니다.");
                    location.href = '/system/license';
                } else {
                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
                    return;
                }
            }
        });
    } else {
        $('#authId').attr('disabled', true);
    }
});

// 데이터를 요청하여 가져온다.
function getLicenseData(authId, licenseId) {

    if(authId != '' && licenseId != '') {
        $.ajax({
            type: "post",
            url: "/system/license/detail/" + authId + "/" + licenseId,
            dataType: "text",
            success: function (result) {
                let license = JSON.parse(result);
                viewDetail(license);
            }
        });
    }
}

function viewDetail(license) {
    $('#authId').val(license.authId);
    $('#licenseId').val(license.licenseId);
    $('#tableauId').val(license.tableauId);
    $('#tableauPw').val(license.tableauPw);
    $('#awsId').val(license.awsId);
    $('#awsPw').val(license.awsPw);
    $('#licenseDesc').val(license.licenseDesc);
    $('#authId').attr('disabled', true);
    //$('#addLicense').hide();
}

function clearInputLicense() {
    $('#licenseId').val('');
    $('#tableauId').val('');
    $('#tableauPw').val('');
    $('#awsId').val('');
    $('#awsPw').val('');
    $('#licenseDesc').val('');
    $('#authId').removeAttr('disabled');
    //$('#addLicense').hide();
}

function validateLicense() {
    if($('#tableauId').val() == '') {
        alert('태블로계정이 입력되어 있지 않습니다.');
        $('#tableauId').focus();
        return false;
    }

    if($('#tableauPw').val() == '') {
        alert('태블로 비밀번호가 입력되어 있지 않습니다.');
        $('#tableauPw').focus();
        return false;
    }

    if($('#awsId').val() == '') {
        alert('AWS계정이 입력되어 있지 않습니다.');
        $('#awsId').focus();
        return false;
    }

    if($('#awsPw').val() == '') {
        alert('AWS 비밀번호가 입력되어 있지 않습니다.');
        $('#awsPw').focus();
        return false;
    }

    return true;
}


$("#addRole").click(function () {

    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");

    if(result) {
        clearInputRole();
    }
});

$("#saveRole").click(function () {

    let validResult = validateRole();

    if(!validResult) return;

    let result = confirm("항목을 저장하시겠습니까?");

    if(result) {

        const formData = $("form[name=roleForm]").serialize();

        $.ajax({
            type: "post",
            url: "/system/role/insert",
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

                location.href = '/system/role';
            }
        });
    }
})

$('#deleteRole').click(function () {

    if($('#authId').val() == '') {
        alert("삭제할 항목이 없습니다.");
        return;
    }

    let result = confirm("선택한 항목을 삭제하시겠습니까?");

    if(result) {
        const formData = $("form[name=roleForm]").serialize();

        $.ajax({
            type: "post",
            url: "/system/role/delete",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Delete") {
                    alert("삭제가 완료되었습니다.");
                    location.href = '/system/role';
                } else if(result == 'UseRoleId') {
                    alert("권한ID를 사용중인 사용자가 있어서 삭제가 실패했습니다.\n확인후 다시 진행해 주세요.");
                    return;
                } else {
                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
                    return;
                }
            }
        });
    }
});

// 데이터를 요청하여 가져온다.
function getRoleData(roleId) {

    if(roleId != '') {
        $.ajax({
            type: "post",
            url: "/system/role/detail/" + roleId,
            dataType: "text",
            success: function (result) {
                let role = JSON.parse(result);
                viewDetail(role);
            }
        });
    }
}

function viewDetail(role) {
    $('#authId').val(role.authId);
    $('#authNm').val(role.authNm);
    $('#authDsc').val(role.authDsc);
    $('#authCl').val(role.authCl).prop("selected",true);
    $('#authId').attr('readonly', true);
    //$('#addRole').hide();
    //$('#addRole').attr('disabled', true);
    //$('#addRole').removeClass("blue").addClass("grayb");
}

function clearInputRole() {
    $('#authId').val('');
    $('#authId').removeAttr('readonly')
    $('#authNm').val('');
    $('#authDsc').val('');
}

function validateRole() {
    if($('#authId').val() == '') {
        alert('권한ID가 입력되어 있지 않습니다.');
        $('#authId').focus();
        return false;
    }

    if($('#authNm').val() == '') {
        alert('권한명이 입력되어 있지 않습니다.');
        $('#authNm').focus();
        return false;
    }

    return true;
}


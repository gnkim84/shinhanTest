// 코드그룹 신규
$("#addGroupCode").click(function () {
    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");
    if(result) {
        clearInputGroupCode();
    }
});

// 코드그룹 저장
$("#saveGroupCode").click(function () {
    let validResult = validateGroupCode();
    if(!validResult) return;
    let result = confirm("항목을 저장하시겠습니까?");
    if(result) {
        const formData = $("form[name=codeGroupForm]").serialize();
        $.ajax({
            type: "post",
            url: "/system/code/insert",
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
                location.href = '/system/code';
            }
        });
    }
});

// 코드그룹 삭제
$('#deleteGroupCode').click(function () {
    if($('#groupCodeId').val() == '') {
        alert("삭제할 항목이 없습니다.");
        return;
    }
    let result = confirm("선택한 항목을 삭제하시겠습니까?");
    if(result) {
        const formData = $("form[name=codeGroupForm]").serialize();
        $.ajax({
            type: "post",
            url: "/system/code/delete",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Delete") {
                    alert("삭제가 완료되었습니다.");
                    location.href = '/system/code';
                } else if(result == 'UseCodeId') {
                    alert("그룹코드를 사용중인 코드가 있어서 삭제가 실패했습니다.\n확인후 다시 진행해 주세요.");
                    return;
                } else {
                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
                    return;
                }
            }
        });
    }
});





// 코드 신규
$("#addCode").click(function () {
    let result = confirm("입력이나 수정중인 항목이 모두 초기화 됩니다.\n진행하시겠습니까?");
    if(result) {
        clearInputCode();
    }
});

// 코드 저장
$("#saveCode").click(function () {
    let validResult = validateCode();
    if(!validResult) return;
    let result = confirm("항목을 저장하시겠습니까?");
    if(result) {
        const formData = $("form[name=codeForm]").serialize();
        $.ajax({
            type: "post",
            url: "/system/code/insert/code",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Update") {
                	console.log('1 : ',formData)
                    alert("항목이 수정되었습니다.");
                } else if(result == "Insert") {
                	console.log('2 : ',formData)
                    alert("신규 항목이 생성되었습니다.");
                } else {
                	console.log('3 : ',formData)
                    alert("항목 저장이 실패했습니다. 다시 시도해 주세요");
                    return;
                }
                location.href = '/system/code';
            }
        });
    }
})

// 코드삭제
$('#deleteCode').click(function () {
    if($('#codeId').val() == '') {
        alert("삭제할 항목이 없습니다.");
        return;
    }
    let result = confirm("선택한 항목을 삭제하시겠습니까?");
    if(result) {
        const formData = $("form[name=codeForm]").serialize();
        $.ajax({
            type: "post",
            url: "/system/code/delete/code",
            dataType: "text",
            data: formData,
            success: function (result) {
                if(result == "Delete") {
                    alert("삭제가 완료되었습니다.");
                    location.href = '/system/code';
                } else {
                    alert("삭제가 실패했습니다. 다시 시도해 주세요.");
                    return;
                }
            }
        });
    }
});




// 코드를 조회한다.
function viewCode(groupCd, groupNm, groupDesc, groupUseYn) {
    $.getJSON("/system/code/detail/"+groupCd, function (codes) {
        printCode(groupCd, groupNm, groupDesc, groupUseYn, codes, $('#codeDiv'), codeTemplate);
    });
}

// 그룹 코드에 대한 하위 코드를 출력한다.
function printCode(groupCd, groupNm, groupDesc, groupUseYn, codeArr, targetId, templateId) {
    let codeTemplate = Handlebars.compile(templateId);
    let html = codeTemplate(codeArr);
    targetId.html(html);

    // 코드 입력 화면을 클리어 한다.
    clearInputCode();

    // 그룹 코드 셋팅
    $('#groupCodeId').val(groupCd);
    $('#groupCodeId').attr('readonly', true);
    $('#groupCodeNm').val(groupNm);
    $('#groupCodeDsc').val(groupDesc);
	$("input:radio[name='groupCodeUseYn'][value='"+groupUseYn+"']").prop("checked",true);

    // 코드 입력 창에 값을 셋팅한다.
    $('#groupId').val(groupCd);
    $('#groupNm').val(groupNm);
    $('#codeInput').show();
    $('#codeDiv').show();
    $('#saveCode').show();
    //$('#addCode').hide();
}

// 코드 상세 보기
function viewCodeDetail(codeId, codeNm, codeDesc, ordSeq, useYn) {
    $('#codeId').val(codeId);
    $('#codeId').attr('readonly', true);
    $('#codeNm').val(codeNm);
    $('#codeDsc').val(codeDesc);
    $('#ordSeq').val(ordSeq);
	$("input:radio[name='useYn'][value='"+useYn+"']").prop("checked",true);    

    $('#deleteCode').show();
    $('#saveCode').show();
    $('#addCode').show();
}

// 그룹코드 신규 버튼 클릭시 입력창 초기화
function clearInputGroupCode() {
    $('#groupCodeId').val('');
    $('#groupCodeId').removeAttr('readonly');
    $('#groupCodeNm').val('');
    $('#groupCodeDsc').val('');
    $("input:radio[name='groupCodeUseYn']").prop("checked",false);

    $('#codeInput').hide();
    $('#codeDiv').hide();
}

// 코드 입력창을 초기화한다.
function clearInputCode() {
    $('#codeId').val('');
    $('#codeId').removeAttr('readonly');
    $('#codeNm').val('');
    $('#codeDsc').val('');
    $('#ordSeq').val('');
    $("input:radio[name='useYn']").prop("checked",false);
}

// 코드 그룹의 필수값을 체크한다.
function validateGroupCode() {
    if($('#groupCodeId').val() == '') {
        alert('그룹코드ID가 입력되어 있지 않습니다.');
        $('#groupCodeId').focus();
        return false;
    }

    if($('#groupCodeNm').val() == '') {
        alert('그룹코드명이 입력되어 있지 않습니다.');
        $('#groupCodeNm').focus();
        return false;
    }

	if($("input:radio[name='groupCodeUseYn']:checked").length == 0){
		alert("코드그룹사용여부를 선택하세요.");
		return false;
	}

    return true;
}

// 코드의 필수값을 체크한다.
function validateCode() {
    if($('#codeId').val() == '') {
        alert('코드ID가 입력되어 있지 않습니다.');
        $('#codeId').focus();
        return false;
    }

    if($('#codeNm').val() == '') {
        alert('코드명이 입력되어 있지 않습니다.');
        $('#codeNm').focus();
        return false;
    }
    
	if($("input:radio[name='useYn']:checked").length == 0){
		alert("사용여부를 선택하세요.");
		return false;
	}    

    return true;
}

const codeTemplate =
'<h2 class="content_h2">코드 목록</h2>\n' +
'<div class="tb01_line"></div>\n' +
'<table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 코드ID, 코드명, 비고, 생성날짜 대한 내용입니다.">\n' +
'    <caption class="hidden">코드 목록</caption>\n' +
'    <colgroup>\n' +
'        <col style="width:6%">\n' +
'        <col style="width:20%">\n' +
'        <col style="width:20%">\n' +
'        <col style="width:25%">\n' +
'        <col style="width:17%">\n' +
'        <col style="width:6%">\n' +
'        <col style="width:6%">\n' +
'    </colgroup>\n' +
'    <thead>\n' +
'        <tr>\n' +
'            <th scope="col">No</th>\n' +
'            <th scope="col">코드ID</th>\n' +
'            <th scope="col">코드명</th>\n' +
'            <th scope="col">비고</th>\n' +
'            <th scope="col">생성일시</th>\n' +
'            <th scope="col">정렬순서</th>\n' +
'            <th scope="col">사용여부</th>\n' +
'        </tr>\n' +
'    </thead>\n' +
'    <tbody>\n' +
'        {{#each.}}\n' +
'        <tr onclick=\'viewCodeDetail("{{codeId}}", "{{codeNm}}","{{codeDsc}}","{{ordSeq}}","{{useYn}}")\'>\n' +
'            <td scope="row">{{rownum}}</td>\n' +
'            <td>{{codeId}}</td>\n' +
'            <td>{{codeNm}}</td>\n' +
'            <td>{{codeDsc}}</td>\n' +
'            <td>{{rgstDtStr}}</td>\n' +
'            <td>{{ordSeq}}</td>\n' +
'            <td>{{useYn}}</td>\n' +
'        </tr>\n' +
'        {{/each}}\n' +
'        {{^each.}}\n' +
'        <tr>\n' +
'            <td colspan="7">조회된 코드 목록이 없습니다.</td>\n' +
'        </tr>\n' +
'        {{/each}}\n' +
'    </tbody>\n' +
'</table>';

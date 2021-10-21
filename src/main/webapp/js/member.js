function search() {
	$('#page').val(1);
	$('#searchForm').submit();
}

function searchSort(type, direction) {
	$('#sortType').val(type);
	$('#sortDirection').val(direction);

	search();
}
//
// $('#startDate').click(function() {
//	 $('#date_before').trigger('click');
// });
//
// $('#endDate').click(function() {
//	 $('#date_after').trigger('click');
// });

$("#saveMember").click(function () {

	if($('#userId').val() == '') {
		alert('정보를 변경하기 원하는 사용자를 선택후에 진행해 주세요.');
		return;
	}

	var date_pattern = /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/;
	if(!date_pattern.test($("input[name='startDate']").val())){
		alert("시작일이 날짜 형식에 맞지 않습니다.");
		return;
	}

	if(!date_pattern.test($("input[name='endDate']").val())){
		alert("종료일이 날짜 형식에 맞지 않습니다.");
		return;
	}

	let result = confirm("항목을 저장하시겠습니까?");

	if(result) {

		const formData = $("form[name=memberForm]").serialize();

		$.ajax({
			type: "post",
			url: "/system/member/insert",
			dataType: "text",
			data: formData,
			success: function (result) {
				console.log("result : ", result);
				if(result == "Update") {
					alert("항목이 수정되었습니다.");
				} else {
					alert("항목 저장이 실패했습니다. 다시 시도해 주세요");
					return;
				}

				//아래처럼 그냥 던져버리면, 검색조건들이 다 풀려버린다.
				//location.href = '/system/member';
				$('#searchForm').submit();
			},
			error : function (result){
				console.log("error result : " , result);
			}
		});
	}
});

// 사용자 데이터를 요청하여 가져온다.
function getMemberData(memberId) {
	if(memberId != '') {
		$.ajax({
			type: "post",
			url: "/system/member/detail/" + memberId,
			dataType: "text",
			success: function (result) {
				let member = JSON.parse(result);
				viewDetail(member);
			}
		});
	}
}

// 가져온 데이터를 상세에 셋팅
function viewDetail(member) {
	$('#orgUserId').val(member.userId);
	$('#userId').val(member.userId);
	$('#orgUserNm').val(member.userNm);
	$('#userNm').val(member.userNm);
	$('#pstnNm').val(member.pstnNm);
	$('#pstnCode').val(member.pstnCode);
	$('#deptCode').val(member.deptCode);
	$('#deptNm').val(member.deptNm);
	$('#companyCode').val(member.companyCode);
	$('#companyNm').val(member.companyNm);
	$('#useYn').val(member.useYn);
	$('#authId').val(member.authId);
	if(member.mgrYn == 'Y'){
		$('#mgrAuthId').val(member.mgrAuthId);
	}else{
		$('#mgrAuthId').val("");
	}
	$('#date_before').val(member.startDt);
	$('#date_after').val(member.endDt);
}

//사용자의 최종 로그인날짜를 NULL로 셋팅한다.
function unlockAccount(memberId) {
	if(memberId != '') {
		$.ajax({
			type: "post",
			url: "/system/member/update/unlock/" + memberId,
			dataType: "text",
			success: function (result) {
				if (result == '1') {
					$("#unlock_" + memberId).empty();
					$("#unlock_" + memberId).parent().removeClass("tb_old");
				} else {
					alert('unlockAccount fail');
				}
			}
		});
	}
}

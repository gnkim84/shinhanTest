<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>SIGN IN | 신한금융투자</title>
	<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
	<link href="/css/common.css" rel="stylesheet" type="text/css" />
    <link href="/webjars/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css" />
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/jquery-ui/jquery-ui.min.js"></script>
	<script src="/js/common.js"></script>
</head>

<script>
$(function() {
	let userInfo = '${userInfo}';
	console.log(userInfo);
	if (userInfo) {
		$("#userId").val(userInfo);
		$("#sso").val("Y");
		$("#frm").submit();
	}

	let msg = '${result}';
	console.log(msg);
	if (msg) {
		alert(msg);
	}

	//엔터키
	$('#userPwd').on('keyup', function(e) {
		if (e.which == 13) {
			loginAction();
		}
	});
	
});

let dbclick = 'Y';
function loginAction() {
	if (dbclick != 'Y') {
		return;
	}
	dbclick = 'N';
	var userId = document.getElementById("userId").value;
	var userPwd = document.getElementById("userPwd").value;
	if(userId == null || userId == ''){
		alert("사번을 입력해 주세요.");
		document.getElementById("userId").focus();
		dbclick = 'Y';
		return;
	}
	if(userPwd == null || userPwd == ''){
		alert("비밀번호를 입력해 주세요.");
		document.getElementById("userPwd").focus();
		dbclick = 'Y';
		return;
	}
	document.getElementById('frm').submit();
}

</script>
<body <c:if test='${not empty userInfo}'>style="display:none;"</c:if>>
	<div class="login login_admin">
		<div class="login_head">
			<h1>신한금융투자</h1>
		</div>
		
		<div class="login_con">
			<h2>데이터분석 플랫폼 관리자시스템</h2>
			<div class="login_dl">
				<dl>
					<dt>Log in</dt>
					<dd>
						<form id="frm" action="/lgn" method="post">
						<label for="sign_sel" class="hidden">선택</label>
						<div class="select">
							<select id="sign_sel">
								<option value="신한금융투자">신한금융투자</option>
							</select>
						</div>
						<label for="userId" class="hidden">사번</label><input type="text" class="input" id="userId" name="userId" placeholder="사번" />
						<label for="userPwd" class="hidden">비밀번호</label><input type="password" class="input" id="userPwd" name="userPwd" placeholder="비밀번호" />
<%--
						<div class="login_flr">
							<div class="checkbox">
								<input type="checkbox" id="rememberUserId">
								<label for="rememberUserId">
									<span class="icon">
										<span></span>
									</span>
									<span class="txt">사번 저장</span>
								</label>
							</div>
						</div>
--%>
						<div class="btn_wrap">
							<input class="btn blue full" type="button" value="로그인" onclick="loginAction();">
						</div>
<%-- 
						<ul class="login_info">
							<li>- ID/PW는 신한i/골드넷/E-HR와 동일합니다.</li>
							<li>- 비밀번호 변경은 신한i/골드넷에서 가능합니다.</li>
							<li>※ 임직원은 비밀번호의 보안을 유지해야 합니다.</li>
							<li>비밀번호 관리에 대한 책임은 임직원 본인에게 있음을 숙지하여 주시기 바랍니다.</li>
						</ul>						
--%>						<input type="hidden" class="input" id="sso" name="sso" value="N" />
						</form>
					</dd>
				</dl>
			</div>
		</div>
		<div class="login_foot">COPYRIGHT©2021 SHINHAN INVESTMENT CORP. All Rights Reserved.</div>
	</div>
</body>
</html>

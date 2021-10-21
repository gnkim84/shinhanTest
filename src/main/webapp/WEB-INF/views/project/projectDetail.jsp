<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">프로젝트 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>분석 관리</li>
			<li>프로젝트 관리</li>
		</ul>
	</div>

	<form name="projectForm" id="projectForm" method="post">
		<input type="hidden" name="projectId" id="projectId" value="${projectInfo.projectId}">
		<input type="hidden" name="projectSe" id="projectSe" value="COM" />		<% //프로젝트 구분[전체 공유 / 일반] => 사용안함 %>
		<input type="hidden" name="projectStat" id="projectStat" value="A">		<% //프로젝트 상태[신청 / 승인 / 반려] %>		
		
		
		<% //프로젝트 기본정보 %>
		<table class="tb02" summary="프로젝트명, 프로젝트 유형, 참여자, 운영담당자, 프로젝트 기간, 프로젝트 설명에 대한 내용입니다.">
			<caption class="hidden">프로젝트 관리 등록 목록</caption>
			<colgroup>
				<col style="width:12.5%">
				<col style="width:87.5%">
			</colgroup>
			<tbody>
			<tr>
				<th scope="row"><label for="projectNm">프로젝트 명</label></th>
				<td>
					<c:out value="${projectInfo.projectNm}" />
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="projectTy">프로젝트 유형</label></th>
				<td>
					<c:forEach var="codePrjType" items="${codePrjTypeList}">
						<c:if test="${projectInfo.projectTy eq codePrjType.codeId}">
						<c:out value="${codePrjType.codeNm}" />
						</c:if>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<th scope="row">프로젝트 기간</th>
				<td>
					${fn:substring(projectInfo.startDt, 0, 10)} ~ ${fn:substring(projectInfo.endDt, 0, 10)}
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="projectDsc">내용</label></th>
				<td><c:out value="${projectInfo.projectDsc}" /></td>
			</tr>
			</tbody>
		</table>
	
		<div class="btn_wrap tr">
			<button type="button" class="btn big grayb" onclick="location.href='/project/project'">목록</button>
		</div>
	</form>

</section>
<script type="text/javascript" src="/js/projectRegist.js"></script>
<script>
	$(function() {

		function visibleUserInfo() {
			let selectedVal = $('.sel_chage :selected').val();
			if (selectedVal == 'VW') {
				$('tr.pcptInfo').hide();
				$('tr.mgrInfo').hide();
			} else {
				$('tr.pcptInfo').show();
				$('tr.mgrInfo').show();
			}
		};

		//기계학습, 시각화 셀렉트
		$('.sel_chage').change(function() {
//			 index = $(".sel_chage option:selected").val();
//			 $('.sel_con').hide();
//			 $('.sel_con' + index).show();
			visibleUserInfo();
		});

		$('#popupUpperMenuId').click(function (e) {
			e.preventDefault();
			let pagetitle = $(this).attr("title");
			let page = "/menu/menuPopup";
			let $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
				.html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
				.dialog({
					autoOpen: false,
					modal: true,
					width: 600,
					height: 550,
					title: pagetitle
				});
			$dialog.dialog('open');
		});

		$("#startDt").datepicker();
		$("#endDt").datepicker();
		
		$(document).ready(function() {
			visibleUserInfo();
			<c:if test="${empty projectInfo.startDt}">
			$("#startDt").val(getToday("-"));	//시작일은 디펄트로 오늘
			</c:if>
		});
		
	});

</script>

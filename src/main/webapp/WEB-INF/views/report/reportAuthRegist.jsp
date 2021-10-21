<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">보고서 권한 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>분석 관리</li>
			<li>보고서 권한 관리</li>
		</ul>
	</div>

	<form name="reportAuthForm" id="reportAuthForm" method="post">
		<table class="tb02"
			summary="번호, 소유자, 요청일, 상태, 권한부여일, 보고서 형태, 보고서명, URL, 비고, 권한부여대상자, 반려사유에 대한 내용입니다.">
			<caption class="hidden">보고서 권환 관리 목록</caption>
			<colgroup>
				<col style="width: 12.5%">
				<col style="width: 37.5%">
				<col style="width: 12.5%">
				<col style="width: 37.5%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">권한부여일</th>
					<td class="tc">
						<c:if test="${empty reportAuthInfo}">-</c:if>
						<c:if test="${not empty reportAuthInfo}"><spring:eval expression='reportAuthInfo.rgstDt'/></c:if>
					</td>
					<th>보고서 유형</th>
					<td>
						<div class="radio_wrap">
							<c:forEach var="codeRptTy" items="${codeRptTyList}" varStatus="status">
								<div class="radio">
								    <input type="radio" name="reportTy" id="reportTy${status.count}" disabled <c:if test="${reportAuthInfo.reportTy eq codeRptTy.codeId}">checked</c:if> value="${codeRptTy.codeId}">
								    <label for="reportTy${status.count}">
								        <span class="icon">
								            <span></span>
								        </span>
								        <span class="txt">${codeRptTy.codeNm}</span>
								    </label>
								</div>
                        	</c:forEach>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">보고서명</th>
					<td colspan="3">
						<div class="tb_wb_wrap">
							<input type="hidden" name="reportId" id="reportId" value="${reportAuthInfo.reportId }">
							<input type="input" class="input" id="reportNm" value="${reportAuthInfo.reportNm }" disabled>
							<button type="button" class="btn small grayr wide" onclick="searchReport();">보고서 상세정보</button>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">미리보기 URL</th>
					<td colspan="3">
						<div class="tb_wb_wrap">
							<input type="input" class="input" name="previewUrl" id="previewUrl" value="${reportAuthInfo.previewUrl }">
							<button type="button" class="btn small grayr wide">미리보기</button>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">비고</th>
					<td colspan="3" class="tc">
						<!-- 임시 name -->
						<input type="input" class="input" name="reportDsc" id="reportDsc" value="전월대비 앱 MAU 동향">
					</td>
				</tr>
				<tr>
					<th scope="row">권한부여 대상자 ID</th>
					<td colspan="3" class="tc">
						<input type="input" class="input" name="userId" id="userId" value="${reportAuthInfo.userId }">
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="rqstResn">요청사유</label></th>
					<td colspan="3"><div class="textarea">
							<textarea name="rqstResn" id="rqstResn">${reportAuthInfo.rqstResn }</textarea>
						</div></td>
				</tr>
				<tr>
					<th scope="row"><label for="rjctResn">반려사유</label></th>
					<td colspan="3"><div class="textarea">
							<textarea name="rjctResn" id="rjctResn">${reportAuthInfo.rjctResn }</textarea>
						</div></td>
				</tr>
			</tbody>
		</table>
		
		<input type="hidden" name="aprvId" id="aprvId" value="${reportAuthInfo.aprvId }">

		<div class="btn_wrap tr">
			<button type="button" class="btn big blue" onclick="upsertInfo();">승인</button>
			<button type="button" class="btn big blue" onclick="deleteInfo();">반려</button>
			<button type="button" class="btn big grayb" onclick="location.href='/project/reportAuth'">취소</button>
		</div>
	</form>

</section>
<script type="text/javascript" src="/js/reportAuthRegist.js"></script>
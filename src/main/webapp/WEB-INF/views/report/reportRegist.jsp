<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">보고서 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>분석 관리</li>
			<li>보고서 관리</li>
		</ul>
	</div>

	<form name="reportForm" id="reportForm" method="post">
		<table class="tb02"
			summary="프로젝트명, 보고서명, 대시보드 URL, 비고, 파라미터값, 대시보드 설명, 요청사항, 보고서 유형, 보고서 성격, 보기 공유자, 관리권한 소유자, 부서, 업데이트 스케쥴, 등록일, 최초 작성자, 노출 썸네일 선택, 보고서 미리보기, 활성화 여부에 대한 내용입니다.">
			<caption class="hidden">보고서 등록 목록</caption>
			<colgroup>
				<col style="width: 15%">
				<col style="width: 35%">
				<col style="width: 15%">
				<col style="width: 35%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">프로젝트명</th>
					<td colspan="3"><c:out value="${reportInfo.projectNm }" /> <input
						type="hidden" id="projectId" name="projectId"
						value="${reportInfo.projectId  }" /></td>
				</tr>
				<!--  
			<tr>
				<th scope="row"><label for="tableauWorkbookId">태블로워크북</label></th>
				<td>
					<div class="select half">
						<c:out value="${reportInfo.reportNm }"/>
						<input type="hidden" id="tableauWorkbookId" name="tableauWorkbookId" value="${reportInfo.tableauWorkbookId  }"/>
						<select name="tableauWorkbookId" id="tableauWorkbookId">
							<option <c:if test="${reportInfo.tableauWorkbookId == null}">selected="selected"</c:if> value="">:워크북선택:</option>							
							<c:forEach var="workbook" items="${tableauWorkbookList}">
							<option <c:if test="${reportInfo.tableauWorkbookId != null && reportInfo.tableauWorkbookId eq workbook.tableau_workbook_id}">selected="selected"</c:if> value="<c:out value='${workbook.tableau_workbook_id}'/>">${workbook.tableau_workbook_nm} (${workbook.tableau_workbook_url})</option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>
			-->
				<tr>
					<th scope="row">보고서명</th>
					<td colspan="3"><c:out value="${reportInfo.reportNm }" /> <input
						type="hidden" class="input" name="reportNm" id="reportNm"
						value="${reportInfo.reportNm }"> <input type="hidden"
						class="input" name="tableauWorkbookId" id="tableauWorkbookId"
						value="${reportInfo.tableauWorkbookId }"> <input
						type="hidden" class="input" name="previewUrl" id="previewUrl"
						value="${reportInfo.previewUrl }"> <input type="hidden"
						class="input" name="ver" id="ver" value="${reportInfo.ver }">
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="reportUrl">보고서 URL</label></th>
					<td colspan="3"><input type="hidden" class="input"
						name="reportUrl" id="reportUrl" value="${reportInfo.reportUrl }">
						<div class="tb_wb_wrap">
							<c:out value="${reportInfo.reportUrl }" />
							<c:if test="${reportInfo.useYn eq 'Y' }">
								<button type="button" class="btn small grayr wide fr"
									id="reportPreview" useYn="${reportInfo.useYn }">보고서 검증</button>
							</c:if>
						</div></td>
				</tr>
				<tr>
					<th scope="row">비고</th>
					<td colspan="3"><input type="text" class="input"
						name="reportDsc" id="reportDsc" value="${reportInfo.reportDsc }"
						maxLength="500" /></td>
				</tr>

				<tr>
					<th scope="row"><label for="date">파라미터값</label>
						<button type="button">
							<img src="/images/icon_plus.png" id="plus" />
						</button></th>
					<td id="paramTd" colspan="3"><input type="hidden"
						class="input" name="reportAttr" id="reportAttr"
						value="${reportAttrs }" /> <c:choose>
							<c:when test="${reportAttrs.length() > 0}">
								<c:forEach var="i" begin="0" end="${reportAttrs.length() -1 }"
									varStatus="status">
									<c:set var="p" value="${reportAttrs.getJSONObject(i) }" />
									<div class="tb_para" style="margin-top: 5px;">
										<div style="display: inline-block;">
											파라미터명 : <input type="text" class="input wmid"
												name="paramList[0].paramNm" id="paramNm"
												value="${p.getString('paramNm') }"> 파라미터타입 :
										</div>
										<div class="select wmid">
											<select name="paramList[0].paramType" id="paramType">
												<option value=""
													<c:if test="${p.getString('paramType') == ''}">selected="selected"</c:if>>파라미터타입</option>
												<option value="1"
													<c:if test="${p.getString('paramType') == '1'}">selected="selected"</c:if>>당일</option>
												<option value="2"
													<c:if test="${p.getString('paramType') == '2'}">selected="selected"</c:if>>전일</option>
												<option value="3"
													<c:if test="${p.getString('paramType') == '3'}">selected="selected"</c:if>>전전일</option>
												<option value="4"
													<c:if test="${p.getString('paramType') == '4'}">selected="selected"</c:if>>당원</option>
												<option value="5"
													<c:if test="${p.getString('paramType') == '5'}">selected="selected"</c:if>>전월</option>
												<option value="6"
													<c:if test="${p.getString('paramType') == '6'}">selected="selected"</c:if>>전전월</option>
												<option value="7"
													<c:if test="${p.getString('paramType') == '7'}">selected="selected"</c:if>>당해년도</option>
												<option value="8"
													<c:if test="${p.getString('paramType') == '8'}">selected="selected"</c:if>>전년도</option>
												<option value="9"
													<c:if test="${p.getString('paramType') == '9'}">selected="selected"</c:if>>전전년도</option>
												<option value="99"
													<c:if test="${p.getString('paramType') == '99'}">selected="selected"</c:if>>직접입력</option>
											</select>
										</div>
										<div style="display: inline-block;">
											파라미터값 : <input type="text" class="input wmid"
												name="paramList[0].paramValue" id="paramValue"
												value="${p.getString('paramValue') }"
												<c:if test="${p.getString('paramType') != '99'}">readonly</c:if>>
										</div>
										<div style="display: inline-block;">
											<label> <span class='icon'><img
													src='/images/minus.png' style="cursor: pointer;" /></span>
											</label>
										</div>
									</div>
								</c:forEach>
							</c:when>
						</c:choose></td>
				</tr>

				<tr>
					<th scope="row"><label for="request">요청사항</label></th>
					<td colspan="3">
						<!-- 임시 name --> <input type="text" class="input" name="rqstResn"
						id="rqstResn" value="${reportInfo.rqstResn}" maxLength="1000" />
					</td>
				</tr>

				<tr>
					<th scope="row"><label for="reportTy">보고서 유형</label></th>
					<td colspan="3">
						<div class="radio_wrap">
							<c:forEach var="codeRptTy" items="${codeRptTyList}"
								varStatus="status">
								<c:if test="${status.first or status.last}">
									<div class="radio">
										<input type="radio" name="reportTy"
											id="reportTy${status.count}"
											<c:if test="${reportInfo.reportTy eq codeRptTy.codeId}">checked</c:if>
											value="${codeRptTy.codeId}"> <label
											for="reportTy${status.count}"> <span class="icon">
												<span></span>
										</span> <span class="txt">${codeRptTy.codeNm}</span>
										</label>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</td>
				</tr>

				<%--
			<tr>
				<th scope="row">보고서 성격</th>
				<td>
					<div class="tb_mr_wrap">
						<div class="select wsmall">
							<label class="hidden" for="data09">성격 유형1</label>
							<select name="data09" name="data09" id="data09">
								<option value="성격1">성격1</option>
							</select>
						</div>
						<div class="select wsmall">
							<label class="hidden" for="data10">성격 유형2</label>
							<select name="data10" name="data10" id="data10">
								<option value="성격2">성격2</option>
							</select>
						</div>
						<div class="select wsmall">
							<label class="hidden" for="data11">성격 유형3</label>
							<select name="data11" name="data11" id="data11">
								<option value="성격3">성격3</option>
							</select>
						</div>
					</div>
				</td>
			</tr>
			--%>

				<tr>
					<th scope="row"><label for="pcptInfo_">보기 공유자</label></th>
					<td colspan="3">
						<textarea class="hidden" name="pcptInfo" id="pcptInfo"><c:out value="${reportInfo.pcptInfo}" /></textarea>
						<textarea class="hidden" name="oldPcptInfo" id="oldPcptInfo"><c:out	value="${reportInfo.pcptInfo}" /></textarea>
						<div class="td_rb_wrap">
							<div class="radio_wrap fl" id="pcptInfoCotainer"
								style="max-width: 90%"></div>
							<button type="button" title="보기 공유자" class="btn small grays fr memberDept_btn" value="2">검색</button>
						</div></td>
				</tr>

				<tr>
					<th scope="row"><label for="mgrInfo_">편집 권한</label></th>
					<td colspan="3">
						<textarea class="hidden" name="mgrInfo"	id="mgrInfo"><c:out value="${reportInfo.mgrInfo}" /></textarea>
						<textarea class="hidden" name="oldMgrInfo" id="oldMgrInfo"><c:out value="${reportInfo.mgrInfo}" /></textarea>
						<div class="td_rb_wrap">
							<div class="radio_wrap fl" id="mgrInfoCotainer"
								style="max-width: 90%"></div>
							<button type="button" title="편집권한" class="btn small grays fr memberDept_btn" value="1">검색</button>
						</div></td>
				</tr>

				<tr>
					<th scope="row"><label for="deptCode">관리 부서</label></th>
					<td colspan="3"><textarea class="hidden" name="deptInfo"
							id="deptInfo"><c:out value="${reportInfo.deptInfo}" /></textarea>
						<textarea class="hidden" name="oldDeptInfo" id="oldDeptInfo"><c:out
								value="${reportInfo.deptInfo}" /></textarea> <input type="hidden"
						id="deptCode" name="deptCode" value="${reportInfo.deptCode}" /> <input
						type="hidden" id="deptNm" name="deptNm"
						value="${reportInfo.deptNm}" />
						<div class="td_rb_wrap">
							<div class="fl" id="deptInfoCotainer" style="max-width: 90%">
								<c:out value="${reportInfo.deptNm}" />
							</div>
							<button type="button" title="관리부서 소유자"
								class="btn small grays fr dept_btn" value="">검색</button>
						</div></td>
				</tr>
				<tr>
					<th scope="row"><label for="deptCode">관리자(정)</label></th>
					<input type="hidden" id="mainPicrId" name="mainPicrId"
						value="${reportInfo.mainPicrId}" />
					<td class="mainPicrId"><c:out value="${reportInfo.mainPicrNm}" />
					</td>
					<th scope="row"><label for="deptCode">관리자(부)</label></th>
					<input type="hidden" id="subPicrId" name="subPicrId"
						value="${reportInfo.subPicrId}" />
					<td class="subPicrId"><c:out value="${reportInfo.subPicrNm}" />
					</td>
				</tr>

				<!--
			<tr>
				<th scope="row"><label for="wrkCat">업무카테고리</label></th>
				<td>
					<input type="text" class="input half mr10" name="wrkCat" id="wrkCat" value="마케팅" disabled>
					<button type="button" class="btn small grays fr">검색</button>
				</td>
			</tr>
			-->
				<!--  
			<tr>
				<th scope="row"><label for="executCycle1">업데이트 스케쥴</label></th>
				<td>
					<%//executCycle1,executCycle2 두개값을 조합하여 저장한다. %>
					<input type="hidden" id="executCycle" name="executCycle" value="${reportInfo.executCycle}" />
					<div class="select wsmall">
						<%//공통코드로 처리!!! 일별,주별,월별,분기별,연별 %>
						<select id="executCycle1">
							<option value="">일별</option>
							<option value="">주별</option>
							<option value="">월별</option>
							<option value="">분기별</option>
							<option value="">연별</option>
						</select>
					</div>
					<input type="text" class="input half" id="executCycle2" value="" />
				</td>
			</tr>
			-->
				<c:if test="${reportId!=null && reportId ne ''}">
					<tr>
						<th scope="row">등록일</th>
						<td colspan="3"><c:if test="${empty reportInfo.rgstDt}">-</c:if>
							<c:if test="${not empty reportInfo.rgstDt}">
								<spring:eval expression='reportInfo.rgstDt' />
							</c:if></td>
					</tr>
					<!--
			<tr>
				<th scope="row">최초 작성자</th>
				<td>
					<c:if test="${empty reportInfo.rgstId}">-</c:if>
					<c:if test="${not empty reportInfo.rgstId}">${reportInfo.rgstId}</c:if>
				</td>
			</tr>
			 -->
				</c:if>

				<!--             <tr> -->
				<!--                 <th scope="row">노출 썸네일 선택</th> -->
				<!--                 <td> -->
				<!--                     <div class="radio_swiper_wrap"><div class="radio_swiper"> -->
				<!--                         <div class="swiper-wrapper"> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_1" id="data15_1" checked=""><label for="data15_1"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum01.png" alt="썸네일1"></span></label></div> -->
				<!--                             </div> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_2" id="data15_2"><label for="data15_2"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum02.png" alt="썸네일2"></span></label></div> -->
				<!--                             </div> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_3" id="data15_3"><label for="data15_3"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum03.png" alt="썸네일3"></span></label></div> -->
				<!--                             </div> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_4" id="data15_4"><label for="data15_4"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum01.png" alt="썸네일1"></span></label></div> -->
				<!--                             </div> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_5" id="data15_5"><label for="data15_5"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum02.png" alt="썸네일2"></span></label></div> -->
				<!--                             </div> -->
				<!--                             <div class="swiper-slide"> -->
				<!--                                 <div class="radio"><input type="radio" name="data15" name="data15_6" id="data15_6"><label for="data15_6"><span class="icon"><span></span></span><span class="img"><img src="/images/img_thum03.png" alt="썸네일3"></span></label></div> -->
				<!--                             </div> -->
				<!--                         </div> -->
				<!--                         <div class="swiper-button-next"></div><div class="swiper-button-prev"></div> -->
				<!--                     </div></div> -->
				<!--                 </td> -->
				<!--             </tr> -->
				<!-- 
			<tr>
				<th scope="row"><label for="previewUrl">보고서 미리보기</label></th>
				<td>
					<input type="text" class="input half mr10" name="previewUrl" id="previewUrl" value="${reportInfo.previewUrl}" />
					<button type="button" class="btn small grays fr">이미지 생성</button>
				</td>
			</tr>
 -->
				<tr>
					<th scope="row"><label for="sanctnInfo">게시방법</label></th>
					<td colspan="3">
						<div class="select half">
							<!-- name 위치 변경 -->
							<select name="atmcAprvYn" id="atmcAprvYn">
								<c:forEach var="codeRptAtmcAprvYn"
									items="${codeRptAtmcAprvYnList}">
									<option
										<c:if test="${reportInfo.atmcAprvYn eq codeRptAtmcAprvYn.codeId}">selected="selected"</c:if>
										value="<c:out value='${codeRptAtmcAprvYn.codeId}'/>">${codeRptAtmcAprvYn.codeNm}</option>
								</c:forEach>
							</select>
						</div>
						<div style="display: inline-block">
							<input type="text" class="input wmid mr10" style="width: 535px"
								name="searchSanctnInfo" id="searchSanctnInfo" value="test"
								disabled>
							<button type="button" class="btn small grays">결재선 선택</button>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="activeYn">활성화 여부</label></th>
					<td colspan="3">
						<div class="select half">
							<select name="activeYn" id="activeYn">
								<c:forEach var="codeActiveYn" items="${codeActiveYnList}">
									<option
										<c:if test="${reportInfo.activeYn eq codeActiveYn.codeId}">selected="selected"</c:if>
										value="<c:out value='${codeActiveYn.codeId}'/>">${codeActiveYn.codeNm}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>

			</tbody>
		</table>

		<input type="hidden" name="reportId" id="reportId"
			value="${reportId }">

		<div class="btn_wrap tr">
			<%--
			<c:if test="${not empty projectInfo}">
				<button type="button" class="btn big blue">신청</button>
				<button type="submit" class="btn big blue">저장</button>
				<button type="button" class="btn big grayb">신청철회</button>
			</c:if>
			--%>
			<c:if test="${reportInfo.useYn eq 'Y' }">
				<button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
			</c:if>
			<button type="button" class="btn big grayb"
				onclick="location.href='/project/report'">목록</button>
		</div>
	</form>

</section>
<%@include file="/WEB-INF/views/popup/memberDeptPopup.jsp"%>
<%@include file="/WEB-INF/views/popup/deptPopup.jsp"%>

<div id="paramDiv" style="display: none;">
	<div class="tb_para" style="margin-top: 5px;">
		<div style="display: inline-block;">
			파라미터명 : <input type="text" class="input wmid"
				name="paramList[0].paramNm" id="paramNm" value=""> 파라미터타입 :
		</div>
		<div class="select wmid">
			<select name="paramList[0].paramType" id="paramType">
				<option value="">파라미터타입</option>
				<option value="1">당일</option>
				<option value="2">전일</option>
				<option value="3">전전일</option>
				<option value="4">당월</option>
				<option value="5">전월</option>
				<option value="6">전전월</option>
				<option value="7">당해년도</option>
				<option value="8">전년도</option>
				<option value="9">전전년도</option>
				<option value="99">직접입력</option>
			</select>
		</div>
		<div style="display: inline-block;">
			파라미터값 : <input type="text" class="input wmid"
				name="paramList[0].paramValue" id="paramValue" value="" readonly>
		</div>
		<div style="display: inline-block;">
			<label> <span class='icon'><img
					src='/images/minus.png' style="cursor: pointer;" /></span>
			</label>
		</div>
	</div>
</div>
<link href="/css/swiper.css" rel="stylesheet" type="text/css" />
<script src="/js/swiper.js"></script>
<script type="text/javascript" src="/js/reportRegist.js"></script>
<script type="text/javascript">
	var swiper = new Swiper('.radio_swiper', {
		slidesPerView : 3,
		spaceBetween : 10,
		navigation : {
			nextEl : '.swiper-button-next',
			prevEl : '.swiper-button-prev',
		},
	});
</script>

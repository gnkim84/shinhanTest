<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div class="content_tit">
		<h1 class="content_h1">사용자 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>시스템 관리</li>
			<li>사용자 관리</li>
		</ul>
	</div>

	<form name="searchForm" id="searchForm" method="post" action="/system/member">
		<input type="hidden" name="page" id="page" value="<c:out value='${pages.page}'/>" />
		<input type="hidden" name="sortType" id="sortType" >
		<input type="hidden" name="sortDirection" id="sortDirection" >
	<div class="select_wrap tr">
			<fieldset>
				<div class="select">
<!-- 				
					<legend class="hidden">사용자 목록 종류 선택</legend>
					<label class="hidden" for="searchCompanyCode">신한 종류 선택</label>
					<select name="searchCompanyCode" id="searchCompanyCode">
						<option value="">전체</option>
					<c:forEach var="companyCd" items="${codeCompanyCdList}">
						<option value="<c:out value='${companyCd.codeId}'/>">${companyCd.codeNm}</option>
					</c:forEach>
					</select>
-->
					<legend class="hidden">권한그룹 종류 선택</legend>
					<label class="hidden" for="searchRoleCode">권한그룹 종류 선택</label>
					<select name="searchRoleCode" id="searchRoleCode">
						<option value="">전체</option>
						<c:forEach var="role" items="${roles}" varStatus="status">
						<option value='<c:out value="${role.authId}"/>'><c:out value="${role.authNm}"/></option>
						</c:forEach>	
					</select>
				
				</div>
				<input type="button" value="검색" onclick="search();" class="btn small gray">
			</fieldset>
			<fieldset>
				<div class="select">
					<label class="hidden" for="searchUseYn">YES, NO 선택</label>
					<select name="searchUseYn" id="searchUseYn">
						<option value="">전체</option>
						<c:forEach var="useYn" items="${codeUseYnList}">
							<option value="<c:out value='${useYn.codeId}'/>">${useYn.codeNm}</option>
						</c:forEach>
					</select>
				</div>
				<input type="button" value="검색" onclick="search();" class="btn small gray"/>
			</fieldset>
			<fieldset>
				<div class="select">
					<label class="hidden" for="filterLockYn">YES, NO 선택</label>
					<select name="filterLockYn" id="filterLockYn">
						<option value="">전체</option>
						<option value="Y">잠김</option>
						<option value="N">미잠김</option>
					</select>
				</div>
				<input type="button" value="검색" onclick="search();" class="btn small gray"/>
			</fieldset>			
	</div>

	<div class="tb01_line"></div>
	<table class="tb01 cursor" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 행사/사번, 성명, 직위, 조직명, 회사명, 권한그룹, 최종접속, 사용기간, 사용여부에 대한 내용입니다.">
		<caption class="hidden">사용자 목록</caption>
		<colgroup>
			<col style="width:6%">
			<col style="width:10%">
			<col style="width:7%">
			<col style="width:7%">
			<col style="width:*">
			<col style="width:10%">
			<col style="width:10%">
			<col style="width:12%">
			<col style="width:14%">
			<col style="width:5%">
			<col style="width:5%">
		</colgroup>
		<thead>
		<tr>
			<th scope="col">No</th>
			<th scope="col">
				사번
				<div class="tb_btn">
					<button type="button" class="btn_up" onclick="searchSort('user_id', 'asc');">사번 오름차순</button>
					<button type="button" class="btn_down" onclick="searchSort('user_id', 'desc');">사번 내림차순</button>
				</div>
			</th>
			<th scope="col">성명
				<div class="tb_btn">
					<button type="button" class="btn_up" onclick="searchSort('user_nm', 'asc');">성명 오름차순</button>
					<button type="button" class="btn_down" onclick="searchSort('user_nm', 'desc');">성명 내림차순</button>
				</div>
			</th>
			<th scope="col">직위</th>
			<th scope="col">부서명
				<div class="tb_btn">
					<button type="button" class="btn_up" onclick="searchSort('dept_nm', 'asc');">부서명 오름차순</button>
					<button type="button" class="btn_down" onclick="searchSort('dept_nm', 'desc');">부서명 내림차순</button>
				</div>
			</th>
			<th scope="col">회사구분</th>
			<th scope="col">권한</th>
			<th scope="col">
				최종접속
				<div class="tb_btn">
					<button type="button" class="btn_up" onclick="searchSort('last_log_dt', 'asc');">최종접속 오름차순</button>
					<button type="button" class="btn_down" onclick="searchSort('last_log_dt', 'desc');">최종접속 내림차순</button>
				</div>
			</th>
			<th scope="col">
				사용기간
				<div class="tb_btn">
					<button type="button" class="btn_up" onclick="searchSort('end_dt', 'asc');">사용기간 오름차순</button>
					<button type="button" class="btn_down" onclick="searchSort('end_dt', 'desc');">사용기간 내림차순</button>
				</div>
			</th>
			<th scope="col">사용여부</th>
			<th scope="col">잠김여부</th>
		</tr>
		</thead>
		<tbody id="memberListTbody">
		<c:forEach var="member" items="${members}" varStatus="status">
		<tr <c:if test="${member.lockYn eq 'Y'}">class="tb_old"</c:if> userId="${member.userId}">
			<td scope="row"><c:out value="${pages.totalCount - (status.index + (pages.page -1) * pages.pageSize)}"/></td>
			<td><c:out value="${member.userId}"/></td>
			<td><c:out value="${member.userNm}"/></td>
			<td><c:out value="${member.pstnNm}"/></td>
			<td><c:out value="${member.deptNm}"/></td>
			<td><c:out value="${member.companyNm}"/></td>
			<td><c:out value="${member.authNm}"/></td>
			<td><spring:eval expression="member.lastLogDt" /></td>
			<td><spring:eval expression="member.startDt"/>~<spring:eval expression="member.endDt"/></td>
			<td><c:out value="${member.useYn}"/></td>
			<td class="unlockAccount" id="unlock_${member.userId}"><c:if test="${member.lockYn eq 'Y'}">[해제]</c:if></td>
		</tr>
		</c:forEach>
		</tbody>
	</table>


	<div class="page"></div>

	<div class="search_wrap">
		<fieldset>
			<legend class="hidden">사용자 목록 게시물 검색</legend>

			<div class="select">
				<label class="hidden" for="pageSize">게시물 갯수</label>
				<select name="pageSize" id="pageSize">
					<option value="10">10건</option>
					<option value="20">20건</option>
					<option value="50">50건</option>
				</select>
			</div>

			<div class="select">
				<label class="hidden" for="searchKey">검색 항목</label>
				<select name="searchKey" id="searchKey">
					<option value="ALL">전체</option>
					<c:forEach var="memSearchCd" items="${codeMemSearchCdList}" varStatus="status">
						<option value="<c:out value='${memSearchCd.codeId}'/>">${memSearchCd.codeNm}</option>
					</c:forEach>
				</select>
			</div>

			<label class="hidden" for="searchValue">검색어</label>
			<input type="text" class="input" id="searchValue" name="searchValue" placeholder="검색어를 입력하세요.">
			<input type="button" value="검색" onclick="search();" class="btn small grays">
		</fieldset>
	</div>
	</form>

	<form name="memberForm" id="memberForm" method="post">
		<h2 class="content_h2">사용자 기본 정보</h2>
		<table class="tb02" summary="행사/사번, 성명, 직위, 조직명, 회사명, 활성화 여부, 권한, 사용기간 등록에 대한 내용입니다.">
			<caption class="hidden">사용자 기본 정보</caption>
			<colgroup>
				<col style="width:12.5%">
				<col style="width:37.5%">
				<col style="width:12.5%">
				<col style="width:37.5%">
			</colgroup>
			<tbody>
			<tr>
				<th scope="row"><label for="orgUserId">사번</label></th>
				<td>
					<input type="text" class="input tc" id="orgUserId" disabled>
					<input type="hidden" id="userId" name="userId" />
				</td>
				<th scope="row"><label for="orgUserNm">성명</label></th>
				<td>
					<input type="text" class="input tc" id="orgUserNm" disabled>
					<input type="hidden" id="userNm" name="userNm" />
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="pstnNm">직위</label></th>
				<td>
					<input type="text" class="input tc" id="pstnNm" disabled />
					<input type="hidden" id="pstnCode" name="pstnCode" />
				</td>
				<th scope="row"><label for="deptNm">부서명</label></th>
				<td>
					<input type="text" class="input tc" id="deptNm" disabled />
					<input type="hidden" name="deptCode" id="deptCode" />
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="companyNm">회사구분</label></th>
				<td>
					<input type="text" class="input tc" id="companyNm" disabled />
					<input type="hidden" name="companyCode" id="companyCode" />
				</td>
				<th scope="row"><label for="useYn">활성화 여부</label></th>
				<td>
					<div class="select">
						<select name="useYn" id="useYn" class="tc">
							<c:forEach var="useYn" items="${codeUseYnList}">
								<option value="<c:out value='${useYn.codeId}'/>">${useYn.codeNm}</option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="authId">사용자 권한</label></th>
				<td>
					<div class="select">
						<input type="hidden" id="userAuthChage" name="userAuthChage" value="N"/>
						<select name="authId" id="authId" class="tc">
							<c:forEach var="role" items="${roles}" varStatus="status">
							<option value='<c:out value="${role.authId}"/>'><c:out value="${role.authNm}"/></option>
							</c:forEach>
						</select>
					</div>
				</td>
				<th scope="row">사용기간등록</th>
				<td>
					<div class="input_date_wrap">
						<div class="input_date">
							<label class="hidden" for="date_before">사용기간 시작 날짜</label>
							<input type="text" class="input" name="startDate" id="date_before">
							<button type="button" id="startDate" class="btn_date">사용기간 시작 날짜 검색</button>
						</div>
						<div class="input_wave">~</div>
						<div class="input_date">
							<label class="hidden" for="date_after">사용기간 끝나는 날짜</label>
							<input type="text" class="input" name="endDate" id="date_after">
							<div id="endDate" class="btn_date">사용기간 끝나는 날짜 검색</div>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="authId">관리자 권한</label></th>
				<td>
					<div class="select">
						<input type="hidden" id="mgrAuthChange" name="mgrAuthChange" value="N"/>
						<select name="mgrAuthId" id="mgrAuthId" class="tc">
							<option value="">권한 없음</option>
							<c:forEach var="mgrRole" items="${mgrRoles}" varStatus="status">
								<option value='<c:out value="${mgrRole.authId}"/>'><c:out value="${mgrRole.authNm}"/></option>
							</c:forEach>
						</select>
					</div>
				</td>
				<th scope="row"></th>
				<td>
				</td>
			</tr>
			</tbody>
		</table>

		<!-- <h2 class="content_h2">권한 설정</h2>
		<div class="search_bg_wrap">
			<label for="search01">프로젝트</label>
			<div class="select">
				<select name="search01" id="search01">
					<option value="나의 프로젝트 리스트">나의 프로젝트 리스트</option>
				</select>
			</div>
			<label for="search02">최종 분석 HDFS 접근이력</label>
			<div class="select">
				<select name="search02" id="search02">
					<option value="2020.10.13 13:00:52">2020.10.13 13:00:52</option>
				</select>
			</div>
			<input type="button" value="검색" class="btn small grayr">
		</div> -->

		<div class="btn_wrap tr">
			<button type="button" id="saveMember" class="btn big blue">저장</button>
		</div>
	</form>

</section>
<script type="text/javascript" src="/js/member.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#searchCompanyCode').val('${pages.searchCompanyCode}');
		$('#searchRoleCode').val('${pages.searchRoleCode}');
		$('#searchUseYn').val('${pages.searchUseYn}');
		$('#filterLockYn').val('${pages.filterLockYn}');
		$('#pageSize').val('${pages.pageSize}');
		$('#searchKey').val('${pages.searchKey}');
		$('#searchValue').val('${pages.searchValue}');

		$("#date_before").datepicker({
// 			showOn : "both",
			dateFormat : 'yy-mm-dd',
			onClose : function(selectedDate){
				$("#date_after").datepicker(
					"option",
					"minDate",
					selectedDate
				);
			}
		});

		$("#date_after").datepicker({
// 			showOn : "both",
			dateFormat : 'yy-mm-dd',
			onClose : function(selectedDate){
				$("#date_before").datepicker(
					"option",
					"maxDate",
					selectedDate
				);
			}
		});

		$("#authId").on("change", function(){
			$("#userAuthChage").val("Y");
		});

		$("#mgrAuthId").on("change", function(){
			$("#mgrAuthChange").val("Y");
		});

		$("#memberListTbody td").click(function () {
			var userId = $(this).parent().attr("userId");
			if ($(this).hasClass("unlockAccount")) {
				unlockAccount(userId);
			} else {
				getMemberData(userId);
			}
		});
	});

	/**
	 * 페이징 처리 공통 함수
	 */
	$('.page').bootpag({		// 페이징을 표시할 div의 클래스
		total: ${pages.totalPage},  // 페이징모델의 전체페이지수
		page: ${pages.page},		// 페이징모델의 현재페이지번호
		maxVisible: 10,  // 보여질 최대 페이지수
		firstLastUse: true,			 // first와 last사용유무
		wrapClass: 'page',			  // 페이징을 감싼 클래스명
		activeClass: 'on',			  // 현재페이지의 클래스명
		disabledClass: 'disabled',	  // 각 항목별 클래스 지정
		nextClass: 'next',
		prevClass: 'prev',
		lastClass: 'last',
		firstClass: 'first'
	}).on("page", function(event, num){
		$("#page").val(num);
		$("#searchForm").submit();
	});
	
</script>

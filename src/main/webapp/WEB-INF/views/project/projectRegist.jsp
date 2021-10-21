<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
					<input type="text" class="input" name="projectNm" id="projectNm" maxLength="50" <c:if test="${not empty projectInfo.projectNm}">value="${projectInfo.projectNm}"</c:if>/>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="projectTy">프로젝트 유형</label></th>
				<td>
					<div class="select half">
						<select name="projectTy" id=projectTy class="sel_chage">
							<c:forEach var="codePrjType" items="${codePrjTypeList}">
								<option <c:if test="${projectInfo.projectTy eq codePrjType.codeId}">selected="selected"</c:if>value="<c:out value='${codePrjType.codeId}'/>">${codePrjType.codeNm}</option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>
<!-- 			
			<tr>
				<th scope="row">참여자</th>
				<td>
					<div class="td_rb_wrap">
						<div class="radio_wrap fl">
							<div class="radio"><input type="radio" name="data03" id="data03_1" checked=""><label for="data03_1"><span class="icon"><span></span></span><span class="txt">홍길동 책임</span></label></div>
							<div class="radio"><input type="radio" name="data03" id="data03_2"><label for="data03_2"><span class="icon"><span></span></span><span class="txt">이순신 선임</span></label></div>
							<div class="radio"><input type="radio" name="data03" id="data03_3"><label for="data03_3"><span class="icon"><span></span></span><span class="txt">장영실 주임</span></label></div>
						</div>
						<button type="button" class="btn small grays fr">검색</button>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="data05">운영담당자</label></th>
				<td>
					<div class="td_rb_wrap">
						<div class="radio_wrap fl">
							<div class="radio"><input type="radio" name="data04" id="data04_1" checked=""><label for="data04_1"><span class="icon"><span></span></span><span class="txt">홍길동 책임</span></label></div>
						</div>
						<button type="button" class="btn small grays fr">검색</button>
					</div>
				</td>
			</tr>
 -->			
			<tr>
				<th scope="row">프로젝트 기간</th>
				<td>
					<div class="input_date_wrap">
						<div class="input_date">
							<label class="hidden" for="startDt">사용기간 시작 날짜</label>
							<input type="text" class="input" name="startDt" id="startDt" <c:if test="${not empty projectInfo.startDt}">value="<spring:eval expression='projectInfo.startDt'/>"</c:if>>
							<button type="button" class="btn_date" id="_startDt">사용기간 시작 날짜 검색</button>
						</div>
						<div class="input_wave">~</div>
						<div class="input_date">
							<label class="hidden" for="endDt">사용기간 끝나는 날짜</label>
							<input type="text" class="input" name="endDt" id="endDt" <c:if test="${not empty projectInfo.endDt}">value="<spring:eval expression='projectInfo.endDt'/>"</c:if>>
							<button type="button" class="btn_date" id="_endDt">사용기간 끝나는 날짜 검색</button>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="projectDsc">내용</label></th>
				<td>
					<div class="textarea">
						<textarea name="projectDsc" id="projectDsc">${projectInfo.projectDsc }</textarea>
					</div>
				</td>
			</tr>
			</tbody>
		</table>
		

		<% //프로젝트 기타정보(IDE 자원신청, 형상관리) %>
		<div class="sel_con sel_con1" style="display:none;">
			
			<% //IDE 자원신청 %>
			<h2 class="content_h2">IDE 자원신청</h2>
			<table class="tb02" summary="GPU, CPU, MEM, DISK, IDE 엔진, IDE 명에 대한 내용입니다.">
				<caption class="hidden">IDE 자원신청 목록</caption>
				<colgroup>
					<col style="width:12.5%">
					<col style="width:37.5%">
					<col style="width:12.5%">
					<col style="width:37.5%">
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="data06">GPU</label></th>
						<td>
							<div class="select">
								<select name="data06" id="data06" class="tc">
									<option value="2 CORE">2 CORE</option>
								</select>
							</div>
						</td>
						<th scope="row"><label for="data07">CPU</label></th>
						<td>
							<div class="select">
								<select name="data07" id="data07" class="tc">
									<option value="2 CORE">2 CORE</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="data08">MEM</label></th>
						<td>
							<div class="select">
								<select name="data08" id="data08" class="tc">
									<option value="8G">8G</option>
								</select>
							</div>
						</td>
						<th scope="row"><label for="data09">DISK</label></th>
						<td>
							<div class="select">
								<select name="data09" id="data09" class="tc">
									<option value="50 GB">50 GB</option>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="data10">DISK</label></th>
						<td>
							<div class="select">
								<select name="data10" id="data10" class="tc">
									<option value="50 GB">50 GB</option>
								</select>
							</div>
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<th scope="row"><label for="data11">IDE 명</label></th>
						<td colspan="3"><input type="text" class="input tc" id="data11"></td>
					</tr>
				</tbody>
			</table>
	
			<% //형상관리 %>
			<h2 class="content_h2">형상관리</h2>
			<table class="tb02" summary="형상관리 주소, 버전, 편집허용 인원, Project 패키지, 신청테이블에 대한 내용입니다.">
				<caption class="hidden">형상관리 목록</caption>
				<colgroup>
					<col style="width:12.5%">
					<col style="width:37.5%">
					<col style="width:12.5%">
					<col style="width:37.5%">
				</colgroup>
				<tbody>
					<tr>
						<th scope="row"><label for="data12">형상관리 주소</label></th>
						<td>
							<div class="tb_ib_wrap">
								<input type="text" class="input" id="data12" value="https://gitlab.com/respository/temp9">
								<button type="button" class="btn small blue">Git 연동</button>
							</div>
						</td>
						<th scope="row"><label for="data13">tb_ib_wrap</label></th>
						<td><input type="text" class="input" id="data13" disabled=""></td>
					</tr>
					<tr>
						<th scope="row"><label for="data14">편집허용 인원</label></th>
						<td colspan="3"><input type="text" class="input" id="data14" value="홍길동, 김길동, 최창민" disabled=""></td>
					</tr>
					<tr>
						<th scope="row">Project 패키지</th>
						<td colspan="3">
							<table class="tb03" summary="Project 버전, 유형, 명, 추가/삭제에 대한 내용입니다.">
								<caption class="hidden">Project 패키지 목록</caption>
								<colgroup>
									<col style="width:20%">
									<col style="width:30%">
									<col style="width:30%">
									<col style="width:20%">
								</colgroup>
								<thead>
									<tr>
										<th scope="col">Package 버전</th>
										<th scope="col">Package 유형</th>
										<th scope="col">Package 명</th>
										<th scope="col">Package 추가 / 삭제</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>2.1</td>
										<td>PYTHON</td>
										<td>PYTHON package1</td>
										<td>
											<button type="button" class="btn small grayy">삭제</button>
											<button type="button" class="btn small blue">추가</button>
										</td>
									</tr>
									<tr>
										<td>4.1</td>
										<td>PYTHON</td>
										<td>PYTHON package2</td>
										<td>
											<button type="button" class="btn small grayy">삭제</button>
											<button type="button" class="btn small blue">추가</button>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<th scope="row"><label for="data15">신청테이블</label></th>
						<td>
							<div class="tb_ib_wrap">
								<input type="text" class="input" id="data15" value="Table1, Table2, Table3, Table4">
								<button type="button" class="btn small grays">목록조회</button>
							</div>
						</td>
						<td colspan="2"></td>
					</tr>
				</tbody>
			</table>
		</div>

		<% //버튼들 %>
		<div class="btn_wrap tr">
			<% //최초 버튼들 %>
			<c:choose>
				<c:when test="${empty projectInfo.projectId}">
			<!--<button type="button" class="btn big blue" onclick="aprvReq();">신청</button>-->
			<button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
			<button type="button" class="btn big grayb" onclick="location.href='/project/project'">목록</button>
				</c:when>
				<c:otherwise>
			<!--<button type="button" class="btn big blue" onclick="aprvReq();">신청</button>-->
			<button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
			<!--<button type="button" class="btn big grayb" onclick="aprvReqCancle();">신청철회</button>-->
			<button type="button" class="btn big grayb" onclick="location.href='/project/project'">목록</button>
				</c:otherwise>
			</c:choose>			
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

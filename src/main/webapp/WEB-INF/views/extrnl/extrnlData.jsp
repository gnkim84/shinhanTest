<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">외부 데이터 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>외부 자산 관리</li>
			<li>외부 데이터 관리</li>
		</ul>
	</div>

	<%-- select 영역 --%>
 	<div class="select_wrap tr">
		<fieldset>
			<div class="select">
				<label class="hidden" for="filterDataTyTmp">유형 선택</label>
				<select id="filterDataTyTmp">
					<option value="">전체</option>
				<c:forEach var="dataTy" items="${dataTyList}">
					<option value="<c:out value='${dataTy.codeId}'/>">${dataTy.codeNm}</option>
				</c:forEach>					
				</select>
			</div>
			<input type="button" value="검색" class="btn small gray filterSearchBtn">
		</fieldset>
		<fieldset>
			<div class="select wmid">
				<label class="hidden" for="filterDataTrnsmisMthdTmp">데이터 전송 방식 선택</label>
				<select id="filterDataTrnsmisMthdTmp">
					<option value="">전체</option>
				<c:forEach var="dataTrnsMthd" items="${dataTrnsMthdList}">
					<option value="<c:out value='${dataTrnsMthd.codeId}'/>">${dataTrnsMthd.codeNm}</option>
				</c:forEach>					
				</select>
			</div>
			<input type="button" value="검색" class="btn small gray filterSearchBtn">
		</fieldset>
		<fieldset>
			<div class="select">
				<label class="hidden" for="filterDataTrnsmisCycleTmp">데이터 전송 주기 선택</label>
				<select id="filterDataTrnsmisCycleTmp">
					<option value="">전체</option>
				<c:forEach var="dataTrnsCycle" items="${dataTrnsCycleList}">
					<option value="<c:out value='${dataTrnsCycle.codeId}'/>">${dataTrnsCycle.codeNm}</option>
				</c:forEach>						
				</select>
			</div>
			<input type="button" value="검색" class="btn small gray filterSearchBtn">
		</fieldset>
	</div>

	<%-- 테이블 데이터 출력 영역 --%>
	<div class="tb01_line"></div>
	<table class="tb01 cursor" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 외부기관명, 관리부서, 데이터명, 유형, 데이터전송방식, 데이터전송주기, 담당자, 등록일에 대한 내용입니다.">
		<caption class="hidden">외부 데이터 관리 목록</caption>
		<colgroup>
			<col style="width:6%">
			<col style="width:10%">
			<col style="width:10%">
			<col style="width:*">
			<col style="width:7%">
			<col style="width:12%">
			<col style="width:10%">
			<col style="width:10%">
			<col style="width:10%">
			<col style="width:6%">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">No</th>
				<th scope="col">
					외부기관명
					<div class="tb_btn">
						<button type="button" class="btn_up" onclick="searchSort('src_sys_code', 'ASC');">외부기관명 오름차순</button>
						<button type="button" class="btn_down" onclick="searchSort('src_sys_code', 'DESC');">외부기관명 내림차순</button>
					</div>
				</th>
				<th scope="col">
					관리부서
					<div class="tb_btn">
						<button type="button" class="btn_up" onclick="searchSort('mng_dept_code', 'ASC');">관리부서 오름차순</button>
						<button type="button" class="btn_down" onclick="searchSort('mng_dept_code', 'DESC');">관리부서 내림차순</button>
					</div>
				</th>
				<th scope="col">데이터명</th>
				<th scope="col">유형</th>
				<th scope="col">
					데이터전송방식
					<div class="tb_btn">
						<button type="button" class="btn_up" onclick="searchSort('data_trnsmis_mthd', 'ASC');">데이터전송방식 오름차순</button>
						<button type="button" class="btn_down" onclick="searchSort('data_trnsmis_mthd', 'DESC');">데이터전송방식 내림차순</button>
					</div>
				</th>
				<th scope="col">데이터전송주기</th>
				<th scope="col">담당자</th>
				<th scope="col">
					등록일
					<div class="tb_btn">
						<button type="button" class="btn_up" onclick="searchSort('rgst_dt', 'ASC')">등록일 오름차순</button>
						<button type="button" class="btn_down" onclick="searchSort('rgst_dt', 'DESC')">등록일 내림차순</button>
					</div>
				</th>
				<th scope="col">수정</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${empty extrnlDataList || extrnlDataList.size() eq 0 }">
			<tr>
				<td scope="row" colspan="10">검색 결과가 없습니다.</td>
			</tr>
		</c:if>		
		<c:forEach var="extrnlData" items="${extrnlDataList}" varStatus="status">
				<tr>
					<td><c:out value="${extrnlData.rownum}"/></td>
					<td><c:out value="${extrnlData.srcSysNm}"/></td>
					<td><c:out value="${extrnlData.mngDeptNm}"/></td>
					<td class="tl">
		  				<button type="button" class="btn_pop_view" dataNm="${extrnlData.dataNm}" dataDsc="${extrnlData.dataDsc }" dataTyNm="${extrnlData.dataTyNm }" 
		  				dataItem="${extrnlData.dataItem}" endDt="${extrnlData.endDt}" purchsPicrNm="${extrnlData.purchsPicrNm }" purchsDeptNm="${extrnlData.purchsDeptNm }"
		  				srcSysNm="${extrnlData.srcSysNm}" srcSysUrl="${extrnlData.srcSysUrl }" mngDeptNm="${extrnlData.mngDeptNm }" mngPicrNm="${extrnlData.mngPicrNm }"
		  				dataLc="${extrnlData.dataLc }" langNm="${extrnlData.langNm }" rmk="${extrnlData.rmk }" dataTrnsmisMthdNm="${extrnlData.dataTrnsmisMthdNm }"
		  				cycle="<c:forEach items="${cycle}" var="cycle">
							<c:forEach items="${fn:split(cycle.dataTrnsmisCycleNm, ',')}" var="cycleList" varStatus="stat">
								<c:if test="${cycle.dataId == extrnlData.dataId}">
									<c:if test="${stat.count == 1}">
										${fn:split(cycle.dataTrnsmisCycleNm, ',')[0]}
									</c:if>
									<c:if test="${stat.count != 1}">
										 <c:out value=" / " />${fn:split(cycle.dataTrnsmisCycleNm, ',')[stat.index]}
									</c:if>
								</c:if>
							</c:forEach>
						</c:forEach>"
						deptInfo="<c:forEach items="${deptInfo}" var="dept">
							<c:if test="${dept.dataId == extrnlData.dataId}">
								${dept.deptInfoNm }
							</c:if>
						</c:forEach>
						"
						>
		  					<c:out value="${extrnlData.dataNm}" />
		  				</button>
					</td>
					<td><c:out value="${extrnlData.dataTyNm}"/></td>
					<td><c:out value="${extrnlData.dataTrnsmisMthdNm}"/></td>
					<td>
						<c:forEach items="${cycle}" var="cycle">
							<c:forEach items="${fn:split(cycle.dataTrnsmisCycleNm, ',')}" var="cycleList" varStatus="stat">
								<c:if test="${cycle.dataId == extrnlData.dataId}">
									<c:if test="${stat.count == 1}">
										${fn:split(cycle.dataTrnsmisCycleNm, ',')[0]}
									</c:if>
									<c:if test="${stat.count != 1}">
										 <c:out value=" / " />${fn:split(cycle.dataTrnsmisCycleNm, ',')[stat.index]}
									</c:if>
								</c:if>
							</c:forEach>
						</c:forEach>
					</td>
					<td><c:out value="${extrnlData.mngPicrNm}"/></td>
					<td><c:out value="${fn:substring(extrnlData.rgstDt, 0, 10)}"/></td>
					<td class="tc">
						<a class="tc" href="/external/data/modify/<c:out value="${extrnlData.dataId}"/>">[수정]</a>
					</td>
				</tr>		
			</c:forEach>			
		</tbody>
	</table>
		
	<%-- 버튼영역 --%>
	<div class="btn_wrap tr">
		<button type="button" class="btn big blue" onclick="location.href='/external/data/regist'">데이터 등록</button>
		<button type="button" class="btn big blue btn_pop_batch">일괄 등록</button>
	</div>		
	<%-- 페이징 영역 --%>
	<div class="page"></div>
	<%-- 검색form 영역 --%>
	<form class="search_wrap" id="searchForm" name="searchForm">
		<input type="hidden" name="page" id="page" value="<c:out value='${pages.page}'/>" />
        <input type="hidden" name="sortType" id="sortType" >
        <input type="hidden" name="sortDirection" id="sortDirection" >
		<input type="hidden" name="filterDataTy" id="filterDataTy" />
		<input type="hidden" name="filterDataTrnsmisMthd" id="filterDataTrnsmisMthd" />
		<input type="hidden" name="filterDataTrnsmisCycle" id="filterDataTrnsmisCycle" />
		<fieldset>
			<legend class="hidden">사용자 목록 게시물 검색</legend>
			<div class="select">
				<label class="hidden" for="pageSize">게시물 개수</label>
				<select name="pageSize" id="pageSize">
					<option value="10">10건</option>
					<option value="20">20건</option>
					<option value="30">30건</option>
				</select>
			</div>
			<div class="select">
				<label class="hidden" for="searchKey">검색 항목</label>
				<select name="searchKey" id="searchKey">
					<option value="ALL">전체</option>
					<option value="extOrgNm">외부기관명</option>
					<option value="mgrDeptNm">관리부서</option>
				</select>
			</div>
			<label class="hidden" for="searchValue">검색어</label>
			<input type="text" class="input" id="searchValue" name="searchValue" placeholder="검색어를 입력하세요." value="" />
			<input type="submit" value="검색" class="btn small grays" />
		</fieldset>
	</form>

</section>

<%-- 외부데이터 상세보기 팝업 --%>
<div class="pop pop_ver2 pop_view">
	<div class="pop_wrap">
		<div class="pop_tit">
			외부데이터 상세보기
			<button type="button" class="pop_close">팝업닫기</button>
		</div>
		<div class="pop_con">
			<div class="pop_con_scroll">
				<table class="tb02" summary="데이터명, 설명, 데이터 항목, 유형, 사용종료 예정일, 구매 담당자, 구매 부서, 외부 기관명, 사이트 주소, 관리 담당자, 관리 부서, 데이터 전송 방식, 데이터 전송 주기, 사용 부서, 위치, 언어, 비고에 대한 내용입니다.">
					<caption class="hidden">외부 데이터 상세보기</caption>
					<colgroup>
						<col style="width:15%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>
					<tbody>
						<tr>
							<th scope="row"><label for="data01">데이터명</label></th>
							<td colspan="3">
								<input type="text" class="input" id="popupDataNm" value="" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data0221">설명</label></th>
							<td colspan="3">
								<input type="text" class="input" id="popupDataDsc" value="" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data031">데이터 항목</label></th>
							<td colspan="3">
								<input type="text" class="input" id="popupDataItem" value="" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data0332">유형</label></th>
							<td>
								<input type="text" class="input" id="popupDataTy" value="" disabled>
							</td>
							<th scope="row"><label for="data0342">사용종료 예정일</label></th>
							<td>
								<input type="text" class="input" id="popupEndDt" value="" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data12">구매 담당자</label></th>
							<td>
								<input type="text" class="input" id="popupPurchsPicrId" disabled>
							</td>
							<th scope="row"><label for="data132">구매 부서</label></th>
							<td>
								<input type="text" class="input" id="popupPurchsDeptCode" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data023">외부 기관명</label></th>
							<td>
								<input type="text" class="input" id="popupSrcSysCode" disabled>
							</td>
							<th scope="row"><label for="data186">사이트 주소</label></th>
							<td>
								<input type="text" class="input" id="popupSrcSysUrl" disabled>
							</td>
						</tr>	
						<tr>
							<th scope="row"><label for="data142">관리 담당자</label></th>
							<td>
								<input type="text" class="input" id="popupMngPicrId" disabled>
							</td>
							<th scope="row"><label for="data1342">관리 부서</label></th>
							<td>
								<input type="text" class="input" id="popupMngDeptCode" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data03233">데이터 전송 방식</label></th>
							<td>
								<input type="text" class="input" id="popupDataTrnsmisMthd" disabled>
							</td>
							<th scope="row"><label for="data16">데이터 전송 주기</label></th>
							<td>
								<input type="text" class="input" id="popupDataTrnsmisCycle" disabled>
							</td>
						</tr>	
						<tr>
							<th scope="row"><label for="data1442">사용 부서</label></th>
							<td colspan="3">
								<input type="text" class="input" id="popupDeptInfo" disabled>
							</td>
						</tr>
						<tr>
							<th scope="row"><label for="data0333">위치</label></th>
							<td>
								<input type="text" class="input" id="popupDataLc" disabled>
							</td>
							<th scope="row"><label for="data16">언어</label></th>
							<td>
								<input type="text" class="input" id="popupLang" disabled>
							</td>
						</tr>	
						<tr>
							<th scope="row"><label for="data04">비고</label></th>
							<td colspan="3"><div class="textarea" style="resize: none;">
								<textarea name="data04" id="popupRmk" style="resize: none;" disabled></textarea>
							</div></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="pop_btn btn_wrap tr tb0">
			<button type="submit" class="btn big blue btn_close">확인</button>
		</div>
	</div>
	<div class="pop_bg"></div>
</div>

<%-- 외부데이터 일괄등록 팝업 --%>
<div class="pop pop_ver2 pop_batch">
	<div class="pop_wrap">
		<div class="pop_tit">
			외부데이터 일괄등록
			<button type="button" class="pop_close">팝업닫기</button>
		</div>
		<div class="pop_con">
			<div class="pop_con_scroll">
				<p class="batch_desc">
					간단한 설명이나 안내문구 추가될 예정이므로 2~3줄 여유로 안내문구 공간 확보 필요. 간단한 설명이나 안내문구 추가될 예정이므로 2~3줄 여유로 안내문구 공간 확보 필요. 간단한 설명이나 안내문구 추가될 예정이므로 2~3줄 여유로 안내문구 공간 확보 필요 간단한 설명이나 안내문구 추가될 예정이므로 2~3줄 여유로 안내 문구 공간 확보 필요
				</p>
				<table class="tb02" summary="첨부파일에 대한 내용입니다.">
					<caption class="hidden">외부 데이터 일괄 등록</caption>
					<colgroup>
						<col style="width:15%">
						<col style="width:85%">
					</colgroup>
					<tbody>
						<tr>
							<th scope="row">첨부파일</th>
							<td>
								<div>
									<form action="/external/data/excelUpload" class="dropzone" id="myDropzone">
										<div class="dz-message needsclick" id="myDropzoneDiv">Drag &amp; Drop</div>
									</form>
								</div>								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="pop_btn btn_wrap tr tb0">
			<form id="multiInsertFrm" name="multiInsertFrm" action="/external/data/multiInsert" method="post">
			<input type="hidden" id="excelNewFileName" name="excelNewFileName" value="" />
			<a href="/down/외부데이터_샘플.xlsx"><button type="button" class="btn big blue" id="multiInsertSampleBtn">템플릿 다운로드</button></a>
			<button type="button" class="btn big blue" id="multiInsertBtn">확인</button>
			<button type="button" class="btn big grayl" id="multiInsertCancleBtn">취소</button>
			</form>
		</div>
	</div>
	<div class="pop_bg"></div>
</div>


<script type="text/javascript" src="/js/extrnl.js"></script>
<script type="text/javascript">
	$(function() {
		
		//검색값들 셋팅
		$('#filterDataTyTmp').val('${pages.filterDataTy}');
		$('#filterDataTrnsmisMthdTmp').val('${pages.filterDataTrnsmisMthd}');
		$('#filterDataTrnsmisCycleTmp').val('${pages.filterDataTrnsmisCycle}');
		$('#pageSize').val('${pages.pageSize}');
		$('#searchKey').val('${pages.searchKey}');
		$('#searchValue').val('${pages.searchValue}');		
		
		//팝업 상세보기 
		$('.btn_pop_view').click(function() {
			var popCont = $(this)[0].attributes;
			
			$("#popupDataNm").val(popCont.datanm.value);
			$("#popupDataDsc").val(popCont.datadsc.value);
			$("#popupDataItem").val(popCont.dataitem.value);
			$("#popupDataTy").val(popCont.datatynm.value);
			$("#popupEndDt").val(popCont.enddt.value);
			$("#popupPurchsPicrId").val(popCont.purchspicrnm.value);
			$("#popupPurchsDeptCode").val(popCont.purchsdeptnm.value);
			$("#popupSrcSysCode").val(popCont.srcsysnm.value);
			$("#popupSrcSysUrl").val(popCont.srcsysurl.value);
			$("#popupMngPicrId").val(popCont.mngpicrnm.value);
			$("#popupMngDeptCode").val(popCont.mngdeptnm.value);
			$("#popupDataTrnsmisMthd").val(popCont.datatrnsmismthdnm.value);
			$("#popupDataTrnsmisCycle").val($(this)[0].parentElement.parentElement.children[6].innerText);
 			$("#popupDeptInfo").val((popCont.deptInfo.value).trim());
			$("#popupDataLc").val(popCont.datalc.value);
			$("#popupLang").val(popCont.langnm.value);
			$("#popupRmk").val(popCont.rmk.value);
			$('.pop_view').fadeIn(300);
		});
		// 팝업 일괄등록
		$('.btn_pop_batch').click(function() {
			$('.pop_batch').fadeIn(300);
		});
		// 팝업 닫기
		$('.pop_close, .pop_bg, .pop .btn_close, #multiInsertCancleBtn').click(function() {
			//일괄등록 팝업레이어는 파일업로드관련 reset 해주어야 함
			if ($(".pop_batch").eq(0).css("display") == 'block') {
				dropzoneClear();
			}
			$('.pop').fadeOut(300);
		});
	
		//팝업 높이
		function funThead(){
			var theight = $(window).height() - 200;
			$('.pop_con').css({'height':theight+'px'});
		}
		funThead();
		window.onresize = funThead;

		/**
		 * 페이징 처리 공통 함수
		 */
		$('.page').bootpag({		// 페이징을 표시할 div의 클래스
			total: ${pages.totalPage},  // 페이징모델의 전체페이지수
			page: ${pages.page},		// 페이징모델의 현재페이지번호
			maxVisible: ${pages.pageSize},  // 보여질 최대 페이지수
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

	    $('#search').click(function() {
			search();
		});

		$('#pageSizeTmp').change(function(){
			$('#pageSize').val($(this).val());
			search();
		});	
		
		//필터 검색
		$('.filterSearchBtn').click(function() {
			$('#page').val("1");
			search();
		});		
		//검색어 엔터키
		$('#searchValue').on('keyup', function(e) {
			if (e.which == 13) {
				$('#page').val("1"); 
				search(); 
			}
		});		
		//페이지사이즈 변경
		$('#pageSize').change(function(){
			$('#page').val("1");
			search();
		});

		//일괄등록 팝업 > [확인]
		$("#multiInsertBtn").on("click", function(){
			var data = $("#multiInsertFrm").serialize();
			$.ajax({
				type: "post",
				url: $("#multiInsertFrm").attr("action"),
				dataType: "json",
				data: data,
				success: function (data) {
					if(data.message == 'success'){
						alert("총 " + data.resultCnt + " 건의 데이터가 저장되었습니다.");
						window.location.reload();
					} else {
						alert("일괄등록을 실패하였습니다.");
						console.log(data.message);
					}
				}
			});		
		});		
	
	});
	
	//검색
	function search() {
		//필터검색값 set
		$('#page').val(1);
		$("#filterDataTy").val($("#filterDataTyTmp").val());
		$("#filterDataTrnsmisMthd").val($("#filterDataTrnsmisMthdTmp").val());
		$("#filterDataTrnsmisCycle").val($("#filterDataTrnsmisCycleTmp").val());
		$("#searchForm").submit();
	}
	
	function searchSort(type, direction) {
        $('#sortType').val(type);
        $('#sortDirection').val(direction);
        search();
    }
	
	Dropzone.options.myDropzone = {
		url : "/external/data/excelUpload",
		params : {},
		addRemoveLinks : true,
		dictRemoveFile : 'remove',
		autoProcessQueue : true,
		maxFiles : 1, 
		uploadMultiple : true,
		parallelUploads : 20,
		//Mb 단위
		maxFilesize : 100,	
		method : 'post',
		acceptFiles : ".xls,.xlsx",
		clickable : true,
		//IE10 미만 버전
		fallback : function(){
			alert("지원하지 않는 브라우저 입니다.");
		},
		init : function (){
			var myDropzone = this;
			var sendBtn = $(".sendServer");
			this.on("maxfilesexceeded",function(){
				alert("최대 업로드 파일 수는 1개 입니다.");
			});
			this.on("addedfile",function(file){
				if(parseFloat((file.size / (1024*1024)).toFixed(2)) >= 10){
					alert("대용량(10MB 이상) 파일은 180일후 자동 삭제됩니다.");
				}
			});
			this.on("removedfile",function(file){
				console.log("removefile : ", file.xhr);
				if(file.xhr != undefined){
					var text = file.xhr.responseText;
					var res = JSON.parse(text);
					console.log("res :", res);
					var fileId = res.fileId;
					$(".fileIds").each(function(index, item){
						if(fileId == $(this).val()){
							$(this).remove();
						}
					});
				}
			});
			this.on("complete",function(data){
				console.log("complete files : ", this.getUploadingFiles().length);
				if(data.xhr != undefined){
					var text = data.xhr.responseText;
					console.log("text : ", text);
					var res = JSON.parse(text);
					var fileId = "";
					if(res.result == "success"){
						var newFileName = res.newFileName;
						$("#excelNewFileName").val(newFileName);
					}else{
						alert("엑셀 업로드에 실패하였습니다.");
					}
				}
			});
			this.on("resetFiles",function(){
				if (this.files.length != 0) {
					this.files[i].previewElement.remove();
				}
				this.files.length = 0;
			});
		}
	}

	function dropzoneClear() {
		$("#multiInsertFrm")[0].reset();	//form 리셋
		$(".dz-preview").remove();			//dropzone 파일들 삭제
	}
	
</script>

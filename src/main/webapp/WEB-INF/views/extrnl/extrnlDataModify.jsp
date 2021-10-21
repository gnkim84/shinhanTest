<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
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

	<h2 class="content_h2">데이터 등록</h2>

	<form action="/external/data/insert" method="post" name="submitForm">
	<input type="hidden" id="dataId" name="dataId" value="${extrnlDataInfo.dataId}">
	<table class="tb02" summary="데이터명, 설명, 데이터 항목, 유형, 사용종료 예정일, 구매 담당자, 구매 부서, 외부 기관명, 사이트 주소, 관리 담당자, 관리 부서, 데이터 전송 방식, 데이터 전송 주기, 사용 부서, 위치, 언어, 비고에 대한 내용입니다.">
		<caption class="hidden">외부 데이터 관리 등록</caption>
		<colgroup>
			<col style="width:15%">
			<col style="width:35%">
			<col style="width:15%">
			<col style="width:35%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="row"><label for="dataNm">데이터명</label></th>
				<td colspan="3">
					<input type="text" class="input" id="dataNm" name="dataNm" value="${extrnlDataInfo.dataNm }" placeholder="데이터명을 입력해 주세요." maxlength="100">
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="dataDsc">설명</label></th>
				<td colspan="3">
					<input type="text" class="input" id="dataDsc" name="dataDsc" value="${extrnlDataInfo.dataDsc }" placeholder="어떠한 목적으로 어떤 데이터를 사용하는지 입력하세요." maxlength="1000">
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="dataItem">데이터 항목</label></th>
				<td colspan="3">
					<input type="text" class="input" id="dataItem" name="dataItem" value="${extrnlDataInfo.dataItem }" placeholder="데이터 항목을 입력해 주세요." maxlength="1000">
				</td>
			</tr>
			
			<tr>
				<th scope="row"><label for="dataTy">유형</label></th>
				<td> 
					<div class="radio_wrap">
						<c:forEach items="${dataTyList}" var="list" varStatus="status">
							<div class="radio" <c:if test="${status.count == 1 }">id="firstStatus"</c:if>>
								<input type="radio" id="${list.codeId}" <c:if test="${extrnlDataInfo.dataTy == list.codeId }">checked="checked"</c:if>>
								<label for="${list.codeId}">
									<span class="icon" name="${list.codeId}">
										<span></span>
									</span>
									<span class="txt">${list.codeNm}</span>
								</label>
							</div>
						</c:forEach>
						<input type="hidden" name="dataTy" id="dataTy" class="dataTy" value="">
					</div>
				</td>
				<th scope="row"><label for="endDt">사용종료 예정일</label></th>
				<td>
					<div class="input_date">
						<label class="hidden" for="endDt">사용기간 끝나는 날짜</label>
						<input type="text" class="input" name="endDt" id="endDt" <c:if test="${not empty extrnlDataInfo.endDt}">value="<spring:eval expression='extrnlDataInfo.endDt'/>"</c:if>>
						<button type="button" class="btn_date" id="_endDt">사용기간 끝나는 날짜 검색</button>
					</div>
				</td>
			</tr>

			<tr>
				<th scope="row"><label for="purchsDeptId">구매 담당자</label></th>
				<td>
					<div style="display: none;" class="input btn_input mr10" id="pcptInfoCotainer" >
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.purchsPicrId }" refnm="${extrnlDataInfo.purchsPicrNm }" refty="1" deptcode="${extrnlDataInfo.purchsDeptCode }" deptnm="${extrnlDataInfo.purchsDeptCode }">
							<pre class="hidden pcptInfo">
								{"refTy" : "1", "refId": "${extrnlDataInfo.purchsPicrId }","refNm": "${extrnlDataInfo.purchsPicrNm }","deptCode": "${extrnlDataInfo.purchsDeptCode }","deptNm": "${extrnlDataInfo.purchsDeptCode }"}
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.purchsPicrNm }</span>
							</label>
						</div>
					</div>
					<div class="input btn_input mr10" id="purchsDeptId" value="" readonly>
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.purchsPicrId }" refnm="${extrnlDataInfo.purchsPicrNm }" refty="1" deptcode="${extrnlDataInfo.purchsDeptCode }" deptnm="${extrnlDataInfo.purchsDeptNm }">
							<pre class="hidden pcptInfo">
								{"refTy" : "1", "refId": "${extrnlDataInfo.purchsPicrId }","refNm": "${extrnlDataInfo.purchsPicrNm }","deptCode": "${extrnlDataInfo.purchsDeptCode }","deptNm": "${extrnlDataInfo.purchsDeptNm }"}
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.purchsPicrNm }</span>
							</label>
						</div>
					</div>
					<input type="hidden" id="pPicrId" name="purchsPicrId" value="">
					<button type="button" class="btn small grayr memberDept_btn" value="2">검색</button>
				</td>
				<th scope="row"><label for="purchsDeptCode">구매 부서</label></th>
				<td>
					<div type="text" class="input btn_input mr10" id="purchsDeptCode" name="purchsDeptCode" value="" disabled="" readonly >
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.purchsPicrId }" refnm="${extrnlDataInfo.purchsPicrNm }" refty="1" deptcode="${extrnlDataInfo.purchsDeptCode }" deptnm="${extrnlDataInfo.purchsDeptNm }">
							<pre class="hidden pcptInfo">
								${extrnlDataInfo.purchsDeptNm }
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.purchsDeptNm }</span>
							</label>
						</div>
					</div>
					<input type="hidden" id="pDeptCode" name="purchsDeptCode" value="">
				</td>
			</tr>
			 
			<tr>
				<th scope="row"><label for="srcSysCode">외부 기관명</label></th>
				<td>
					<div class="select">
						<select name="srcSysCode" id="srcSysCode">
							<c:forEach items="${srcSysCodeList}" var="list">
								<option value="${list.codeId}" <c:if test="${list.codeId == extrnlDataInfo.srcSysCode }">selected="selected"</c:if> >${list.codeNm}</option>
							</c:forEach>
						</select>
					</div>
				</td>
				<th scope="row"><label for="srcSysUrl">사이트 주소</label></th>
				<td>
					<input type="text" class="input" id="srcSysUrl" name="srcSysUrl" value="${extrnlDataInfo.srcSysUrl }" placeholder="https://" maxlength="256">
				</td>
			</tr>	

			<tr>
				<th scope="row"><label for="mngDeptId">관리 담당자</label></th>
				<td>
					<div style="display: none;" class="input btn_input mr10" id="mgrInfoCotainer" >
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.mngPicrId }" refnm="${extrnlDataInfo.mngPicrNm }" refty="1" deptcode="${extrnlDataInfo.mngDeptCode }" deptnm="${extrnlDataInfo.mngDeptNm }">
							<pre class="hidden mgrInfo">
								${extrnlDataInfo.mngPicrNm }
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.mngPicrNm }</span>
							</label>
						</div>
					</div>
					<div class="input btn_input mr10" id="mngDeptId" value="" readonly>
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.mngPicrId }" refnm="${extrnlDataInfo.mngPicrNm }" refty="1" deptcode="${extrnlDataInfo.mngDeptCode }" deptnm="${extrnlDataInfo.mngDeptNm }">
							<pre class="hidden mgrInfo">
								${extrnlDataInfo.mngPicrNm }
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.mngPicrNm }</span>
							</label>
						</div>
					</div>
					<input type="hidden" id="mPicrId" name="mngPicrId" value="">
					<button type="button" class="btn small grayr memberDept_btn" value="1">검색</button>
				</td>
				<th scope="row"><label for="mngDeptCode">관리 부서</label></th>
				<td>
					<div type="text" class="input btn_input mr10" id="mngDeptCode" value="" disabled="" readonly>
						<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;"
							refId="${extrnlDataInfo.purchsPicrId }" refnm="${extrnlDataInfo.mngPicrNm }" refty="1" deptcode="${extrnlDataInfo.mngDeptCode }" deptnm="${extrnlDataInfo.mngDeptNm }">
							<pre class="hidden mgrInfo">
								${extrnlDataInfo.mngDeptNm }
							</pre>	<input type="radio" checked="">	
							<label>
								<span class="txt">${extrnlDataInfo.mngDeptNm }</span>
							</label>
						</div>
					</div>
					<input type="hidden" id="mDeptCode" name="mngDeptCode" value="">
				</td>
			</tr>

			<tr>
				<th scope="row"><label for="dataTrnsMthd">데이터 전송 방식</label></th>
				<td>
					<div class="select">
						<select name="dataTrnsmisMthd" id="dataTrnsmisMthd">
							<c:forEach items="${dataTrnsMthdList}" var="list">
								<option value="${list.codeId}" <c:if test="${list.codeId == extrnlDataInfo.dataTrnsmisMthd }">selected="selected"</c:if>>${list.codeNm}</option>
							</c:forEach>
						</select>
					</div>
				</td>
				<th scope="row"><label for="dataTrnsmisCycle">데이터 전송 주기</label></th>
				<td>
					<div class="checkbox_wrap">
						<c:forEach items="${dataTrnsCycleList}" var="list">
							<div class="checkbox" id="checkbox${list.codeId}">
								<input type="checkbox" class="cycleBox" name="${list.codeNm}" id="${list.codeId}" >
								<label for="${list.codeNm}">
									<span class="icon">
										<span></span>
									</span>
									<span class="txt">${list.codeNm}</span>
								</label>
							</div>
						</c:forEach>
						<input type="hidden" id="dataTrnsmisCycle" name="dataTrnsmisCycle" class="dataTrnsmisCycle" value="">
					</div>
				</td>
			</tr>	

			<tr>
				<th scope="row"><label for="deptInfo">사용 부서</label></th>
				<td colspan="3">
					<div type="text" class="input btn_input mr10" id="deptInfoCotainer" name="deptInfo" value="" readonly>
						<c:forEach items="${deptInfo }" var="deptInfo">
							<c:forEach items="${fn:split(deptInfo.deptInfoNm, ',')}" var="info" varStatus="status">
								<div class="radio btn_delete_img" style="margin-right:15px;padding-top:3px;padding-bottom:3px;" 
									refid="" refnm="" refty="2" deptcode="${fn:split(deptInfo.deptInfoCode, ',')[status.index]}" deptnm="${fn:split(deptInfo.deptInfoNm, ',')[status.index]}">
									<pre class="hidden deptInfo">
										{"refTy" : "2", "refId" : "", "refNm" : "","deptCode": "${fn:split(deptInfo.deptInfoCode, ',')[status.index]}","deptNm": "${fn:split(deptInfo.deptInfoNm, ',')[status.index]}"}
									</pre>
									<input type="radio" checked="">
									<label>
										<span class="txt">
											${fn:split(deptInfo.deptInfoNm, ',')[status.index]}
										</span>
									</label>
								</div>
							</c:forEach>
						</c:forEach>
					</div>
					<button type="button" class="btn small grayr dept_btn" value="3">검색</button>
					<input type="hidden" id="deptInfo" name="deptInfo">
				</td>
			</tr>

			<tr>
				<th scope="row"><label for="dataLc">위치</label></th>
				<td>
					<input type="text" class="input" id="dataLc" name="dataLc" value="${extrnlDataInfo.dataLc}" placeholder="데이터가 적재/활용되는 위치를 입력하세요." maxlength="1000">
				</td>
				<th scope="row"><label for="lang">언어</label></th>
				<td>
					<div class="select">
						<select name="lang" id="lang">
							<c:forEach items="${langList}" var="list">
								<option value="${list.codeId}" <c:if test="${list.codeId == extrnlDataInfo.lang }">selected="selected"</c:if>>${list.codeNm}</option>
							</c:forEach>
						</select>
					</div>
				</td>
			</tr>	

			<tr>
				<th scope="row"><label for="rmk">비고</label></th>
				<td colspan="3"><div class="textarea">
					<textarea name="rmk" id="rmk" maxlength="1000">${extrnlDataInfo.rmk }</textarea>
				</div></td>
			</tr>
		</tbody>
	</table>

	<div class="btn_wrap tr">
		<button type="button" class="btn big blue" onclick="submitBtn()">저장</button>
		<button type="button" class="btn big grayr">삭제</button>
		<button type="button" class="btn big grayb">취소</button>
	</div>
	</form>
</section>
<%@include file="/WEB-INF/views/popup/extrnlDeptPopup.jsp"%> 
<%@include file="/WEB-INF/views/popup/deptInfoPopup.jsp"%>
<link href="/css/swiper.css" rel="stylesheet" type="text/css" />
<script src="/js/swiper.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		$("#endDt").datepicker({
			dateFormat : 'yy-mm-dd',
			onClose : function(selectedDate){
			}
		});

		$(".dataTy").val($("#firstStatus").children("input").attr("id"));
		
		$(".radio").on("click", function(){
			$(".radio input").prop("checked", "");
			$(this).children("input").prop("checked", "checked");
			var dataTy = $(this).children("input").attr("id");
			$(".dataTy").val(dataTy);
		});

		var cycle = "";
		$(".checkbox").on("click", function(){
			if($(this).children("input")[0].checked == true){
				$(this).children("input").removeAttr("checked");
				var rip = "\"" + $(this).children("input").attr("id") + "\"";
				cycle = cycle.replace(rip, "");
				cycle = cycle.replace(",,", ",");
				console.log("first : ",cycle);
				if(cycle.substr(0,1) == ","){
					cycle = cycle.replace(cycle.substr(0,1), "");
				}
				if(cycle.charAt(cycle.length -1) == ","){
					cycle = cycle.substr(0,cycle.length -1);
				}
			}else{
				$(this).children("input").attr("checked", "checked");
				cycle += "\"" + $(this).children("input").attr("id") + "\"";
			}
			cycle = cycle.replace("\"\"", "\",\"");
 			$("#dataTrnsmisCycle").val("["+cycle+"]");
		});

		//데이터 전송 주기 세팅
		var dataCycle = ${extrnlDataInfo.dataTrnsmisCycle};
		dataCycle = JSON.stringify(dataCycle);
		dataCycle = dataCycle.replace("[\"", "");
		dataCycle = dataCycle.replace("\"]", "");
		dataCycle = dataCycle.split("\",\"");
		for(var i = 0; i < dataCycle.length; i++){
			$("#checkbox"+dataCycle[i]).trigger("click");
		}

	});

	function submitBtn(){
		var purchs = $("#purchsDeptId")[0].children;
		var mng = $("#mngDeptId")[0].children;

		$("#pPicrId").val(purchs[0].attributes.refId.value);
		$("#pDeptCode").val(purchs[0].attributes.deptCode.value);
		$("#mPicrId").val(mng[0].attributes.refId.value);
		$("#mDeptCode").val(mng[0].attributes.deptCode.value);

//		for(var i = 0; i < children.length; i++){
//			console.log(children[i].attributes.refTy);
//		}
			
		var dept = $("#deptInfoCotainer")[0].children;
		var deptJson = ""

		for(var i = 0; i < dept.length; i++){
			if(i == 0){
				deptJson = "\""+dept[i].attributes.deptCode.value+"\"";
			}else{
				deptJson += ", \""+dept[i].attributes.deptCode.value+"\"";
			}
		}
		$("#deptInfo").val("[" +deptJson+ "]");
		
		document.submitForm.submit();
	}

</script>

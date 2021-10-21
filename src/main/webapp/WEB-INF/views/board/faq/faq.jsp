<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">FAQ 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>게시판</li>
			<li>FAQ 관리</li>
		</ul>
	</div>

	<div class="faq_all">총 <c:out value='${pages.totalCount}' />건</div>
	<ul class="faq_ul">
		<c:if test="${empty faqList || faqList.size() eq 0 }">
		<li>
			<dl class="faq_dl tc" style="padding:14px 0;font-size:14px;">검색 결과가 없습니다.</dl>
		</li>
		</c:if>
		<c:forEach var="faq" items="${faqList}" begin="0" end="${faqList.size()}" varStatus="status">	 
		<li>
			<dl class="faq_dl">
				<dt class="faq_dt">
					<button type="button">
						<span class="faq_tit">Q<span>질문</span></span>
						<span class="faq_cate <c:choose><c:when test="${faq.clCode eq 'ETC'}">ETC</c:when><c:otherwise>DEFAULT</c:otherwise></c:choose>">${faq.faqCateNm}</span>
						<span class="faq_con">${faq.qstn}</span>
						<span class="faq_cate MOD modifyInfo" style="float:right" value="${faq.faqId }">수정</span>
				<c:choose>
					<c:when test="${faq.useYn eq 'Y'}">
						<span class="faq_useyn" style="color:blue;">게시</span>
					</c:when>
					<c:otherwise>
						<span class="faq_useyn" style="color:red;">미게시</span>
					</c:otherwise>				
				</c:choose>						
					</button>
				</dt>
				<dd class="faq_dd">
					<span class="faq_tit">A<span>답변</span></span>
					<span class="faq_con"><c:out value="${faq.answ}" escapeXml="false"/></span>
					<c:if test="${!empty faq.fileList && faq.fileList.size() > 0 }">
					<div class="faq_btn">
						<ul class="attach_list">
						<c:forEach var="file" items="${faq.fileList}" varStatus="status">
							<li>
								<a href="#none" url="/file/detail/${file.fileUrl}">${file.fileNm}</a>
							</li>
						</c:forEach>					
						</ul>
					</div>
					</c:if>					
				</dd>
			</dl>
		</li>		
		</c:forEach>
	</ul>

	<div class="btn_wrap tr">
		<button type="submit" class="btn big blue" onclick="location.href='/board/faq/regist'">등록</button>
	</div>

	<div class="page"></div>

	<form class="search_wrap" name="searchForm" id="searchForm" method="post">
		<input type="hidden" name="page" id="page" value="<c:out value='${pages.page}'/>" />
	<fieldset>
		<legend class="hidden">FAQ 게시물 검색</legend>

		<div class="select">
			<label class="hidden" for="pageSize">게시물 갯수</label>
			<select name="pageSize" id="pageSize">
				<option value="10">10건</option>
				<option value="20">20건</option>
				<option value="50">50건</option>
			</select>
		</div>

		<div class="select">
			<label class="hidden" for="searchUseYn">게시여부</label>
			<select name="searchUseYn" id="searchUseYn">
				<option value="">게시여부</option>
				<option value="Y">게시</option>
				<option value="N">미게시</option>
			</select>
		</div>

		<div class="select">
			<label class="hidden" for="searchKey">검색 항목</label>
			<select name="searchKey" id="searchKey">
				<option value="ALL">전체</option>
				<option value="QSTN">질문</option>
				<option value="ANSW">답변</option>
			</select>
		</div>

		<label class="hidden" for="searchValue">검색어</label>
		<input type="text" class="input" id="searchValue" name="searchValue" placeholder="검색어를 입력하세요.">
		<input type="button" value="검색" id="search" class="btn small grays">
	</fieldset>
	</form>

	<form id="frmFile" name="frmFile" action="/" method="post">
		<input type="hidden" name="fileUrl" value="">
	</form>

</section>
<script>
	$(function() {
		// 002. 팝업 초점 이동 추가
		$('.faq_dt button').on('click', function() {
			$(this).parent().toggleClass('on').parent().children('.faq_dd').slideToggle(300);
		});
		
		$('#search').click(function() {
			$('#page').val(1);
			$('#searchForm').submit();
		});
		
		$('#pageSize').change(function(){
			$("#page").val("1");
			$('#searchForm').submit();
		});		 

		$('.modifyInfo').click(function(e) {
			e.stopPropagation();
			const faqId = $(this).attr('value');
			$(location).attr('href', '/board/faq/modify/' + faqId);
		});

		//첨부파일 다운로드
		$(".attach_list a").on("click", function(){
			var frmFile = $("#frmFile");
			frmFile.attr("action", $(this).attr("url"));
			frmFile.submit();
		});		
	});

	$(document).ready(function() {
		$('#pageSize').val('${pages.pageSize}');
		$('#searchUseYn').val('${pages.searchUseYn}');
		$('#searchKey').val('${pages.searchKey}');
		$('#searchValue').val('${pages.searchValue}');
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

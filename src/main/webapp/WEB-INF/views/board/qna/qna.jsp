<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div class="content_tit">
		<h1 class="content_h1">Q&amp;A 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>게시판</li>
			<li>Q&amp;A 관리</li>
		</ul>
	</div>

	<div class="faq_all">총 ${pages.totalCount } 건</div>
	<div class="tb01_line"></div>
	<table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 제목, 상태, 등록일에 대한 내용입니다.">
		<caption class="hidden">Q&amp;A 목록</caption>
		<colgroup>
			<col style="width:6%">
			<col style="width:*">
			<col style="width:8%">
			<col style="width:8%">
			<col style="width:10%">
			<col style="width:10%">
			<col style="width:6%">
		</colgroup>
		<thead>
		<tr>
			<th scope="col">No</th>
			<th scope="col">제목</th>
			<th scope="col">공개</th>
			<th scope="col">답변상태</th>
			<th scope="col">등록일</th>
			<th scope="col">수정일</th>
			<th scope="col">게시여부</th>
		</tr>
		</thead>
		<tbody>
		<c:if test="${empty searchqnaList || searchqnaList.size() eq 0 }">
			<tr>
				<td scope="row" colspan="7">검색 결과가 없습니다.</td>
			</tr>
		</c:if>
		<c:forEach var="qna" items="${searchqnaList}" begin="0" end="${searchqnaList.size()}" varStatus="status">		
			<tr>
				<td scope="row">${criteria.totalCount - (status.index + (pages.page -1) * pages.pageSize)}</td>
				<td class="tl"><a href="/board/qna/modify/${qna.qnaId }">${qna.sj}</a></td>
				<td>${qna.openYnNm}</td>
				<td>
				<c:choose>
					<c:when test="${qna.qnaStat eq 'UNREAD'}">
						<span style="color:red;">${qna.qnaStatNm}</span>
					</c:when>
					<c:when test="${qna.qnaStat eq 'READED'}">
						<span style="color:orange;">${qna.qnaStatNm}</span>
					</c:when>
					<c:when test="${qna.qnaStat eq 'ANSWERED'}">
						<span style="color:blue;">${qna.qnaStatNm}</span>
					</c:when>						
					<c:otherwise>
						<span>${qna.qnaStatNm}</span>
					</c:otherwise>				
				</c:choose>					
				</td>
				<td>${fn:substring(qna.rgstDt, 0, 10)}</td>
			   	<td>${fn:substring(qna.modiDt, 0, 10)}</td>
				<td>
			<c:choose>
				<c:when test="${qna.useYn eq 'Y'}">
					<span style="color:blue;">게시</span>
				</c:when>
				<c:otherwise>
					<span style="color:red;">미게시</span>
				</c:otherwise>				
			</c:choose>				
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<%-- 
	<div class="btn_wrap tr">
		<button type="button" class="btn big blue" onclick="location.href='/board/qna/regist'">등록</button>
	</div>
	 --%>

	<div class="page"></div>

	<form class="search_wrap" name="searchForm" id="searchForm">
		<input type="hidden" name="page" id="page" value="<c:out value='${pages.page}'/>" />
		<fieldset>
			<legend class="hidden">사용자 목록 게시물 검색</legend>

			<div class="select">
				<label class="hidden" for="pageSize">게시물 갯수</label>
				<select name="pageSize" id="pageSize">
					<option value="10">10건</option>
					<option value="20">20건</option>
					<option value="30">30건</option>
				</select>
			</div>
			
			<div class="select">
				<label class="hidden" for="searchQnaStat">답변상태</label>
				<select name="searchQnaStat" id="searchQnaStat">
					<option value="">답변상태</option>
					<option value="ANSWERED">답변 완료</option>
					<option value="READED">확인중</option>
					<option value="UNREAD">미확인</option>
				</select>
			</div>			

			<div class="select">
				<label class="hidden" for="searchKey">검색 항목</label>
				<select name="searchKey" id="searchKey">
					<option value="ALL">전체</option>
					<option value="sj">제목</option>
					<option value="cn">내용</option>
				</select>
			</div>

			<label class="hidden" for="searchValue">검색어</label>
			<input type="text" class="input" id="searchValue" name="searchValue" placeholder="검색어를 입력하세요.">
			<input type="button" value="검색" id="search" class="btn small grays">
		</fieldset>
	</form>

</section>
<script src="/js/jquery.bootpag.js"></script>
<script type="text/javascript">
	$(function() {
		
		$(document).ready(function() {

			var _pageSize = '${pages.pageSize}';
			var _searchKey = '${pages.searchKey}';
			if(_pageSize == null || _pageSize == undefined  || _pageSize == ''){
				_pageSize = 10;
			}
			if(_searchKey == null || _searchKey == undefined  || _searchKey == ''){
				_searchKey = 'ALL';
			}

			$('#pageSize').val(_pageSize);
			$('#searchQnaStat').val('${pages.searchQnaStat}');
			$('#searchKey').val(_searchKey);
			$('#searchValue').val('${pages.searchValue}');
		});

		var totalPageNo = '${pages.totalPage}';
		var curPageNo = '${pages.page}';
		
		if(totalPageNo == null || totalPageNo == undefined || totalPageNo == ''){
			totalPageNo = 1;
		}
		
		if(curPageNo == null || curPageNo == undefined  || curPageNo == ''){
			curPageNo = 1;
		}
		
		/**
		 * 페이징 처리 공통 함수
		 */
		$('.page').bootpag({		// 페이징을 표시할 div의 클래스
			total: totalPageNo,  // 페이징모델의 전체페이지수
			page: curPageNo,		// 페이징모델의 현재페이지번호
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

		$('#search').click(function() {
			$('#page').val(1);
			$('#searchForm').submit();
		});
		
		$('#pageSize').change(function(){
			$("#page").val("1");
			$('#searchForm').submit();
		});		
	});
</script>
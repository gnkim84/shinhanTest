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

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 형태, 보고서명, 소유부서, 소유자, 비고, 배포권한, 활성, 미리보기, 등록일에 대한 내용입니다.">
        <caption class="hidden">보고서 권한 관리 목록</caption>
        <colgroup>
            <col style="width:6%">
            <col style="width:9%">
            <col style="">
            <col style="width:9%">
            <col style="width:9%">
            <col style="width:14%">
            <col style="width:10%">
            <col style="width:10%">
            <col style="width:10%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col">No</th>
            <th scope="col">유형</th>
            <th scope="col">보고서명</th>
            <th scope="col">소유부서</th>
            <th scope="col">소유자</th>
            <th scope="col">수정자</th>
            <th scope="col">등록자</th>
            <th scope="col">미리보기</th>
            <th scope="col">등록일자</th>
        </tr>
        </thead>
        <tbody>
			<c:forEach var="reportAuth" items="${reportAuthList}" varStatus="status">
				<tr>
					<td scope="row">${status.count }</td>
					<td>
						<c:forEach var="codeRptTy" items="${codeRptTyList}">
	                        <c:if test="${reportAuth.reportTy eq codeRptTy.codeId}">                        
	                            <c:out value="${codeRptTy.codeNm}"/>
	                        </c:if> 
						</c:forEach>
					</td>
					<td>${reportAuth.reportNm }</td>
					<td>${reportAuth.deptCode }</td>
					<td>${reportAuth.userNm }</td>
					<td>${reportAuth.modiId }</td>
					<td>${reportAuth.rgstId }</td>
					<td>
						<button type="button" class="btn small blue detailBtn" onclick="location.href='/project/reportAuth/modify/${reportAuth.aprvId}'">미리보기</button>
					</td>
					<td><spring:eval expression='reportAuth.rqstDt'/></td>
				</tr>
            </c:forEach>
        </tbody>
    </table>
    
    <div class="btn_wrap tr">
    <%--
        <button type="button" class="btn big blue" onclick="location.href='/project/reportAuth/regist'">보고서 권한 등록</button>
     --%>
    </div>

    <div class="page"></div>

    <form class="search_wrap" name="searchForm" id="searchForm" method="post">
		<fieldset>
		    <legend class="hidden">사용자 목록 게시물 검색</legend>
		
		    <div class="select">
		        <label class="hidden" for="search01">게시물 갯수</label>
		        <select name="search01" id="search01">
		            <option value="10">10건</option>
		            <option value="20">20건</option>
		            <option value="30">30건</option>
		        </select>
		    </div>
		
		    <div class="select">
		        <label class="hidden" for="search02">검색 항목</label>
		        <select name="search02" id="search02">
		            <option value="ALL">전체</option>
		            <option value="reportNm">보고서명</option>
		        </select>
		    </div>
		
		    <label class="hidden" for="searchValue">검색어</label>
		    <input type="text" class="input" name="searchValue" id="searchValue" placeholder="검색어를 입력하세요.">
		    <input type="submit" value="검색" onclick="search();" class="btn small grays">
		</fieldset>
    </form>
</section>

<script type="text/javascript">
    $(document).ready(function() {
// 		$(".detailBtn").click(function() {
// 			window.location.href="/project/reportAuth/modify/AR2021_999999999"
// 		});
    });

    /**
     * 페이징 처리 공통 함수
     */
    $('.page').bootpag({        // 페이징을 표시할 div의 클래스
        total: ${pages.totalPage},  // 페이징모델의 전체페이지수
        page: ${pages.page},        // 페이징모델의 현재페이지번호
        maxVisible: 10,  // 보여질 최대 페이지수
        firstLastUse: true,             // first와 last사용유무
        wrapClass: 'page',              // 페이징을 감싼 클래스명
        activeClass: 'on',              // 현재페이지의 클래스명
        disabledClass: 'disabled',      // 각 항목별 클래스 지정
        nextClass: 'next',
        prevClass: 'prev',
        lastClass: 'last',
        firstClass: 'first'
    }).on("page", function(event, num){
        $("#page").val(num);
        $("#searchForm").submit();
    });

    function search() {
        $('#page').val(1);
        $('#searchForm').submit();
    }
</script>
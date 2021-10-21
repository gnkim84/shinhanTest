<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

    <div class="content_tit">
        <h1 class="content_h1">보고서 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>분석 관리</li>
            <li>보고서 관리</li>
        </ul>
    </div>

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 프로젝트명, 보고서명, 상태, 권한, 소유자, 등록일에 대한 내용입니다.">
        <caption class="hidden">보고서 관리 목록</caption>
        <colgroup>
            <col style="width:5%">
            <col style="width:15%">
            <col style="width:10%">
            <col style="width:20%">
            <col style="width:20%">
<%--             <col style="width:10%"> --%>
            <col style="width:10%">
            <col style="width:10%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col">No</th>
            <th scope="col">프로젝트명</th>
            <th scope="col">보고서 유형</th>
            <th scope="col">보고서명</th>
            <th scope="col">보고서Url</th>
<!--             <th scope="col">상태</th> -->
            <th scope="col">활성화여부</th>
            <th scope="col">등록일</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty reportList || reportList.size() eq 0 }">
            <tr>
                <td scope="row" colspan="8">검색 결과가 없습니다.</td>
            </tr>
        </c:if>
        <c:forEach var="report" items="${reportList }" begin="0" end="${reportList.size() }" varStatus="status">
	        <tr onclick="location.href='/project/report/modify/${report.reportId}'" style="cursor: pointer">
<%-- 	            <td scope="row">${((pages.page -1) * pages.pageSize) + status.count}</td> --%>
	            <td scope="row">${criteria.totalCount - (status.index + (pages.page -1) * pages.pageSize)}</td>
	            <td class="tc">${report.projectNm}</td>
	            <td class="tc">${report.reportTyNm}</td>
	            <td class="tc">${report.reportNm}</td>
	            <td class="tc">${report.reportUrl}</td>
<%-- 	            <td class="tc">${report.reportStatNm}</td> --%>
	            <td class="tc">${report.activeYn}</td>
	            <td><spring:eval expression="report.rgstDt"/></td>
	        </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="btn_wrap tr">
<!--         <button type="button" class="btn big blue" onclick="location.href='/project/report/regist'">등록</button> -->
    </div>

    <div class="page"></div>

    <form class="search_wrap" name="searchForm" id="searchForm" method="post">
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
	            <label class="hidden" for="searchKey">검색 항목</label>
	            <select name="searchKey" id="searchKey">
	                <option value="ALL">전체</option>
	                <option value="projectNm">프로젝트명</option>
	                <option value="reportNm">보고서명</option>
	            </select>
	        </div>
	
	        <label class="hidden" for="searchValue">검색어</label>
	        <input type="text" class="input" name="searchValue" id="searchValue" placeholder="검색어를 입력하세요.">
	        <input type="button" value="검색" onclick="search();" class="btn small grays">
        </fieldset>
    </form>

</section>
<script src="/js/jquery.bootpag.js"></script>
<script type="text/javascript">
    $(function() {
        
        $(document).ready(function() {
            $('#pageSize').val('${pages.pageSize}');
            $('#searchKey').val('${pages.searchKey}');
            $('#searchValue').val('${pages.searchValue}');
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
        
    });

    function search() {
        $('#page').val(1);
        $('#searchForm').submit();
    }
</script>

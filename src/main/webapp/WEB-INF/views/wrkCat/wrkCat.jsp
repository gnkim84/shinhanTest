<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

    <div class="content_tit">
        <h1 class="content_h1">업무 카테고리 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>시스템 관리</li>
            <li>업무 카테고리 관리</li>
        </ul>
    </div>

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 업무 ID, 업무명, 비고, 생성날짜 대한 내용입니다.">
        <caption class="hidden">업무카테고리 관리 목록</caption>
        <colgroup>
            <col style="width:6%">
            <col style="width:22%">
            <col style="width:22%">
            <col style="width:25%">
            <col style="width:25%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col">No</th>
            <th scope="col">카테고리ID</th>
            <th scope="col">카테고리명</th>
            <th scope="col">비고</th>
            <th scope="col">생성날짜</th>
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${wrks}" var="wrk" varStatus="status">
            <tr onclick="getWrkData('<c:out value="${wrk.wrkId}"/>')">
                <td scope="row"><c:out value="${criteria.totalCount - (status.index + (pages.page -1) * pages.pageSize)}"/></td>
                <td><c:out value="${wrk.wrkId}"/></td>
                <td><c:out value="${wrk.wrkNm}"/></td>
                <td><c:out value="${wrk.wrkDsc}"/></td>
                <td><spring:eval expression="wrk.rgstDt" /></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="page"></div>

    <form id="wrkForm" name="wrkForm" method="post">
        <input type="hidden" name="page" id="page" />
        <input type="hidden" name="pageSize" id="pageSize" value="10" />
        <h2 class="content_h2">업무카테고리 수정</h2>
        <table class="tb02" summary="업무 ID, 업무명, 비고에 대한 내용입니다.">
            <caption class="hidden">업무카테고리 등록 목록</caption>
            <colgroup>
                <col style="width:12.5%">
                <col style="width:37.5%">
                <col style="width:12.5%">
                <col style="width:37.5%">
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><label for="wrkId">카테고리 ID</label></th>
                <td><input type="text" class="input tc" id="wrkId" name="wrkId" readonly maxLength="32"></td>
                <th scope="row"><label for="wrkNm">카테고리명</label></th>
                <td><input type="text" class="input tc" id="wrkNm" name="wrkNm" maxLength="50"></td>
            </tr>
            <tr>
                <th scope="row"><label for="wrkDsc">비고</label></th>
                <td colspan="3"><input type="text" class="input tl" id="wrkDsc" name="wrkDsc" maxLength="500"></td>
            </tr>
            </tbody>
        </table>

        <div class="btn_wrap tr">
            <button type="button" id="addWrk" class="btn big blue">신규</button>
            <button type="button" id="saveWrk" class="btn big blue">저장</button>
            <button type="button" id="deleteWrk" class="btn big grayb">삭제</button>
        </div>
    </form>

</section>
<script type="text/javascript" src="/js/wrk.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

    });

    /**
     * 페이징 처리 공통 함수
     */
    $('.page').bootpag({        // 페이징을 표시할 div의 클래스
        total: ${pages.totalPage},  // 페이징모델의 전체페이지수
        page: ${pages.page},        // 페이징모델의 현재페이지번호
        maxVisible: ${pages.pageSize},  // 보여질 최대 페이지수
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
        $("#wrkForm").submit();
    });
</script>

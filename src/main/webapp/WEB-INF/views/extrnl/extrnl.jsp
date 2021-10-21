<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

    <div class="content_tit">
        <h1 class="content_h1">외부 시스템 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>외부 자산 관리</li>
            <li>외부 시스템 관리</li>
        </ul>
    </div>

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 외부시스템 ID, 외부시스템명, 비고, 생성날짜 대한 내용입니다.">
        <caption class="hidden">외부시스템 목록</caption>
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
            <th scope="col">외부시스템 ID</th>
            <th scope="col">외부시스템명</th>
            <th scope="col">비고</th>
            <th scope="col">생성일시</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="extrnl" items="${extrnls}">
        <tr onclick="getExtrnlData('<c:out value="${extrnl.extrnlId}"/>')">
            <td scope="row"><c:out value="${extrnl.rownum}"/></td>
            <td><c:out value="${extrnl.extrnlId}"/></td>
            <td><c:out value="${extrnl.extrnlNm}"/></td>
            <td><c:out value="${extrnl.extrnlDsc}"/></td>
            <td><spring:eval expression="extrnl.rgstDt"/></td>
        </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="page"></div>

    <form id="extrnlForm" name="extrnlForm" method="post">
        <input type="hidden" name="page" id="page" />
        <input type="hidden" name="pageSize" id="pageSize" value="10" />
        <h2 class="content_h2">외부시스템 등록</h2>
        <table class="tb02" summary="외부시스템 ID, 외부시스템명, 비고에 대한 내용입니다.">
            <caption class="hidden">권한그룹 등록 목록</caption>
            <colgroup>
                <col style="width:12.5%">
                <col style="width:37.5%">
                <col style="width:12.5%">
                <col style="width:37.5%">
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><label for="extrnlId">외부시스템 ID</label></th>
                <td><input type="text" class="input tc" id="extrnlId" name="extrnlId" readonly></td>
                <th scope="row"><label for="extrnlNm">외부시스템명</label></th>
                <td><input type="text" class="input tc" id="extrnlNm" name="extrnlNm"></td>
            </tr>
            <tr>
                <th scope="row"><label for="extrnlUrl">외부시스템URL</label></th>
                <td colspan="3"><input type="text" class="input tl" id="extrnlUrl" name="extrnlUrl"></td>
            </tr>
            <tr>
                <th scope="row"><label for="extrnlDsc">비고</label></th>
                <td colspan="3"><input type="text" class="input tl" id="extrnlDsc" name="extrnlDsc"></td>
            </tr>
            </tbody>
        </table>

        <div class="btn_wrap tr">
            <button type="button" id="addExtrnl" class="btn big blue">신규</button>
            <button type="button" id="saveExtrnl" class="btn big blue">저장</button>
            <button type="button" id="deleteExtrnl" class="btn big grayb">삭제</button>
        </div>
    </form>

</section>
<script type="text/javascript" src="/js/extrnl.js"></script>
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
        $("#extrnlForm").submit();
    });
</script>

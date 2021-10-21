<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

    <div class="content_tit">
        <h1 class="content_h1">라이선스 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>시스템 관리</li>
            <li>라이선스 관리</li>
        </ul>
    </div>

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 권한그룹 ID, 권한그룹명, 태블로 시스템계정, AWS 시스템 계정, 비고, 생성날짜 대한 내용입니다.">
        <caption class="hidden">라이선스 관리 목록</caption>
        <colgroup>
            <col style="width:6%">
            <col style="width:12%">
            <col style="width:12%">
            <col style="width:16%">
            <col style="width:16%">
            <col style="width:19%">
            <col style="width:19%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col">No</th>
            <th scope="col">권한그룹 ID</th>
            <th scope="col">권한그룹명</th>
            <th scope="col">태블로 시스템계정</th>
            <th scope="col">AWS 시스템 계정</th>
            <th scope="col">비고</th>
            <th scope="col">생성일시</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty licenses}">
            <tr>
                <td colspan="7">
                    검색 결과가 없습니다.
                </td>
            </tr>
        </c:if>
        <c:forEach var="license" items="${licenses}" varStatus="status">
            <tr onclick="getLicenseData('<c:out value="${license.authId}"/>','<c:out value="${license.licenseId}"/>')">
            <td scope="row"><c:out value="${license.rownum}"/></td>
            <td><c:out value="${license.authId}"/></td>
            <td><c:out value="${license.authNm}"/></td>
            <td><c:out value="${license.tableauId}"/></td>
            <td><c:out value="${license.awsId}"/></td>
            <td><c:out value="${license.licenseDsc}"/></td>
            <td><spring:eval expression="license.rgstDt" /></td>
        </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="page"></div>

    <form id="licenseForm" name="licenseForm" method="post">
        <input type="hidden" name="page" id="page" />
        <input type="hidden" name="pageSize" id="pageSize" value="10" />
        <input type="hidden" name="licenseId" id="licenseId">
        <h2 class="content_h2">라이선스 관리 추가</h2>
        <table class="tb02" summary="권한그룹, Tableau 계정, Tableau 비밀번호, AWS 계정, AWS 비밀번호, 비고에 대한 내용입니다.">
            <caption class="hidden">라이선스 관리 추가 목록</caption>
            <colgroup>
                <col style="width:12.5%">
                <col style="width:37.5%">
                <col style="width:12.5%">
                <col style="width:37.5%">
            </colgroup>
            <tbody>
            <tr>
                <th scope="row"><label for="authId">권한그룹</label></th>
                <td>
                    <div class="select">
                        <select name="authId" id="authId" class="tc">
                            <c:forEach var="role" items="${roles}" varStatus="status">
                            <option value="<c:out value='${role.authId}'/>"><c:out value="${role.authNm}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <th scope="row"><label for="tableauId">Tableau 계정</label></th>
                <td><input type="text" class="input tc" id="tableauId" name="tableauId"></td>
                <th scope="row"><label for="tableauPw">Tableau 비밀번호</label></th>
                <td><input type="password" class="input tc" id="tableauPw" name="tableauPw">
                    <button type="button" id="eye1">비밀번호 보기</button>
                </td>
            </tr>
            <tr>
                <th scope="row"><label for="awsId">AWS 계정</label></th>
                <td><input type="text" class="input tc" id="awsId" name="awsId"></td>
                <th scope="row"><label for="awsPw">AWS 비밀번호</label></th>
                <td><input type="password" class="input tc" id="awsPw" name="awsPw">
                    <button type="button" id="eye2">비밀번호 보기</button></td>
            </tr>
            <tr>
                <th scope="row"><label for="licenseDsc">비고</label></th>
                <td colspan="3"><input type="text" class="input tl" id="licenseDsc" name="licenseDsc"></td>
            </tr>
            </tbody>
        </table>

        <div class="btn_wrap tr">
            <button type="button" id="addLicense" class="btn big blue">신규</button>
            <button type="button" id="saveLicense" class="btn big blue">저장</button>
            <button type="button" id="deleteLicense" class="btn big grayb">삭제</button>
        </div>
    </form>

</section>
<script type="text/javascript" src="/js/license.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $('#eye1').on("mousedown", function(){
            $('#tableauPw').attr('type',"text");
        }).on('mouseup mouseleave', function() {
            $('#tableauPw').attr('type',"password");
        });
        $('#eye2').on("mousedown", function(){
            $('#awsPw').attr('type',"text");
        }).on('mouseup mouseleave', function() {
            $('#awsPw').attr('type',"password");
        });
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
        $("#licenseForm").submit();
    });
</script>
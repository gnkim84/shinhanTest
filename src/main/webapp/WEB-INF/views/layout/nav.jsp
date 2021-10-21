<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">

</script>
<nav class="lnb">
    <button type="button" name="button" class="btn_lnb">좌측메뉴 열기/닫기</button>
    <div class="lnb_right" style="width:340px;">
        <ul>
            <c:forEach var="subMenu" items="${subMenuList}" varStatus="status">
                <li class="<c:if test="${subMenu.menuUrl eq myUri}">on</c:if>" menu-id="${subMenu.menuId}"><a href="${subMenu.menuUrl}">${subMenu.menuNm}</a></li>
            </c:forEach>
            <!--
            <li><a href="/member/">사용자 관리</a></li>
            <li><a href="/role/">권한 관리</a></li>
            <li><a href="/license/">라이선스 관리</a></li>
            <li><a href="/log/">사용자 로그 관리</a></li>
            -->
        </ul>
        <!--
        <ul>
            <li><a href="#none">프로젝트 관리</a></li>
            <li><a href="#none">모델 자산화 신청</a></li>
            <li><a href="#none">모델 배포 신청</a></li>
            <li><a href="#none">프로젝트 자원 공유</a></li>
            <li><a href="#none">분석 형상 관리</a></li>
        </ul>
        -->
    </div>
</nav>
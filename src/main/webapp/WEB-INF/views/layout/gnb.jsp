<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<tiles:importAttribute name="topMenuList" ignore="true" />

<ul class="gnb">
    <c:forEach var="topMenu" items="${topMenuList}" varStatus="status">
    	<li><a href="${topMenu.menuUrl}">${topMenu.menuNm}</a></li>
    </c:forEach>
</ul>
<%--<ul class="gnb">--%>
<%--    <li><a href="/member">사용자 관리</a></li>--%>
<%--    <li><a href="/code">시스템 관리</a></li>--%>
<%--    <li><a href="/project">프로젝트 관리</a></li>--%>
<%--    <li><a href="#none">자원 관리</a></li>--%>
<%--    <li><a href="#none">감사 관리</a></li>--%>
<%--</ul>--%>
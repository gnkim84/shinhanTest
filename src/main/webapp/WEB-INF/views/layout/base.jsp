<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<tiles:insertAttribute name="head"/> <!--  /WEB-INF/views/layout/head.jsp -->
<body>
<tiles:insertAttribute name="header" />
<tiles:insertAttribute name="gnb" />
<tiles:insertAttribute name="nav" />
<div class="container">
    <tiles:insertAttribute name="body" />
</div>
<tiles:insertAttribute name="footer"/> <!-- /WEB-INF/views/layout/footer.jsp -->
</body>
</html>
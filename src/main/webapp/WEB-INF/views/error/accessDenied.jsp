<%--
  Created by IntelliJ IDEA.
  User: allnewangel
  Date: 2020/12/10
  Time: 8:27 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>사용자 목록 | 사용자 관리 | 신한금융투자</title>
    <link href="/css/common.css" rel="stylesheet" type="text/css" />
	<link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:100,300,400,500,700,900&amp;subset=korean" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/jquery-ui/jquery-ui.min.js"></script>
    <script src="/webjars/handlebars/dist/handlebars.js"></script>
    <script src="/js/common.js"></script>
</head>
<body>
<h1>접근 거부 페이지</h1>

<sec:authorize access="isAuthenticated()">
    <a href="#" onclick="document.getElementById('logout').submit();">로그아웃 후 로그인 페이지로 이동</a>
</sec:authorize>


<form id="logout" action="/logout" method="POST">
</form>

</body>
</html>

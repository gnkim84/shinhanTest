<%--
  Created by IntelliJ IDEA.
  User: allnewangel
  Date: 2020/12/09
  Time: 5:28 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Test Page</title>
    <link href="/css/common.css" rel="stylesheet" type="text/css" />
    <link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon" />
	<link href="/webjars/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css" />
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/jquery-ui/jquery-ui.min.js"></script>
    <script src="/webjars/handlebars/dist/handlebars.js"></script>
    <script src="/js/jquery.bootpag.js"></script>
    <script src="/js/common.js"></script>
</head>
<body>

<p>익명 전부 보임</p>
<br />
<sec:authorize access="isAuthenticated()">
<p>로그인 하면 보임</p>
<p>name : <sec:authentication property="name" /></p>
<p>name : <sec:authentication property="principal.memberModel.companyNm" /></p>
<br />
</sec:authorize>

<sec:authorize access="hasRole('MANAGER')">
<p>로그인 하고 ROLE_MANAGER면 보임</p>
<br />
</sec:authorize>

<sec:authorize access="hasRole('ADMIN')">
<p>로그인 하고 ROLE_ADMIN면 보임</p>
<br />
</sec:authorize>

<sec:authorize access="hasAnyRole('MANAGER','ADMIN')">
<p>로그인 하고 ROLE_MANAGER 이거나 ROLE_ADMIN면 보임</p>
<br />
</sec:authorize>

<sec:authorize access="hasRole('MANAGER') and hasRole('ADMIN')">
<p>로그인 하고 ROLE_MANAGER이고 ROLE_ADMIN면 보임</p>
<br />
</sec:authorize>

</body>
</html>

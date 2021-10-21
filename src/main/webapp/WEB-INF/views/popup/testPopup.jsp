<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
    <title>태블로 테스트</title>
    <link href="/css/menuPopup/com.css" rel="stylesheet" type="text/css">
    <link href="/css/menuPopup/button.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        var imgpath = "/images/menu/";
    </script>
    <script type="text/javaScript" src="/js/menu.js" /></script>
    <script type="text/javaScript">

        /* ********************************************************
         * 상세내역조회 함수
         ******************************************************** */
        function choiceNodes(nodeNum) {
            var nodeValues = treeNodes[nodeNum].split("|");
            parent.document.menuForm.upMenuId.value = nodeValues[4];
            parent.$('.ui-dialog-content').dialog('close');
        }

    </script>
</head>
<body style="overflow-x:hidden;overflow-y:auto">

태블로 테스트

</body>
</html>


<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
    <title>메뉴</title><!-- 메뉴이동 -->
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
<form name="searchUpperMenuIdForm" method="post">
    <div style="visibility:hidden;display:none;"><input name="iptSubmit" type="submit" value="전송" title="전송"></div>
    <c:forEach var="menu" items="${menus}" varStatus="status">
        <input type="hidden" name="tmp_menuNmVal" value="${menu.menuId}|${menu.upMenuId}|${menu.menuNm}|${menu.menuUrl}|${menu.menuId}|${menu.ordSeq}|${menu.menuNm}|${menu.upMenuId}|${menu.menuDsc}|" />
    </c:forEach>

    <div class="wTableFrm" style="width:580px">
        <!-- 타이틀 -->
        <h2>메뉴이동</h2><!-- 메뉴이동 -->

        <div style="clear:both;"></div>
    </div>

    <DIV id="main" style="display:">

        <table width="570" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="10">&nbsp;</td>
            </tr>
        </table>

        <table width="570" cellpadding="8" class="table-line">
            <tr>
                <td>
                    <div class="tree" style="width:400px;">
                        <script language="javascript" type="text/javaScript">

                            var Tree = new Array;

                            if ( typeof document.searchUpperMenuIdForm.tmp_menuNmVal == "object"
                                && document.searchUpperMenuIdForm.tmp_menuNmVal.length > 0 ) {
                                for (var j = 0; j < document.searchUpperMenuIdForm.tmp_menuNmVal.length; j++) {
                                    Tree[j] = document.searchUpperMenuIdForm.tmp_menuNmVal[j].value;
                                }
                                createTree(Tree, true);
                            }else{
                                alert("메뉴가 존재하지 않습니다.");
                                window.close();
                            }
                        </script>
                    </div>
                </td>
            </tr>
        </table>
    </DIV>

</form>
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="/js/menu.js"></script>
<form name="menuForm" method="post">
    <input type="hidden" name="chkBrowser" value="chkBrowser" />
    <input type="hidden" name="tmp_CheckVal" />
    <!-- menu.menuAttr Json 형태 못받음. -->
    <c:forEach var="menu" items="${menus}" varStatus="status">
        <input type="hidden" name="tmp_menuNmVal" value="${menu.menuId}|${menu.upMenuId}|${menu.menuNm}|${menu.menuUrl}|${menu.menuId}|${menu.upMenuId}|${menu.menuNm}|${menu.menuUrl}|${menu.menuDsc}|${menu.ordSeq}|${menu.menuSe}|${menu.useYn}|${menu.menuAttr}|${menu.lv}|${menu.fullPathId}|${menu.fullPathNm}|${menu.fullOrdSeq}|" />
    </c:forEach>

<section class="content">
    <div class="content_tit">
        <h1 class="content_h1">메뉴 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>시스템 관리</li>
            <li>메뉴 관리</li>
        </ul>
    </div>

	<div class="see_wrap">
      <dl class="pw_select first">
        <dt>
          메뉴
<!--           <div class="pw_search"><div> -->
<!--             <label class="hidden" for="search01">메뉴 검색어</label> -->
<!--             <input type="text" class="input" id="search01" placeholder="메뉴를 입력하세요."> -->
<!--             <button type="button" class="btn_search">검색</button> -->
<!--           </div></div> -->
        </dt>
        <dd>
          <div class="organ_list">
<!--           	<div class="tree" style="overflow:scroll; width:280px; height:383px; padding:5px; border:1px solid #ddd; font-family: '돋움', '굴림', 'Arial', 'AppleGothic', 'sans-serif'; font-size: 12px"> -->
               <script language="javascript" type="text/javaScript">
                   var chk_Object = true;
                   var chk_browse = "";
                   if (eval(document.menuForm.chkBrowser)=="[object]") chk_browse = "IE";
                   if (eval(document.menuForm.chkBrowser)=="[object NodeList]") chk_browse = "Fox";
                   if (eval(document.menuForm.chkBrowser)=="[object Collection]") chk_browse = "safai";

                   var Tree = new Array;
                   if(chk_browse=="IE"&&eval(document.menuForm.tmp_menuNmVal)!="[object]"){
                       alert("메뉴 목록 데이타가 존재하지 않습니다."); //메뉴 목록 데이타가 존재하지 않습니다.
                       chk_Object = false;
                   }
                   if(chk_browse=="Fox"&&eval(document.menuForm.tmp_menuNmVal)!="[object NodeList]"){
                       alert("메뉴 목록 데이타가 존재하지 않습니다."); //메뉴 목록 데이타가 존재하지 않습니다.
                       chk_Object = false;
                   }
                   if(chk_browse=="safai"&&eval(document.menuForm.tmp_menuNmVal)!="[object Collection]"){
                       alert("메뉴 목록 데이타가 존재하지 않습니다."); //메뉴 목록 데이타가 존재하지 않습니다.
                       chk_Object = false;
                   }
                   if( chk_Object ){
                       for (var j = 0; j < document.menuForm.tmp_menuNmVal.length; j++) {
                           Tree[j] = document.menuForm.tmp_menuNmVal[j].value;
                       }
                       createTree(Tree, true);
                   }else{
                       alert("메뉴가 존재하지 않습니다. 메뉴 등록 후 사용하세요."); //메뉴가 존재하지 않습니다. 메뉴 등록 후 사용하세요.
                   }
               </script>
<!--            </div> -->
<!--             <ul> -->
<!--               <li><button type="button"><span class="ico"></span><span class="txt">사용자 포털</span></button> -->
<!--                 <ul> -->
<!--                   <li><button type="button"><span class="ico"></span><span class="txt">사용자 메인</span></button> -->
<!--                     <ul> -->
<!--                       <li><button type="button"><span class="ico"></span><span class="txt">뎁스</span></button> -->
<!--                         <ul> -->
<!--                           <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                         </ul> -->
<!--                       </li> -->
<!--                       <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                     </ul> -->
<!--                   </li> -->
<!--                   <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                 </ul> -->
<!--               </li> -->
<!--               <li><button type="button"><span class="ico"></span><span class="txt">분석가 포털</span></button> -->
<!--                 <ul> -->
<!--                   <li><button type="button"><span class="ico"></span><span class="txt">뎁스</span></button> -->
<!--                     <ul> -->
<!--                       <li><button type="button"><span class="ico"></span><span class="txt">뎁스</span></button> -->
<!--                         <ul> -->
<!--                           <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                         </ul> -->
<!--                       </li> -->
<!--                       <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                     </ul> -->
<!--                   </li> -->
<!--                   <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--                 </ul> -->
<!--               </li> -->
<!--               <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--               <li><button type="button" class="last"><span class="ico"></span><span class="txt">마지막뎁스</span></button></li> -->
<!--             </ul> -->
          </div>
        </dd>
      </dl>
      <div class="see_con">
        <table class="tb02 tl" summary="시각화명, 이미지, 담당자명, 설명에 대한 내용입니다.">
          <caption class="hidden">시각화 권한신청 목록</caption>
          <colgroup>
            <col style="width:22%">
            <col style="width:78%">
          </colgroup>
          <tbody>
            <tr style="height:60px;">
                <th>상위 메뉴ID</th>
                <td>
                    <input class="input tc" name="upMenuId" id="upMenuId" readonly style="width:85%;"/>
                    <a id="popupUpperMenuId" href="/system/menu/popup" target="_blank">
                        <img src="/images/menu/search2.gif" alt='' width="15" height="15" />(메뉴선택)
                    </a><!-- 메뉴선택 검색 -->
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴ID</th>
                <td>
                    <input class="input" name="menuId" id="menuId" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴명</th>
                <td>
                    <input class="input" name="menuNm" id="menuNm" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴URL</th>
                <td>
                    <input class="input" name="menuUrl" id="menuUrl" readonly/>
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴설명</th>
                <td>
                    <input class="input" name="menuDsc" id="menuDsc" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴순서</th>
                <td>
                    <input class="input" name="ordSeq" id="ordSeq" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>사용여부(사용 : Y / 미사용 : N)</th>
                <td>
                    <input class="input" name="useYn" id="useYn" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴구분(메뉴 : M / 기능 : F)</th>
                <td>
                    <input class="input" name="menuSe" id="menuSe" />
                </td>
            </tr>
            <tr style="height:60px;">
                <th>메뉴속성</th> 
                <td>
                    <input class="input" name="menuAttr" id="menuAttr" readonly />
                </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <div class="btn_wrap tr">
        <%-- <button type="button" id="addMenu" class="btn big blue">신규</button> --%>
        <button type="button" id="saveMenu" class="btn big blue">저장</button>
        <%-- <button type="button" id="deleteMenu" class="btn big grayb">삭제</button> --%>
    </div>

</section>
</form>
<script type="text/javascript">

    /* ********************************************************
     * 메뉴등록/수정 처리 함수
     ******************************************************** */
    function updateMenuList() {
        if(!fn_validatorMenuList()){return;}
        //if(document.menuForm.tmp_CheckVal.value != "U"){alert("상세조회시는 수정혹은 삭제만 가능합니다. 추가하신 후 등록하세요."); return;} //상세조회시는 수정혹은 삭제만 가능합니다. 초기화 하신 후 등록하세요.
        document.menuForm.action = "/system/menu/insert";
        document.menuForm.submit();
    }

    /* ********************************************************
     * 메뉴삭제 처리 함수
     ******************************************************** */
    function deleteMenuList() {
        if(!fn_validatorMenuList()){return;}
        if(document.menuForm.tmp_CheckVal.value != "U"){alert("상세조회시는 수정혹은 삭제만 가능합니다."); return;} //상세조회시는 수정혹은 삭제만 가능합니다.
        document.menuForm.action = "/system/menu/delete";
        document.menuForm.submit();
    }

    /* ********************************************************
     * 초기화 함수
     ******************************************************** */
    function initlMenuList() {
        document.menuForm.upMenuId.value="";
        document.menuForm.menuId.value="";
        document.menuForm.menuNm.value="";
        document.menuForm.menuUrl.value="";
        document.menuForm.menuDsc.value="";
        document.menuForm.ordSeq.value="";
        document.menuForm.menuId.readOnly=false;
        document.menuForm.tmp_CheckVal.value = "";
    }

    /* ********************************************************
     * 상세내역조회 함수
     ******************************************************** */
    function choiceNodes(nodeNum) {
        var nodeValues = treeNodes[nodeNum].split("|");
        console.log(nodeValues);
        document.menuForm.upMenuId.value = nodeValues[5];
        document.menuForm.menuId.value = nodeValues[4];
        document.menuForm.ordSeq.value = nodeValues[9];
        document.menuForm.menuNm.value = nodeValues[6];
        document.menuForm.menuDsc.value = nodeValues[8];
        document.menuForm.menuUrl.value = nodeValues[7];
        document.menuForm.useYn.value = nodeValues[11];
        document.menuForm.menuSe.value = nodeValues[10];
        document.menuForm.menuAttr.value = "{\"attr\": {\"delete\": true, \"detail\": true, \"insert\": true, \"update\": true}}";
        document.menuForm.menuId.readOnly=true;
        document.menuForm.tmp_CheckVal.value = "U";
        $('#addCode').hide();
    }

    /* ********************************************************
     * 입력값 validator 함수
     ******************************************************** */
    function fn_validatorMenuList() {

        //if(document.menuForm.menuId.value == ""){alert("메뉴번호는 Not Null 항목입니다"); return false;} //메뉴번호는 Not Null 항목입니다.
        //if(!checkNumber(document.menuForm.menuId.value)){alert("메뉴번호는 숫자만 입력 가능합니다."); return false;} //메뉴번호는 숫자만 입력 가능합니다.

        if(document.menuForm.ordSeq.value == ""){alert("메뉴순서는 Not Null 항목입니다."); return false;} //메뉴순서는 Not Null 항목입니다.
        if(!checkNumber(document.menuForm.ordSeq.value)){alert("메뉴순서는 숫자만 입력 가능합니다."); return false;} //메뉴순서는 숫자만 입력 가능합니다.

        if(document.menuForm.upMenuId.value == ""){alert("상위메뉴번호는 Not Null 항목입니다."); return false;} //상위메뉴번호는 Not Null 항목입니다.
        //if(!checkNumber(document.menuForm.upMenuId.value)){alert("상위메뉴번호는 숫자만 입력 가능합니다."); return false;} //상위메뉴번호는 숫자만 입력 가능합니다.

        if(document.menuForm.menuUrl.value == ""){alert("메뉴URL은 Not Null 항목입니다."); return false;} //프로그램파일명은 Not Null 항목입니다.
        if(document.menuForm.menuNm.value == ""){alert("메뉴명은 Not Null 항목입니다."); return false;} //메뉴명은 Not Null 항목입니다.

        return true;
    }

    /* ********************************************************
     * 필드값 Number 체크 함수
     ******************************************************** */
    function checkNumber(str) {
        var flag=true;
        if (str.length > 0) {
            for (i = 0; i < str.length; i++) {
                if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                    flag=false;
                }
            }
        }
        return flag;
    }
</script>
<script type="text/javascript">
    $(document).ready(function () {
        // 메뉴이동 화면 호출 함수
        $('#popupUpperMenuId').click(function (e) {
            e.preventDefault();
            var pagetitle = $(this).attr("title");
            var page = "/system/menu/popup";
            var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
                .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
                .dialog({
                    autoOpen: false,
                    modal: true,
                    width: 600,
                    height: 550,
                    title: pagetitle
                });
            $dialog.dialog('open');
        });
    });

    $("#addMenu").click(function () {
        let result = confirm("메뉴입력 창이 모두 초기화 됩니다.\n진행하시겠습니까?");

        if(result) {
            initlMenuList();
        }
    });

    $("#saveMenu").click(function () {
        let result = confirm("메뉴를 저장하시겠습니까?");

        if(result) {
            updateMenuList();
        }

    });

    $("#deleteMenu").click(function () {
        let result = confirm("메뉴를 삭제하시겠습니까?");

        if(result) {
            deleteMenuList();
        }

    });
</script>
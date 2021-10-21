<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="/js/menu.js"></script>
<form name="menuForm" method="post">
    <input type="hidden" name="chkBrowser" value="chkBrowser" />
    <input type="hidden" name="tmp_CheckVal" />
    <c:forEach var="menu" items="${menus}" varStatus="status">
        <input type="hidden" name="tmp_menuNmVal" value="${menu.menuId}|${menu.upMenuId}|${menu.menuNm}|${menu.menuUrl}|${menu.menuId}|${menu.upMenuId}|${menu.menuNm}|${menu.menuUrl}|${menu.menuDsc}|${menu.ordSeq}|${menu.menuSe}|${menu.menuAttr}|${menu.useYn}|${menu.lv}|${menu.fullPathId}|${menu.fullPathNm}|${menu.fullOrdSeq}|" />
    </c:forEach>
<section class="content">
    <div class="content_tit">
        <h1 class="content_h1">메뉴 권한 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>시스템 관리</li>
            <li>메뉴 권한 관리</li>
        </ul>
    </div>

	<div class="see_wrap see_wrap2">
	<!--     <input type="hidden" id="upMenuId" name="upMenuId">
    <input type="hidden" id="menuId" name="menuId">
    <input type="hidden" id="ordSeq" name="ordSeq">
    <input type="hidden" id="menuNm" name="menuNm">
    <input type="hidden" id="menuDsc" name="menuDsc">
    <input type="hidden" id="menuUrl" name="menuUrl">
    <input type="hidden" id="rownum" name="rownum">
    <input type="hidden" id="authId" name="authId">
    <input type="hidden" id="authCl" name="authCl">
    <input type="hidden" id="authNm" name="authNm">
    <input type="hidden" id="authDsc" name="authDsc"> -->
    <input type="hidden" id="authId" name="authId">
      <dl class="pw_select left">
      <form class="search_wrap" id="frm" name="frm" action="/system/menuAuth" method="post">
        <dt>
          권한목록
          <div class="pw_search"><div>
            <label class="hidden" for="authNm">이름 권한목록</label>
            <input type="text" class="input" id="authNm" name="authNm" placeholder="권한그룹목록을 입력하세요.">
<!--             <button type="button" class="btn_search" >검색</button> -->
            <button type="button" class="btn_search" onclick="roleSearch()">검색</button>
          </div></div>
        </dt>
        </form>
        <dd class="row2">
          <div class="tb01_wrap">
            <div class="tb01_line"></div>
            <table class="tb01 line ck" summary="권한ID, 권한명, 권한분류에 대한 내용입니다.">
              <caption class="hidden">권한 목록 이름 목록</caption>
              <colgroup>
                <col style="width:33.33%">
                <col style="width:33.33%">
                <col style="width:33.33%">
              </colgroup>
              <thead>
                <th scope="col">권한ID</th>
                <th scope="col">권한명</th>
                <th scope="col">권한분류</th>
              </tr></thead>
              <tbody>
                <c:forEach var="role" items="${roles}">
			        <tr class="roleTable" style="cursor:pointer" name="roleTable" id="${role.authId}" onclick="menuAuthUser('${role.rownum}', '${role.authId}', '${role.authCl}', '${role.authNm}', '${role.authDsc}')">
			        	<td id="${role.authId }"><c:out value="${role.authId}"/></td>
			            <td id="${role.authNm}"><c:out value="${role.authNm}"/></td>
			            <td id="${role.authCl}">
			            	<c:if test="${role.authCl == 'U' }">유저</c:if>
			            	<c:if test="${role.authCl == 'A' }">분석가</c:if>
			            	<c:if test="${role.authCl == 'D' }">임원</c:if>
			            	<input type="hidden" id="${role.authId}" value="${role.authId}">
			            </td>
			        </tr>
		        </c:forEach>
              </tbody>
            </table>
          </div>
        </dd>
      </dl>
      <dl class="pw_select right">
        <dt>
          메뉴
          <div class="pw_search">
          	<div style="padding-right:0px">
	            <label class="hidden" for="divAuthNm">메뉴 검색어</label>
	            <input type="text" class="input" id="divAuthNm" readonly="readlony" />
<!-- 	            <button type="button" class="btn_search">검색</button> -->
          	</div>
          </div>
        </dt>
        <dd class="row2">
          <div class="organ_list">
				<span id="tree" >
				</span>
				<span id="fancytreeKey">
				
				</span>
				<span id="fancytreeRootKey">
				
				</span>
          </div>
        </dd>
      </dl>
    </div>
    
    <div class="btn_wrap tr">
<!--         <button type="button" id="expandAll" class="btn big blue" onclick="javascript:treeExpandAll();">전체 펼침</button> -->
<!--         <button type="button" id="collapseAll" class="btn big blue" onclick="javascript:treeCollapseAll();">전체 닫침</button> -->
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
        document.menuForm.selectUpMenuId.value="";
        document.menuForm.selectMenuId.value="";
        document.menuForm.selectMenuNm.value="";
        document.menuForm.selectMenuUrl.value="";
        document.menuForm.selectMenuDsc.value="";
        document.menuForm.selectOrdSeq.value="";
        document.menuForm.selectMenuId.readOnly=false;
        document.menuForm.tmp_CheckVal.value = "";
    }

     function choiceNodes(nodeNum) {
         var nodeValues = treeNodes[nodeNum].split("|");
         $("#selectUpMenuId").val(nodeValues[5]);
         $("#selectMenuId").val(nodeValues[4]);
         $("#selectOrdSeq").val(nodeValues[9]);
         $("#selectMenuNm").val(nodeValues[6]);
         $("#selectMenuDsc").val(nodeValues[8]);
         $("#selectMenuUrl").val(nodeValues[7]);
         document.menuForm.tmp_CheckVal.value = "U";
     }

     function menuAuthUser(rownum, authId, authCl, authNm, authDsc){
         $("#rownum").val(rownum);
         $("#authId").val(authId);
         var clValue = '';
         if(authCl == 'U'){
         	clValue = '유저';
         }
         if(authCl == 'A'){
         	clValue = '분석가';
         }
         if(authCl == 'D'){
         	clValue = '임원';
         } 
         $("#clValue").val(clValue);
         $("#authCl").val(authCl);
         $("#authNm").val(authNm);
         $("#authDsc").val(authDsc);
         //$("#divAuthNm").text(authNm);
         $("#divAuthNm").val(authNm);

         $.ajax({
             type: "post",
             url: "/system/menuAuth/" + $("#authId").val() + "/popup",
             dataType: "json",
             data: "",
             success: function (data) {
                 if(data.result){
					$.ui.fancytree.getTree("#tree").selectAll(false);
					//console.log("data : ", data.menuAuths);
					for(var i=0;i < data.menuAuths.length;i++){
						var item = data.menuAuths[i];
						if(item.useYn == "Y"){
							//console.log("useYn : ", item.useYn);
							var _tree = $.ui.fancytree.getTree("#tree"); 

							var rootnode = _tree.getRootNode().children[0]; 
							if(rootnode.children != null){
								nodeLoopCheck(rootnode.children, item.menuId);
							}
						}
					}
                 }
             }
         });
        var node = $.ui.fancytree.getTree("#tree");
     }

    /* ********************************************************
     * 입력값 validator 함수
     ******************************************************** */
    function fn_validatorMenuList() {

        //if(document.menuForm.menuId.value == ""){alert("메뉴번호는 Not Null 항목입니다"); return false;} //메뉴번호는 Not Null 항목입니다.
        //if(!checkNumber(document.menuForm.menuId.value)){alert("메뉴번호는 숫자만 입력 가능합니다."); return false;} //메뉴번호는 숫자만 입력 가능합니다.

        if(document.menuForm.selectOrdSeq.value == ""){alert("메뉴순서는 Not Null 항목입니다."); return false;} //메뉴순서는 Not Null 항목입니다.
        if(!checkNumber(document.menuForm.ordSeq.value)){alert("메뉴순서는 숫자만 입력 가능합니다."); return false;} //메뉴순서는 숫자만 입력 가능합니다.

        if(document.menuForm.selectUpMenuId.value == ""){alert("상위메뉴번호는 Not Null 항목입니다."); return false;} //상위메뉴번호는 Not Null 항목입니다.
        //if(!checkNumber(document.menuForm.upMenuId.value)){alert("상위메뉴번호는 숫자만 입력 가능합니다."); return false;} //상위메뉴번호는 숫자만 입력 가능합니다.

        if(document.menuForm.selectMenuUrl.value == ""){alert("메뉴URL은 Not Null 항목입니다."); return false;} //프로그램파일명은 Not Null 항목입니다.
        if(document.menuForm.selectMenuNm.value == ""){alert("메뉴명은 Not Null 항목입니다."); return false;} //메뉴명은 Not Null 항목입니다.

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

    function roleSearch(){
		var frm = $("#frm");
		frm.submit();
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

    let treeJson = new Array();
    $("#saveMenu").click(function () {
        if($("#authId").val() == ""){
			alert("권한을 선택하세요.");
			return;
		}
        treeJson = new Array();
		var _tree = $.ui.fancytree.getTree("#tree"); 

		var rootnode = _tree.getRootNode().children[0]; 
		treeJson.push(JSON.parse('{ "menuId" : "'+rootnode.data.menuId+'", "checked" : "'+rootnode.partsel+'", "authId" : "'+$("#authId").val()+'" }'));
		if(rootnode.children != null){ 
			nodeLoop(rootnode.children);
		}
		
		$.ajax({
            type: "post",
            url: "/system/menuAuth/update/" + $("#authId").val() + "/popup",
            dataType: "json",
            data: "treeJson="+JSON.stringify(treeJson),
            success: function (data) {
                if(data.result){
                    console.log("메뉴 권한을 저장하였습니다.");
                    //alert("메뉴 권한을 저장하였습니다.");
                    location.reload();
                }
            }
        });
        
//         let result = confirm("메뉴를 저장하시겠습니까?");
//         if(result) {
//             updateMenuList();
//         }

    });

	function nodeLoop(nodes){
		$.map(nodes, function(node){
			//console.log(node);
			//console.log(node.getLevel());
			treeJson.push(JSON.parse('{ "menuId" : "'+node.data.menuId+'", "checked" : "'+node.isSelected()+'", "authId" : "'+$("#authId").val()+'" }'));
			//node.setSelected(true);
			if(node.children != null){
				nodeLoop(node.children);
			}
		});
	}

	function nodeLoopCheck(nodes, menuId){
		$.map(nodes, function(node){
			//console.log(node);
			//console.log(node.getLevel());
			//console.log("node.data.menuId : " + node.data.menuId);
			//console.log("menuId : " + menuId);
			if(menuId == node.data.menuId){
				node.setSelected(true);
			}
			
			if(node.children != null){
				nodeLoopCheck(node.children, menuId);
			}
		});
	}

	//tree 전체 펼침
	function treeExpandAll(){
		$.ui.fancytree.getTree("#tree").expandAll();
	}

	function treeCollapseAll(){
		$.ui.fancytree.getTree("#tree").expandAll(false);
	}
</script>
<script type="text/javascript">
$(document).ready(function() {
	$('#chkAll').on("click", function(){
		$(".memberChk").prop("checked", $(this).prop("checked"));
	});
});




//조직도에서 조직선택시 소속 직원들 출력
function searchUserByTree(node) {
	$('#fancytreeKey').val(node.key);
	$('#menuId').val(node.data.menuId);
	$("#userPopupSearchForm").submit();
};	

let tree = [];
$(function() {
	<c:forEach var='menus' items='${menus }'>
		createTree(${menus.fullPathId}, ${menus.fullPathNm});
	</c:forEach>


	/**
	*	노드 생성 함수
	*/		
	function createTree(fullPathId, fullPathNm) {
		tree.push({title: fullPathNm[fullPathNm.length - 1], checkbox:true, selected : true, expanded: true, children: [], data: { menuId: fullPathId[fullPathId.length - 1], upMenuId: fullPathId[fullPathId.length - 2]}});
	}

	/**
	*	트리 구성 함수
	*/
	function setTree(nodes) {
		for (let index = nodes.length - 1; index > -1; index--) {
			const parent = nodes.find(n => n.data.menuId == nodes[index].data.upMenuId);
			if (parent) parent.children.unshift(nodes.pop(index));
		}
	};

	/**
	*	트리 이미지 변경 함수
	*/
	function checkNodesForChildren(nodes) {
		$.each(nodes, function(index, node) {
			if (node.children && node.children.length > 0) {
				node.folder = true;
				checkNodesForChildren(node.children);
			} else {
				node.folder = false;
			}
		});
	}

	$(document).ready(function() {
		$('#tree').fancytree({
			checkbox: true,
			selectMode: 3,
			source: tree,
			init: function(e, d){
				d.tree.visit(function(n){
					n.key = n.data.menuId;
					n.expanded = true;
				});
			},
			loadChildren : function(e, d){
				d.node.fixSelection3AfterClick();

			},
			laztLoad : function(e, d){
				d.result = tree;
			},
			activate: function(e, d) {
				var selKeys = $.map(d.tree.getSelectedNodes(), function(node){
					return node.key;
				});
				$("#fancytreeKey").text(selKeys.join(", "));

				var selRootNodes = d.tree.getSelectedNodes(true);
				var selRootKeys = $.map(selRootNodes, function(node){
					return node.key;
				});
			},
			select: function(e, d){
				var selKeys = $.map(d.tree.getSelectedNodes(), function(node){
					//console.log(node.parent);
					//console.log(node.data);
					//console.log(node);
					//console.log(node.getLevel());
					var parents = "";
					for(var i =0;i < node.getLevel()-1;i++){
						//parents += ".parent";
						//console.log(node+parents+".menuId");
						//console.log("menuId : ",node.parent.data.menuId);
					}
					return node.key;
				});
				//$("#fancytreeKey").text(selKeys.join(","));
			},
 			postProcess: function(e, d) { setTree(d.response); checkNodesForChildren(d.response); }
		});

		$('#pageSize').val('${pages.pageSize}');
		$('#searchKey').val('${pages.searchKey}');
		$('#searchValue').val('${pages.searchValue}');
	});
});


</script>
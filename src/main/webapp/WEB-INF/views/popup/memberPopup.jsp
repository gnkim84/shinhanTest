<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
	<link href="/css/common.css" rel="stylesheet" type="text/css" />
	<link href="/webjars/fancytree/dist/skin-lion/ui.fancytree.min.css" rel="stylesheet" type="text/css" />
	<script src="/webjars/jquery/jquery.min.js"></script>
	<script src="/webjars/fancytree/dist/jquery.fancytree-all-deps.min.js"></script>
	<script src="/js/jquery.bootpag.js"></script>
</head>
<body style="overflow-x:hidden;overflow-y:auto">

	<form name="userPopupSearchForm" id="userPopupSearchForm" action="/system/member/popup">
	<input type="hidden" id="page" name="page" value="<c:out value='${pages.page}'/>" />
	<input type="hidden" id="searchUseYn" name="searchUseYn" value="<c:out value='Y'/>" /><% //사용여부가 'Y'인 계정만 추출 %>
	<input type="hidden" name="deptCode" id="deptCode" value="${pages.deptCode}"/>
	<input type="hidden" name="deptNm" id="deptNm" value="${pages.deptNm}"/>
	<input type="hidden" name="fancytreeKey" id="fancytreeKey" value="${pages.fancytreeKey}"/>
		<div class="wTableFrm" style="width:580px">
			<h2>사용자선택</h2><!-- 메뉴이동 -->
			<div style="clear:both;"></div>
		</div>
		<div class="tb01_line"></div>
		<div>
			<div class="select wsmall" style="display: inline-block">
				<label class="hidden" for="searchKey">검색 항목</label>
				<select name="searchKey" id="searchKey">
					<option value="ALL">전체</option>
					<c:forEach var="memSearchCd" items="${codeMemSearchCdList}" varStatus="status">
						<option value="<c:out value='${memSearchCd.codeId}'/>">${memSearchCd.codeNm}</option>
					</c:forEach>
				</select>				
			</div>
			<div class="input wmid" style="display: inline-block; width: 580px;">
				<label class="hidden" for="searchValue">검색어</label>
				<input type="text" name="searchValue" id="searchValue" placeholder="검색어를 입력하세요.">
			</div>
			<input type="button" value="검색" onclick="searchUser();" class="btn small grays" style="float: right;">
		</div>
		<span id="tree" style="
						display: inline-block;
						float: left;
						width: 30%;
						height: 85%;
						margin-top: 10px;
						padding-left: 5px;">
		</span>
		<span style="
				display: inline-block;
				float: right;
				width: 65%;
				height: 85%;
				margin-top: 10px;
				margin-right: 10px;">
			<table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 프로젝트명, 보고서명, 상태, 권한, 소유자, 등록일에 대한 내용입니다.">
				<caption class="hidden">보고서 관리 목록</caption>
				<colgroup>
					<col style="width:6%">
					<col style="width:24%">
					<col style="width:40%">
					<col style="width:30%">
				</colgroup>
				<thead>
				<tr>
					<th scope="col">
						<input type="checkbox" id="chkAll" style="-webkit-appearance:checkbox !important;">
					</th>
					<th scope="col">이름</th>
					<th scope="col">부서</th>
					<th scope="col">직위</th>
				</tr>
				</thead>
				<tbody>
				<c:if test="${empty members || members.size() eq 0 }">
					<tr>
						<td scope="row" colspan="8">검색 결과가 없습니다.</td>
					</tr>
				</c:if>
				<c:forEach var="member" items="${members }" varStatus="status">
					<tr>
						<td scope="row">
							<input type="checkbox" class="memberChk" style="-webkit-appearance:checkbox !important;" 
								value='{"refTy" : "1", "refId": "${member.userId}","refNm": "${member.userNm}","deptCode": "${member.deptCode}","deptNm": "${member.deptNm}","pstnCode": "${member.pstnCode}","pstnNm": "${member.pstnNm}","tableauUserId":"${member.tableauUserId}"}'
							/>
						</td>
						<td class="tc">${member.userNm}</td>
						<td class="tc">${member.deptNm}</td>
						<td class="tc">${member.pstnNm}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div class="page" style="overflow: hidden;"></div>
		</span>
	</form>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#chkAll').on("click", function(){
			$(".memberChk").prop("checked", $(this).prop("checked"));
		});
	});

	//일반검색
	function searchUser() {
		$("#userPopupSearchForm").submit();
	}

	//조직도에서 조직선택시 소속 직원들 출력
	function searchUserByTree(node) {
		$('#fancytreeKey').val(node.key);
		$('#deptCode').val(node.data.deptCode);
		$("#userPopupSearchForm").submit();
	};	

	let tree = [];
	$(function() {
		<c:forEach var='dept' items='${depts }'>
		createTree(${dept.fullPathId}, ${dept.fullPathNm});
		</c:forEach>

		/**
		*	노드 생성 함수
		*/		
		function createTree(fullPathId, fullPathNm) {
			tree.push({title: fullPathNm[fullPathNm.length - 1], folder: true, children: [], data: { deptCode: fullPathId[fullPathId.length - 1], upDeptCode: fullPathId[fullPathId.length - 2]}});
		}

		/**
		*	트리 구성 함수
		*/
		function setTree(nodes) {
			for (let index = nodes.length - 1; index > -1; index--) {
				const parent = nodes.find(n => n.data.deptCode == nodes[index].data.upDeptCode);
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
				source: tree,
				//부서코드로 검색시, 해당 부서코드로 조직도에 focus되게 처리
				init: function(e, d) {
					var key = $('#fancytreeKey').val();
					if (key != '') {
						d.tree.activateKey(key);
					}
				},
				activate: function(e, d) {
					//아래처럼 이벤트를 체크하지 않으면, init에서 activateKey하면서 무한로딩 됨
					if ((!(!e.originalEvent)) == true) {
						searchUserByTree(d.node);
					}
				},
				postProcess: function(e, d) { setTree(d.response); checkNodesForChildren(d.response); }
			});
			$('#pageSize').val('${pages.pageSize}');
			$('#searchKey').val('${pages.searchKey}');
			$('#searchValue').val('${pages.searchValue}');
		});

		/**
		 * 페이징 처리 공통 함수
		 */
		$('.page').bootpag({		// 페이징을 표시할 div의 클래스
			total: ${pages.totalPage},  // 페이징모델의 전체페이지수
			page: ${pages.page},		// 페이징모델의 현재페이지번호
			maxVisible: 5,  // 보여질 최대 페이지수
			firstLastUse: true,			 // first와 last사용유무
			wrapClass: 'page',			  // 페이징을 감싼 클래스명
			activeClass: 'on',			  // 현재페이지의 클래스명
			disabledClass: 'disabled',	  // 각 항목별 클래스 지정
			nextClass: 'next',
			prevClass: 'prev',
			lastClass: 'last',
			firstClass: 'first'
		}).on("page", function(event, num){
			$("#page").val(num);
			$("#userPopupSearchForm").submit();
		});
		
	});

	function treeMake(){
		let trees = new Array();
		$("li").each(function(index, item){
			var element = new Object();
	// 		if($(this).is(":checked")){
				element.menuId = $(this).attr(""); 
				element.useYn = ($(this).children() == true ? "Y" : "N"); 
				trees.push(element);
	// 		}
			
		});
		$("#").val(JSON.Stringfy(trees));
	}
</script>

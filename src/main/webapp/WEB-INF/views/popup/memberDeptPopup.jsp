<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
	var delay = 500, clicks = 0, timer = null;
	var gGubun = "";
	$(document).ready(function() {
		 //팝업
		$('.memberDept_btn').click(function() {
			gGubun = $(this).val();
			memberDeptPopupSet(gGubun);
			$('.memberDept').fadeIn(300);
		});
		$('.pop_close, .pop_bg, .pop .btn_close').click(function() {
			$('.memberDept').fadeOut(300);
		});

		//팝업 높이
		function funThead(){
		var theight = $(window).height() - 200;
			$('.pop_con').css({'height':theight+'px'});
		}
		funThead();
		window.onresize = funThead;

		$('.memberDept .organ_list button .ico').on('click', function(e) {
			e.stopPropagation();
			//$('.memberDept .organ_list .txt').not($(this).parent().children('.txt')).removeClass('on');
			//$(this).parent().children('.txt').addClass('on');
			$(this).parent().toggleClass('on').parent().children('ul').fadeToggle(0);
		});

		/*
		$('.memberDept .organ_list button').on('click', function() {
			$('.memberDept .organ_list .txt').not($(this).children('.txt')).removeClass('on');
			$(this).children('.txt').toggleClass('on');
			//$(this).toggleClass('on').parent().children('ul').fadeToggle(0);
		});
		*/
		$('.memberDept input#userNm').on('keyup', function(e) {
			console.log(e.which);
			if (e.which == 13) {
				searchMemberDeptUser("userNm", $(this).val());
			}
		});


		$(".memberDept .pw_select_list .tb_custom tbody").on("dblclick", "tr", function(){
			var refId = $(this).attr("refId");
			var refTy = $(this).attr("refTy");
			var result = false;
			$(".memberDept .right .tb01 tbody tr").each(function(index, item){
				if(refId == $(this).attr("refId") && refTy == $(this).attr("refTy")){
					result = true;
				}
			});
			if(result){
				alert("이미 선택된 사용자입니다.");
				return;
			}
			var dblhtml = '';
			dblhtml += "<tr refTy='"+refTy+"' refId='"+refId+"' refNm='"+$(this).attr("refNm")+"' deptCode='"+$(this).attr("deptCode")+"' deptNm='"+$(this).attr("deptNm")+"' pstnCode='"+$(this).attr("pstnCode")+"' pstnNm='"+$(this).attr("pstnNm")+"' tableauUserId='"+$(this).attr("tableauUserId")+"' fileUrl='"+$(this).attr("fileUrl")+"' user='"+$(this).attr("user")+"'>";
			dblhtml += '	<td>';
			dblhtml += '		<div class="checkbox">';
			dblhtml += "			<input type='checkbox' name='"+refId+"' id='"+refId+"' value='"+$(this).attr("user")+"'>";
			dblhtml += '			<label for="'+refId+'">';
			dblhtml += '				<span class="icon">';
			dblhtml += '					<span></span>';
			dblhtml += '				</span>';
			dblhtml += '			</label>';
			dblhtml += '		</div>';
			dblhtml += '	</td>';
			dblhtml += '	<td scope="row">'+$(this).attr("refNm")+'</td>';
			dblhtml += '	<td scope="row">'+$(this).attr("pstnNm")+'</td>';
			dblhtml += '	<td>'+refId+'</td>';
			dblhtml += '</tr>';

			$(".memberDept .right .tb01 tbody").append(dblhtml);

			//$(this).clone().appendto($(".memberDept .right .tb01 tbody")).prepend("");
		});

        
		//사용자 조회
		$('.memberDept .userNmSearch').on('click', function(e) {
			searchMemberDeptUser("userNm", $(".memberDept input#userNm").val());
		});

		//조직도 클릭, 더블클릭
		$(".memberDept .organ_list button").on("click", function(e){
			var deptCode = $(this).attr("deptCode");
			$('.memberDept .organ_list .txt').not($(this).children('.txt')).removeClass('on');
			$(this).children('.txt').toggleClass('on');
			var hasOn = $(this).children('.txt').hasClass('on');
			clicks++;
			if(clicks === 1){
				timer = setTimeout(function(){
							if(hasOn){
								searchMemberDeptUser("deptCode", deptCode);
							}
							clicks = 0;
						}, delay);
			}else{
				clearTimeout(timer);
// 				var refId = $(this).attr("refId");
// 				var refTy = $(this).attr("refTy");
// 				var result = false;
// 				$(".memberDept .right .tb01 tbody tr").each(function(index, item){
// 					if(refId == $(this).attr("refId") && refTy == $(this).attr("refTy")){
// 						result = true;
// 					}
// 				});
// 				if(result){
// 					alert("이미 선택된 부서입니다.");
// 					return;
// 				}
// 				var dblhtml = '';
// 				dblhtml += "<tr refTy='"+refTy+"' refId='"+refId+"' refNm='"+$(this).attr("refNm")+"' deptCode='"+$(this).attr("deptCode")+"' deptNm='"+$(this).attr("deptNm")+"' dept='"+$(this).attr("dept")+"'>";
// 				dblhtml += '	<td>';
// 				dblhtml += '		<div class="checkbox">';
// 				dblhtml += "			<input type='checkbox' name='"+refId+"' id='"+refId+"' value='"+$(this).attr("dept")+"'>";
// 				dblhtml += '			<label for="'+refId+'">';
// 				dblhtml += '				<span class="icon">';
// 				dblhtml += '					<span></span>';
// 				dblhtml += '				</span>';
// 				dblhtml += '			</label>';
// 				dblhtml += '		</div>';
// 				dblhtml += '	</td>';
// 				dblhtml += '	<td scope="row" colspan="3">'+$(this).attr("refNm")+'</td>';
// 				dblhtml += '	</tr>';

// 				$(".memberDept .right .tb01 tbody").append(dblhtml);
				clicks = 0;
			}
		}).on("dblclick",function(e){e.preventDefault});

		//선택된 사용자 체크박스
		$(".memberDept #checkAll").on("click",function(){
			if($(this).is(":checked")){
				$(".memberDept .pw_select.right table tbody input[type='checkbox']").prop("checked", true);
			}else{
				$(".memberDept .pw_select.right table tbody input[type='checkbox']").prop("checked", false);
			}
		});

		//확인 버튼
		$(".memberDept .confirm").on("click", function(){
			memberDeptRefSet(gGubun);
			$('.pop_close, .pop_bg, .pop .btn_close').click();
		});

		//선택 삭제
		$(".memberDept .selDel").on("click", function(){
			$(".memberDept .pw_select.right table tbody input[type='checkbox']").each(function(index, item){
				if($(this).is(":checked")){
					$(this).parent().parent().parent().remove();
				}
			});
			$(".memberDept #checkAll").prop("checked", false);
		});

		//취소
		$(".memberDept .cancel").on("click", function(){
			$('.pop_close, .pop_bg, .pop .btn_close').click();
		});
	});

	//사용자 검색
	function searchMemberDeptUser(code, value) {
		var userNm = $("#userNm");
		if(userNm.val().length < 2 && code == "userNm"){
			alert("이름을 2자 이상 입력하세요.");
			return;
		}
		var data = "searchKey="+code+"&searchValue="+value;
		$.ajax({
            type: "post",
            url: "/system/member/list/popup",
            dataType: "json",
            data: data,
            success: function (data) {
                if(data.result){
                    var tbody = $(".memberDept .pw_select_list table tbody");
                    tbody.empty();
					for(var i=0;i < data.members.length;i++){
						var member = data.members[i];
						var value='{"refTy" : "1", "refId": "'+member.userId+'","refNm": "'+member.userNm+'","deptCode": "'+member.deptCode+'","deptNm": "'+member.deptNm+'","pstnCode": "'+member.pstnCode+'","pstnNm": "'+member.pstnNm+'","tableauUserId":"'+member.tableauUserId+'","fileUrl":"'+member.fileUrl+'"}'
						var html = '';
							html += "<tr refTy='1' refId='"+member.userId+"' refNm='"+member.userNm+"' deptCode='"+member.deptCode+"' deptNm='"+member.deptNm+"' pstnCode='"+member.pstnCode+"' pstnNm='"+member.pstnNm+"' tableauUserId='"+member.tableauUserId+"' fileUrl='"+member.fileUrl+"' user='"+value+"'>";
							html += '	<td class="tl">';
							html += '		<div class="td_user" style="text-align:left;">';
							html += '			<span class="user_img" style="display:inline-block;">';
							html += '				<img src=/file/photo/'+member.fileUrl+' onerror=\'this.src="/images/icon_gnb_user.png"\'>';
							html += '			</span>';
							html += '			<span class="user_name">'+member.deptNm+' '+member.userNm+' '+member.pstnNm+'</span>';
							html += '		</div>';
							html += '	</td>';
							html += '</tr>';
							tbody.append(html);
					}
                }
            }
        });
		
	}

	function memberDeptRefSet(gubun){
		var g = "";
		if(gubun == "1"){
			g = "mgr";
		}else if(gubun == "2"){
			g = "pcpt";
		}
		var container = $("#" + g + "InfoCotainer");
		container.empty();
		$(".memberDept .right .tb01 tbody tr").each(function(index, item){
			container.append(returnHtml($(this), gubun));//1 : 편집권한, 2 : 사용자 
		});
	}

	function returnHtml(selector, gubun){
		var returnHtml = "";
		var refString = "";
		if(selector.attr("refTy") == "1"){
			returnHtml += "<div class='radio btn_delete_img' style='margin-right:15px;padding-top:3px;padding-bottom:3px;' refId='"+selector.attr("refId")+"' refNm='"+selector.attr("refNm")+"' refTy='"+selector.attr("refTy")+"' deptCode='"+selector.attr("deptCode")+"' deptNm='"+selector.attr("deptNm")+"' pstnCode='"+selector.attr("pstnCode")+"' pstnNm='"+selector.attr("pstnNm")+"' tableauUserId='"+selector.attr("tableauUserId")+"' fileUrl='"+selector.attr("fileUrl")+"'>";
			refString = selector.attr("user");
		}else if(selector.attr("refTy") == "2"){
			returnHtml += "<div class='radio btn_delete_img' style='margin-right:15px;padding-top:3px;padding-bottom:3px;' refId='"+selector.attr("refId")+"' refNm='"+selector.attr("refNm")+"' refTy='"+selector.attr("refTy")+"' deptCode='"+selector.attr("deptCode")+"' deptNm='"+selector.attr("deptNm")+"'>";
			refString = selector.attr("dept");
		}
		if(gubun == '1'){
			returnHtml += "	<pre class='hidden mgrInfo'>"+refString+"</pre>";
		}else if(gubun == '2'){
			returnHtml += "	<pre class='hidden pcptInfo'>"+refString+"</pre>";
		}
		returnHtml += "	<input type='radio' checked=''>";
		returnHtml += "	<label>";
// 		returnHtml += "		<span class='icon'><img src='/images/minus.png'/></span>";
		returnHtml += "		<span class='txt'>"+selector.attr("refNm")+"</span>";
		returnHtml += "	</label>";
		returnHtml += "</div>";
		
		return returnHtml;
	}

	function memberDeptPopupSet(gubun){
		var g = "";
		if(gubun == "1"){
			g = "mgr";
		}else if(gubun == "2"){
			g = "pcpt";
		}
		var container = $("#" + g + "InfoCotainer div");
		$(".memberDept .right .tb01 tbody").empty();
		container.each(function(index, item){
			var refTy = $(this).attr("refTy");
			var refId = $(this).attr("refId");
			var value = "";
			var html = '';
			if(refTy == "1"){
				value='{"refTy" : "'+refTy+'", "refId": "'+refId+'","refNm": "'+$(this).attr("refNm")+'","deptCode": "'+$(this).attr("deptCode")+'","deptNm": "'+$(this).attr("deptNm")+'","pstnCode": "'+$(this).attr("pstnCode")+'","pstnNm": "'+$(this).attr("pstnNm")+'","tableauUserId":"'+$(this).attr("tableauUserId")+'","fileUrl":"'+$(this).attr("fileUrl")+'"}'
				html += "<tr refTy='"+refTy+"' refId='"+refId+"' refNm='"+$(this).attr("refNm")+"' deptCode='"+$(this).attr("deptCode")+"' deptNm='"+$(this).attr("deptNm")+"' pstnNm='"+$(this).attr("pstnNm")+"' tableauUserId='"+$(this).attr("tableauUserId")+"' fileUrl='"+$(this).attr("fileUrl")+"' user='"+value+"'>";
			}else if(refTy == "2"){
				value='{"refTy" : "'+refTy+'", "refId": "'+refId+'","refNm": "'+$(this).attr("refNm")+'","deptCode": "'+$(this).attr("deptCode")+'","deptNm": "'+$(this).attr("deptNm")+'"}'
				html += "<tr refTy='"+refTy+"' refId='"+refId+"' refNm='"+$(this).attr("refNm")+"' deptCode='"+$(this).attr("deptCode")+"' deptNm='"+$(this).attr("deptNm")+"' dept='"+value+"'>";
			}
				html += '	<td>';
				html += '		<div class="checkbox">';
				html += "			<input type=\"checkbox\" name=\""+refId+"\" id=\""+refId+"\" value='"+value+"'>";
				html += '			<label for="'+refId+'">';
				html += '				<span class="icon">';
				html += '					<span></span>';
				html += '				</span>';
				html += '			</label>';
				html += '		</div>';
				html += '	</td>';
				if(refTy == "1"){
					html += '	<td scope="row">'+$(this).attr("refNm")+'</td>';
					html += '	<td scope="row">'+$(this).attr("pstnNm")+'</td>';
					html += '	<td>'+refId+'</td>';
				}else if(refTy == "2"){
					html += '	<td scope="row" colspan="3">'+$(this).attr("refNm")+'</td>';
				}
				html += '</tr>';
				$(".memberDept .right .tb01 tbody").append(html);
		});
	}
</script>

<div class="pop pop2 pop_ver2 table_lookup memberDept" style="display:none;">
  <div class="pop_wrap">
    <div class="pop_tit">
      사용자 선택
      <button type="button" class="pop_close">팝업닫기</button>
    </div>

    <div class="pop_con">
      <div class="pop_con_scroll">

        <div class="see_wrap payment">
          <dl class="pw_select first">
            <dt>
              부서
<!--               <div class="pw_search"><div> -->
<!--                 <label class="hidden" for="deptNm">부서 검색어</label> -->
<!--                 <input type="text" class="input" id="deptNm" name="deptNm" placeholder="부서를 입력하세요." maxlength="10"> -->
<!--                 <button type="button" class="btn_search">검색</button> -->
<!--               </div></div> -->
            </dt>
            <dd class="row2" style="top:55px;">
              <div class="organ_list">
                <ul>
                	<c:set var="preDepth" value="0"/>
                	<c:set var="preDeptCode" value="0"/>
                	<c:set var="nextDepth" value="0"/>
                	<c:set var="nextDeptCode" value="0"/>
                	<c:forEach var="dept" items="${depts }" varStatus="status">
                		<c:set var="nextDepth" value="${depts[status.index+1].lv }"/>
                		<c:set var="nextDeptCode" value="${depts[status.index+1].deptCode }"/>
                		<c:if test="${status.first }">
                			<li><button type="button" refTy="2" refId="${dept.deptCode }" refNm="${dept.deptNm }" deptCode="${dept.deptCode }" deptNm="${dept.deptNm }" dept='{"refTy" : "2", "refId" : "${dept.deptCode }", "refNm" : "${dept.deptNm }","deptCode": "${dept.deptCode}","deptNm": "${dept.deptNm}"}'><span class="ico"></span><span class="txt">${dept.deptNm}</span></button>
                		</c:if>
                		
                		<c:if test="${!status.first and !status.last}">
                			<c:choose>
                				<c:when test="${dept.lv > preDepth }">
                					<ul>
                      				<li><button type="button" refTy="2" refId="${dept.deptCode }" refNm="${dept.deptNm }" deptCode="${dept.deptCode }" deptNm="${dept.deptNm }" dept='{"refTy" : "2", "refId" : "${dept.deptCode }", "refNm" : "${dept.deptNm }","deptCode": "${dept.deptCode}","deptNm": "${dept.deptNm}"}' <c:if test="${dept.lv >=  nextDepth}">class="last"</c:if>><span class="ico"></span><span class="txt">${dept.deptNm}</span></button>
                				</c:when>
                				<c:when test="${dept.lv < preDepth }">
                					<c:forEach var="i" begin="1" end="${preDepth - dept.lv}" varStatus="depth">
                							</ul>
                						</li>
                					</c:forEach>
                					<li><button type="button" refTy="2" refId="${dept.deptCode }" refNm="${dept.deptNm }" deptCode="${dept.deptCode }" deptNm="${dept.deptNm }" dept='{"refTy" : "2", "refId" : "${dept.deptCode }", "refNm" : "${dept.deptNm }","deptCode": "${dept.deptCode}","deptNm": "${dept.deptNm}"}' <c:if test="${dept.lv >=  nextDepth}">class="last"</c:if>><span class="ico"></span><span class="txt">${dept.deptNm}</span></button>
                				</c:when>
                				<c:when test="${dept.lv == preDepth }">
                					<li><button type="button" refTy="2" refId="${dept.deptCode }" refNm="${dept.deptNm }" deptCode="${dept.deptCode }" deptNm="${dept.deptNm }" dept='{"refTy" : "2", "refId" : "${dept.deptCode }", "refNm" : "${dept.deptNm }","deptCode": "${dept.deptCode}","deptNm": "${dept.deptNm}"}' <c:if test="${dept.lv >=  nextDepth}">class="last"</c:if>><span class="ico"></span><span class="txt">${dept.deptNm}</span></button>
                				</c:when>
                			</c:choose>
                		</c:if>
                		
                		<c:if test="${status.last }">
                			<c:if test="${dept.lv > preDepth}">
                				<ul>
                			</c:if>
                			<li><button type="button" refTy="2" refId="${dept.deptCode }" refNm="${dept.deptNm }" deptCode="${dept.deptCode }" deptNm="${dept.deptNm }" dept='{"refTy" : "2", "refId" : "${dept.deptCode }", "refNm" : "${dept.deptNm }","deptCode": "${dept.deptCode}","deptNm": "${dept.deptNm}"}'><span class="ico"></span><span class="txt">${dept.deptNm }</span></button>
                			<c:forEach var="i" begin="1" end="${dept.lv }" varStatus="depth">
                					</ul>
                				</li>	
                			</c:forEach>
                		</c:if>
                		<c:set var="preDepth" value="${dept.lv }"/>
                		<c:set var="preDept" value="${dept.deptCode }"/>
                	</c:forEach>
                </ul>
              </div>
            </dd>
          </dl>
          <dl class="pw_select left">
            <dt>
              이름
              <div class="pw_search"><div>
                <label class="hidden" for="userNm">이름 검색어</label>
                <input type="text" class="input" id="userNm" name="userNm" placeholder="이름을 입력하세요." maxlength="10">
                <button type="button" class="btn_search userNmSearch">검색</button>
              </div></div>
            </dt>
            <dd class="row2 pw_select_list">
              <table class="tb01 tb_custom cursor">
                <caption class="hidden">이름 검색 결과</caption>
                <tbody>
                </tbody>
              </table>
            </dd>
          </dl>
          
          <dl class="pw_select right">
            <dt>선택된 사용자</dt>
            <dd>
              <div class="tb01_wrap">
                <div class="tb01_line"></div>
                <table class="tb01" summary="이름, 직급, 계정에 대한 내용입니다.">
                  <caption class="hidden">사용자 권한관리 이름 목록</caption>
                  <colgroup>
                    <col style="width:10%">
                    <col style="width:30%">
                    <col style="width:30%">
                    <col style="width:30%">
                  </colgroup>
                  <thead>
                    <tr>
                      <th scope="col">
                        <div class="checkbox">
                          <input type="checkbox" name="checkAll" id="checkAll">
                          <label for="checkAll">
                            <span class="icon">
                              <span></span>
                            </span>
                          </label>
                        </div>
                      </th>
                      <th scope="col">이름</th>
                      <th scope="col">직급</th>
                      <th scope="col">계정</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
              </div>
            </dd>
          </dl>
        </div>
      </div>
    </div>
      
    <div class="pop_btn btn_wrap tr tb0">
      <button type="button" class="btn big blue confirm">확인</button>
      <button type="button" class="btn big grayr selDel">선택삭제</button>
      <button type="button" class="btn big grayb cancel">취소</button>
    </div>
  </div>
  <div class="pop_bg"></div>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">
	var delay = 500, clicks = 0, timer = null;
	var gGubun = "";
	$(document).ready(function() {
		 //팝업
		$('.dept_btn').click(function() {
			deptPopupSet();
			$('.dept').fadeIn(300);
		});
		$('.pop_close, .pop_bg, .pop .btn_close').click(function() {
			$('.dept').fadeOut(300);
		});

		//팝업 높이
		function funThead(){
		var theight = $(window).height() - 200;
			$('.pop_con').css({'height':theight+'px'});
		}
		funThead();
		window.onresize = funThead;

		// 통합검색 탭
		$('.tab_con').show();
		$('.tab_con1').hide();
		$('#sub_tab li').on('click',function(){
			index = $(this).index() + 1;
			$('#sub_tab li').removeClass('on');
			$(this).addClass('on')
			$('.tab_con').hide();
			$('.tab_con'+ index).show();
		});
		
		$('.dept .organ_list button .ico').on('click', function(e) {
			e.stopPropagation();
			//$('.dept .organ_list .txt').not($(this).parent().children('.txt')).removeClass('on');
			//$(this).parent().children('.txt').addClass('on');
			$(this).parent().toggleClass('on').parent().children('ul').fadeToggle(0);
		});

		//조직도 클릭, 더블클릭
		$(".dept .organ_list button").on("click", function(e){
			$('.dept .organ_list .txt').not($(this).children('.txt')).removeClass('on');
			$(this).children('.txt').toggleClass('on');
			var deptCode = $(this).attr("deptCode");
			var hasOn = $(this).children('.txt').hasClass('on');
			clicks++;
			if(clicks === 1){
				timer = setTimeout(function(){
							if(hasOn){
								//searchdeptUser("deptCode", deptCode);
							}
							clicks = 0;
						}, delay);
			}else{
				clearTimeout(timer);
				//선택된 사용자 세팅
				var refId = $(this).attr("refId");
				var refTy = $(this).attr("refTy");
				var result = false;
				$(".dept .row1 .tb01 tbody tr").each(function(index, item){
					if(refId == $(this).attr("refId") && refTy == $(this).attr("refTy")){
						result = true;
					}
				});
				if(result){
					alert("이미 선택된 부서입니다.");
					return;
				}
				$(".dept .right .row1 .tb01 tbody").empty();
				var dblhtml = '';
				dblhtml += "<tr refTy='"+refTy+"' refId='"+refId+"' refNm='"+$(this).attr("refNm")+"' deptCode='"+$(this).attr("deptCode")+"' deptNm='"+$(this).attr("deptNm")+"' dept='"+$(this).attr("dept")+"'>";
				dblhtml += '	<td>';
				dblhtml += '		<div class="checkbox">';
				dblhtml += "			<input type='checkbox' name='"+refId+"' id='"+refId+"' value='"+$(this).attr("dept")+"'>";
				dblhtml += '			<label for="'+refId+'">';
				dblhtml += '				<span class="icon">';
				dblhtml += '					<span></span>';
				dblhtml += '				</span>';
				dblhtml += '			</label>';
				dblhtml += '		</div>';
				dblhtml += '	</td>';
				dblhtml += '	<td scope="row" colspan="3">'+$(this).attr("refNm")+'</td>';
				dblhtml += '</tr>';

				$(".dept .right .row1 .tb01 tbody").append(dblhtml);
				clicks = 0;
				searchDeptUser("deptCode", refId);
			}
		}).on("dblclick",function(e){e.preventDefault});

		//선택된 부서 체크박스
		$(".dept #dCheckAll").on("click",function(){
			if($(this).is(":checked")){
				$(".dept .right .row1 table tbody input[type='checkbox']").prop("checked", true);
			}else{
				$(".dept .right .row1 table tbody input[type='checkbox']").prop("checked", false);
			}
		});

		//관리자 체크박스
		$(".dept #uCheckAll").on("click",function(){
			if($(this).is(":checked")){
				$(".dept .right .row2 table tbody input[type='checkbox']").prop("checked", true);
			}else{
				$(".dept .right .row2 table tbody input[type='checkbox']").prop("checked", false);
			}
		});

		//확인 버튼
		$(".dept .confirm").on("click", function(){
			deptRefSet();
			$('.pop_close, .pop_bg, .pop .btn_close').click();
		});

		//선택 삭제
		$(".dept .selDel").on("click", function(){
			$(".dept .right .row1 table tbody input[type='checkbox']").each(function(index, item){
				if($(this).is(":checked")){
					$(this).parent().parent().parent().remove();
				}
			});
			$(".dept #gCheckAll").prop("checked", false);
		});

		//취소
		$(".dept .cancel").on("click", function(){
			$('.pop_close, .pop_bg, .pop .btn_close').click();
		});
	});

	//사용자 검색
	function searchDeptUser(code, value) {
		var data = "searchKey="+code+"&searchValue="+value;
		$.ajax({
            type: "post",
            url: "/system/member/list/popup",
            dataType: "json",
            data: data,
            success: function (data) {
                if(data.result){
                    var tbody = $(".dept .right .row2 table tbody");
                    tbody.empty();
					for(var i=0;i < data.members.length;i++){
						var member = data.members[i];
						var value='{"refTy" : "1", "refId": "'+member.userId+'","refNm": "'+member.userNm+'","deptCode": "'+member.deptCode+'","deptNm": "'+member.deptNm+'","pstnCode": "'+member.pstnCode+'","pstnNm": "'+member.pstnNm+'","tableauUserId":"'+member.tableauUserId+'","fileUrl":"'+member.fileUrl+'"}'
						var html = '';
							html += "<tr refTy='1' refId='"+member.userId+"' refNm='"+member.userNm+"' deptCode='"+member.deptCode+"' deptNm='"+member.deptNm+"' pstnCode='"+member.pstnCode+"' pstnNm='"+member.pstnNm+"' tableauUserId='"+member.tableauUserId+"' fileUrl='"+member.fileUrl+"' user='"+value+"'>";
							html += '	<td>';
							html += '		<div class="radio">';
							html += "			<input type='radio' name='mainPicrId' id='main_"+member.userId+"' value='"+value+"'>";
							html += '			<label for="main_'+member.userId+'">';
							html += '				<span class="icon">';
							html += '					<span></span>';
							html += '				</span>';
							html += '			</label>';
							html += '		</div>';
							html += '	</td>';
							html += '	<td>';
							html += '		<div class="radio">';
							html += "			<input type='radio' name='subPicrId' id='sub_"+member.userId+"' value='"+value+"'>";
							html += '			<label for="sub_'+member.userId+'">';
							html += '				<span class="icon">';
							html += '					<span></span>';
							html += '				</span>';
							html += '			</label>';
							html += '		</div>';
							html += '	</td>';
							html += '	<td scope="row">'+member.userNm+'</td>';
							html += '	<td scope="row">'+member.pstnNm+'</td>';
							html += '	<td scope="row">'+member.userId+'</td>';
							html += '</tr>';
							tbody.append(html);
					}
					//관리자 정/부 체크
					var mainPicrId = $("#mainPicrId").val();
					var subPicrId = $("#subPicrId").val();
					$(".dept .right .row2 table tbody tr").each(function(index, item){
						var userId = $(this).attr("refId");
						if(mainPicrId == userId){
							$(this).children().children().find("label[for='main_"+mainPicrId+"']").click();
						}
						if(subPicrId == userId){
							$(this).children().children().find("label[for='sub_"+subPicrId+"']").click();
						}
					});
                }
            }
        });
		
	}

	function deptRefSet(){
		var container = $("#deptInfoCotainer");
		container.empty();

		$(".dept .right .row1 .tb01 tbody tr").each(function(index, item){
			$("#deptCode").val($(this).attr("refId"));
			$("#deptNm").val($(this).attr("refNm"));
			container.append(returnHtml($(this), "3"));
		});
		
		$(".dept .right .row2 .tb01 tbody tr").each(function(index, item){
			mainChecked = $(this).children().find("input[name='mainPicrId']").is(":checked");
			subChecked = $(this).children().find("input[name='subPicrId']").is(":checked");
			if(mainChecked){
				$(".mainPicrId").empty();
				$("#mainPicrId").val($(this).attr("refId"));
				$(".mainPicrId").append(returnHtml($(this), "3"));
			}
			if(subChecked){
				$(".subPicrId").empty();
				$("#subPicrId").val($(this).attr("refId"));
				$(".subPicrId").append(returnHtml($(this), "3"));
			}
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
		}else if(gubun == '3'){
			returnHtml += "	<pre class='hidden deptInfo'>"+refString+"</pre>";
		}
		returnHtml += "	<input type='radio' checked=''>";
		returnHtml += "	<label>";
// 		returnHtml += "		<span class='icon'><img src='/images/minus.png'/></span>";
		returnHtml += "		<span class='txt'>"+selector.attr("refNm")+"</span>";
		returnHtml += "	</label>";
		returnHtml += "</div>";
		
		return returnHtml;
	}

	function deptPopupSet(){
		var deptCode = $("#deptCode").val();
		var deptNm = $("#deptNm").val();
		var container = $(".dept .right .row1 .tb01 tbody");
		container.empty();
		if(deptCode != ""){
			var deptHtml = "";
			var value = "";
			value='{"refTy" : "2", "refId": "'+deptCode+'","refNm": "'+deptNm+'","deptCode": "'+deptCode+'","deptNm": "'+deptNm+'"}'
			deptHtml += "<tr refTy='2' refId='"+deptCode+"' refNm='"+deptNm+"' deptCode='"+deptCode+"' deptNm='"+deptNm+"' dept='"+value+"'>";
			deptHtml += '	<td>';
			deptHtml += '		<div class="checkbox">';
			deptHtml += "			<input type=\"checkbox\" name=\""+deptCode+"\" id=\""+deptCode+"\" value='"+value+"'>";
			deptHtml += '			<label for="'+deptCode+'">';
			deptHtml += '				<span class="icon">';
			deptHtml += '					<span></span>';
			deptHtml += '				</span>';
			deptHtml += '			</label>';
			deptHtml += '		</div>';
			deptHtml += '	</td>';
			deptHtml += '	<td scope="row" colspan="3">'+deptNm+'</td>';
			deptHtml += '</tr>';
			container.append(deptHtml);

			searchDeptUser("deptCode", deptCode);
		}
	}
</script>

<div class="pop popselect pop_type4 pop_ver2 dept" >
    <div class="pop_wrap">
      <div class="pop_tit">
        권한 부여
        <button type="button" class="pop_close">팝업닫기</button>
      </div>

      <div class="pop_con">
        <div class="pop_con_scroll">
          <div class="tab_con tab_con2">
            <div class="see_wrap select_gr">
              <dl class="pw_select left" style="height:630px;">
                <dt>
                  부서
                </dt>
                <dd class="row2" style="top:55px;height:calc(100% - 55px);">
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
              <div class="pw_select right" style="height:630px;">
                <dl class="row1">
                  <dt>선택된 부서</dt>
                  <dd>
                    <div class="tb01_wrap">
                      <div class="tb01_line"></div>
                      <table class="tb01" summary="이름, 부서에 대한 내용입니다.">
                        <caption class="hidden">선택된 부서 목록</caption>
                        <colgroup>
                          <col style="width:14%">
                          <col style="width:76%">
                        </colgroup>
                        <thead>
                          <tr>
                            <th scope="col">
                              <div class="checkbox">
                                <input type="checkbox" name="dCheckAll" id="dCheckAll">
                                <label for="dCheckAll">
                                  <span class="icon">
                                    <span></span>
                                  </span>
                                </label>
                              </div>
                            </th>
                            <th scope="col">부서</th>
                          </tr>
                        </thead>
                        <tbody>
                        </tbody>
                      </table>
                    </div>
                  </dd>
                </dl>
                <dl class="row2">
					<dt>관리자</dt>
					<dd>
                    <div class="tb01_wrap">
                      <div class="tb01_line"></div>
                      <table class="tb01" summary="관리자에 대한 내용입니다.">
						<caption class="hidden">선택된 관리자 목록</caption>
                        <colgroup>
							<col style="width:13%">
							<col style="width:13%">
							<col style="width:24%">
							<col style="width:24%">
							<col style="width:24%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">정
<!-- 									<div class="checkbox"> -->
<!-- 										<input type="checkbox" name="uCheckAll" id="uCheckAll"> -->
<!-- 										<label for="uCheckAll"> -->
<!-- 											<span class="icon"> -->
<!-- 												<span></span> -->
<!-- 											</span> -->
<!-- 										</label> -->
<!-- 									</div> -->
								</th>
								<th scope="col">부</th>
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
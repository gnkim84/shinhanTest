<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="user" property="principal" />
</sec:authorize>
<header>
    <h1><a href="/">신한금융투자</a></h1>
    <h2><button type="button">GOLDNET</button></h2>
    <div class="head_search">
		<form>
			<fieldset>
				<legend class="hidden">검색하기</legend>
				<label class="hidden" for="head_search">검색</label>
				<input type="text" id="head_search" placeholder="통합검색">
				<button type="submit" name="button">검색하기</button>
			</fieldset>
		</form>
    </div>
    
    <div class="head_right">
<!--     	<button type="button" class="btn_alarm">알림<span class="num">99</span></button> -->
		<!-- 알림클릭시 나오는 부분 -->
<!-- 		<div class="head_ing"> -->
<!-- 			<ul class="head_ing_tab"> -->
<!-- 				<li class="on"> -->
<!-- 					<button type="button">보고서 신청</button> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<button type="button">용어등록 요청</button> -->
<!-- 				</li> -->
<!-- 			</ul> -->
<!-- 			<div class="head_ing_con head_ing_con1"> -->
<!-- 				<ul> -->
<!-- 					<li> -->
<!-- 						<span class="text">승인중</span> -->
<!-- 						<span class="number"><b>20</b>건</span> -->
<!-- 					</li> -->
<!-- 					<li> -->
<!-- 						<span class="text">승인완료</span> -->
<!-- 						<span class="number"><b>10</b>건</span> -->
<!-- 					</li> -->
<!-- 				</ul> -->
<!-- 				<div class="link-area"> -->
<!-- 					<a href="#none">승인내역 전체보기</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="head_ing_con head_ing_con2"> -->
<!-- 				<ul> -->
<!-- 					<li> -->
<!-- 						<span class="text">승인중</span> -->
<!-- 						<span class="number"><b>10</b>건</span> -->
<!-- 					</li> -->
<!-- 					<li> -->
<!-- 						<span class="text">승인완료</span> -->
<!-- 						<span class="number"><b>5</b>건</span> -->
<!-- 					</li> -->
<!-- 				</ul> -->
<!-- 				<div class="link-area"> -->
<!-- 					<a href="#none">승인내역 전체보기</a> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<!-- //알림클릭시 나오는 부분 -->
	    <dl class="head_dl">
	       <dt>
	       		<div class="thumbnail">
	       			<img src="/images/icon_gnb_user.png" onerror="this.src='/images/icon_gnb_user.png'" alt="사진" class="mr10" />
	       		</div>
	            ${user.memberModel.userNm}
	       </dt>
	       <dd>
	           ${user.memberModel.pstnNm}
	       </dd>
	       <dd>
	           ${user.memberModel.deptNm}
	       </dd>
	       <dd>       
		       <button type="button" class="mgl10 logout">로그아웃</button>
	       </dd>
	    </dl>
    </div>
</header>
<script type="text/javascript">
    $(function() {
        $(".logout").click(function(){
            location.href = '/logout';
        });
    });
</script>
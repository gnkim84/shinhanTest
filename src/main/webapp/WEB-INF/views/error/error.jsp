<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<section class="content on">
  <div class="err_contents">
    <div class="err_img">
      <img src="/images/erro_img.png"/>
    </div>
    <div class="err_msg_wrap">
      <h1>요청한 페이지를 찾을 수 없습니다.</h1>
    </div>
    <div class="err_msg_detail_wrap">
      <p>
          방문 원하시는 페이지의 주소가 잘못 입력되었거나,<br/>
          변경 혹은 삭제되어 요청하신 페이지를 찾을 수가 없습니다.<br/>
          <br><br/>
          자세한 문의는 담당자에게 연락주십시오.<br/>
          (담당자 : 데이터사이언스팀 박성근/김현우/이경재/장수정)
      </p>
    </div>
    <div class="err_btn_wrap">
      <button class="btn big blue" onclick="$(location).attr('href', '/')">메인으로</button>
    </div>
  </div>
</section>
<script>
$(function() {

});
</script>
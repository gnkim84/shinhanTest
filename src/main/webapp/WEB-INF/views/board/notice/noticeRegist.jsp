<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div class="content_tit">
		<h1 class="content_h1">공지 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>게시판</li>
			<li>공지 관리</li>
		</ul>
	</div>

	<form name="noticeForm" id="noticeForm" method="post" group="fileForm">
		<input type="hidden" name="noticeId" id="noticeId" value="${noticeInfo.noticeId}">
		<table class="tb02" summary="번호, 등록일, 제목, 게시여부, 중요여부, 내용, 첨부파일에 대한 내용입니다.">
			<caption class="hidden">공지사항 등록</caption>
			<colgroup>
				<col style="width:12.5%">
				<col style="width:37.5%">
				<col style="width:12.5%">
				<col style="width:37.5%">
			</colgroup>
			<tbody>
				<c:if test="${!empty noticeInfo && null ne noticeInfo.noticeId}">
				<tr>
					<th scope="row">ID</th>
					<td class="tc">
						<input type="text" class="input tc" value="${noticeInfo.noticeId}" disabled />
					</td>
					<th scope="row">최종수정일</th>
					<td class="tc">
						<input type="text" class="input tc" value="<spring:eval expression='noticeInfo.modiDt'/>" disabled />
					</td>
				</tr>
				</c:if>					
				<tr>
					<th scope="row"><label for="sj">제목</label></th>
					<td colspan="3">
						<input type="text" class="input" name="sj" id="sj" value="${noticeInfo.sj}" maxLength="50">
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="popupYn">팝업공지여부</label></th>
					<td>
						<div class="select">
							<select name="popupYn" id="popupYn" class="tc">
								<c:forEach var="popupYnCode" items="${popupYnList}">
									<option <c:if test="${noticeInfo.popupYn eq popupYnCode.codeId}">selected</c:if> value="<c:out value='${popupYnCode.codeId}'/>">${popupYnCode.codeNm}</option>
								</c:forEach>
							</select>
						</div>
					</td>
					<th scope="row"><label for="startDt">팝업공지일자</label></th>
					<td>
				        <div class="input_date_wrap">
				            <div class="input_date">
				                <label class="hidden" for="startDt">팝업공지일자 시작일</label>
				                <input type="text" class="input" id="startDt" name="startDt" value="" />
				                <button type="button" class="btn_date">팝업공지일자 시작일 검색</button>
				            </div>
				            <div class="input_wave">~</div>
				            <div class="input_date">
				                <label class="hidden" for="endDt">팝업공지일자 종료일</label>
				                <input type="text" class="input" id="endDt" name="endDt" value="" />
				                <button type="button" class="btn_date">팝업공지일자 종료일 검색</button>
				            </div>
				        </div>
					</td>					
				</tr>
				<tr>
					<th scope="row"><label for="useYn">게시여부</label></th>
					<td>
						<div class="select">
							<select name="useYn" id="useYn" class="tc">
								<c:forEach var="useYn" items="${codeUseYnList}">
									<option <c:if test="${noticeInfo.useYn eq useYn.codeId}">selected</c:if> value="<c:out value='${useYn.codeId}'/>">${useYn.codeNm}</option>
								</c:forEach>
							</select>
						</div>
					</td>
					<th scope="row"><label for="importantYn">중요여부</label></th>
					<td>
						<div class="select">
							<select name="importantYn" id="importantYn" class="tc">
								<c:forEach var="importantYn" items="${codeImportantYnList}">
									<option <c:if test="${noticeInfo.importantYn eq importantYn.codeId}">selected</c:if> value="<c:out value='${importantYn.codeId}'/>">${importantYn.codeNm}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="cn">내용</label></th>
					<td colspan="3" style="max-width: 1200px;">
						<div class="textarea">
							<textarea name="cn" id="cn" class="summernote">${noticeInfo.cn}</textarea>
						</div>
					</td>
				</tr>
				<c:if test="${fn:length(fileList) > 0 }">
				<tr>
					<th scope="row">첨부파일</th>
					<td colspan="3">
						<ul class="attach">
						<c:forEach var="file" items="${fileList }" varStatus="status">
							<li style="margin-top:5px;">
								<a href="#none" url="/file/detail/${file.fileUrl }">${file.fileNm }</a>
								&nbsp;&nbsp;<img src="/images/icon_close.png" url="/file/delete/${file.fileUrl }" fileId="${file.fileId }" style="cursor:pointer;background:#0055b4;" alt="삭제"/>
							</li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				</c:if>
			</tbody>
		</table>
		<c:forEach var="file" items="${fileList }" varStatus="status">
			<input type="hidden" class="fileIds" name="fileIds" value="${file.fileId }"/>
		</c:forEach>
		</form>
		
		<table class="tb02" summary="번호, 등록일, 제목, 게시여부, 중요여부, 내용, 첨부파일에 대한 내용입니다.">
			<caption class="hidden">공지사항 등록</caption>
			<colgroup>
				<col style="width:12.5%">
				<col style="width:37.5%">
				<col style="width:12.5%">
				<col style="width:37.5%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">첨부파일</th>
					<td colspan="3">
						<div>
							<form action="/file/insert" class="dropzone" id="myDropzone">
								<div class="dz-message needsclick">Drag & Drop</div>
							</form>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		
		<div class="btn_wrap tr">
<%-- 			<c:if test="${not empty noticeInfo}"> --%>
<!-- 				<button type="button" class="btn big blue" onclick="deleteInfo();">삭제</button> -->
<%-- 			</c:if> --%>
			<button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
			<button type="button" class="btn big grayb" onclick="location.href='/board/notice'">목록</button>
		</div>
</section>
<form id="frmFile" name="frmFile" action="/" method="post">
	<input type="hidden" name="fileUrl" value="">
</form>
<script type="text/javascript" src="/js/noticeRegist.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		$(".attach a").on("click", function(){
			var frmFile = $("#frmFile");
			frmFile.attr("action", $(this).attr("url"));
			frmFile.submit();
		});

		$(".attach img").on("click", function(){
			var _this = $(this);
			var fileId = _this.attr("fileId");
			$.ajax({
	            type: "post",
	            url: _this.attr("url"),
	            dataType: "json",
	            data: '',
	            async:true,
	            success: function (result) {
	                //console.log(result);
	                console.log("_this : ",_this.parent());
	                _this.parent().remove();
	                $(".fileIds").each(function(index, item){
						if(fileId == $(this).val()){
							$(this).remove();
						}
		            });
	            },
	            error: function (result) {
	                console.log("error : ",result);
	            }        
	        })
		});

		//팝업공지여부 onchange 팝업공지일자 활성화 toggle 
		$("#popupYn").on("change", function(){
			if ($(this).val() == 'Y') {
	    		$('#startDt').prop("readonly",false);
	    		$('#endDt').prop("readonly",false);  				
			} else {
	    		$('#startDt').prop("readonly",true);
	    		$('#endDt').prop("readonly",true);  				
			}
		});		
		

		//팝업공지 시작일
        $("#startDt").datepicker({
            dateFormat:'yy-mm-dd',
            monthNamesShort:[ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ],
            dayNamesMin:[ '일', '월', '화', '수', '목', '금', '토' ],
            changeMonth:true,
            changeYear:true,
            showMonthAfterYear:true,

            // timepicker 설정
            timeFormat:'HH:mm:ss',
            controlType:'select',
            oneLine:true,
            onClose : function(selectedDate){
				$("#endDt").datepicker(
					"option",
					"minDate",
					selectedDate
				);
			},
			//리드온리 속성이면 아무 동작 하지 않는다.
			beforeShow : function(i) {
				if ($(i).prop("readonly") == true) {
					return false;	
				}
			}
        });
		//팝업공지 종료일
        $("#endDt").datepicker({
            dateFormat:'yy-mm-dd',
            monthNamesShort:[ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ],
            dayNamesMin:[ '일', '월', '화', '수', '목', '금', '토' ],
            changeMonth:true,
            changeYear:true,
            showMonthAfterYear:true,

            // timepicker 설정
            timeFormat:'HH:mm:ss',
            controlType:'select',
            oneLine:true,
            //리드온리 속성이면 아무 동작 하지 않는다.
			beforeShow : function(i) {
				if ($(i).prop("readonly") == true) {
					return false;	
				}
			}
        });
		//팝업공지 시작일,종료일 value 셋팅
    	var startDtVal = '<c:if test="${not empty noticeInfo.startDt}"><spring:eval expression='noticeInfo.startDt'/></c:if>';
    	var endDtVal = '<c:if test="${not empty noticeInfo.endDt}"><spring:eval expression='noticeInfo.endDt'/></c:if>';
    	if (startDtVal == '') {
    		$('#startDt').val(getToday("-"));
    	} else {
    		$('#startDt').val(startDtVal);
    	}
    	if (endDtVal == '') {
			$('#endDt').val(getSomeday("-",7));
    	} else {
	        var _endDate = ''; 
	        if(endDtVal.length > 10){
	        	_endDate = endDtVal.substring(0,10);
	        }else{
	        	_endDate = endDtVal;
	        }
	        $('#endDt').val(_endDate);    		
    	}
    	//팝업공지여부가 '팝업미사용'이면 팝업공지일자를 readonly 처리한다.
    	if ($("#popupYn").val() == 'N') {
    		$('#startDt').prop("readonly",true);
    		$('#endDt').prop("readonly",true);
    	} else {
    		$('#startDt').prop("readonly",false);
    		$('#endDt').prop("readonly",false);    		
    	}

	});

	Dropzone.options.myDropzone = {
			url : "/file/insert",
			params : {},
			addRemoveLinks : true,
			dictRemoveFile : 'remove',
			autoProcessQueue : true,
			maxFiles : 5, 
			uploadMultiple : true,
			parallelUploads : 20,
			//Mb 단위
			maxFilesize : 100,	
			method : 'post',
			acceptFiles : ".jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF,.ppt,.PPT,.pptx,.PPTX,.xls,.xlsx,.hwp,.HWP,.doc,.DOC,.docx,.DOCX,.zip,.ZIP,.rar,.RAR,.pdf,.PDF",
			clickable : true,
			//IE10 미만 버전
			fallback : function(){
				alert("지원하지 않는 브라우저 입니다.");
			},
			init : function (){
				var myDropzone = this;
				var sendBtn = $(".sendServer");
				/*
				sendBtn.addEventListener("click", function(){
					this.processQueue();
				});
				*/
				/*
				var data = JSON.parse('${fileJsonList}');
				
				console.log("data : " , data.length);
				
				if(data.length != 0 && data != null){
					var existingFileCount = 0;
					$.each(data, function(index, value){
						console.log("index : ", index);
						console.log("value : ", value);
						console.log("value.fileId : ", value.fileId);
						var mockFile = {
							name : value.fileNm,
							size : value.fileSize
						};
						myDropzone.emit("addedfile", mockFile);

						myDropzone.emit("thumbnail", mockFile, "/images/icon_board_user.png");
					});
				}
				*/
				this.on("maxfilesexceeded",function(){
					alert("최대 업로드 파일 수는 5개 입니다.");
				});

				this.on("addedfile",function(file){
					if(parseFloat((file.size / (1024*1024)).toFixed(2)) >= 10){
						alert("대용량(10MB 이상) 파일은 180일후 자동 삭제됩니다.");
					}
				});

				this.on("removedfile",function(file){
					console.log("removefile : ", file.xhr);
					if(file.xhr != undefined){
						var text = file.xhr.responseText;
						var res = JSON.parse(text);
						console.log("res :", res);
						var fileId = res.fileId;
						$(".fileIds").each(function(index, item){
							if(fileId == $(this).val()){
								$(this).remove();
							}
			            });
					}
				});
				this.on("complete",function(data){
					console.log("complete files : ", this.getUploadingFiles().length);
//	 				if(this.getUploadingFiles().length === 0 && this.getQueuedFiles.length === 0){
						if(data.xhr != undefined){
							var text = data.xhr.responseText;
							console.log("text : ", text);
							var res = JSON.parse(text);
							var fileId = "";
							if(res.result == "success"){
								console.log("");
								var fileIds = res.fileIds;
								console.log("fileIds : ", fileIds);
								for(var i=0;i< fileIds.length;i++){	
									$("form[group='fileForm']").append("<input type='hidden' class='fileIds' name='fileIds' value='"+res.fileId+"' />");
								}
							}else{
								alert("이미지 업로드에 실패하였습니다.");
							}
						}
//	 				}
				});
			}
		}
</script>
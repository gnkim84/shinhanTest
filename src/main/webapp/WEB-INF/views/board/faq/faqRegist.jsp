<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div class="content_tit">
		<h1 class="content_h1">FAQ 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>게시판</li>
			<li>FAQ 관리</li>
		</ul>
	</div>

	<form name="faqForm" id="faqForm" method="post" group="fileForm">
	<input type="hidden" name="faqId" id="faqId" value="${faqInfo.faqId}">
	<table class="tb02" summary="번호, 등록일, 질문, 분류, 게시여부, 내용, 첨부파일에 대한 내용입니다.">
		<caption class="hidden">FAQ 등록</caption>
		<colgroup>
			<col style="width:12.5%">
			<col style="width:37.5%">
			<col style="width:12.5%">
			<col style="width:37.5%">
		</colgroup>
		<tbody>
			<c:if test="${!empty faqInfo && null ne faqInfo.faqId}">
			<tr>
				<th scope="row">ID</th>
				<td class="tc">
                    <input type="text" class="input tc" name="faqId" id="faqId" <c:if test="${not empty faqInfo.faqId}">value="${faqInfo.faqId}"</c:if> disabled />
                </td>
				<th scope="row">최종수정일</th>
				<td class="tc">
                    <input type="text" class="input tc" <c:if test="${not empty faqInfo.modiDt}">value="<spring:eval expression='faqInfo.modiDt'/>"</c:if> disabled>
                </td>
			</tr>
			</c:if>
			<tr>
				<th scope="row"><label for="qstn">질문</label></th>
				<td colspan="3"><input type="text" class="input" id="qstn" name="qstn" value="<c:out value='${faqInfo.qstn}' />" maxLength="50"/></td>
			</tr>
			<tr>
				<th scope="row"><label for="clCode">분류</label></th>
				<td>
					<div class="select">
						<select name="clCode" id="clCode" class="tc">
                            <c:forEach var="faqCateCd" items="${codeFaqCateCdList}">
                                <option <c:if test="${faqInfo.clCode eq faqCateCd.codeId}">selected</c:if> value="<c:out value='${faqCateCd.codeId}'/>">${faqCateCd.codeNm}</option>
                            </c:forEach>						
						</select>
					</div>
				</td>
				<th scope="row"><label for="useYn">게시여부</label></th>
				<td>
					<div class="select">
						<select name="useYn" id="useYn" class="tc">
							<c:forEach var="useYn" items="${codeUseYnList}">
                                <option <c:if test="${faqInfo.useYn eq useYn.codeId}">selected</c:if> value="<c:out value='${useYn.codeId}'/>">${useYn.codeNm}</option>
                            </c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<th scope="row"><label for="answ">답변</label></th>
				<td colspan="3"><div class="textarea">
					<textarea name="answ" id="answ" class="summernote">${faqInfo.answ}</textarea>
				</div></td>
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
	<%-- 삭제를 게시여부 SELECT로 선택하여 수정로직으로 빠지게 되서 사용안함
	    <c:if test="${not empty faqInfo}">
               <button type="button" class="btn big blue" onclick="deleteInfo();">삭제</button>
           </c:if>
	 --%>
           <button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
		<button type="button" class="btn big grayb" onclick="location.href='/board/faq'">목록</button>
	</div>
</section>
<form id="frmFile" name="frmFile" action="/" method="post">
	<input type="hidden" name="fileUrl" value="">
</form>
<script type="text/javascript" src="/js/faqRegist.js"></script>
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
	});
	
var totalsize=0.0;
var MAX_TOTAL_SIZE = 100;
	Dropzone.options.myDropzone = {
		url : "/file/insert",
		params : {},
		addRemoveLinks : true,
		dictRemoveFile : 'remove',
		autoProcessQueue : true,
		maxFiles : 5, 
		uploadMultiple : false,
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
		//토탈 사이즈 제한.
// 		accept: function(file, done){
// 			console.log("accept : ",totalsize," : ", MAX_TOTAL_SIZE);
// 			if(totalsize >= MAX_TOTAL_SIZE){
// 				file.status = Dropzone.CANCLED;
// 				this._errorProcessing([file], "Max limit reached", null);
// 			}else{
// 				done();
// 			}
// 		},
		init : function (){
			var myDropzone = this;
			var sendBtn = $(".sendServer");
			this.on("maxfilesexceeded",function(){
				alert("최대 업로드 파일 수는 5개 입니다.");
			});

			this.on("addedfile",function(file){
				totalsize += parseFloat((file.size / (1024*1024)).toFixed(2));
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
							totalsize -= parseFloat((file.size / (1024*1024)).toFixed(2));
						}
		            });
				}
			});
			this.on("complete",function(data){
				console.log("complete files : ", this.getUploadingFiles().length);
// 				if(this.getUploadingFiles().length === 0 && this.getQueuedFiles.length === 0){
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
// 				}
			});
			this.on("error",function(file){
				totalsize -= parseFloat((file.size / (1024*1024)).toFixed(2));
			});
		}
	}
</script>

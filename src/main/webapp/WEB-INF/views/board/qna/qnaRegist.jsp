<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">
	<div class="content_tit">
		<h1 class="content_h1">Q&amp;A 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>게시판</li>
			<li>Q&amp;A 관리</li>
		</ul>
	</div>

	<form name="qnaForm" id="qnaForm" method="post" group="fileForm">
	<input type="hidden" name="qnaId" id="qnaId" value="${qnaId }" group="fileForm"/>
		<table class="tb02" summary="번호, 등록일, 제목, 게시여부, 중요여부, 내용, 첨부파일에 대한 내용입니다.">
			<caption class="hidden">Q&amp;A 등록</caption>
			<colgroup>
				<col style="width:12.5%">
				<col style="width:37.5%">
				<col style="width:12.5%">
				<col style="width:37.5%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">ID</th>
					<td class="tc">
					   ${qnaInfo.qnaId}
					</td>
					<th scope="row">등록일</th>
					<td class="tc">
					   <spring:eval expression='qnaInfo.rgstDt'/>
					</td>
				</tr>
				<tr>
					<th scope="row">작성자</th>
					<td>
						<img src="/file/photo/${qnaInfo.rgstPhotoUrl}" onerror="this.src='/images/icon_top_user.png'" alt="작성자사진" style="vertical-align:middle;margin:0 4px; width:27px;height:27px;" class="mr10">
						${qnaInfo.rgstNm} ${qnaInfo.rgstPstnNm} (${qnaInfo.rgstDeptNm})
					</td>				
					<th scope="row">분류</th>
					<td>
						<c:forEach var="codeList" items="${codeQnaCateCdList}">
							<c:if test="${codeList.codeId eq qnaInfo.clCode}">${codeList.codeNm}</c:if>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="sj">제목</label></th>
					<td colspan="3">
						${qnaInfo.sj }
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="cn">내용</label></th>
					<td colspan="3" style="max-width: 1200px;">
						<c:out value="${qnaInfo.cn }" escapeXml="false"/>
					</td>
				</tr>
				<%-- 
				<tr>
					<th scope="row">첨부파일</th>
					<td colspan="3">
						<ul class="attach attach_list">
							<c:forEach var="file" items="${qFileList }" varStatus="status">
								<li style="margin-top:5px;">
									<a href="#none" url="/file/detail/${file.fileUrl }">${file.fileNm }</a>
									&nbsp;&nbsp;<img src="/images/icon_close.png" url="/file/delete/${file.fileUrl }" fileId="${file.fileId }" style="cursor:pointer;background:#0055b4;" alt="삭제"/>
								</li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				--%>
				<tr>
					<th scope="row">답변상태</th>
					<td>
						<div class="select">
							<select name="qnaStat" id="qnaStat" class="tc">
	                            <c:forEach var="qnaStatCd" items="${codeQnaStatCdList}">
	                                <option <c:if test="${qnaInfo.qnaStat eq qnaStatCd.codeId}">selected</c:if> value="<c:out value='${qnaStatCd.codeId}'/>">${qnaStatCd.codeNm}</option>
	                            </c:forEach>						
							</select>
						</div>					
					</td>				
					<th scope="row"><label for="openYn">공개여부</label></th>
					<td>
						<div class="select">
							<select name="openYn" id="openYn" class="tc">
	                            <c:forEach var="openYnCd" items="${openYnCdList}">
	                                <option <c:if test="${qnaInfo.openYn eq openYnCd.codeId}">selected</c:if> value="<c:out value='${openYnCd.codeId}'/>">${openYnCd.codeNm}</option>
	                            </c:forEach>						
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row"><label for="useYn">게시여부</label></th>
					<td>
						<div class="select">
							<select name="useYn" id="useYn" class="tc">
								<c:forEach var="useYn" items="${codeUseYnList}">
	                                <option <c:if test="${qnaInfo.useYn eq useYn.codeId}">selected</c:if> value="<c:out value='${useYn.codeId}'/>">${useYn.codeNm}</option>
	                            </c:forEach>
							</select>
						</div>
					</td>
					<td></td>
					<td></td>
				</tr>		
				<tr>
					<th scope="row"><label for="answ">답변</label></th>
					<td colspan="3">
						<div class="textarea">
							<textarea name="answ" id="answ" value="${qnaInfo.answ}" class="summernote" >${qnaInfo.answ}</textarea>
						</div>
					</td>
				</tr>
				<%-- 
				<tr>
					<th scope="row">첨부파일</th>
					<td colspan="3">
						<ul class="attach">
							<c:forEach var="file" items="${aFileList }" varStatus="status">
								<li style="margin-top:5px;">
									<a href="#none" url="/file/detail/${file.fileUrl }">${file.fileNm }</a>
									&nbsp;&nbsp;<img src="/images/icon_close.png" url="/file/delete/${file.fileUrl }" fileId="${file.fileId }" style="cursor:pointer;background:#0055b4;" alt="삭제"/>
								</li>
							</c:forEach>
						</ul>
					</td>
				</tr>
				--%>
			</tbody>
		</table>
		</form>
		
		<%-- 
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
		--%>
		
		<div class="btn_wrap tr">
			<%--<button type="button" class="btn big blue" onclick="deleteInfo();">삭제</button> 수정화면에서 게시여부(use_yn)을 같이 update하면서 사용안하게 됨--%>
			<button type="button" class="btn big blue" onclick="upsertInfo();">저장</button>
			<button type="button" class="btn big grayb" onclick="location.href='/board/qna'">목록</button>
		</div>
</section>
<form id="frmFile" name="frmFile" action="/" method="post">
	<input type="hidden" name="fileUrl" value="">
</form>
<script type="text/javascript" src="/js/qnaRegist.js"></script>
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

	Dropzone.options.myDropzone = {
		url : "/file/insert",
		params : {"fileCl" : "A"},
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
		}
	}
</script>
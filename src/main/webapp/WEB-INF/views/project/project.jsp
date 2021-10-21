<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<section class="content">

    <div class="content_tit">
        <h1 class="content_h1">프로젝트 관리</h1>
        <ul class="content_nav">
            <li>HOME</li>
            <li>분석 관리</li>
            <li>프로젝트 관리</li>
        </ul>
    </div>

    <form name="searchForm" id="searchForm" method="post">
        <input type="hidden" name="page" id="page" value="<c:out value='${pages.page}'/>" />
        <input type="hidden" name="sortType" id="sortType" >
        <input type="hidden" name="sortDirection" id="sortDirection" >
        <div class="select_wrap tr">
	        <fieldset>
	            <div class="select">
	                <legend class="hidden">프로젝트와 현황 선택</legend>
	                <label class="hidden" for="projectSe">프로젝트 종류 선택</label>
	                <select name="projectSe" id="projectSe">
	                    <option value="">전체</option>
                        <c:forEach var="codePrjSe" items="${codePrjSeList}">
                                <option value="${codePrjSe.codeId }">${codePrjSe.codeNm}</option>
                        </c:forEach>
	                </select>
	            </div>
	            <input type="button" value="검색" onclick="search();" class="btn small gray">
	            
	            <div class="select">
	                <label class="hidden" for="projectStat">현황 선택</label>
	                <select name="projectStat" id="projectStat">
	                    <option value="">전체</option>
                        <c:forEach var="codePrjStat" items="${codePrjStatList}">
                                <option value="${codePrjStat.codeId }">${codePrjStat.codeNm}</option>
                        </c:forEach>
	                </select>
	            </div>
	            <input type="button" value="검색" onclick="search();" class="btn small gray">
	        </fieldset>
        </div>

    <div class="tb01_line"></div>
    <table class="tb01" summary="첫째줄은 순차적인 게시물 번호이며, 다른 칼럼들은 프로젝트 ID, 프로젝트명, 프로젝트 유형, 상태, 프로젝트 리더, 프로젝트 참여자, 기간, 최근접속일에 대한 내용입니다.">
        <caption class="hidden">프로젝트 관리 목록</caption>
        <colgroup>
            <col style="width:6%">
            <col style="width:10%">
            <col style="width:10%">
            <col style="width:15%">
            <col style="width:15%">
            <col style="width:14%">
            <col style="width:16%">
            <col style="width:16%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col">No</th>
            <th scope="col">
                프로젝트 ID
                <div class="tb_btn">
                    <button type="button" class="btn_up" onclick="searchSort('project_id', 'ASC');">행번/사번 오름차순</button>
                    <button type="button" class="btn_down" onclick="searchSort('project_id', 'DESC');">행번/사번 내림차순</button>
                </div>
            </th>
            <th scope="col">프로젝트명</th>
            <th scope="col">프로젝트 유형</th>
            <th scope="col">상태</th>
            <th scope="col">프로젝트 리더</th>
            <th scope="col">프로젝트 참여자</th>
            <th scope="col">기간</th>
<%--             <th scope="col"> --%>
<%--                 최근접속일 --%>
<%--                 <div class="tb_btn"> --%>
<%--                     <button type="button" class="btn_up" onclick="searchSort('modi_dt', 'ASC');">사용기간 오름차순</button> --%>
<%--                     <button type="button" class="btn_down" onclick="searchSort('modi_dt', 'DESC');">사용기간 내림차순</button> --%>
<%--                 </div> --%>
<%--             </th> --%>
<%--            <th scope="col">변경구분</th> --%>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty projectList || projectList.size() eq 0 }">
            <tr>
                <td scope="row" colspan="10">검색 결과가 없습니다.</td>
            </tr>
        </c:if>
        <c:forEach var="project" items="${projectList }" begin="0" end="${projectList.size() }" varStatus="status">
	        <tr onclick="location.href='/project/project/modify/${project.projectId}'" style="cursor: pointer">
	            <td scope="row">${projectCriteria.totalCount - (status.index + (pages.page -1) * pages.pageSize)}</td>
                <td>
<%--                     <a href="/project/project/modify/${project.projectId}" style="text-align: center"> --%>
                        ${project.projectId }
<%--                     </a> --%>
                </td>
                <td>${project.projectNm }</td>
                <td>
                    <c:forEach var="codePrjType" items="${codePrjTypeList}">
                        <c:if test='${project.projectTy eq codePrjType.codeId}'>                        
                            <c:out value="${codePrjType.codeNm}"/>
                        </c:if> 
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="codePrjStat" items="${codePrjStatList}">
                        <c:if test='${project.projectStat eq codePrjStat.codeId}'>                        
                            <c:out value="${codePrjStat.codeNm}"/>
                        </c:if> 
                    </c:forEach>
                </td>
<%--                 <td> --%>
<%--                     <c:forEach var="codePrjSe" items="${codePrjSeList}"> --%>
<%--                         <c:if test='${project.projectSe eq codePrjSe.codeId}'> --%>
<%--                             <c:out value="${codePrjSe.codeNm}"/> --%>
<%--                         </c:if>  --%>
<%--                     </c:forEach> --%>
<%--                 </td> --%>
                <td>
                    <c:forEach var="mgr" items="${project.mgrInfo.optJSONArray('list').toList()}" varStatus="status">
                        ${mgr.userNm }<c:if test="${not status.last }">, </c:if>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="pcpt" items="${project.pcptInfo.optJSONArray('list').toList()}" varStatus="status">
                        ${pcpt.userNm }<c:if test="${not status.last }">, </c:if>
                    </c:forEach>
                </td>
                <td>
                    <spring:eval expression="project.startDt"/>
                     ~ <spring:eval expression="project.endDt"/>
                </td>
<%--                 <td>${project.projectDsc }</td> --%>
<%--                 <td>${project.ordSeq }</td> --%>
<%--                 <td>${project.useYn }</td> --%>
<%--                 <td>${project.rgstId }</td> --%>
<%--                 <td>${project.rgstDt }</td> --%>
<%--                 <td>${project.modiId }</td> --%>
<%--                 <td>${project.modiDt }</td> --%>
<%--                 <td>${project.ver }</td> --%>
<%--                 <td>-최근접속일-</td> --%>
<%--                <td> --%>
<%--                    <button type="button" class="btn small blue" onclick="modify('${project.projectId}');">변경</button> --%>
<%--                    <button type="button" class="btn small gray" onclick="alert('서비스 예정입니다.')">신청철회</button> --%>
<%--                </td> --%>
	        </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="btn_wrap tr">
    	<%-- 향후 기계학습 카테고리 사용시 원상복구 시켜야함 - 20210214
        <button type="button" class="btn big blue" onclick="location.href='/project/project/regist'">등록</button>
        --%>
    </div>

        <div class="page"></div>

        <div class="search_wrap">
		    <fieldset>
		        <legend class="hidden">사용자 목록 게시물 검색</legend>
		
		        <div class="select">
	                <label class="hidden" for="pageSize">게시물 갯수</label>
	                <select name="pageSize" id="pageSize">
		                <option value="10">10건</option>
		                <option value="20">20건</option>
		                <option value="30">30건</option>
		            </select>
		        </div>
		
		        <div class="select">
	                <label class="hidden" for="searchKey">검색 항목</label>
	                <select name="searchKey" id="searchKey">
		                <option value="ALL">전체</option>
		                <option value="projectNm">프로젝트명</option>
		                <option value="projectDsc">프로젝트설명</option>
		            </select>
		        </div>
		
		        <label class="hidden" for="searchValue">검색어</label>
		        <input type="text" class="input" id="searchValue" name="searchValue" placeholder="검색어를 입력하세요.">
		        <input type="submit" value="검색" onclick="search();" class="btn small grays">
		    </fieldset>
	    </div>
	    
	    <%--테스트 코드 --%>
<%--
        def 값 변경 예정
        구분은 화면에 설정 추가 예정 
--%>
	    
    </form>
</section>
<script src="/js/jquery.bootpag.js"></script>
<script type="text/javascript">
    $(function() {
        
        $(document).ready(function() {
            $('#projectTy').val('${pages.projectTy}');
            $('#projectSe').val('${pages.projectSe}');
            $('#projectStat').val('${pages.projectStat}');
            $('#pageSize').val('${pages.pageSize}');
            $('#searchKey').val('${pages.searchKey}');
            $('#searchValue').val('${pages.searchValue}');
        });
        
        /**
         * 페이징 처리 공통 함수
         */
        $('.page').bootpag({        // 페이징을 표시할 div의 클래스
            total: ${pages.totalPage},  // 페이징모델의 전체페이지수
            page: ${pages.page},        // 페이징모델의 현재페이지번호
            maxVisible: 10,  // 보여질 최대 페이지수
            firstLastUse: true,             // first와 last사용유무
            wrapClass: 'page',              // 페이징을 감싼 클래스명
            activeClass: 'on',              // 현재페이지의 클래스명
            disabledClass: 'disabled',      // 각 항목별 클래스 지정
            nextClass: 'next',
            prevClass: 'prev',
            lastClass: 'last',
            firstClass: 'first'
        }).on("page", function(event, num){
            $("#page").val(num);
            $("#searchForm").submit();
        });

    });
    
    function search() {
        $('#page').val(1);
        $('#searchForm').submit();
    }

    function searchSort(type, direction) {
        $('#sortType').val(type);
        $('#sortDirection').val(direction);

        search();
    }
    
    function modify(projectId) {
        $('#searchForm')
        .attr('action', '/project/project/modify/' + projectId)
        .attr('method', 'get')
        .submit();
    }
</script>

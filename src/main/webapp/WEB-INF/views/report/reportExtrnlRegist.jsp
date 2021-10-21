<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<section class="content">

	<div class="content_tit">
		<h1 class="content_h1">외부 보고서 배포 관리</h1>
		<ul class="content_nav">
			<li>HOME</li>
			<li>분석 관리</li>
			<li>외부 보고서 배포 관리</li>
		</ul>
	</div>

	<form>
		<table class="tb02"
			summary="번호, 소유자, 요청일, 활성화, 권한부여일, 보고서형태, 보고서명, URL, 비고, 권한부여대상자, 요청시스템, 호출 ID, 호출 API, 비고에 대한 내용입니다.">
			<caption class="hidden">외부 보고서 배포 관리 목록</caption>
			<colgroup>
				<col style="width: 12.5%">
				<col style="width: 37.5%">
				<col style="width: 12.5%">
				<col style="width: 37.5%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">번호</th>
					<td class="tc">111</td>
					<th>소유자</th>
					<td class="tc">디지털팀 홍길동</td>
				</tr>
				<tr>
					<th scope="row">요청일</th>
					<td class="tc">2020.10.13</td>
					<th><label for="data01">활성화</label></th>
					<td>
						<div class="select">
							<select name="data01" id="data01" class="tc">
								<option value="활성">활성</option>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">권한부여일</th>
					<td class="tc">2020.10.13</td>
					<th>보고서 형태</th>
					<td>
						<div class="radio_wrap">
							<div class="radio">
								<input type="radio" name="data02" id="data02_1" checked=""><label
									for="data02_1"><span class="icon"><span></span></span><span
									class="txt">그룹</span></label>
							</div>
							<div class="radio">
								<input type="radio" name="data02" id="data02_2"><label
									for="data02_2"><span class="icon"><span></span></span><span
									class="txt">부서</span></label>
							</div>
							<div class="radio">
								<input type="radio" name="data02" id="data02_3"><label
									for="data02_3"><span class="icon"><span></span></span><span
									class="txt">개인</span></label>
							</div>
							<div class="radio">
								<input type="radio" name="data02" id="data02_4"><label
									for="data02_4"><span class="icon"><span></span></span><span
									class="txt">기계학습</span></label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">보고서명</th>
					<td colspan="3">
						<div class="tb_fb_wrap">
							대시보드명
							<button type="button" class="btn small grayr wide">보고서
								상세정보</button>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">보고서명</th>
					<td colspan="3">
						<div class="tb_fb_wrap">
							https://www.bigdataplat.com/?idx=111
							<button type="button" class="btn small grayr wide">미리보기</button>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">비고</th>
					<td colspan="3" class="tc">신한금융투자 분석 보고서</td>
				</tr>
				<tr>
					<th scope="row">권한부여 대상자</th>
					<td class="tc">EveryOne <span class="opa">(외부요청시
							EveryOne으로 변경)</span>
					</td>
					<th><label for="data03">요청시스템</label></th>
					<td>
						<div class="select">
							<select name="data03" id="data03" class="tc">
								<option value="요청시스템">요청시스템</option>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<th scope="row">호출 ID</th>
					<td class="tc">REP 1</td>
					<th>호출 API</th>
					<td class="tc">https://portal.shinhan.com/REP1</td>
				</tr>
				<tr>
					<th scope="row"><label for="data04">비고</label></th>
					<td colspan="3"><div class="textarea">
							<textarea id="data04">비고 내용</textarea>
						</div></td>
				</tr>
			</tbody>
		</table>

		<div class="btn_wrap tr">
			<button type="submit" class="btn big blue">저장</button>
			<button type="button" class="btn big grayb">취소</button>
		</div>
	</form>

</section>
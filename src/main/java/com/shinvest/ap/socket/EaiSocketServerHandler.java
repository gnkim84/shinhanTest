package com.shinvest.ap.socket;

import java.nio.ByteBuffer;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.config.props.EaiSocketProps;
import com.shinvest.ap.socket.service.EaiSocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EaiSocketServerHandler {

	@Resource(name="eaiSocketProps")
	private EaiSocketProps props;
	
	@Resource(name="commonUtil")
	private CommonUtil commonUtil;
	
	@Resource(name="idUtil")
	private IdUtil idUtil;
	
	@Resource(name="eaiSocketService")
	private EaiSocketService service;
	
	private final String DEFAULT_PAD = " ";
	
	public byte[] handler(String message, MessageHeaders headers) {
		byte[] result = null;
		JSONObject recvData = null;
		JSONObject sendData = null;
		try {
			// 골드윙 -> EAI 요청 메시지
			recvData = getRecvData(message);
			log.debug("socket server recvData : {}",recvData.toString());
		} catch (Exception e) {
			log.warn("EAI Socket Server 수신 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		try {
			if (recvData != null) {
				// EAI -> 골드윙 응답 메시지
				sendData = getSendData(recvData);
			} else {
				sendData = new JSONObject();
				service.setErrorMsg(sendData);
			}
			log.debug("socket server sendData : {}",sendData.toString());
			result = getSendBytes(sendData);
		} catch (Exception e) {
			log.warn("EAI Socket Server 송신 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * EAI 소켓 수신 전문
	 */
	public JSONObject getRecvData(String recvMsg) throws Exception {
		JSONObject result = new JSONObject();
		//표준 전문 헤더(270)
		// STND_COM_LENG               / LENGTH: 8    / OFFSET: 0    / 표준전문길이
		// COM_VER_TP_CODE             / LENGTH: 2    / OFFSET: 8    / 전문버전구분코드
		// COM_GEN_YMD                 / LENGTH: 6    / OFFSET: 10   / 글로벌ID(GBL_ID) : 전문생성일자
		// COM_GEN_SYSTEM_NM           / LENGTH: 8    / OFFSET: 16   / 글로벌ID(GBL_ID) : 전문생성시스템명
		// STND_COM_SEQ                / LENGTH: 16   / OFFSET: 24   / 글로벌ID(GBL_ID) : 표준전문순번
		// STND_COM_PRGS_NO            / LENGTH: 2    / OFFSET: 40   / 글로벌ID(GBL_ID) : 표준전문진행번호
		// IPV6_ADDR                   / LENGTH: 32   / OFFSET: 42   / 최초송신 시스템 정보(FST_SND_SYS_INFO) : IPV6주소
		// COM_MAC_ADDR                / LENGTH: 12   / OFFSET: 74   / 최초송신 시스템 정보(FST_SND_SYS_INFO) : 전문MAC주소
		// XA_TRX_TP_CODE              / LENGTH: 1    / OFFSET: 86   / 전문처리 정보(COM_PROC_INFO) : XA트랜잭션구분코드
		// RQST_RSP_TP_CODE            / LENGTH: 1    / OFFSET: 87   / 전문처리 정보(COM_PROC_INFO) : 요구응답구분코드
		// TRX_SYNC_TP_CODE            / LENGTH: 1    / OFFSET: 88   / 전문처리 정보(COM_PROC_INFO) : 트랜잭션동기화구분코드
		// RECV_SVC_CODE               / LENGTH: 9    / OFFSET: 89   / 서비스코드 정보(SVC_CD_INFO) : 수신서비스코드
		// RSLT_RECV_SVC_CODE          / LENGTH: 9    / OFFSET: 98   / 서비스코드 정보(SVC_CD_INFO) : 결과수신서비스코드
		// EAI_INTF_ID                 / LENGTH: 12   / OFFSET: 107  / 서비스코드 정보(SVC_CD_INFO) : EAI인터페이스ID
		// PROC_RSLT_TP_CODE           / LENGTH: 1    / OFFSET: 119  / 응답결과 정보(RQST_RSLT_INFO) : 처리결과구분코드
		// OPRT_COM_TP_CODE            / LENGTH: 1    / OFFSET: 120  / 응답결과 정보(RQST_RSLT_INFO) : 출력전문구분코드
		// COM_CONT_SEQ                / LENGTH: 2    / OFFSET: 121  / 응답결과 정보(RQST_RSLT_INFO) : 전문연속순번
		// CONT_TRX_KEY                / LENGTH: 50   / OFFSET: 123  / 응답결과 정보(RQST_RSLT_INFO) : 연속트랜잭션키
		// TRBL_SYSTEM_NM              / LENGTH: 8    / OFFSET: 173  / 응답결과 정보(RQST_RSLT_INFO) : 장애시스템명
		// LANG_TP_CODE                / LENGTH: 2    / OFFSET: 181  / 기타정보(ETC_INFO) : 언어구분코드
		// CH_TP_CODE                  / LENGTH: 2    / OFFSET: 183  / 기타정보(ETC_INFO) : 채널구분코드
		// AUCT_INCL_YN                / LENGTH: 1    / OFFSET: 185  / 기타정보(ETC_INFO) : 공인인증 포함여부
		// CUST_IDNT_TP_CODE           / LENGTH: 1    / OFFSET: 186  / 기타정보(ETC_INFO) : 고객식별구분코드
		// CUST_IDNT_CNTT              / LENGTH: 21   / OFFSET: 187  / 기타정보(ETC_INFO) : 고객식별내용
		// USER_ID                     / LENGTH: 12   / OFFSET: 208  / 기타정보(ETC_INFO) : 사용자ID
		// SCRN_ID                     / LENGTH: 5    / OFFSET: 220  / 기타정보(ETC_INFO) : 화면ID
		// VISOR_TP_CODE               / LENGTH: 1    / OFFSET: 225  / 기타정보(ETC_INFO) : 관리자구분코드
		// REQST_ITMN                  / LENGTH: 6    / OFFSET: 226  / 기타정보(ETC_INFO) : 요청건수
		// RELT_YN                     / LENGTH: 1    / OFFSET: 232  / 기타정보(ETC_INFO) : 연동여부
		// CMBN_VRFY_YN                / LENGTH: 1    / OFFSET: 233  / 기타정보(ETC_INFO) : 통합인증여부
		// STND_COM_USER_DEFN_FLD      / LENGTH: 10   / OFFSET: 234  / 기타정보(ETC_INFO) : 표준전문사용자정의필드
		// CH_ENCD_YN                  / LENGTH: 1    / OFFSET: 244  / 기타정보(ETC_INFO) : 채널암호화여부
		// CH_OWENC_YN                 / LENGTH: 1    / OFFSET: 245  / 기타정보(ETC_INFO) : 채널단방향암호화여부
		// DINFO_INCL_YN               / LENGTH: 1    / OFFSET: 246  / 기타정보(ETC_INFO) : 단말정보포함여부
		// ACCT_PWD_INP_METHOD_TP_CODE / LENGTH: 1    / OFFSET: 247  / 기타정보(ETC_INFO) : 계좌비밀번호 입력수단구분코드
		// INFO_CHG_CNFM_TP_CODE       / LENGTH: 1    / OFFSET: 248  / 기타정보(ETC_INFO) : 정보변경확인구분코드
		// USER_DEFN_VERF_TP_CODE      / LENGTH: 1    / OFFSET: 249  / 기타정보(ETC_INFO) : 사용자정의검증구분코드
		// USER_DEFN_VERF_CNTT         / LENGTH: 10   / OFFSET: 250  / 기타정보(ETC_INFO) : 사용자정의검증내용
		// FILLER                      / LENGTH: 10   / OFFSET: 260  / Filler
		//데이터부
		// APRTYPE      / LENGTH: 12   / OFFSET: 270  / 문서상태코드
		    // DRAFTINIT (최초 기안문을 작성하기 위해서 호출 시)
		    // DRAFTSTART (최초 기안자 결재 시)
		    // DRAFTPROC (중간 결재선 결재 시)
		    // DRAFTEND (기안부서 결재 완료 시) - 최종완료
		    // BANSONG (기안부서 반송 시)
		    // SUSININIT (수신 호출 시)
		    // SUSINSTART (수신 접수자 기안 시)
		    // SUSINPROC (수신 중간 결재자 결재 시)
		    // SUSINEND (수신 결재 완료 시) - 최종완료
		    // SUSINBANSONG (수신부서 반송 시) - CLEAR 여부는 업무 시스템에서 판단 필요
		    // SUSINHESONG (수신부서 회송 시) - CLEAR 여부는 업무 시스템에서 판단 필요
		    // DRAFTDELETE (기안부서삭제)
		    // SUSINHESU (기안부서에서 접수문서 회수)
		    // SUSINCANCEL (수신부서접수취소)
		result.put(Constant.SocketServer.APRV_TYPE, new String(recvMsg.getBytes(props.getEncoding()), 270, 12, props.getEncoding()).trim());
		// CALLUSER     / LENGTH: 30   / OFFSET: 282  / 호출한 사용자ID(회사코드 + 사번)
		result.put(Constant.SocketServer.CALL_USER, new String(recvMsg.getBytes(props.getEncoding()), 282, 30, props.getEncoding()).trim());
		    // companyCode + userId
		// CALLCOMPANY  / LENGTH: 10   / OFFSET: 312  / 호출한 사용자 회사코드
		result.put(Constant.SocketServer.CALL_COMPANY, new String(recvMsg.getBytes(props.getEncoding()), 312, 10, props.getEncoding()).trim());
		    // companyCode
		// KEY          / LENGTH: 100  / OFFSET: 322  / 업무시스템의 고유키
		    // aprvId + "_" + Constant.AprvType
		String key = new String(recvMsg.getBytes(props.getEncoding()), 322, 100, props.getEncoding()).trim();
		String[] keySplit = key.split("_");
		result.put(Constant.SocketServer.APRV_ID, keySplit[0]);
		result.put(Constant.SocketServer.APRV_CL, keySplit[1]);
		// DOCID        / LENGTH: 20   / OFFSET: 422  / 전자결재 문서 ID
		result.put(Constant.SocketServer.DOC_ID, new String(recvMsg.getBytes(props.getEncoding()), 422, 20, props.getEncoding()).trim());
		// DOCNUM       / LENGTH: 100  / OFFSET: 442  / 전자결재 문서 번호
		result.put(Constant.SocketServer.DOC_NO, new String(recvMsg.getBytes(props.getEncoding()), 442, 100, props.getEncoding()).trim());
		// FORMID       / LENGTH: 20   / OFFSET: 542  / 전자결재 FORM ID
		result.put(Constant.SocketServer.FORM_ID, new String(recvMsg.getBytes(props.getEncoding()), 542, 20, props.getEncoding()).trim());
		// WRITERID     / LENGTH: 20   / OFFSET: 562  / 사번
		result.put(Constant.SocketServer.USER_ID, new String(recvMsg.getBytes(props.getEncoding()), 562, 20, props.getEncoding()).trim());
		// WRITERNM     / LENGTH: 50   / OFFSET: 582  / 이름
		result.put(Constant.SocketServer.USER_NM, new String(recvMsg.getBytes(props.getEncoding()), 582, 50, props.getEncoding()).trim());
		// WRITERDEPTID / LENGTH: 20   / OFFSET: 632  / 부서 CN
		result.put(Constant.SocketServer.DEPT_CODE, new String(recvMsg.getBytes(props.getEncoding()), 632, 20, props.getEncoding()).trim());
		// WRITERDEPTNM / LENGTH: 50   / OFFSET: 652  / 결재자 부서명
		result.put(Constant.SocketServer.DEPT_NM, new String(recvMsg.getBytes(props.getEncoding()), 672, 50, props.getEncoding()).trim());
		// APRINFO      / LENGTH: 3000 / OFFSET: 702  / 결재선정보
		    // 순번|결재상태|결재방법|결재일자|결재자|결재자명@~
		String aprvStat = Constant.SocketServer.STAT_APPLY;
		if (StringUtils.equalsAny("DRAFTINIT", result.optString(Constant.SocketServer.APRV_TYPE))) {
			//최초 요청은 결재선 정보 없음
		} else {
			String aprInfoString = new String(recvMsg.getBytes(props.getEncoding()), 702, 3000, props.getEncoding()).trim();
			JSONArray aprInfo = new JSONArray();
			String[] aprInfoSplit = aprInfoString.split("@");
			for (int i=0; i<aprInfoSplit.length; i++) {
				JSONObject obj = new JSONObject();
				String[] info = aprInfoSplit[i].split("\\|");
				
				obj.put(Constant.SocketServer.APRV_SEQ, info[0]);
				obj.put(Constant.SocketServer.APRV_STAT, info[1]);
				obj.put(Constant.SocketServer.APRV_SE, info[2]);
				if (StringUtils.isBlank(info[3])) {
					obj.put(Constant.SocketServer.APRV_DT, JSONObject.NULL);
				} else {
					obj.put(Constant.SocketServer.APRV_DT, info[3]);
				}
				obj.put(Constant.SocketServer.APRVR_ID, info[4]);
				obj.put(Constant.SocketServer.APRVR_NM, info[5]);
				obj.put(Constant.SocketServer.APRV_ID, result.optString(Constant.SocketServer.APRV_ID));
				obj.put(Constant.SocketServer.USER_ID, result.optString(Constant.SocketServer.USER_ID));
				
				aprInfo.put(obj);
				
				//최종 결재 상태
				// 결재선 중 골드윙 결재 상태 코드 004 반송, 006 회수 가 하나라도 있으면 R 반려(회수)
				// 마지막 결재선의 골드윙 결재 상태 코드 010 완료, 017 수신완료 면 C 승인완료
				// 그 외의 경우는 모두 A 결재중
				if (StringUtils.equals(Constant.SocketServer.STAT_APPLY, aprvStat) && StringUtils.equalsAny(obj.optString(Constant.SocketServer.APRV_STAT), "004","006")) {
					aprvStat = Constant.SocketServer.STAT_REJECT;
					result.put(Constant.SocketServer.APRVR_ID, obj.optString(Constant.SocketServer.APRVR_ID));
					if (StringUtils.isBlank(obj.optString(Constant.SocketServer.APRV_DT))) {
						result.put(Constant.SocketServer.APRV_DT, commonUtil.getDateString("yyyyMMdd"));
					} else {
						result.put(Constant.SocketServer.APRV_DT, obj.optString(Constant.SocketServer.APRV_DT));
					}
				}
				
				if (i == (aprInfoSplit.length-1)) {
					if (StringUtils.equals(Constant.SocketServer.STAT_APPLY, aprvStat) && StringUtils.equalsAny(obj.optString(Constant.SocketServer.APRV_STAT), "010","017")) {
						aprvStat = Constant.SocketServer.STAT_APRV;
						result.put(Constant.SocketServer.APRVR_ID, obj.optString(Constant.SocketServer.APRVR_ID));
						if (StringUtils.isBlank(obj.optString(Constant.SocketServer.APRV_DT))) {
							result.put(Constant.SocketServer.APRV_DT, commonUtil.getDateString("yyyyMMdd"));
						} else {
							result.put(Constant.SocketServer.APRV_DT, obj.optString(Constant.SocketServer.APRV_DT));
						}
					}
				}
			}
			result.put(Constant.SocketServer.APRV_DTL_INFO, aprInfo);
		}
		result.put(Constant.SocketServer.APRV_STAT, aprvStat);
		
		return result;
	}
	
	/**
	 * EAI 소켓 응답 전문 
	 * @return
	 * @throws Exception
	 */
	private byte[] getSendBytes(JSONObject params) throws Exception {
		byte[] byteArray = null;
		StringBuilder data = new StringBuilder();
		log.debug("######################## socket server getSendBytes()");
		//표준 전문 헤더(270)
		// STND_COM_LENG               / LENGTH: 8    / OFFSET: 0    / 표준전문길이
		    // 전문 구성 완료 후 처리 : byte 계산 시 미포함
		
		// COM_VER_TP_CODE             / LENGTH: 2    / OFFSET: 8    / 전문버전구분코드
		data.append("10");
		// COM_GEN_YMD                 / LENGTH: 6    / OFFSET: 10   / 글로벌ID(GBL_ID) : 전문생성일자 / YYMMDD
		data.append(commonUtil.getDateString("yyMMdd"));
		// COM_GEN_SYSTEM_NM           / LENGTH: 8    / OFFSET: 16   / 글로벌ID(GBL_ID) : 전문생성시스템명
		data.append("apapwl01");
		// STND_COM_SEQ                / LENGTH: 16   / OFFSET: 24   / 글로벌ID(GBL_ID) : 표준전문순번 / UNIQUE
		data.append(rpadByte(idUtil.getEaiSocketStndComSeq(),16,DEFAULT_PAD,props.getEncoding()));
		// STND_COM_PRGS_NO            / LENGTH: 2    / OFFSET: 40   / 글로벌ID(GBL_ID) : 표준전문진행번호
		data.append("00");
		// IPV6_ADDR                   / LENGTH: 32   / OFFSET: 42   / 최초송신 시스템 정보(FST_SND_SYS_INFO) : IPV6주소
		data.append(rpadByte(commonUtil.getServerIP(),32,DEFAULT_PAD,props.getEncoding()));
		// COM_MAC_ADDR                / LENGTH: 12   / OFFSET: 74   / 최초송신 시스템 정보(FST_SND_SYS_INFO) : 전문MAC주소
		data.append(rpadByte(commonUtil.getServerMac(),12,DEFAULT_PAD,props.getEncoding()));
		// XA_TRX_TP_CODE              / LENGTH: 1    / OFFSET: 86   / 전문처리 정보(COM_PROC_INFO) : XA트랜잭션구분코드
		data.append("0");
		// RQST_RSP_TP_CODE            / LENGTH: 1    / OFFSET: 87   / 전문처리 정보(COM_PROC_INFO) : 요구응답구분코드
		data.append("R");
		// TRX_SYNC_TP_CODE            / LENGTH: 1    / OFFSET: 88   / 전문처리 정보(COM_PROC_INFO) : 트랜잭션동기화구분코드
		data.append("S");
		// RECV_SVC_CODE               / LENGTH: 9    / OFFSET: 89   / 서비스코드 정보(SVC_CD_INFO) : 수신서비스코드
		data.append(rpadByte(null,9,DEFAULT_PAD,props.getEncoding()));
		// RSLT_RECV_SVC_CODE          / LENGTH: 9    / OFFSET: 98   / 서비스코드 정보(SVC_CD_INFO) : 결과수신서비스코드
		data.append(rpadByte(null,9,DEFAULT_PAD,props.getEncoding()));
		// EAI_INTF_ID                 / LENGTH: 12   / OFFSET: 107  / 서비스코드 정보(SVC_CD_INFO) : EAI인터페이스ID
		data.append("DAPGDWON0001");
		// PROC_RSLT_TP_CODE           / LENGTH: 1    / OFFSET: 119  / 응답결과 정보(RQST_RSLT_INFO) : 처리결과구분코드
		data.append(DEFAULT_PAD);
		// OPRT_COM_TP_CODE            / LENGTH: 1    / OFFSET: 120  / 응답결과 정보(RQST_RSLT_INFO) : 출력전문구분코드
		data.append("0");
		// COM_CONT_SEQ                / LENGTH: 2    / OFFSET: 121  / 응답결과 정보(RQST_RSLT_INFO) : 전문연속순번
		data.append("00");
		// CONT_TRX_KEY                / LENGTH: 50   / OFFSET: 123  / 응답결과 정보(RQST_RSLT_INFO) : 연속트랜잭션키
		data.append(rpadByte(null,50,DEFAULT_PAD,props.getEncoding()));
		// TRBL_SYSTEM_NM              / LENGTH: 8    / OFFSET: 173  / 응답결과 정보(RQST_RSLT_INFO) : 장애시스템명
		data.append(rpadByte(null,8,DEFAULT_PAD,props.getEncoding()));
		// LANG_TP_CODE                / LENGTH: 2    / OFFSET: 181  / 기타정보(ETC_INFO) : 언어구분코드
		data.append("KR");
		// CH_TP_CODE                  / LENGTH: 2    / OFFSET: 183  / 기타정보(ETC_INFO) : 채널구분코드
		data.append(rpadByte(null,2,DEFAULT_PAD,props.getEncoding()));
		// AUCT_INCL_YN                / LENGTH: 1    / OFFSET: 185  / 기타정보(ETC_INFO) : 공인인증 포함여부
		data.append("0");
		// CUST_IDNT_TP_CODE           / LENGTH: 1    / OFFSET: 186  / 기타정보(ETC_INFO) : 고객식별구분코드
		data.append(DEFAULT_PAD);
		// CUST_IDNT_CNTT              / LENGTH: 21   / OFFSET: 187  / 기타정보(ETC_INFO) : 고객식별내용
		data.append(rpadByte(null,21,DEFAULT_PAD,props.getEncoding()));
		// USER_ID                     / LENGTH: 12   / OFFSET: 208  / 기타정보(ETC_INFO) : 사용자ID
		data.append(rpadByte(params.optString(Constant.SocketServer.USER_ID),12,DEFAULT_PAD,props.getEncoding()));
		// SCRN_ID                     / LENGTH: 5    / OFFSET: 220  / 기타정보(ETC_INFO) : 화면ID
		data.append(rpadByte(null,5,DEFAULT_PAD,props.getEncoding()));
		// VISOR_TP_CODE               / LENGTH: 1    / OFFSET: 225  / 기타정보(ETC_INFO) : 관리자구분코드
		data.append("0");
		// REQST_ITMN                  / LENGTH: 6    / OFFSET: 226  / 기타정보(ETC_INFO) : 요청건수
		data.append("000000");
		// RELT_YN                     / LENGTH: 1    / OFFSET: 232  / 기타정보(ETC_INFO) : 연동여부
		data.append(DEFAULT_PAD);
		// CMBN_VRFY_YN                / LENGTH: 1    / OFFSET: 233  / 기타정보(ETC_INFO) : 통합인증여부
		data.append(DEFAULT_PAD);
		// STND_COM_USER_DEFN_FLD      / LENGTH: 10   / OFFSET: 234  / 기타정보(ETC_INFO) : 표준전문사용자정의필드
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));
		// CH_ENCD_YN                  / LENGTH: 1    / OFFSET: 244  / 기타정보(ETC_INFO) : 채널암호화여부
		data.append(DEFAULT_PAD);
		// CH_OWENC_YN                 / LENGTH: 1    / OFFSET: 245  / 기타정보(ETC_INFO) : 채널단방향암호화여부
		data.append(DEFAULT_PAD);
		// DINFO_INCL_YN               / LENGTH: 1    / OFFSET: 246  / 기타정보(ETC_INFO) : 단말정보포함여부
		data.append(DEFAULT_PAD);
		// ACCT_PWD_INP_METHOD_TP_CODE / LENGTH: 1    / OFFSET: 247  / 기타정보(ETC_INFO) : 계좌비밀번호 입력수단구분코드
		data.append(DEFAULT_PAD);
		// INFO_CHG_CNFM_TP_CODE       / LENGTH: 1    / OFFSET: 248  / 기타정보(ETC_INFO) : 정보변경확인구분코드
		data.append(DEFAULT_PAD);
		// USER_DEFN_VERF_TP_CODE      / LENGTH: 1    / OFFSET: 249  / 기타정보(ETC_INFO) : 사용자정의검증구분코드
		data.append(DEFAULT_PAD);
		// USER_DEFN_VERF_CNTT         / LENGTH: 10   / OFFSET: 250  / 기타정보(ETC_INFO) : 사용자정의검증내용
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));
		// FILLER                      / LENGTH: 10   / OFFSET: 260  / Filler
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));

		//표준응답 메시지 헤더부
		// MSG_CODE      / LENGTH: 5    / OFFSET: 270  / 메시지코드
		data.append(rpadByte(null,5,DEFAULT_PAD,props.getEncoding()));
		// MSG_CNTT      / LENGTH: 80   / OFFSET: 275  / 메시지내용
		data.append(rpadByte(null,80,DEFAULT_PAD,props.getEncoding()));
		// MSG_LEVL_CODE / LENGTH: 1    / OFFSET: 355  / 메시지수준코드
		data.append("P");
		// SVC_NM        / LENGTH: 12   / OFFSET: 356  / 서비스명
		data.append(rpadByte(null,12,DEFAULT_PAD,props.getEncoding()));
		// FUNC_NM       / LENGTH: 16   / OFFSET: 368  / 함수명
		data.append(rpadByte(null,16,DEFAULT_PAD,props.getEncoding()));
		// LINE_NO       / LENGTH: 5    / OFFSET: 384  / 라인번호
		data.append("00000");
		// MSG_COLR_CODE / LENGTH: 1    / OFFSET: 389  / 메시지컬러코드
		data.append("0");
		// ANX_MSG_CNTT  / LENGTH: 80   / OFFSET: 390  / 부가메시지내용
		data.append(rpadByte(null,80,DEFAULT_PAD,props.getEncoding()));
		
		//데이터부
		// RESULT      / LENGTH: 10     / OFFSET: 470    / 성공 실패 여부 / "OK", "ERROR"
		data.append(rpadByte(params.optString(Constant.SocketServer.RESULT),10,DEFAULT_PAD,props.getEncoding()));
		// ERRORREASON / LENGTH: 200    / OFFSET: 480    / 실패 시 사유 / "전자결재 처리 중 오류가 발생하였습니다."
		data.append(rpadByte(params.optString(Constant.SocketServer.ERROR_REASON),200,DEFAULT_PAD,props.getEncoding()));
		// HTMLDATA    / LENGTH: 50000 / OFFSET: 680   / 전자결재문서내용 / 전자결재 내에 들어갈 HTML Body / DRAFTINIT 경우만 입력
		data.append(rpadByte(params.optString(Constant.SocketServer.HTML_DATA),50000,DEFAULT_PAD,props.getEncoding()));
		// SUBJECT     / LENGTH: 200    / OFFSET: 50680    / 전자결재제목 / 문서 최상단 제목 / DRAFTINIT 경우만 입력
		data.append(rpadByte(params.optString(Constant.SocketServer.TITLE),200,DEFAULT_PAD,props.getEncoding()));
		// SUBJECTSUB  / LENGTH: 200    / OFFSET: 50880    / 전자결재부제목 / 문서 내용 제목 / DRAFTINIT 경우만 입력
		data.append(rpadByte(params.optString(Constant.SocketServer.SUB_TITLE),200,DEFAULT_PAD,props.getEncoding()));
		// RCVDEPTID   / LENGTH: 20    / OFFSET: 51080   / 수신처 부서코드 / 문서 수신할 부서코드 / DRAFTINIT 경우만 입력
		data.append(rpadByte(params.optString(Constant.SocketServer.RCV_DEPT_CODE),20,DEFAULT_PAD,props.getEncoding()));
		// RCVDEPTNAME / LENGTH: 100    / OFFSET: 51100   / 수신처 부서명 / 문서 수신할 부서명 / DRAFTINIT 경우만 입력
		data.append(rpadByte(params.optString(Constant.SocketServer.RCV_DEPT_NM),100,DEFAULT_PAD,props.getEncoding()));

		// COM_END                     / LENGTH: 2   / OFFSET: 290 / 전문종료부
		data.append("ZZ");

		// STND_COM_LENG               / LENGTH: 8    / OFFSET: 0    / 표준전문길이
		data.insert(0, lpadByte(String.valueOf(data.toString().getBytes(props.getEncoding()).length),8,"0",props.getEncoding()));

		log.debug("socket server sendMsg : {}",data.toString());
		byteArray = data.toString().getBytes(props.getEncoding());

		return byteArray;
	}
	
	/**
	 * 응답 메시지 작성
	 * @param recvData
	 * @return
	 */
	public JSONObject getSendData(JSONObject recvData) {
		JSONObject result = null;
		if (StringUtils.equals("DRAFTINIT", recvData.optString(Constant.SocketServer.APRV_TYPE))) {
			//최초 요청
			result = getSendDataDraftInit(recvData);
		} else {
			result = getSendDataDraftProc(recvData);
		}
		return result;
	}
	
	/**
	 * 수신 메시지가 최초 결재 요청인 경우 : DRAFTINIT
	 * @param recvData
	 * @return
	 */
	public JSONObject getSendDataDraftInit(JSONObject recvData) {
		JSONObject result = null;
		// 결재 타입에 따라 HTML 생성
		switch (recvData.optString(Constant.SocketServer.APRV_CL)) {
		case Constant.GoldwingAprvType.REPORT_OPEN:
			//보고서 승인
			result = service.reportAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.REPORT_ROLE:
			//보고서 역할 승인 - 사용자 승인
			result = service.reportRoleAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.PROJECT:
			//프로젝트 승인
			result = service.projectAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.PROJECT_NIGHT_USE:
			//프로젝트 야간 사용 승인
			result = service.projectNightUseAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.AUTH_CHANGE:
			//사용자 권한 승인 - 일반사용자 -> 분석사용자
			result = service.userAuthAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.DATA_RESOURCE:
			//데이터 권한 승인
			result = service.dataAprvFirst(recvData);
			break;
		case Constant.GoldwingAprvType.MODEL_DEPLOYED:
			//모델 배포 승인
			result = service.modelDeployAprvFirst(recvData);
			break;
		default:
			break;
		}
		return result;
	}
	
	/**
	 * 수신 메시지가 최초 결재 요청이 아닌 경우 : DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param recvData
	 * @return
	 */
	public JSONObject getSendDataDraftProc(JSONObject recvData) {
		JSONObject result = null;
		// 결재 타입에 따라 응답 생성
		switch (recvData.optString(Constant.SocketServer.APRV_CL)) {
		case Constant.GoldwingAprvType.REPORT_OPEN:
			//보고서 승인
			result = service.reportAprv(recvData);
			break;
		case Constant.GoldwingAprvType.REPORT_ROLE:
			//보고서 역할 승인 - 사용자 승인
			result = service.reportRoleAprv(recvData);
			break;
		case Constant.GoldwingAprvType.PROJECT:
			//프로젝트 승인
			result = service.projectAprv(recvData);
			break;
		case Constant.GoldwingAprvType.PROJECT_NIGHT_USE:
			//프로젝트 야간 사용 승인
			result = service.projectNightUseAprv(recvData);
			break;
		case Constant.GoldwingAprvType.AUTH_CHANGE:
			//사용자 권한 승인 - 일반사용자 -> 분석사용자
			result = service.userAuthAprv(recvData);
			break;
		case Constant.GoldwingAprvType.DATA_RESOURCE:
			//데이터 권한 승인
			result = service.dataAprv(recvData);
			break;
		case Constant.GoldwingAprvType.MODEL_DEPLOYED:
			//모델 배포 승인
			result = service.modelDeployAprv(recvData);
			break;
		default:
			break;
		}
		return result;
	}
	
	/**
	 * 문자열 앞에 byte로 추가
	 * @return
	 * @throws Exception
	 */
	public String lpadByte(String str, int size, String pad, String encoding) throws Exception {
		String result = null;
		if (StringUtils.isEmpty(str)) {
			String tmp = rpadByte(str, size, new StringBuilder(pad).reverse().toString(), encoding);
			result = new StringBuilder(tmp).reverse().toString();
		} else {
			byte[] b = str.getBytes(encoding);
			if (b.length == size) {
				result = str;
			} else if (b.length < size) {
				String tmp = rpadByte(new StringBuilder(str).reverse().toString(), size, new StringBuilder(pad).reverse().toString(), encoding);
				result = new StringBuilder(tmp).reverse().toString();
			} else {
				result = new String(b, b.length-size, b.length, encoding);
			}
		}
		return result;
	}

	/**
	 * 문자열 뒤에 byte로 추가
	 * @return
	 * @throws Exception
	 */
	public String rpadByte(String str, int size, String pad, String encoding) throws Exception {
		String result = null;
		if (StringUtils.isEmpty(str)) {
			byte[] pb = pad.getBytes(encoding);
			ByteBuffer bb = ByteBuffer.allocate(size);
			while(bb.hasRemaining()) {
				int s = size - bb.position();
				if (s < pb.length) {
					for (int i=0; i<s; i++) {
						bb.put(pb[i]);
					}
				} else {
					bb.put(pad.getBytes(encoding));
				}
			}
			result = new String(bb.array(), 0, size, encoding);
		} else {
			byte[] b = str.getBytes(encoding);
			if (b.length == size) {
				result = str;
			} else if (b.length < size) {
				byte[] pb = pad.getBytes(encoding);
				ByteBuffer bb = ByteBuffer.allocate(size);
				bb.put(b);
				while(bb.hasRemaining()) {
					int s = size - bb.position();
					if (s < pb.length) {
						for (int i=0; i<s; i++) {
							bb.put(pb[i]);
						}
					} else {
						bb.put(pad.getBytes(encoding));
					}
				}
				result = new String(bb.array(), 0, size, encoding);
			} else {
				result = new String(b, 0, size, encoding);
			}
		}
		return result;
	}
}

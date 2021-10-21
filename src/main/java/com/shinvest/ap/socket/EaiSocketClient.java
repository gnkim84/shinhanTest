package com.shinvest.ap.socket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.config.props.EaiSocketProps;

import lombok.extern.slf4j.Slf4j;

/**
 * EAI 소켓 통신 클라이어트
 *     골드윙 결재를 위한 토큰 요청
 */
@Slf4j
@Component
public class EaiSocketClient {

	@Resource(name="eaiSocketProps")
	private EaiSocketProps props;
	
	@Resource(name="commonUtil")
	private CommonUtil commonUtil;
	
	@Resource(name="idUtil")
	private IdUtil idUtil;
	
	private final String DEFAULT_PAD = " ";
	
	/**
	 * EAI 소켓 통신 : 골드윙 SSO 토큰 요청
	 * @param String userId
	 * @param String companyCode
	 * @return
	 */
	public JSONObject requestToken(String userId, String companyCode) {
		JSONObject params = new JSONObject();
		params.put("userId", userId);
		params.put("companyCode", companyCode);
		return requestToken(params);
	}
	
	/**
	 * EAI 소켓 통신 : 골드윙 SSO 토큰 요청
	 * @param params 필수 JSONObject key : userId, companyCode
	 * @return
	 */
	public JSONObject requestToken(JSONObject params) {
		JSONObject result = new JSONObject();
		try {
			if (StringUtils.isAnyBlank(params.optString("userId"),params.optString("companyCode"))) {
				//필수 파라메터 누락
				result.put("result", "필수 파라메터 누락");
				result.put("token", StringUtils.EMPTY);
			} else {
				// 토큰 요청
				String recvMsg = request(params);
				log.debug("goldwing aprv token result : {}",recvMsg);
				// 토큰 추출
				if (StringUtils.isNotBlank(recvMsg)) {
					//EAI 자체 오류 메시지 보다 길면 골드윙 응답 메시지 포함으로 처리
					if (recvMsg.getBytes(props.getEncoding()).length > 472) {
						// new String(string.getByte("encoding"),offset,getSize,"encoding").trim();
						String ssoRecvMsg = new String(recvMsg.getBytes(props.getEncoding()), 470, 50, props.getEncoding()).trim();
						/* <RETURNVALUE>TOKEN</RETURNVALUE>
						 * ACCOUNT IS NULL (사용자 ID null 값인 경우 발생)
						 * 36자리의 GUID 형식의 토큰 값 리턴 (정상 값 반환)
						 * GET USER TOKEN DB ERROR (토큰 값 추출 둥 DB 에러 발생)
						 * USERS ARE NOT IDENTIFIED (시스템에 등록되지 않은 계정)
						 * CONNECT SERVER IS ACCESS DENIED (미등록 요청 서버 접근 시)
						 * GET USER TOKEN EXCEPTION (해당 함수 Exception 메시지)
						 * 2021.03.08. XML에서 50자 문자열로 변경
						*/
						if (StringUtils.startsWith(ssoRecvMsg, "<") && StringUtils.endsWith(ssoRecvMsg, ">")) {
							InputStream in = new ByteArrayInputStream(ssoRecvMsg.getBytes());
							DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
							f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
							DocumentBuilder b = f.newDocumentBuilder();
							Document d = b.parse(in);
							XPath x = XPathFactory.newInstance().newXPath();
							String token = (String) x.evaluate("//RETURNVALUE", d, XPathConstants.STRING);
							if (token.length() == 36) {
								result.put("result", "S");
								result.put("token", token);
							} else {
								if (StringUtils.equals(token, "ACCOUNT IS NULL")) {
									result.put("result", "사용자 ID 전달 실패");
								} else if (StringUtils.equals(token, "GET USER TOKEN DB ERROR")) {
									result.put("result", "골드윙 토큰 추출 중 DB 오류");
								} else if (StringUtils.equals(token, "USERS ARE NOT IDENTIFIED")) {
									result.put("result", "골드윙 시스템 미등록 계정");
								} else if (StringUtils.equals(token, "CONNECT SERVER IS ACCESS DENIED")) {
									result.put("result", "골드윙 미등록 서버 요청");
								} else if (StringUtils.equals(token, "GET USER TOKEN EXCEPTION")) {
									result.put("result", "골드윙 토큰 추출 중 Exception 발생");
								} else {
									result.put("result", "골드윙 SSO 토큰 요청 실패");
								}
								result.put("token", StringUtils.EMPTY);
							}
						} else {
							if (ssoRecvMsg.length() == 36) {
								result.put("result", "S");
								result.put("token", ssoRecvMsg);
							} else {
								if (StringUtils.equals(ssoRecvMsg, "ACCOUNT IS NULL")) {
									result.put("result", "사용자 ID 전달 실패");
								} else if (StringUtils.equals(ssoRecvMsg, "GET USER TOKEN DB ERROR")) {
									result.put("result", "골드윙 토큰 추출 중 DB 오류");
								} else if (StringUtils.equals(ssoRecvMsg, "USERS ARE NOT IDENTIFIED")) {
									result.put("result", "골드윙 시스템 미등록 계정");
								} else if (StringUtils.equals(ssoRecvMsg, "CONNECT SERVER IS ACCESS DENIED")) {
									result.put("result", "골드윙 미등록 서버 요청");
								} else if (StringUtils.equals(ssoRecvMsg, "GET USER TOKEN EXCEPTION")) {
									result.put("result", "골드윙 토큰 추출 중 Exception 발생");
								} else {
									result.put("result", "골드윙 SSO 토큰 요청 실패");
								}
								result.put("token", StringUtils.EMPTY);
							}
						}
					} else {
						//EAI 자체 오류 메시지
						String errMsg = new String(recvMsg.getBytes(props.getEncoding()), 275, 80, props.getEncoding()).trim();
						result.put("result", errMsg);
						result.put("token", StringUtils.EMPTY);
					}
				} else {
					result.put("result", "소켓 수신 메시지 없음");
					result.put("token", StringUtils.EMPTY);
				}
			}
		} catch(Exception e) {
			result.put("result", "골드윙 SSO 토큰 요청 중 오류");
			result.put("token", StringUtils.EMPTY);
			log.warn("EAI 소켓 통신 - 골드윙 SSO 토큰 요청 중 오류 발생");
			log.warn(e.getMessage());
		}
		return result;
	}
	
	/**
	 * EAI 소켓 연결 및 메시지 송수신
	 */
	private String request(JSONObject params) {
		String result = null;
		Socket socket = null;
		try {
			socket = new Socket();
			SocketAddress point = new InetSocketAddress(props.getHost(),props.getPort());
			socket.connect(point, 30000);
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			byte[] data = getSendBytes(params);
			out.write(data,0,data.length);
			out.flush();
			
			result = getRecvMsg(in);
		} catch (Exception e) {
			log.warn("EAI 소켓 통신 중 오류 발생");
			log.warn(e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.warn(e.getMessage());
				}
			}
		}
		return result;
	}
	
	/**
	 * EAI 소켓 수신 메시지 추출
	 * @param in
	 * @return
	 */
	private String getRecvMsg(DataInputStream in) {
		StringBuilder msg = new StringBuilder();
		
		byte[] b = new byte[8];
		int cnt = 0;
		int size = 0;
		try {
			while (in.read(b,0,b.length) != -1) {
				if (cnt == 0) {
					msg.append(new String(b));
					size = Integer.parseInt(new String(b));
					b = new byte[size];
				} else {
					msg.append(new String(b,props.getEncoding()));
					in.close();
					break;
				}
				cnt++;
			}
		} catch (IOException e) {
			log.warn("EAI 소켓 수신 메시지 추출 중 오류 발생");
			log.warn(e.getMessage());
		}
		return msg.toString();
	}
	
	/**
	 * EAI 소켓 요청 전문 생성
	 * @return
	 * @throws Exception
	 */
	private byte[] getSendBytes(JSONObject params) throws Exception {
		byte[] byteArray = null;
		StringBuilder data = new StringBuilder();
	
		// COM_VER_TP_CODE             / LENGTH: 2   / OFFSET: 8   / 전문버전구분코드
		data.append("10");
		// COM_GEN_YMD                 / LENGTH: 6   / OFFSET: 10  / 글로벌ID(GBL_ID) : 전문생성일자 / YYMMDD
		data.append(commonUtil.getDateString("yyMMdd"));
		// COM_GEN_SYSTEM_NM           / LENGTH: 8   / OFFSET: 16  / 글로벌ID(GBL_ID) : 전문생성시스템명
		data.append("apapwl01");
		// STND_COM_SEQ                / LENGTH: 16  / OFFSET: 24  / 글로벌ID(GBL_ID) : 표준전문순번 / UNIQUE
		data.append(rpadByte(idUtil.getEaiSocketStndComSeq(),16,DEFAULT_PAD,props.getEncoding()));
		// STND_COM_PRGS_NO            / LENGTH: 2   / OFFSET: 40  / 글로벌ID(GBL_ID) : 표준전문진행번호
		data.append("00");
		// IPV6_ADDR                   / LENGTH: 32  / OFFSET: 42  / 최초송신시스템정보(FST_SND_SYS_INFO) : IPV6주소
		data.append(rpadByte(commonUtil.getServerIP(),32,DEFAULT_PAD,props.getEncoding()));
		// COM_MAC_ADDR                / LENGTH: 12  / OFFSET: 74  / 최초송신시스템정보(FST_SND_SYS_INFO) : 전문MAC주소
		data.append(rpadByte(commonUtil.getServerMac(),12,DEFAULT_PAD,props.getEncoding()));
		// XA_TRX_TP_CODE              / LENGTH: 1   / OFFSET: 86  / 전문처리정보(COM_PROC_INFO) : XA트랜잭션구분코드
		data.append("0");
		// RQST_RSP_TP_CODE            / LENGTH: 1   / OFFSET: 87  / 전문처리정보(COM_PROC_INFO) : 요구응답구분코드
		data.append("S");
		// TRX_SYNC_TP_CODE            / LENGTH: 1   / OFFSET: 88  / 전문처리정보(COM_PROC_INFO) : 트랜잭션동기화구분코드
		data.append("S");
		// RECV_SVC_CODE               / LENGTH: 9   / OFFSET: 89  / 서비스코드정보(SVC_CD_INFO) : 수신서비스코드
		data.append(rpadByte(null,9,DEFAULT_PAD,props.getEncoding()));
		// RSLT_RECV_SVC_CODE          / LENGTH: 9   / OFFSET: 98  / 서비스코드정보(SVC_CD_INFO) : 결과수신서비스코드
		data.append(rpadByte(null,9,DEFAULT_PAD,props.getEncoding()));
		// EAI_INTF_ID                 / LENGTH: 12  / OFFSET: 107 / 서비스코드정보(SVC_CD_INFO) : EAI인터페이스ID
		data.append("DAPGDWON0001");
		// PROC_RSLT_TP_CODE           / LENGTH: 1   / OFFSET: 119 / 응답결과정보(RQST_RSLT_INFO) : 처리결과구분코드
		data.append(DEFAULT_PAD);
		// OPRT_COM_TP_CODE            / LENGTH: 1   / OFFSET: 120 / 응답결과정보(RQST_RSLT_INFO) : 출력전문구분코드
		data.append("0");
		// COM_CONT_SEQ                / LENGTH: 2   / OFFSET: 121 / 응답결과정보(RQST_RSLT_INFO) : 전문연속순번
		data.append("00");
		// CONT_TRX_KEY                / LENGTH: 50  / OFFSET: 123 / 응답결과정보(RQST_RSLT_INFO) : 연속트랜잭션키
		data.append(rpadByte(null,50,DEFAULT_PAD,props.getEncoding()));
		// TRBL_SYSTEM_NM              / LENGTH: 8   / OFFSET: 173 / 응답결과정보(RQST_RSLT_INFO) : 장애시스템명
		data.append(rpadByte(null,8,DEFAULT_PAD,props.getEncoding()));
		// LANG_TP_CODE                / LENGTH: 2   / OFFSET: 181 / 기타정보(ETC_INFO) : 언어구분코드
		data.append("KR");
		// CH_TP_CODE                  / LENGTH: 2   / OFFSET: 183 / 기타정보(ETC_INFO) : 채널구분코드
		data.append(rpadByte(null,2,DEFAULT_PAD,props.getEncoding()));
		// AUCT_INCL_YN                / LENGTH: 1   / OFFSET: 185 / 기타정보(ETC_INFO) : 공인인증 포함여부
		data.append("0");
		// CUST_IDNT_TP_CODE           / LENGTH: 1   / OFFSET: 186 / 기타정보(ETC_INFO) : 고객식별구분코드
		data.append(DEFAULT_PAD);
		// CUST_IDNT_CNTT              / LENGTH: 21  / OFFSET: 187 / 기타정보(ETC_INFO) : 고객식별내용
		data.append(rpadByte(null,21,DEFAULT_PAD,props.getEncoding()));
		// USER_ID                     / LENGTH: 12  / OFFSET: 208 / 기타정보(ETC_INFO) : 사용자ID
		data.append(rpadByte(params.optString("userId"),12,DEFAULT_PAD,props.getEncoding()));
		// SCRN_ID                     / LENGTH: 5   / OFFSET: 220 / 기타정보(ETC_INFO) : 화면ID
		data.append(rpadByte(null,5,DEFAULT_PAD,props.getEncoding()));
		// VISOR_TP_CODE               / LENGTH: 1   / OFFSET: 225 / 기타정보(ETC_INFO) : 관리자구분코드
		data.append("0");
		// REQST_ITMN                  / LENGTH: 6   / OFFSET: 226 / 기타정보(ETC_INFO) : 요청건수
		data.append("000000");
		// RELT_YN                     / LENGTH: 1   / OFFSET: 232 / 기타정보(ETC_INFO) : 연동여부
		data.append(DEFAULT_PAD);
		// CMBN_VRFY_YN                / LENGTH: 1   / OFFSET: 233 / 기타정보(ETC_INFO) : 통합인증여부
		data.append(DEFAULT_PAD);
		// STND_COM_USER_DEFN_FLD      / LENGTH: 10  / OFFSET: 234 / 기타정보(ETC_INFO) : 표준전문사용자정의필드
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));
		// CH_ENCD_YN                  / LENGTH: 1   / OFFSET: 244 / 기타정보(ETC_INFO) : 채널암호화여부
		data.append(DEFAULT_PAD);
		// CH_OWENC_YN                 / LENGTH: 1   / OFFSET: 245 / 기타정보(ETC_INFO) : 채널단방향암호화여부
		data.append(DEFAULT_PAD);
		// DINFO_INCL_YN               / LENGTH: 1   / OFFSET: 246 / 기타정보(ETC_INFO) : 단말정보포함여부
		data.append(DEFAULT_PAD);
		// ACCT_PWD_INP_METHOD_TP_CODE / LENGTH: 1   / OFFSET: 247 / 기타정보(ETC_INFO) : 계좌비밀번호입력수단구분코드
		data.append(DEFAULT_PAD);
		// INFO_CHG_CNFM_TP_CODE       / LENGTH: 1   / OFFSET: 248 / 기타정보(ETC_INFO) : 정보변경확인구분코드
		data.append(DEFAULT_PAD);
		// USER_DEFN_VERF_TP_CODE      / LENGTH: 1   / OFFSET: 249 / 기타정보(ETC_INFO) : 사용자정의검증구분코드
		data.append(DEFAULT_PAD);
		// USER_DEFN_VERF_CNTT         / LENGTH: 10  / OFFSET: 250 / 기타정보(ETC_INFO) : 사용자정의검증내용
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));
		// FILLER                      / LENGTH: 10  / OFFSET: 260 / Filler
		data.append(rpadByte(null,10,DEFAULT_PAD,props.getEncoding()));

		// SSO_MSG
		// USERACCOUNT                 / LENGTH: 10  / OFFSET: 270 / SSO메시지
		data.append(rpadByte(StringUtils.joinWith(null, params.optString("companyCode"),params.optString("userId")),10,DEFAULT_PAD,props.getEncoding()));
		// SERVICENAME                 / LENGTH: 10  / OFFSET: 280 / SSO메시지 : 서비스이름
		data.append(rpadByte("GS_SDAP",10,DEFAULT_PAD,props.getEncoding()));
		
		// COM_END                     / LENGTH: 2   / OFFSET: 290 / 전문종료부
		data.append("ZZ");

		// STND_COM_LENG               / LENGTH: 8   / OFFSET: 0   / 표준전문길이
		data.insert(0, lpadByte(String.valueOf(data.toString().getBytes(props.getEncoding()).length),8,"0",props.getEncoding()));

		byteArray = data.toString().getBytes(props.getEncoding());

		return byteArray;
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

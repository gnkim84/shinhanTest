package com.shinvest.ap.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.shinvest.ap.config.props.EaiSocketProps;

import lombok.extern.slf4j.Slf4j;

/**
 * EAI 소켓 쪽지 알림 통신 클라이어트
 */
@Slf4j
@Component
public class EaiSocketNotifyClient {
	
	@Resource(name="eaiSocketProps")
	private EaiSocketProps props;

	//쪽지 알림 전문 시작 문자 ASCII 02
	private final byte[] START = new String("\u0002").getBytes(StandardCharsets.US_ASCII);
	//쪽지 알림 전문 종료 문자 ASCII 03
	private final byte[] END = new String("\u0003").getBytes(StandardCharsets.US_ASCII);
	//쪽지 알림 전문 내 필드 구분자 ASCII 05
	private final byte[] SEPARATOR = new String("\u0005").getBytes(StandardCharsets.US_ASCII);
	
	/**
	 * 쪽지 알림 발송
	 * @param receiverIds 수신자 ID 목록 "," 구분
	 * @param senderId 발송자 ID
	 * @param senderNm 발송자 명
	 * @param message 메시지
	 * @param link 링크
	 */
	public void requestNotify(String receiverIds, String senderId, String senderNm, String message, String link) {
		request(receiverIds, senderId, senderNm, message, link);
	}
	
	/**
	 * 쪽지 알림 발송
	 * @param receiverIds 수신자 ID 목록
	 * @param senderId 발송자 ID
	 * @param senderNm 발송자 명
	 * @param message 메시지
	 * @param link 링크
	 */
	public void requestNotify(List<String> receiverIds, String senderId, String senderNm, String message, String link) {
		request(StringUtils.join(receiverIds,","), senderId, senderNm, message, link);
	}
	
	/**
	 * 쪽지 알림 발송
	 * @param receiverIds 수신자 ID 목록
	 * @param senderId 발송자 ID
	 * @param senderNm 발송자 명
	 * @param message 메시지
	 * @param link 링크
	 */
	public void requestNotify(String[] receiverIds, String senderId, String senderNm, String message, String link) {
		request(StringUtils.join(receiverIds,","), senderId, senderNm, message, link);
	}
	
	/**
	 * EAI 소켓 쪽지 알림 연결 및 메시지 송수신
	 */
	private String request(String receiverIds, String senderId, String senderNm, String message, String link) {
		String result = null;
		Socket socket = null;
		try {
			socket = new Socket();
			SocketAddress point = new InetSocketAddress(props.getHost(),props.getNotifyPort());
			socket.connect(point, 30000);
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			byte[] data = getSendBytes(receiverIds,senderId,senderNm,message,link,props.getEncoding());
			out.write(data,0,data.length);
			out.flush();
			
			result = getRecvMsg(in);
		} catch (Exception e) {
			log.warn("EAI 소켓 쪽지 알림 통신 중 오류 발생");
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
	 * EAI 소켓 쪽지 알림 수신 메시지 추출
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
			log.warn("EAI 소켓 쪽지 알림 수신 메시지 추출 중 오류 발생");
			log.warn(e.getMessage());
		}
		return msg.toString();
	}
	
	/**
	 * EAI 소켓 쪽지 알림 요청 전문 생성
	 * @return
	 * @throws Exception
	 */
	private byte[] getSendBytes(String receiverIds, String senderId, String senderNm, String message, String link, String encoding) throws Exception {
		int size = 0;
		//전문 시작
		size += START.length;
		//수신자 ID 목록
		byte[] rIds = receiverIds.getBytes(encoding);
		size += rIds.length;
		//필드 구분자
		size += SEPARATOR.length;
		//발신자 ID
		byte[] sId = senderId.getBytes(encoding);
		size += sId.length;
		//필드 구분자
		size += SEPARATOR.length;
		//발신자 명
		byte[] sNm = senderId.getBytes(encoding);
		size += sNm.length;
		//필드 구분자
		size += SEPARATOR.length;
		//메시지 5000byte 제한
		byte[] msg = message.getBytes(encoding);
		size += msg.length;
		//필드 구분자
		size += SEPARATOR.length;
		//링크
		byte[] lnk = null;
		if (StringUtils.isNotBlank(link)) {
			lnk = link.getBytes(encoding);
			size += lnk.length;
		}
		//전문 종료
		size += END.length;
		
		ByteBuffer result = ByteBuffer.allocate(size);
		result.put(START);
		result.put(rIds);
		result.put(SEPARATOR);
		result.put(sId);
		result.put(SEPARATOR);
		result.put(sNm);
		result.put(SEPARATOR);
		result.put(msg);
		result.put(SEPARATOR);
		if (lnk != null) {
			result.put(lnk);
		}
		result.put(END);
		return result.array();
	}
}

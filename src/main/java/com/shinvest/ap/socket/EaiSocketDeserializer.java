package com.shinvest.ap.socket;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.core.serializer.Deserializer;
import org.springframework.stereotype.Component;

import com.shinvest.ap.config.props.EaiSocketProps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EaiSocketDeserializer implements Deserializer<String> {

	@Resource(name = "eaiSocketProps")
	private EaiSocketProps props;

	@Override
	public String deserialize(InputStream inputStream) throws IOException {
		StringBuilder result = new StringBuilder();
		//전문 byte 길이 8자
		byte[] b = new byte[8];
		int cnt = 0;
		int size = 0;
		while (inputStream.read(b, 0, b.length) != -1) {
			if (cnt == 0) {
				result.append(new String(b));
				size = Integer.parseInt(new String(b));
				b = new byte[size];
			} else {
				result.append(new String(b, props.getEncoding()));
				inputStream.close();
				break;
			}
			cnt++;
		}
		log.debug("Socket Server - EAI 수신 전문 : {}",result.toString());
		return result.toString();
	}
}

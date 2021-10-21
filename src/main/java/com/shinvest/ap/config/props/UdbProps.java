package com.shinvest.ap.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * UDB 설정 정보
 */
@Data
@Component
@ConfigurationProperties(prefix = "udb-props")
public class UdbProps {
	private String channelType;
	private String hostName;
	private String prodCheck;
}

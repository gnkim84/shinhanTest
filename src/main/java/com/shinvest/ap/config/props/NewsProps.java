package com.shinvest.ap.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 외부 제공 데이터
 *     오픈 컨텐츠 부족으로 추가
 */
@Data
@Component
@ConfigurationProperties(prefix = "news-props")
public class NewsProps {

	private String encoding;
	private String newsPath;
	private String newsFile;
	private String researchPath;
	private String researchFile;
	private String rankingPath;
	private String onlineRankingFile;
	private String scatchinterestRankingFile;
	private String scatchstbdRankingFile;
}

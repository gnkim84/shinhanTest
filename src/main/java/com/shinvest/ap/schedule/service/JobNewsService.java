package com.shinvest.ap.schedule.service;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.ParseUtil;
import com.shinvest.ap.config.props.NewsProps;
import com.shinvest.ap.schedule.mapper.JobNewsMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobNewsService {

	@Resource(name = "newsProps")
	private NewsProps props;

	@Resource(name = "jobNewsMapper")
	private JobNewsMapper mapper;

	@Resource(name = "commonUtil")
	private CommonUtil util;

	@Resource(name = "parseUtil")
	private ParseUtil parseUtil;

	private final String[] HEADER_NEWS = { "rgstde", "rgsttm", "newsSrc", "newsSj", "newsCn" };
	private final String[] HEADER_RESEARCH = { "rgstde", "rgstSeq", "wrkSeq", "companyNm", "opende", "stbdCode", "sj", "userNm", "fileNm" };
	private final String[] HEADER_RANK = { "rankSeq", "rankNm", "viewCnt" };

	/**
	 * 최신 뉴스
	 * 
	 * @param msg
	 */
	@Transactional
	public void syncNews(JSONObject msg) throws Exception {
		// 파일이 있는 경우만 시작하고 종료 후 파일 삭제
		Path file = Paths.get(props.getNewsPath(), props.getNewsFile());
		if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
			List<Map<String, String>> data = parseUtil.parseCSV(file, HEADER_NEWS, '"', Constant.HR.SEPERATOR, props.getEncoding());
			for (Map<String, String> map : data) {
				map.put("newsCn", map.get("newsCn").replace("<", "&lt;").replace(">", "&gt;"));
				log.debug(map.toString());
			}
			Map<String, List<Map<String, String>>> insertData = new HashMap<>();
			insertData.put("data", data);
			mapper.deleteNews();
			mapper.insertNews(insertData);
			// 파일 삭제
			Files.delete(file);
		}
	}

	/**
	 * 리서치
	 * 
	 * @param msg
	 */
	@Transactional
	public void syncResearch(JSONObject msg) throws Exception {
		// 파일이 있는 경우만 시작하고 종료 후 파일 삭제
		Path file = Paths.get(props.getResearchPath(), props.getResearchFile());
		if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
			List<Map<String, String>> data = parseUtil.parseCSV(file, HEADER_RESEARCH, null, Constant.HR.SEPERATOR, props.getEncoding());
//			for (Map<String, String> map : data) {
//				for (String key : map.keySet()) {
//					log.debug("{} : {}", key, new String(map.get(key).getBytes("8859_1"), "EUC-KR"));
//				}
//			}
			Map<String, List<Map<String, String>>> insertData = new HashMap<>();
			insertData.put("data", data);
			mapper.deleteResearch();
			mapper.insertResearch(insertData);
			// 파일 삭제
			Files.delete(file);
		}
	}

	/**
	 * 온라인 랭킹
	 * 
	 * @param msg
	 */
	@Transactional
	public void syncRankOnline(JSONObject msg) throws Exception {
		// 파일이 있는 경우만 시작하고 종료 후 파일 삭제
		// online
		Path file = Paths.get(props.getRankingPath(), props.getOnlineRankingFile());
		if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
			List<Map<String, String>> data = parseUtil.parseCSV(file, HEADER_RANK, null, Constant.HR.SEPERATOR, props.getEncoding());
			for (Map<String, String> map : data) {
				map.put("rankCl", "ONLINE");
			}
			Map<String, List<Map<String, String>>> insertData = new HashMap<>();
			insertData.put("data", data);
			mapper.deleteRank("ONLINE");
			mapper.insertRank(insertData);
			// 파일 삭제
			Files.delete(file);
		}
	}

	/**
	 * S캐치 종목 랭킹
	 * 
	 * @param msg
	 */
	@Transactional
	public void syncRankStbd(JSONObject msg) throws Exception {
		// 파일이 있는 경우만 시작하고 종료 후 파일 삭제
		// scatch stbd
		Path file = Paths.get(props.getRankingPath(), props.getScatchstbdRankingFile());
		if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
			List<Map<String, String>> data = parseUtil.parseCSV(file, HEADER_RANK, null, Constant.HR.SEPERATOR, props.getEncoding());
			for (Map<String, String> map : data) {
				map.put("rankCl", "STBD");
			}
			Map<String, List<Map<String, String>>> insertData = new HashMap<>();
			insertData.put("data", data);
			mapper.deleteRank("STBD");
			mapper.insertRank(insertData);
			// 파일 삭제
			Files.delete(file);
		}
	}

	/**
	 * S캐치 관심 랭킹
	 * 
	 * @param msg
	 */
	@Transactional
	public void syncRankInterest(JSONObject msg) throws Exception {
		// 파일이 있는 경우만 시작하고 종료 후 파일 삭제
		// scatch interest
		Path file = Paths.get(props.getRankingPath(), props.getScatchinterestRankingFile());
		if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
			List<Map<String, String>> data = parseUtil.parseCSV(file, HEADER_RANK, null, Constant.HR.SEPERATOR, props.getEncoding());
			for (Map<String, String> map : data) {
				map.put("rankCl", "INTEREST");
			}
			Map<String, List<Map<String, String>>> insertData = new HashMap<>();
			insertData.put("data", data);
			mapper.deleteRank("INTEREST");
			mapper.insertRank(insertData);
			// 파일 삭제
			Files.delete(file);
		}
	}
}

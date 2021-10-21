package com.shinvest.ap.schedule.mapper;

import java.util.List;
import java.util.Map;

import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface JobNewsMapper {

	// 뉴스
	int deleteNews() throws Exception;
	int insertNews(Map<String,List<Map<String, String>>> data) throws Exception;
	
	// 리서치
	int deleteResearch() throws Exception;
	int insertResearch(Map<String,List<Map<String, String>>> data) throws Exception;
	
	// 랭킹
	int deleteRank(String rankCl) throws Exception;
	int insertRank(Map<String,List<Map<String, String>>> data) throws Exception;
	
}

package com.shinvest.ap.schedule.mapper;

import java.util.List;
import java.util.Map;

import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface JobSrchKwdMapper {

	List<Map<String,Object>> selectNewKwd();
	int deleteNewKwd();
	int insertNewKwd(Map<String,List<Map<String,Object>>> list);
	
	List<Map<String,Object>> selectHitKwd();
	int deleteHitKwd();
	int insertHitKwd(Map<String,List<Map<String,Object>>> list);
}

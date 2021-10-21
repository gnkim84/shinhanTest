package com.shinvest.ap.schedule.job;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.shinvest.ap.app.schedule.model.ScheduleLogModel;
import com.shinvest.ap.app.schedule.model.ScheduleModel;
import com.shinvest.ap.app.schedule.service.ScheduleService;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.schedule.service.JobNewsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobNews extends QuartzJobBean {

	@Resource(name = "commonUtil")
	private CommonUtil util;

	@Resource(name="scheduleService")
	private ScheduleService scheduleService;

	@Resource(name = "jobNewsService")
	private JobNewsService service;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 스케줄러 정보
		ScheduleModel model = scheduleService.selectSchedule("NEWS");
		model.setServerIp(util.getServerIP());
		int lock = 0;

		// 스케줄러 로그
		ScheduleLogModel logModel = new ScheduleLogModel();
		logModel.setStartDt(LocalDateTime.now());
		logModel.setIfTy(model.getScheduleNm());
		logModel.setServerIp(model.getServerIp());

		// 로그 메시지
		JSONObject msg = new JSONObject();

		// 스케줄러 LOCK
		lock = scheduleService.insertScheduleLock(model);

		if (lock == 1) {
			
			try {
				// 최신 뉴스
				service.syncNews(msg);
			} catch (Exception e) {
				log.warn("NEWS 연동 처리 중 오류 발생");
				log.warn(e.getMessage());
			}
			
			try {
				// 리서치
				service.syncResearch(msg);
			} catch (Exception e) {
				log.warn("RESEARCH 연동 처리 중 오류 발생");
				log.warn(e.getMessage());
			}
			
			try {
				// 온라인 랭킹
				service.syncRankOnline(msg);
			} catch (Exception e) {
				log.warn("ONLINE RANK 연동 처리 중 오류 발생");
				log.warn(e.getMessage());
			}
			
			try {
				// S캐치 종목 랭킹
				service.syncRankStbd(msg);
			} catch (Exception e) {
				log.warn("STBD RANK 연동 처리 중 오류 발생");
				log.warn(e.getMessage());
			}
			
			try {
				// S캐치 관심 랭킹
				service.syncRankInterest(msg);
			} catch (Exception e) {
				log.warn("INTEREST RANK 연동 처리 중 오류 발생");
				log.warn(e.getMessage());
			}
			
			logModel.setMsg(msg);
			if (StringUtils.isBlank(logModel.getRsltCode())) {
				logModel.setRsltCode(Constant.NO);
			}
			logModel.setEndDt(LocalDateTime.now());
			// 스케줄러 로그 기록 - news 로그 기록 없음
			// scheduleService.insertScheduleLog(logModel);
			log.info("NEWS SCHEDULE : {}", msg.toString());

			//스케줄러 LOCK 해제 - 서버간 시간 차이로 별도 스케줄러에서 락 해제 -> 배치 간격이 짧아 직접 해제
			scheduleService.deleteScheduleLock(model);
		}
	}
}

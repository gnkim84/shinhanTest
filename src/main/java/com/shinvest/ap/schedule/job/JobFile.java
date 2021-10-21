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
import com.shinvest.ap.schedule.service.JobFileService;

import lombok.extern.slf4j.Slf4j;

/*
 * 파일 배치 처리 Quartz Job
 */
@Slf4j
@Component
public class JobFile extends QuartzJobBean {

	@Resource(name = "jobFileService")
	private JobFileService service;

	@Resource(name = "commonUtil")
	private CommonUtil util;

	@Resource(name="scheduleService")
	private ScheduleService scheduleService;

	/*
	 * 파일 배치 처리
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		/*
		 * 파일 처리
		 */
		// 스케줄러 정보
		ScheduleModel model = scheduleService.selectSchedule("FILE");
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

		// 스케줄러 LOCK 안 걸린 경우에 처리
		if (lock == 1) {
			try {
				
				// 연결 정보 없는 파일 삭제
				service.notRefFiles(msg);
				
				// 대용량 파일 삭제
				service.atmcDelFiles(msg);
				
			} catch (Exception e) {
				log.warn("파일 배치 처리 중 오류 발생");
				log.warn(e.getMessage());
				logModel.setErrMsg(util.getExceptionTrace(e));
			}

			logModel.setMsg(msg);
			if (StringUtils.isBlank(logModel.getRsltCode())) {
				logModel.setRsltCode(Constant.NO);
			}
			logModel.setEndDt(LocalDateTime.now());
			// 스케줄러 로그 기록
			scheduleService.insertScheduleLog(logModel);

			//스케줄러 LOCK 해제 - 서버간 시간 차이로 별도 스케줄러에서 락 해제
			//scheduleService.deleteScheduleLock(model);
		}
	}

}

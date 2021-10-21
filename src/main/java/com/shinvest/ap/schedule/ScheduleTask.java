package com.shinvest.ap.schedule;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shinvest.ap.common.Constant;
import com.shinvest.ap.schedule.service.ScheduleTaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring 스케줄러
 * 비관리 스케줄러 : 관리 스케줄러는 Quartz 스케줄러에 등록되도록 DB 에서 관리
 */
@Slf4j
@Component
//@Profile({Constant.Profile.PROD,Constant.Profile.DEV})
public class ScheduleTask {
	
	@Resource(name="scheduleTaskService")
	private ScheduleTaskService scheduleTaskService;

	/**
	 * Quartz 스케줄 락 확인
	 * @since 2021.01.26.
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void hrSchedule() {
		scheduleTaskService.checkLock();
    }
}

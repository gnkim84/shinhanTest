package com.shinvest.ap.schedule.job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
import com.shinvest.ap.schedule.service.JobHrService;

import lombok.extern.slf4j.Slf4j;

/*
 * HR 연계 Quartz Job
 */
@Slf4j
@Component
public class JobHr extends QuartzJobBean {
	
	@Resource(name="jobHrService")
	private JobHrService service;
	
	@Resource(name="commonUtil")
	private CommonUtil util;
	
	@Resource(name="scheduleService")
	private ScheduleService scheduleService;

	/*
	 * HR 연계
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		/* HR 데이터 처리
		 * 사용자, 부서, 본부, 직위 각 정보를 하나의 정보로 처리하고 단계 중 하나 라도 오류 발생하면 모두 ROLLBACK 후 16번으로 이동
		 * 1. 사용자, 부서, 본부, 직위 csv 파일 파싱
		 *     - 파일 읽기 또는 파싱 에러시 처리 : 에러 파일이 하나라도 있으면 작업 중지
		 * 2. 인터페이스 테이블에 데이터 DELETE
		 * 3. 파싱 데이터 인터페이스 테이블에 INSERT
		 * 4. 삭제 사용자 DELETE 및 이력 처리
		 * 5. 변경 사용자 UPDATE 및 이력 처리
		 * 6. 신규 사용자 INSERT 및 이력 처리
		 * 7. 삭제 본부 DELETE 및 이력 처리
		 * 8. 변경 본부 UPDATE 및 이력 처리
		 * 9. 신규 본부 INSERT 및 이력 처리
		 * 10. 삭제 부서 DELETE 및 이력 처리
		 * 11. 변경 부서 UPDATE 및 이력 처리
		 * 12. 신규 부서 INSERT 및 이력 처리
		 * 13. 삭제 직위 DELETE 및 이력 처리
		 * 14. 변경 직위 UPDATE 및 이력 처리
		 * 15. 신규 직위 INSERT 및 이력 처리
		 * 16. 연계 파일을 백업 폴더로 이동
		 * 17. 배치 로그 기록
		 */
		
		//스케줄러 정보
		ScheduleModel model = scheduleService.selectSchedule("HR");
		model.setServerIp(util.getServerIP());
		int lock = 0;
		
		// 스케줄러 로그
		ScheduleLogModel logModel = new ScheduleLogModel();
		logModel.setStartDt(LocalDateTime.now());
		logModel.setIfTy(model.getScheduleNm());
		logModel.setServerIp(model.getServerIp());
		
		// 로그 메시지
		JSONObject msg = new JSONObject();

		// 파일 있는지 확인
		service.checkFiles(msg);
		
		//파일이 하나라도 있는 경우 처리 시작
		JSONObject checkDetail = msg.getJSONObject(Constant.HR.CHECK_FILES).getJSONObject(Constant.DETAIL);
		if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.USER))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.HDEPT))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.DEPT))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.PSTN))) {
		
			//스케줄러 LOCK
			lock = scheduleService.insertScheduleLock(model);
			
			if (lock == 1) {
				try {
					Map<String,List<Map<String,String>>> data = service.parsingFiles(msg);
					
					//파싱 완료 후 데이터 처리 시작
					if (StringUtils.equals(Constant.YES,msg.getJSONObject(Constant.HR.PARSING_FILES).getString(Constant.RESULT))) {
						// 인터페이스 테이블에 INSERT
						JSONObject insertData = service.insertData(data);
						msg.put("insertData", insertData);
						
						// HR 데이터 동기화
						JSONObject syncData = service.syncData();
						msg.put("syncData", syncData);
						
						logModel.setRsltCode(Constant.YES);
					} else {
						// 파싱 오류 인 경우
						logModel.setErrMsg(msg.getString(Constant.ERROR_MESSAGE));
						msg.remove(Constant.ERROR_MESSAGE);
					}
				
				} catch(Exception e) {
					log.warn("HR 연동 처리 중 오류 발생");
					log.warn(e.getMessage());
					logModel.setErrMsg(util.getExceptionTrace(e));
				}

				try {
					log.debug("파일 백업 시작");
					service.backupFiles(msg);
				} catch(Exception e) {
					log.warn(e.getMessage());
				}
				
				logModel.setMsg(msg);
				if (StringUtils.isBlank(logModel.getRsltCode())) {
					logModel.setRsltCode(Constant.NO);
				}
				logModel.setEndDt(LocalDateTime.now());
				//스케줄러 로그 기록
				scheduleService.insertScheduleLog(logModel);
				
				//스케줄러 LOCK 해제 - 서버간 시간 차이로 별도 스케줄러에서 락 해제
				//scheduleService.deleteScheduleLock(model);
			}
		}
	}

}

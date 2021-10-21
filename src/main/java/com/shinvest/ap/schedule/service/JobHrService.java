package com.shinvest.ap.schedule.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.ParseUtil;
import com.shinvest.ap.config.props.HrProps;
import com.shinvest.ap.schedule.mapper.JobHrMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobHrService {

	@Resource(name="hrProps")
	private HrProps props;
	
	@Resource(name = "jobHrMapper")
	private JobHrMapper mapper;
	
	@Resource(name="commonUtil")
	private CommonUtil util;
	
	@Resource(name="parseUtil")
	private ParseUtil parseUtil;
	
	/**
	 * HR 연계 파일 확인
	 */
	public void checkFiles(JSONObject msg) {
		JSONObject checkFiles = new JSONObject();
		JSONObject detail = new JSONObject();
		
		// 사원 파일 있는지 확인
		boolean checkUser = util.checkFile(Paths.get(props.getBasePath(),props.getUserFile()));
		detail.put(Constant.HR.USER, checkUser?Constant.YES:Constant.NO);
		
		// 본부 파일 있는지 확인
		boolean checkHdept = util.checkFile(Paths.get(props.getBasePath(),props.getHdeptFile()));
		detail.put(Constant.HR.HDEPT, checkHdept?Constant.YES:Constant.NO);
		
		// 부서 파일 있는지 확인
		boolean checkDept = util.checkFile(Paths.get(props.getBasePath(),props.getDeptFile()));
		detail.put(Constant.HR.DEPT, checkDept?Constant.YES:Constant.NO);
		
		// 직위 파일 있는지 확인
		boolean checkPstn = util.checkFile(Paths.get(props.getBasePath(),props.getPstnFile()));
		detail.put(Constant.HR.PSTN, checkPstn?Constant.YES:Constant.NO);
		
		if (checkUser && checkHdept && checkDept && checkPstn) {
			checkFiles.put(Constant.RESULT, Constant.YES);
		} else {
			checkFiles.put(Constant.RESULT, Constant.NO);
		}
		checkFiles.put(Constant.DETAIL, detail);
		msg.put(Constant.HR.CHECK_FILES, checkFiles);
		return;
	}
	
	/**
	 * HR 연계 파일 파싱
	 * @return
	 * @throws Exception
	 */
	public Map<String,List<Map<String,String>>> parsingFiles(JSONObject msg) throws Exception {
		Map<String,List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
		JSONObject parsingFiles = new JSONObject();
		JSONObject detail = new JSONObject();
		try {
			// 사원 파일 파싱
			result.put(Constant.HR.USER, parseUtil.parseCSV(Paths.get(props.getBasePath(),props.getUserFile()), Constant.HR.HEADER_USER, null, Constant.HR.SEPERATOR, props.getEncoding()));
			detail.put(Constant.HR.USER, result.get(Constant.HR.USER).size());
			// 본부 파일 파싱
			result.put(Constant.HR.HDEPT, parseUtil.parseCSV(Paths.get(props.getBasePath(),props.getHdeptFile()), Constant.HR.HEADER_HDEPT, null, Constant.HR.SEPERATOR, props.getEncoding()));
			detail.put(Constant.HR.HDEPT, result.get(Constant.HR.HDEPT).size());
			// 부서 파일 파싱
			result.put(Constant.HR.DEPT, parseUtil.parseCSV(Paths.get(props.getBasePath(),props.getDeptFile()), Constant.HR.HEADER_DEPT, null, Constant.HR.SEPERATOR, props.getEncoding()));
			detail.put(Constant.HR.DEPT, result.get(Constant.HR.DEPT).size());
			// 직위 파일 파싱
			result.put(Constant.HR.PSTN, parseUtil.parseCSV(Paths.get(props.getBasePath(),props.getPstnFile()), Constant.HR.HEADER_PSTN, null, Constant.HR.SEPERATOR, props.getEncoding()));
			detail.put(Constant.HR.PSTN, result.get(Constant.HR.PSTN).size());
			
			parsingFiles.put(Constant.DETAIL, detail);
			parsingFiles.put(Constant.RESULT, Constant.YES);
			msg.put(Constant.HR.PARSING_FILES, parsingFiles);
		} catch (Exception e) {
			parsingFiles.put(Constant.DETAIL, detail);
			parsingFiles.put(Constant.RESULT, Constant.NO);
			msg.put(Constant.HR.PARSING_FILES, parsingFiles);
			log.warn("HR 연동 파일 파싱 처리 중 오류 발생");
			log.warn(e.getMessage());
			msg.put(Constant.ERROR_MESSAGE, util.getExceptionTrace(e));
		}
		return result;
	}
	
	/**
	 * HR 연계 파일 백업
	 */
	public void backupFiles(JSONObject msg) throws Exception {
		JSONObject backupFiles = new JSONObject();
		JSONObject detail = new JSONObject();
		
		JSONObject checkDetail = msg.getJSONObject(Constant.HR.CHECK_FILES).getJSONObject(Constant.DETAIL);
		if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.USER))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.HDEPT))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.DEPT))
				|| StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.PSTN))) {
		
			String ymd = util.getDateString(Constant.DATE_FORMAT.DEFAULT_DAY);
			String yr = ymd.substring(0,4);
			// 년도 폴더 생성 확인
			util.checkPath(Paths.get(props.getBackupPath(),yr));
			// 년월일 폴더 생성 확인
			util.checkPath(Paths.get(props.getBackupPath(),yr,ymd));
			
			// 사원 파일 백업
			if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.USER))) {
				Path file = Files.move(Paths.get(props.getBasePath(),props.getUserFile()), Paths.get(props.getBackupPath(),yr,ymd,props.getUserFile()), StandardCopyOption.REPLACE_EXISTING);
				if (file.toFile().exists()) {
					detail.put(Constant.HR.USER, Constant.YES);
				} else {
					detail.put(Constant.HR.USER, Constant.NO);
				}
			}
			if (!detail.has(Constant.HR.USER)) {
				detail.put(Constant.HR.USER, Constant.NO);
			}
			// 본부 파일 백업
			if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.HDEPT))) {
				Path file = Files.move(Paths.get(props.getBasePath(),props.getHdeptFile()), Paths.get(props.getBackupPath(),yr,ymd,props.getHdeptFile()), StandardCopyOption.REPLACE_EXISTING);
				if (file.toFile().exists()) {
					detail.put(Constant.HR.HDEPT, Constant.YES);
				} else {
					detail.put(Constant.HR.HDEPT, Constant.NO);
				}
			}
			if (!detail.has(Constant.HR.HDEPT)) {
				detail.put(Constant.HR.HDEPT, Constant.NO);
			}
			// 부서 파일 백업
			if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.DEPT))) {
				Path file = Files.move(Paths.get(props.getBasePath(),props.getDeptFile()), Paths.get(props.getBackupPath(),yr,ymd,props.getDeptFile()), StandardCopyOption.REPLACE_EXISTING);
				if (file.toFile().exists()) {
					detail.put(Constant.HR.DEPT, Constant.YES);
				} else {
					detail.put(Constant.HR.DEPT, Constant.NO);
				}
			}
			if (!detail.has(Constant.HR.DEPT)) {
				detail.put(Constant.HR.DEPT, Constant.NO);
			}
			// 직위 파일 백업
			if (StringUtils.equals(Constant.YES, checkDetail.getString(Constant.HR.PSTN))) {
				Path file = Files.move(Paths.get(props.getBasePath(),props.getPstnFile()), Paths.get(props.getBackupPath(),yr,ymd,props.getPstnFile()), StandardCopyOption.REPLACE_EXISTING);
				if (file.toFile().exists()) {
					detail.put(Constant.HR.PSTN, Constant.YES);
				} else {
					detail.put(Constant.HR.PSTN, Constant.NO);
				}
			}
			if (!detail.has(Constant.HR.PSTN)) {
				detail.put(Constant.HR.PSTN, Constant.NO);
			}
			backupFiles.put(Constant.DETAIL, detail);
			backupFiles.put(Constant.RESULT, Constant.YES);
			
		}  else {
			// 파일이 하나도 없으면
			detail.put(Constant.HR.USER, Constant.NO);
			detail.put(Constant.HR.HDEPT, Constant.NO);
			detail.put(Constant.HR.DEPT, Constant.NO);
			detail.put(Constant.HR.PSTN, Constant.NO);
			backupFiles.put(Constant.DETAIL, detail);
			backupFiles.put(Constant.RESULT, Constant.NO);
		}
		msg.put(Constant.HR.BACKUP_FILES, backupFiles);
		return;
	}
	
	/**
	 * HR 데이터 등록
	 */
	@Transactional
	public JSONObject insertData(Map<String,List<Map<String,String>>> data) throws Exception {
		JSONObject result = new JSONObject();
		
		//사용자
		JSONObject userResult = new JSONObject();
		int userDeleteCount = mapper.deleteIfUser();
		userResult.put("delete", userDeleteCount);
		List<Map<String,String>> userList = data.get(Constant.HR.USER);
		List<List<Map<String,String>>> userPartitionList = Lists.partition(userList, 100);
		int userInsertCount = 0;
		for (List<Map<String,String>> list : userPartitionList) {
			Map<String,List<Map<String,String>>> map = new HashMap<String,List<Map<String,String>>>();
			map.put(Constant.HR.USER, list);
			int count = mapper.insertIfUser(map);
			userInsertCount += count;
		}
		userResult.put("insert", userInsertCount);
		result.put(Constant.HR.USER, userResult);
		
		//본부
		JSONObject hdeptResult = new JSONObject();
		int hdeptDeleteCount = mapper.deleteIfHdept();
		hdeptResult.put("delete", hdeptDeleteCount);
		List<Map<String,String>> hdeptList = data.get(Constant.HR.HDEPT);
		List<List<Map<String,String>>> hdeptPartitionList = Lists.partition(hdeptList, 100);
		int hdeptInsertCount = 0;
		for (List<Map<String,String>> list : hdeptPartitionList) {
			Map<String,List<Map<String,String>>> map = new HashMap<String,List<Map<String,String>>>();
			map.put(Constant.HR.HDEPT, list);
			int count = mapper.insertIfHdept(map);
			hdeptInsertCount += count;
		}
		hdeptResult.put("insert", hdeptInsertCount);
		result.put(Constant.HR.HDEPT, hdeptResult);
		
		//부서
		JSONObject deptResult = new JSONObject();
		int deptDeleteCount = mapper.deleteIfDept();
		deptResult.put("delete", deptDeleteCount);
		List<Map<String,String>> deptList = data.get(Constant.HR.DEPT);
		List<List<Map<String,String>>> deptPartitionList = Lists.partition(deptList, 100);
		int deptInsertCount = 0;
		for (List<Map<String,String>> list : deptPartitionList) {
			Map<String,List<Map<String,String>>> map = new HashMap<String,List<Map<String,String>>>();
			map.put(Constant.HR.DEPT, list);
			int count = mapper.insertIfDept(map);
			deptInsertCount += count;
		}
		deptResult.put("insert", deptInsertCount);
		result.put(Constant.HR.DEPT, deptResult);
		
		//직위
		JSONObject pstnResult = new JSONObject();
		int pstnDeleteCount = mapper.deleteIfPstn();
		pstnResult.put("delete", pstnDeleteCount);
		List<Map<String,String>> pstnList = data.get(Constant.HR.PSTN);
		List<List<Map<String,String>>> pstnPartitionList = Lists.partition(pstnList, 100);
		int pstnInsertCount = 0;
		for (List<Map<String,String>> list : pstnPartitionList) {
			Map<String,List<Map<String,String>>> map = new HashMap<String,List<Map<String,String>>>();
			map.put(Constant.HR.PSTN, list);
			int count = mapper.insertIfPstn(map);
			pstnInsertCount += count;
		}
		pstnResult.put("insert", pstnInsertCount);
		result.put(Constant.HR.PSTN, pstnResult);
		
		//사용자 본부 코드
		mapper.updateIfUser();
		result.put(Constant.RESULT, Constant.YES);
		return result;
	}
	
	/**
	 * HR 데이터 등록
	 */
	@Transactional
	public JSONObject syncData() throws Exception {
		JSONObject result = new JSONObject();
		
		//본부
		JSONObject hdeptResult = new JSONObject();
		//삭제 본부
		hdeptResult.put("delete", mapper.syncDeleteHdept());
		mapper.syncDeleteHdeptHist();
		//변경 본부
		hdeptResult.put("update", mapper.syncUpdateHdept());
		mapper.syncUpdateHdeptHist();
		//신규 본부
		hdeptResult.put("insert", mapper.syncInsertHdept());
		mapper.syncInsertHdeptHist();
		result.put(Constant.HR.HDEPT,hdeptResult);
		//본부 수정 구분 정리
		mapper.updateModiSeHdept();
		
		//부서
		JSONObject deptResult = new JSONObject();
		//삭제 부서
		deptResult.put("delete", mapper.syncDeleteDept());
		mapper.syncDeleteDeptHist();
		//변경 부서
		deptResult.put("update", mapper.syncUpdateDept());
		mapper.syncUpdateDeptHist();
		//신규 부서
		deptResult.put("insert", mapper.syncInsertDept());
		mapper.syncInsertDeptHist();
		result.put(Constant.HR.DEPT,deptResult);
		//부서 수정 구분 정리
		mapper.updateModiSeDept();
		
		//직위
		JSONObject pstnResult = new JSONObject();
		//삭제 직위
		pstnResult.put("delete", mapper.syncDeletePstn());
		mapper.syncDeletePstnHist();
		//변경 부서
		pstnResult.put("update", mapper.syncUpdatePstn());
		mapper.syncUpdatePstnHist();
		//신규 부서
		pstnResult.put("insert", mapper.syncInsertPstn());
		mapper.syncInsertPstnHist();
		result.put(Constant.HR.PSTN,pstnResult);
		//직위 수정 구분 정리
		mapper.updateModiSePstn();
		
		//사용자
		JSONObject userResult = new JSONObject();
		//삭제 사용자
		userResult.put("delete", mapper.syncDeleteUser());
		//삭제 사용자 권한을 모두 사용 중지로 변경
		mapper.syncDeleteAuth();
		//삭제 사용자 이력
		mapper.syncDeleteUserHist();
		//사용자 이전 부서 코드 초기화
		//mapper.updateBfDeptCode();
		//변경 사용자
		userResult.put("update", mapper.syncUpdateUser());
		mapper.syncUpdateUserHist();
		//신규 사용자
		userResult.put("insert", mapper.syncInsertUser());
		//신규 사용자 권한을 모두 "AU2020_100000001"로 등록
		mapper.syncInsertAuth();
		//신규 사용자 이력
		mapper.syncInsertUserHist();
		result.put(Constant.HR.USER,userResult);
		//사용자 수정 구분 정리
		mapper.updateModiSeUser();
		
		result.put(Constant.RESULT, Constant.YES);
		
		return result;
	}
}

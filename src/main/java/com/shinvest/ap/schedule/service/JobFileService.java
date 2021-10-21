package com.shinvest.ap.schedule.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.aws.AwsS3Util;
import com.shinvest.ap.config.props.HrProps;
import com.shinvest.ap.schedule.mapper.JobFileMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobFileService {

	@Resource(name="hrProps")
	private HrProps props;
	
	@Resource(name = "jobFileMapper")
	private JobFileMapper mapper;
	
	@Resource(name="awsS3Util")
	private AwsS3Util awsS3Util;
	
	/**
	 * 연결 정보 없는 파일 삭제 : REF_ID 없는 파일
	 */
	public void notRefFiles(JSONObject msg) {
		JSONObject result = new JSONObject();
		int count = 0;
		try {
			// 삭제 대상 조회
			List<FileModel> files = mapper.selectNotRefFiles();
			if (files != null && files.size() > 0) {
				for(FileModel file : files) {
					if (StringUtils.equals(Constant.File.AWS_S3, file.getStorageSe())) {
						if (!StringUtils.isAnyBlank(file.getBucketNm(),file.getSavePath(),file.getSaveFileNm())) {
							awsS3Util.delete(file);
						}
					}
					mapper.updateNotRefFile(file);
					count++;
				}
			}
			result.put(Constant.RESULT, Constant.YES);
		} catch (Exception e) {
			result.put(Constant.RESULT, Constant.NO);
			log.warn("연결 정보 없는 파일 삭제 처리 중 오류 발생");
			log.warn(e.getMessage());
		}
		result.put("delCnt", count);
		msg.put("notRef", result);
	}
	
	/**
	 * 대용량 파일 삭제 : 자동 삭제 일시가 당일인 경우 삭제
	 */
	public void atmcDelFiles(JSONObject msg) {
		JSONObject result = new JSONObject();
		int count = 0;
		try {
			//삭제 대상 조회
			List<FileModel> files = mapper.selectAtmcDelFiles();
			if (files != null && files.size() > 0) {
				for(FileModel file : files) {
					if (StringUtils.equals(Constant.File.AWS_S3, file.getStorageSe())) {
						if (!StringUtils.isAnyBlank(file.getBucketNm(),file.getSavePath(),file.getSaveFileNm())) {
							awsS3Util.delete(file);
						}
					}
					mapper.updateAtmcDelFile(file);
					count++;
				}
			}
			result.put(Constant.RESULT, Constant.YES);
		} catch (Exception e) {
			result.put(Constant.RESULT, Constant.NO);
			log.warn("대용량 파일 삭제 처리 중 오류 발생");
			log.warn(e.getMessage());
		}
		result.put("delCnt", count);
		msg.put("atmcDel", result);
	}
	
}

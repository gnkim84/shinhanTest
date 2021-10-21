package com.shinvest.ap.schedule.mapper;

import java.util.List;

import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface JobFileMapper {
	
	// 연결 정보 없는 파일 조회
	List<FileModel> selectNotRefFiles() throws Exception;
	// 연결 정보 없는 파일 삭제 후 업데이트
	int updateNotRefFile(FileModel model) throws Exception;
	
	// 대용량 삭제 대상 조회
	List<FileModel> selectAtmcDelFiles() throws Exception;
	// 대용량 삭제 후 업데이트
	int updateAtmcDelFile(FileModel model) throws Exception;
}

package com.shinvest.ap.schedule.mapper;

import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface JobUserPhotoMapper {

	MemberModel selectUser(String userId) throws Exception;
	int insertFile(FileModel model) throws Exception;
	int updateUserFileInfo(FileModel model) throws Exception;
	FileModel selectFile(String fileId) throws Exception;
	int updateFile(FileModel model) throws Exception;
	
}

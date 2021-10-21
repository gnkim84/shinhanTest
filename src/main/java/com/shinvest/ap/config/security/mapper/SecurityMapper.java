package com.shinvest.ap.config.security.mapper;

import java.util.Map;

import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.menu.model.MenuModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface SecurityMapper {

	MemberModel selectUser(String userId);
	
	int updateLastLogDt(String userId);
	
	String selectLoginMessage(String codeId);
	
	MenuModel selectMenuWithAuth(Map<String,String> param);
}

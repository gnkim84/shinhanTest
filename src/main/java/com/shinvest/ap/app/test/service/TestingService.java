package com.shinvest.ap.app.test.service;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.shinvest.ap.app.test.mapper.TestMapper;
import com.shinvest.ap.common.Constant;

@Service
@Profile({Constant.Profile.DEV,Constant.Profile.LOCAL})
public class TestingService {
	
	@Resource
	private TestMapper mapper;

	public void testExceptoin() throws Exception {
		mapper.selectTestExceptoin();
	}
}

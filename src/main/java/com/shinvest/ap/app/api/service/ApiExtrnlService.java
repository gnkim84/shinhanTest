package com.shinvest.ap.app.api.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shinvest.ap.app.extrnl.mapper.ExtrnlMapper;
import com.shinvest.ap.app.extrnl.model.ExtrnlModel;

@Service
public class ApiExtrnlService {

	@Resource
    private ExtrnlMapper extrnlMapper;
	
	/**
     * 외부시스템 모델을 조회한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    public ExtrnlModel select(ExtrnlModel model) {
        return extrnlMapper.select(model);
    }
}

package com.shinvest.ap.app.aprv.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.app.aprv.mapper.AprvMapper;
import com.shinvest.ap.app.aprv.model.AprvModel;

@Service
public class AprvService {

	@Resource
	private AprvMapper aprvMapper;
	
	public List<AprvModel> selectAprvList(AprvModel model) {
		return aprvMapper.selectAprvList(model);
	}
	
	public int selectAprvListCount(AprvModel model) {
		return aprvMapper.selectAprvListCount(model);
	}
	
	public AprvModel selectAprv(AprvModel model) {
		return aprvMapper.selectAprv(model);
	}
	
	@Transactional
	public long insertAprv(AprvModel model) {
		model.setUseYn("Y");
		return aprvMapper.insertAprv(model);
	}

	@Transactional
	public long insertAprvDtl(AprvModel model) {
		model.setUseYn("Y");
		return aprvMapper.insertAprvDtl(model);
	}
	
}

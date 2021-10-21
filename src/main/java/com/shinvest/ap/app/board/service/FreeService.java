package com.shinvest.ap.app.board.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shinvest.ap.app.board.mapper.FreeMapper;
import com.shinvest.ap.app.board.model.FreeModel;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.paging.Criteria;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 713034
 * Notice Service
 */
@Slf4j
@Service
public class FreeService {

    @Resource
    private FreeMapper freeMapper;
    
	@Resource(name="idUtil")
	private IdUtil idUtil;

	/**
	 * Search All Notice List
	 * @param none
	 * @return List<Object>
	 */
	public List<FreeModel> selectFreeList(Criteria criteria) {
		return freeMapper.selectFreeList(criteria);
	}
	
	public int selectFreeListCount(Criteria criteria) {
		return freeMapper.selectFreeListCount(criteria);
	}
	
	
	public FreeModel selectFree(FreeModel model) {
		return freeMapper.selectFree(model);
	}
	
	public long deleteFree(FreeModel model) {
		return freeMapper.deleteFree(model);
	}

	public long updateFree(FreeModel model) {
		return freeMapper.updateFree(model);
	}
	
	/**
	 * Get Free Id
	 * @return
	 */
	public String getFreeId() {
		
		String FreeId = idUtil.getFreeId();
		return FreeId;
		
	}
	
}

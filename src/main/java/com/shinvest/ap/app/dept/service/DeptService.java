package com.shinvest.ap.app.dept.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shinvest.ap.app.dept.mapper.DeptMapper;
import com.shinvest.ap.app.dept.model.DeptClModel;

@Service
public class DeptService {

	@Resource
	private DeptMapper deptMapper;
	
	public DeptClModel selectDeptCl(String deptCode) {
		return deptMapper.selectDeptCl(deptCode);
	}
	
	public List<DeptClModel> selectDeptClList() {
		return deptMapper.selectDeptClList();
	}

	public List<DeptClModel> selectDeptClSearchList(DeptClModel model) {
		return deptMapper.selectDeptClSearchList(model);
	}
	
}

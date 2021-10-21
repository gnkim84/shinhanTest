package com.shinvest.ap.app.dept.mapper;

import java.util.List;

import com.shinvest.ap.app.dept.model.DeptClModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface DeptMapper {
	
	//부서 분류 조회
	DeptClModel selectDeptCl(String deptCode);
	List<DeptClModel> selectDeptClList();
	List<DeptClModel> selectDeptClSearchList(DeptClModel model);

}

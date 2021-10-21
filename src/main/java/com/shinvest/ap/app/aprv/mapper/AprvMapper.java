package com.shinvest.ap.app.aprv.mapper;

import java.util.List;

import com.shinvest.ap.app.aprv.model.AprvModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface AprvMapper {
	
	List<AprvModel> selectAprvList(AprvModel criteria);
	int selectAprvListCount(AprvModel criteria);
	AprvModel selectAprv(AprvModel model);
	
	long insertAprv(AprvModel model);
	long insertAprvDtl(AprvModel model);

}

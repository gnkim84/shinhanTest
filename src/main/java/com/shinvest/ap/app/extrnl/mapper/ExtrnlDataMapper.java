package com.shinvest.ap.app.extrnl.mapper;

import com.shinvest.ap.app.extrnl.model.ExtrnlDataModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ConnMapperFirst
public interface ExtrnlDataMapper {
	
    List<ExtrnlDataModel> selectList(Criteria criteria);
    List<ExtrnlDataModel> selectCycleList(Criteria criteria);
    List<ExtrnlDataModel> selectDeptList(Criteria criteria);
    int selectListCount(Criteria criteria);
    ExtrnlDataModel select(ExtrnlDataModel model);
    long delete(ExtrnlDataModel model);
    long update(ExtrnlDataModel model);
    long insert(ExtrnlDataModel model);
    
    //long insertMulti(ArrayList<ExtrnlDataModel> modelList);
    long insertMulti(HashMap<String, Object> map);
}

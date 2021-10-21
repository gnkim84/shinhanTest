package com.shinvest.ap.app.board.mapper;

import java.util.List;

import com.shinvest.ap.app.board.model.FreeModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

@ConnMapperFirst
public interface FreeMapper {

    List<FreeModel> selectFreeList(Criteria criteria);
    int selectFreeListCount(Criteria criteria);
    FreeModel selectFree(FreeModel model);
    long updateFree(FreeModel model);
    long deleteFree(FreeModel model);
    
}

package com.shinvest.ap.app.board.mapper;

import java.util.List;

import com.shinvest.ap.app.board.model.QnaModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

@ConnMapperFirst
public interface QnaMapper {

    List<QnaModel> selectQnaList(Criteria criteria);
    int selectQnaListCount(Criteria criteria);
    QnaModel selectQna(QnaModel model);
    long updateQna(QnaModel model);
    long deleteQna(QnaModel model);
    
}

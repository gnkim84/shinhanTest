package com.shinvest.ap.app.board.mapper;

import com.shinvest.ap.app.board.model.FaqModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.List;

@ConnMapperFirst
public interface FaqMapper {

	List<FaqModel> selectFaqList(Criteria criteria);
	int selectFaqListCount(Criteria criteria);
	FaqModel selectFaq(FaqModel model);
	long deleteFaq(FaqModel model);
	long insertFaq(FaqModel model);
	long updateFaq(FaqModel model);	
	
}

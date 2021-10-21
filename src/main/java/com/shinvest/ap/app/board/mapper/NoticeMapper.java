package com.shinvest.ap.app.board.mapper;

import com.shinvest.ap.app.board.model.NoticeModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.List;

@ConnMapperFirst
public interface NoticeMapper {

	List<NoticeModel> selectNoticeList(Criteria criteria);
	int selectNoticeListCount(Criteria criteria);
	NoticeModel selectNotice(NoticeModel model);
	long deleteNotice(NoticeModel model);
	long insertNotice(NoticeModel model);
	long updateNotice(NoticeModel model);		
	
}

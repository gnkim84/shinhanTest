package com.shinvest.ap.app.wrkCat.mapper;

import com.shinvest.ap.app.wrkCat.model.WrkModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.List;

/**
 * Mybatis 업무카테고리관리 매핑 Interface
 */
@ConnMapperFirst
public interface WrkMapper {

    /**
     * 업무카테고리 목록을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    List<WrkModel> selectList(Criteria criteria);

    /**
     * 업무카테고리 목록 카운트를 조회한다.
     *
     * @param criteria 페이지 모델
     * @return
     */
    int selectListCount(Criteria criteria);

    /**
     * 업무카테고리를 조회한다.
     *
     * @param model 업무카테고리ID 사용
     * @return
     */
    WrkModel select(WrkModel model);

    /**
     * 업무카테고리 모델을 삭제한다.
     *
     * @param model 업무카테고리ID를 사용
     * @return
     */
    long delete(WrkModel model);

    /**
     * 업무카테고리 모델을 수정한다.
     *
     * @param model 업무카테고리ID를 사용
     * @return
     */
    long update(WrkModel model);

    /**
     * 업무카테고리 모델을 신규생성한다.
     *
     * @param model
     * @return
     */
    long insert(WrkModel model);
}

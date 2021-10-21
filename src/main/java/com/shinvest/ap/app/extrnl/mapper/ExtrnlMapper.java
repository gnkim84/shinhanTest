package com.shinvest.ap.app.extrnl.mapper;

import com.shinvest.ap.app.extrnl.model.ExtrnlModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.List;

/**
 * Mybatis 외부시스템관리 매핑 Interface
 */
@ConnMapperFirst
public interface ExtrnlMapper {

    /**
     * 외부시스템 관리 모델을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    List<ExtrnlModel> selectList(Criteria criteria);

    /**
     * 외부시스템 관리 모델 카운트를 조회한다.
     *
     * @param criteria
     * @return
     */
    int selectListCount(Criteria criteria);

    /**
     * 외부시스템ID에 따른 데이터를 조회한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    ExtrnlModel select(ExtrnlModel model);

    /**
     * 외부시스템모델을 삭제한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    long delete(ExtrnlModel model);

    /**
     * 외부시스템ID의 외부시스템모델을 업데이트 한다.
     *
     * @param model 외부시스템모델
     * @return
     */
    long update(ExtrnlModel model);

    /**
     * 외부시스템 모델을 신규생성한다.
     *
     * @param model 외부시스템모델
     * @return
     */
    long insert(ExtrnlModel model);
}

package com.shinvest.ap.app.license.mapper;

import com.shinvest.ap.app.license.model.LicenseModel;
import com.shinvest.ap.app.wrkCat.model.WrkModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

import java.util.List;

/**
 * Mybatis 라이센스관리 매핑 Interface
 */
@ConnMapperFirst
public interface LicenseMapper {

    /**
     * 라이센스 목록을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    List<LicenseModel> selectList(Criteria criteria);

    /**
     * 라이센스 목록 카운트를 조회한다.
     *
     * @param criteria 페이지 모델
     * @return
     */
    int selectListCount(Criteria criteria);

    /**
     * 라이센스를 조회한다.
     *
     * @param model 권한ID, 라이센스ID 사용
     * @return
     */
    LicenseModel select(LicenseModel model);

    /**
     * 라이센스를 삭제한다.
     *
     * @param model 권한ID, 라이센스ID 사용
     * @return
     */
    long delete(LicenseModel model);

    /**
     * 라이센스 모델을 수정한다.
     *
     * @param model 권한ID, 라이센스ID 사용
     * @return
     */
    long update(LicenseModel model);

    /**
     * 라이센스 모델을 신규생성한다.
     *
     * @param model
     * @return
     */
    long insert(LicenseModel model);
}

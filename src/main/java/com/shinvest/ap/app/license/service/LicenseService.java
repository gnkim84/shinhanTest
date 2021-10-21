package com.shinvest.ap.app.license.service;

import com.shinvest.ap.app.license.mapper.LicenseMapper;
import com.shinvest.ap.app.license.model.LicenseModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 라이센스관리 서비스 클래스
 *
 */
@Service
public class LicenseService {

    @Resource
    private LicenseMapper licenseMapper;

    /**
     * 라이센스 목록을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public List<LicenseModel> selectList(Criteria criteria) {
        return licenseMapper.selectList(criteria);
    }

    /**
     * 라이센스 목록 카운트를 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public int selectListCount(Criteria criteria) {
        return licenseMapper.selectListCount(criteria);
    }

    /**
     * 라이센스 모델을 조회한다.
     *
     * @param model 권한ID와 라이센스ID를 사용
     * @return
     */
    public LicenseModel select(LicenseModel model) {
        return licenseMapper.select(model);
    }

    /**
     * 라이센스 모델을 삭제한다.
     *
     * @param model 권한ID와 라이센스ID를 사용
     * @return
     */
    @Transactional
    public String delete(LicenseModel model) {

        long count = licenseMapper.delete(model);

        if(count > 0) {
            return Constant.DB.DELETE;
        } else {
            return Constant.DB.FAIL;
        }
    }

    /**
     * 라이센스정보를 저장한다. 권한ID와 라이센스ID의 모델이 존재하면 업데이트 아니면 신규생성한다.
     *
     * @param model
     * @return
     */
    @Transactional
    public String save(LicenseModel model) {

        LicenseModel existModel = null;

        if(model.getLicenseId() != null) {
            existModel = select(model);
        }

        if(existModel != null) {
            long count = licenseMapper.update(model);

            if(count > 0) {
                return Constant.DB.UPDATE;
            } else {
                return Constant.DB.FAIL;
            }
        } else {
            long count = licenseMapper.insert(model);

            if(count > 0) {
                return Constant.DB.INSERT;
            } else {
                return Constant.DB.FAIL;
            }
        }
    }
}

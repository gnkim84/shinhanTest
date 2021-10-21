package com.shinvest.ap.app.extrnl.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.app.extrnl.mapper.ExtrnlMapper;
import com.shinvest.ap.app.extrnl.model.ExtrnlModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;

/**
 * 외부시스템관리 서비스 클래스
 *
 */
@Service
public class ExtrnlService {

    @Resource
    private ExtrnlMapper extrnlMapper;

    /**
     * 외부시스템 목록을 조회한다.
     *
     * @param criteria 페이징모델
     * @return
     */
    public List<ExtrnlModel> selectList(Criteria criteria) {
        return extrnlMapper.selectList(criteria);
    }

    /**
     * 외부시스템 목록 카운트를 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public int selectListCount(Criteria criteria) {
        return extrnlMapper.selectListCount(criteria);
    }

    /**
     * 외부시스템 모델을 조회한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    public ExtrnlModel select(ExtrnlModel model) {
        return extrnlMapper.select(model);
    }

    /**
     * 외부시스템 모델을 삭제한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    @Transactional
    public String delete(ExtrnlModel model) {
        long count = extrnlMapper.delete(model);

        if(count > 0) {
            return Constant.DB.DELETE;
        } else {
            return Constant.DB.FAIL;
        }
    }

    /**
     * 외부시스템 모델을 저장한다. 외부시스템ID가 존재하면 수정하고 없으면 신규생성한다.
     *
     * @param model 외부시스템ID를 사용
     * @return
     */
    @Transactional
    public String save(ExtrnlModel model) {
        ExtrnlModel existModel = select(model);

        if(existModel != null) {
            long count = extrnlMapper.update(model);

            if(count > 0) {
                return Constant.DB.UPDATE;
            } else {
                return Constant.DB.FAIL;
            }
        } else {
            long count = extrnlMapper.insert(model);

            if(count > 0) {
                return Constant.DB.INSERT;
            } else {
                return Constant.DB.FAIL;
            }
        }
    }
}

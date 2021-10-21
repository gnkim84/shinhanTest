package com.shinvest.ap.app.wrkCat.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.app.wrkCat.mapper.WrkMapper;
import com.shinvest.ap.app.wrkCat.model.WrkModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;

/**
 * 업무카테고리 서비스 클래스
 *
 */
@Service
public class WrkService {

    @Resource
    private WrkMapper wrkMapper;

    /**
     * 업무카테고리 목록을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public List<WrkModel> selectList(Criteria criteria) {
        return wrkMapper.selectList(criteria);
    }

    /**
     * 업무카테고리 목록 카운트를 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public int selectListCount(Criteria criteria) {
        return wrkMapper.selectListCount(criteria);
    }

    /**
     * 업무카테고리 모델을 조회한다.
     *
     * @param model 업무카테고리ID를 사용
     * @return
     */
    public WrkModel select(WrkModel model) {
        return wrkMapper.select(model);
    }

    /**
     * 업무카테고리 모델을 삭제한다.
     *
     * @param model 업무카테고리ID를 사용
     * @return
     */
    @Transactional
    public String delete(WrkModel model) {

        long count = wrkMapper.delete(model);

        if(count > 0) {
            return Constant.DB.DELETE;
        } else {
            return Constant.DB.FAIL;
        }
    }

    /**
     * 업무카테고리를 저장한다. 업무카테고리ID의 모델이 존재하면 업데이트 아니면 신규생성한다.
     *
     * @param model
     * @return
     */
    @Transactional
    public String save(WrkModel model) {
        WrkModel existModel = select(model);

        if(existModel != null) {
            long count = wrkMapper.update(model);

            if(count > 0) {
                return Constant.DB.UPDATE;
            } else {
                return Constant.DB.FAIL;
            }
        } else {
            long count = wrkMapper.insert(model);

            if(count > 0) {
                return Constant.DB.INSERT;
            } else {
                return Constant.DB.FAIL;
            }
        }
    }
}

package com.shinvest.ap.app.member.service;

import com.shinvest.ap.app.member.mapper.MemberMapper;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.role.model.RoleModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 사용자관리 서비스 클래스
 */
@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    /**
     * 사용자관리 목록을 조회한다.
     *
     * @param criteria 페이징 모델
     * @return
     */
    public List<MemberModel> selectMemberList(Criteria criteria) {
        return memberMapper.selectMemberList(criteria);
    }

    /**
     * 사용자 목록 카운트를 조회한다.
     *
     * @param criteria  페이징 모델
     * @return
     */
    public int selectMemberListCount(Criteria criteria) {
        return memberMapper.selectMemberListCount(criteria);
    }

    /**
     * 권한ID를 사용하는 사용자의 카운트를 조회한다.
     *
     * @param roleId 권한ID
     * @return
     */
    public int selectMemberListCountForRoleId(String roleId) {
        return memberMapper.selectMemberListCountForRoleId(roleId);
    }

    /**
     * 사용자 목록을 조회한다.(전체)
     *
     * @param criteria 페이징 모델
     * @return
     */
    public List<MemberModel> selectMemberAllList(Criteria criteria) {
        return memberMapper.selectMemberAllList(criteria);
    }
    
    /**
     * 사번을 이용하여 사용자를 조회한다.
     *
     * @param model memberId(사번)을 사용
     * @return
     */
    public MemberModel selectMember(MemberModel model) {
        return memberMapper.selectMember(model);
    }

    /**
     * 사용자 정보를 수정한다.
     *
     * @param model 사번을 사용
     * @return
     */
    @Transactional
    public String save(MemberModel model) {
    	if("Y".equals(model.getUserAuthChage())) {
    		memberMapper.upsertAuth(model);
    	}
    	
    	if("Y".equals(model.getMgrAuthChange())) {
    		MemberModel m = new MemberModel();
    		m.setAuthId(model.getMgrAuthId());
    		m.setUserId(model.getUserId());
    		m.setRgstId(model.getRgstId());
    		m.setRgstDt(model.getRgstDt());
    		m.setModiId(model.getModiId());
    		m.setModiDt(model.getModiDt());
    		if("".equals(model.getMgrAuthId())) {
    			m.setUseYn("N");
    		}else {
    			m.setUseYn("Y");
    		}
    		memberMapper.upsertMgrAuth(m);
    	}
    	memberMapper.insertUserHst(model);
        long count = memberMapper.update(model);

        if(count > 0) {
            return Constant.DB.UPDATE;
        } else {
            return Constant.DB.FAIL;
        }
    }
    
    /**
     * 관리자 권한ID를 사용하는 사용자의 카운트를 조회한다.
     *
     * @param roleId 권한ID
     * @return
     */
    public int selectMemberListCountForMgrRoleId(String roleId) {
        return memberMapper.selectMemberListCountForMgrRoleId(roleId);
    }
    
    @Transactional
    public long unlockAccount(MemberModel model) {
        return memberMapper.unlockAccount(model);
    }    
}

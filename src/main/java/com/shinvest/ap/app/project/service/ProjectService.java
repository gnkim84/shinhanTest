package com.shinvest.ap.app.project.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.project.mapper.ProjectMapper;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.common.Constant;

import lombok.extern.slf4j.Slf4j;

import com.shinvest.ap.app.project.model.ProjectCriteria;

@Service
@Slf4j
public class ProjectService {

	@Resource
	private ProjectMapper projectMapper;

	@Resource
	CodeService codeService;
	
	public List<ProjectModel> selectProjectList(ProjectCriteria criteria) {
		return projectMapper.selectProjectList(criteria);
	}
	
	public int selectProjectListCount(ProjectCriteria criteria) {
		return projectMapper.selectProjectListCount(criteria);
	}
	
	public ProjectModel selectProject(ProjectModel model) {
		return projectMapper.selectProject(model);
	}
	
	@Transactional
	public String deleteProject(ProjectModel model) {
		projectMapper.insertProjectHst(model);
		long count = projectMapper.deleteProject(model);
		
		if(count > 0) {
            return Constant.DB.DELETE;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String insertProject(ProjectModel model) {

		model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);
    	
		long count = projectMapper.insertProject(model);
		projectMapper.insertProjectHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String updateProject(ProjectModel model) {
		
		// 메이저 버전 업데이트/ 마이너 버전 업데이트 여부에 따른 JAVA 혹은 DB 상의 값 셋팅
		model.setVer(new BigDecimal("0.1").toString());											// 마이너
		model.setUseYn(Constant.YES);
		
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("0.1")).toString());		// 마이너
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("1")).toString());		// 메이저
		
		long count = projectMapper.updateProject(model);
		projectMapper.insertProjectHst(model);
		
		if(count > 0) {
            return Constant.DB.UPDATE;
        } else {
            return Constant.DB.FAIL;
        }
	}
	

    /**
     * 프로젝트 코드 가져오기
     * @param model
     * @return
     */
	public void selectAllProjectCodeList(Model model) {
		
    	model.addAttribute("codePrjSeList", codeService.selectGroupIdAllList("PRJ_SE_CODE"));
    	model.addAttribute("codePrjTypeList", codeService.selectGroupIdAllList("PRJ_TYPE_CODE"));
    	model.addAttribute("codePrjStatList", codeService.selectGroupIdAllList("PRJ_STAT_CODE"));
    	
    }
	
    /**
     * 프로젝트 기본값 셋팅(참여자와 운영자를 자신으로 지정)
     * @param projectInfo
     * @param user
     */
    public void setProjectInfoDefault(ProjectModel projectInfo, MemberModel user) {
    	JSONArray tmpList = new JSONArray(); 
    	JSONObject tmpObj = new JSONObject();
    	JSONObject userJson = new JSONObject()
    			.put(Constant.KEY.USER_ID, user.getUserId())
    			.put(Constant.KEY.USER_NM, user.getUserNm())
    			.put(Constant.KEY.DEPT_CODE, user.getDeptCode())
    			.put(Constant.KEY.PSTN_CODE, user.getPstnNm());
    	tmpList.put(userJson);
    	tmpObj.put("list", tmpList);
    	projectInfo.setPcptInfo(tmpObj);
    	projectInfo.setMgrInfo(tmpObj);
    	projectInfo.setUseYn(Constant.YES);
    }
}

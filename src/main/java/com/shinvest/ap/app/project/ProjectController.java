package com.shinvest.ap.app.project;

import javax.annotation.Resource;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.project.model.ProjectCriteria;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.app.project.service.ProjectService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로젝트관리/프로젝트관리 컨트롤러
 */
@RequestMapping("/project")
@Controller
@Slf4j
public class ProjectController {
	
	@Resource
	IdUtil idUtil;
	
	@Resource
	ProjectService projectService;
    
    /**
     * 프로젝트관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/project")
    public String project(@ModelAttribute ProjectCriteria criteria, Model model) {
    	model.addAttribute("projectList", projectService.selectProjectList(criteria));
    	criteria.setTotalCount(projectService.selectProjectListCount(criteria));
    	model.addAttribute("pages", criteria);
    	projectService.selectAllProjectCodeList(model);
        return "project/project";
    }

	/**
     * 프로젝트관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @PostMapping("/project")
    public String project(@ModelAttribute ProjectCriteria criteria, RedirectAttributes attributes) {
        attributes.addFlashAttribute("projectCriteria", criteria);
    	return "redirect:/project/project";
    }

    /**
     * 프로젝트관리 등록페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/project/regist")
    public String projectRegist(Model model) {
    	projectService.selectAllProjectCodeList(model);
        return "project/projectRegist";
    }
    
    /**
     * 프로젝트 변경페이지로 이동한다.
     * @param projectId
     * @param model
     * @return
     */
    @GetMapping("/project/modify/{projectId}")
    public String select(@ModelAttribute ProjectCriteria criteria, @PathVariable String projectId, Model model) {
    	model.addAttribute("projectCriteria", criteria);
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectId(projectId);
        projectModel = projectService.selectProject(projectModel);
        model.addAttribute("projectInfo", projectModel);
        projectService.selectAllProjectCodeList(model);
        
        //시각화프로젝트일경우엔 view페이지로 떨군다. (태블로에서 싱크로 가져온 데이터를 수정할순 없다.)
        if ("VW".equals(projectModel.getProjectTy())) {
        	return "project/projectDetail";
        } else {
        	return "project/projectRegist";
        }
        
    }
    
    
    /**
     * 프로젝트 신규 생성
     * @param projectInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/project/insert")
    public String insert(@ModelAttribute ProjectModel projectInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	MemberModel user = authUser.getMemberModel();
    	
    	log.debug("PROJECT INSERT USER: {}", user);
    	
    	projectInfo.setRgstId(user.getUserId());
    	projectInfo.setModiId(user.getUserId());
    	projectInfo.setDeptCode(user.getDeptCode());
    	projectInfo.setProjectId(idUtil.getProjectId());
    	
    	//시각화이면 참여자와 운영자를 자신으로 지정
    	if (projectInfo.getProjectTy().equals(Constant.PROJECT.TYPE_VW)) {
    		projectService.setProjectInfoDefault(projectInfo, user);
    	}
		
    	model.addAttribute("projectInsertInfo", projectService.insertProject(projectInfo));
    	
    	log.debug("PROJECT INSERT INFO: {}", model);
    	
    	return "redirect:/project/project";
    }
    
    
    /**
     * Update Old Info
     * @param projectInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/project/update")
    public String update(@ModelAttribute ProjectModel projectInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	MemberModel user = authUser.getMemberModel();
    	
    	log.debug("PROJECT UPDATE USER: {}", user);
    	
    	projectInfo.setModiId(user.getUserId());
    	
    	//시각화일시
    	if (projectInfo.getProjectTy().equals(Constant.PROJECT.TYPE_VW)) {
    		projectService.setProjectInfoDefault(projectInfo, user);
    	}
    	
    	model.addAttribute("projectUpdateInfo", projectService.updateProject(projectInfo));
    	
    	log.debug("PROJECT UPDATE INFO: {}", model);
    	
    	return "redirect:/project/project";
    }
    
    /**
     * Delete Old Info
     * @param projectId
     * @param model
     * @return
     */
    @PostMapping("/project/delete")
    public String delete(@ModelAttribute ProjectModel projectInfo, Model model) {
    	model.addAttribute("projectDeleteInfo", projectService.deleteProject(projectInfo));
    	
    	log.debug("PROJECT DELETE INFO: {}", model);
    	
    	return "redirect:/project/project";
    }
    
    //분석 관리/데이터 권한 신청 관리
    @GetMapping("/dataNew")
    public String dataNew(@ModelAttribute ProjectCriteria criteria, Model model) {

        return "project/dataNew";
    }
    
    //분석 관리/모델 신청 관리 : 사용안함 
    @GetMapping("/modelResource")
    public String modelResource(@ModelAttribute ProjectCriteria criteria, Model model) {

        return "project/modelResource";
    }    
    
    //분석 관리/모델 배포 관리
    @GetMapping("/modelDeploy")
    public String modelDeploy(@ModelAttribute ProjectCriteria criteria, Model model) {

        return "project/modelDeploy";
    }
    
}

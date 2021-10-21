package com.shinvest.ap.app.report;

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

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.report.model.ReportAuthModel;
import com.shinvest.ap.app.report.service.ReportAuthService;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로젝트관리/보고서권한관리 컨트롤러
 */
@RequestMapping("/project")
@Controller
@Slf4j
public class ReportAuthController {
	
	@Resource
	IdUtil idUtil;
	
	@Resource
	ReportAuthService reportAuthService;

    /**
     * 보고서권한관리 페이지로 이동한다.
     *
     * @param criteria
     * @param model
     * @return
     */
    @GetMapping("/reportAuth")
    public String reportAuth(@ModelAttribute Criteria criteria, Model model) {
    	model.addAttribute("reportAuthList", reportAuthService.selectReportAuthList(criteria));
    	criteria.setTotalCount(reportAuthService.selectReportAuthListCount(criteria));
    	model.addAttribute("pages", criteria);
    	reportAuthService.selectAllReportAuthCodeList(model);
        return "report/reportAuth";
    }
    
    /**
     * 보고서관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @PostMapping("/reportAuth")
    public String reportAuth(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {
        attributes.addFlashAttribute("criteria", criteria);
    	return "redirect:/project/reportAuth";
    }

    /**
     * 보고서 등록페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/reportAuth/regist")
    public String reportAuthRegist(Model model) {
    	reportAuthService.selectAllReportAuthCodeList(model);
        return "report/reportAuthRegist";
    }
    
    /**
     * 보고서 변경페이지로 이동한다.
     * @param reportAuthId
     * @param model
     * @return
     */
    @GetMapping("/reportAuth/modify/{reportAuthId}")
    public String select(@PathVariable String reportAuthId, Model model) {
    	ReportAuthModel reportAuthModel = new ReportAuthModel();
        reportAuthModel.setAprvId(reportAuthId);
    	model.addAttribute("reportAuthInfo", reportAuthService.selectReportAuth(reportAuthModel));
    	reportAuthService.selectAllReportAuthCodeList(model);
    	return "report/reportAuthRegist";
    }

    /**
     * 보고서 신규 생성
     * @param reportAuthInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/reportAuth/insert")
    public String insert(@ModelAttribute ReportAuthModel reportAuthInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	MemberModel user = authUser.getMemberModel();
    	reportAuthInfo.setRgstId(user.getUserId());
    	reportAuthInfo.setModiId(user.getUserId());
    	reportAuthInfo.setAprvId(idUtil.getAprvId());
    	model.addAttribute("reportAuthInsertInfo", reportAuthService.insertReportAuth(reportAuthInfo));
    	
    	log.debug("PROJECT INSERT INFO: {}", model);

    	return "redirect:/project/reportAuth";
    }    
    
    /**
     * Update Old Info
     * @param reportAuthInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/reportAuth/update")
    public String update(@ModelAttribute ReportAuthModel reportAuthInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	MemberModel user = authUser.getMemberModel();
    	reportAuthInfo.setModiId(user.getUserId());
    	model.addAttribute("reportAuthUpdateInfo", reportAuthService.updateReportAuth(reportAuthInfo));
    	
    	log.debug("PROJECT UPDATE INFO: {}", model);
    	
    	return "redirect:/project/reportAuth";
    }
    
    /**
     * Delete Old Info
     * @param reportAuthId
     * @param model
     * @return
     */
    @PostMapping("/reportAuth/delete")
    public String delete(@ModelAttribute ReportAuthModel reportAuthInfo, Model model) {
    	model.addAttribute("reportAuthDeleteInfo", reportAuthService.deleteReportAuth(reportAuthInfo));
    	
    	log.debug("PROJECT DELETE INFO: {}", model);
    	
    	return "redirect:/project/reportAuth";
    }

}

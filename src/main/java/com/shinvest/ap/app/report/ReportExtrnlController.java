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
import com.shinvest.ap.app.report.model.ReportExtrnlModel;
import com.shinvest.ap.app.report.service.ReportExtrnlService;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로젝트관리/보고서 외부 배포 관리 컨트롤러
 */
@RequestMapping("/project")
@Controller
@Slf4j
public class ReportExtrnlController {
	
	
	@Resource
	IdUtil idUtil;
	
	@Resource
	ReportExtrnlService reportExtrnlService;

	@Resource
	CodeService codeService;
	

    /**
     * 보고서권한관리 페이지로 이동한다.
     *
     * @param criteria
     * @param model
     * @return
     */
    @GetMapping("/reportExtrnl")
    public String reportExtrnl(@ModelAttribute Criteria criteria, Model model) {
    	model.addAttribute("reportExtrnlList", reportExtrnlService.selectReportExtrnlList(criteria));
    	criteria.setTotalCount(reportExtrnlService.selectReportExtrnlListCount(criteria));
    	model.addAttribute("pages", criteria);
        return "report/reportExtrnl";
    }
    
    /**
     * 보고서관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @PostMapping("/reportExtrnl")
    public String reportExtrnl(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {
        attributes.addFlashAttribute("criteria", criteria);
    	return "redirect:/project/reportExtrnl";
    }

    /**
     * 보고서 등록페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/reportExtrnl/regist")
    public String reportExtrnlRegist(Model model) {
        return "report/reportExtrnlRegist";
    }

    /**
     * 보고서 변경페이지로 이동한다.
     * @param reportExtrnlId
     * @param model
     * @return
     */
    @GetMapping("/reportExtrnl/modify/{reportExtrnlId}")
    public String select(@PathVariable String reportExtrnlId, Model model) {
        ReportExtrnlModel reportExtrnlModel = new ReportExtrnlModel();
//        reportExtrnlModel.setReportExtrnlId(reportExtrnlId);
        model.addAttribute("reportExtrnlInfo", reportExtrnlService.selectReportExtrnl(reportExtrnlModel));
//    	model.addAttribute("codePrjTypeList", codeService.selectGroupIdAllList("PRJ_TYPE_CODE"));
        //프로젝트 리스트의 Id 와 Nm 가져오기 
        return "report/reportExtrnlRegist";
    }
    
    /**
     * 보고서 신규 생성
     * @param reportExtrnlInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/reportExtrnl/insert")
    public String insert(@ModelAttribute ReportExtrnlModel reportExtrnlInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
//    	reportExtrnlInfo.setRgstId(authUser.getUsername());
//    	reportExtrnlInfo.setModiId(authUser.getUsername());
//    	reportExtrnlInfo.setReportExtrnlId(idUtil.getReportExtrnlId());
    	model.addAttribute("reportExtrnlInsertInfo", reportExtrnlService.insertReportExtrnl(reportExtrnlInfo));
    	
    	log.debug("PROJECT INSERT INFO: {}", model);

    	return "redirect:/project/reportExtrnl";
    }
    
    
    /**
     * Update Old Info
     * @param reportExtrnlInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/reportExtrnl/update")
    public String update(@ModelAttribute ReportExtrnlModel reportExtrnlInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
//    	reportExtrnlInfo.setModiId(authUser.getUsername());
    	model.addAttribute("reportExtrnlUpdateInfo", reportExtrnlService.updateReportExtrnl(reportExtrnlInfo));
    	
    	log.debug("PROJECT UPDATE INFO: {}", model);
    	
    	return "redirect:/project/reportExtrnl";
    }
    
    /**
     * Delete Old Info
     * @param reportExtrnlId
     * @param model
     * @return
     */
    @PostMapping("/reportExtrnl/delete")
    public String delete(@ModelAttribute ReportExtrnlModel reportExtrnlInfo, Model model) {
    	model.addAttribute("reportExtrnlDeleteInfo", reportExtrnlService.deleteReportExtrnl(reportExtrnlInfo));
    	
    	log.debug("PROJECT DELETE INFO: {}", model);
    	
    	return "redirect:/project/reportExtrnl";
    }

}

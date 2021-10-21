package com.shinvest.ap.app.audit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shinvest.ap.app.audit.service.LogService;
import com.shinvest.ap.common.paging.Criteria;

/**
 * 감사관리 컨트롤러
 */
@RequestMapping("/audit")
@Controller
public class AuditController {

    @Resource
    private LogService logService;
    
    //자원 접근 이력
    @GetMapping("/extrnlLog")
    public String extrnlLog(@ModelAttribute Criteria criteria, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) {
//    	if(!StringUtils.isEmpty(startDate)) {
//			criteria.setStartDt(startDate);
//		}
//		if(!StringUtils.isEmpty(endDate)) {
//			endDate = endDate+" 23:59:59";
//			criteria.setEndDt(endDate);
//		}
//        model.addAttribute("logs", logService.selectList(criteria));
//        criteria.setTotalCount(logService.selectListCount(criteria));
//        model.addAttribute("pages", criteria);
        return "audit/extrnlLog";
    }      
    
    //야간사용 관리
    @GetMapping("/nightUsingAprv")
    public String nightUsingAprv(@ModelAttribute Criteria criteria, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) {
//    	if(!StringUtils.isEmpty(startDate)) {
//			criteria.setStartDt(startDate);
//		}
//		if(!StringUtils.isEmpty(endDate)) {
//			endDate = endDate+" 23:59:59";
//			criteria.setEndDt(endDate);
//		}
//        model.addAttribute("logs", logService.selectList(criteria));
//        criteria.setTotalCount(logService.selectListCount(criteria));
//        model.addAttribute("pages", criteria);
        return "audit/nightUsingAprv";
    }      
    
    //사용자 로그 관리
    @GetMapping("/userLog")
    public String userLog(@ModelAttribute Criteria criteria, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) {
    	if(!StringUtils.isEmpty(startDate)) {
			criteria.setStartDt(startDate);
		}
		if(!StringUtils.isEmpty(endDate)) {
			endDate = endDate+" 23:59:59";
			criteria.setEndDt(endDate);
		}
        model.addAttribute("logs", logService.selectUserLogList(criteria));
        criteria.setTotalCount(logService.selectUserLogCount(criteria));
        model.addAttribute("pages", criteria);
        return "audit/userLog";
    }
    
    //관리자 로그 관리
    @GetMapping("/mgrLog")
    public String mgrLog(@ModelAttribute Criteria criteria, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, Model model) {
    	if(!StringUtils.isEmpty(startDate)) {
			criteria.setStartDt(startDate);
		}
		if(!StringUtils.isEmpty(endDate)) {
			endDate = endDate+" 23:59:59";
			criteria.setEndDt(endDate);
		}
        model.addAttribute("logs", logService.selectMgrLogList(criteria));
        criteria.setTotalCount(logService.selectMgrLogCount(criteria));
        model.addAttribute("pages", criteria);
        return "audit/mgrLog";
    }    

}

package com.shinvest.ap.app.monitor;

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

import lombok.extern.slf4j.Slf4j;

/**
 * 모니터링
 */
@RequestMapping("/monitor")
@Controller
@Slf4j
public class MonitorController {

/*
/resource/resourceMonitoring	모니터링/자원 모니터링
/resource/appMonitoring			모니터링/서비스 모니터링
/resource/learningMonitoring	모니터링/학습 모니터링 	
 */
	
    //모니터링/배치 모니터링
    @GetMapping("/batchMonitoring")
    public String batchMonitoring(Model model) {

        return "monitor/batchMonitoring";
    }
    
    //모니터링/ETL 모니터링
    @GetMapping("/etlMonitoring")
    public String etlMonitoring(Model model) {

        return "monitor/etlMonitoring";
    }
    
    //모니터링/로그 모니터링
    @GetMapping("/logMonitoring")
    public String logMonitoring(Model model) {

        return "monitor/logMonitoring";
    }    
	
}

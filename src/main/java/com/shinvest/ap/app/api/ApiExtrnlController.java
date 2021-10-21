package com.shinvest.ap.app.api;

import com.shinvest.ap.app.extrnl.model.ExtrnlModel;
import com.shinvest.ap.app.extrnl.service.ExtrnlService;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * 시스템관리 / 외부시스템 요청 처리 컨트롤러
 *
 */
@Controller
public class ApiExtrnlController {

    @Resource
    private ExtrnlService extrnlService;

 
	/**
	 * 외부 시스템 요청 처리
	 * @param extrnlId
	 * @return
	 */
    @PostMapping("/api/extrnl/{extrnlId}")
    @ResponseBody
    public ExtrnlModel select(@PathVariable String extrnlId) {

        ExtrnlModel extrnlModel = new ExtrnlModel();
        extrnlModel.setExtrnlId(extrnlId);

        return extrnlService.select(extrnlModel);
    }
}

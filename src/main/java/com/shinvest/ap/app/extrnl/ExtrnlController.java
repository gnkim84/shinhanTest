package com.shinvest.ap.app.extrnl;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinvest.ap.app.extrnl.model.ExtrnlModel;
import com.shinvest.ap.app.extrnl.service.ExtrnlService;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

/**
 * 외부 자산 관리 > 외부 시스템 관리	/external/extrnl
 * 외부 자산 관리 > 외부 데이터 관리	/external/data
 *
 */
@RequestMapping("/external")
@Controller
public class ExtrnlController {

    @Resource
    private ExtrnlService extrnlService;

    /**
     * 외부시스템 관리 페이지로 이동한다.
     *
     * @param criteria 페이징 모델
     * @param model
     * @return
     */
    @GetMapping("/extrnl")
    public String extrnl(@ModelAttribute Criteria criteria, Model model) {
        model.addAttribute("extrnls", extrnlService.selectList(criteria));
        criteria.setTotalCount(extrnlService.selectListCount(criteria));
        model.addAttribute("pages", criteria);
        return "extrnl/extrnl";
    }

    /**
     * 외부시스템 관리 데이터를 저장한다.
     *
     * @param extrnlModel
     * @return
     */
    @PostMapping("/extrnl/insert")
    public ResponseEntity<String> save(@ModelAttribute ExtrnlModel extrnlModel, @AuthenticationPrincipal AuthUser authUser) {
        try {
            extrnlModel.setRgstId(authUser.getMemberModel().getUserId());
            extrnlModel.setModiId(authUser.getMemberModel().getUserId());
            String result = extrnlService.save(extrnlModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 외부시스템 관리 데이터를 삭제한다.
     *
     * @param extrnlModel
     * @return
     */
    @PostMapping("/extrnl/delete")
    public ResponseEntity<String> delete(@ModelAttribute ExtrnlModel extrnlModel, @AuthenticationPrincipal AuthUser authUser) {
        try {
            extrnlModel.setRgstId(authUser.getMemberModel().getUserId());
            extrnlModel.setModiId(authUser.getMemberModel().getUserId());
            String result = extrnlService.delete(extrnlModel);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    
    @RequestMapping("/extrnl/detail/{extrnlId}")
    @ResponseBody
    public ExtrnlModel select(@PathVariable String extrnlId) {
        ExtrnlModel extrnlModel  = new ExtrnlModel ();
        extrnlModel.setExtrnlId(extrnlId);

        return extrnlService.select(extrnlModel);
    }    
    
}

package com.shinvest.ap.app.wrkCat;

import com.shinvest.ap.app.wrkCat.model.WrkModel;
import com.shinvest.ap.app.wrkCat.service.WrkService;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

import software.amazon.awssdk.utils.StringUtils;

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
 * 시스템관리 / 업무카테고리 컨트롤러
 *
 */
@RequestMapping("/system")
@Controller
public class WrkCatController {

    @Resource
    private WrkService wrkService;
    
    @Resource
    private IdUtil idUtil;

    /**
     * 업무카테고리 페이지로 이동한다.
     *
     * @param criteria 페이징 모델
     * @param model
     * @return
     */
    @GetMapping("/wrkCat")
    public String wrkCat(@ModelAttribute Criteria criteria, Model model) {

        model.addAttribute("wrks", wrkService.selectList(criteria));
        criteria.setTotalCount(wrkService.selectListCount(criteria));
        model.addAttribute("pages", criteria);

        return "wrkCat/wrkCat";
    }

    /**
     * 업무카테고리 페이지로 이동한다.
     *
     * @param criteria 페이징 모델
     * @param attributes 페이지로 데이터 전달속성
     * @return
     */
    @PostMapping("/wrkCat")
    public String wrkCat(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {

        attributes.addFlashAttribute("criteria", criteria);

        return "redirect:/system/wrkCat";
    }

    /**
     * 업무카테고리 모델을 저장한다.
     *
     * @param wrkModel
     * @return
     */
    @PostMapping("/wrkCat/insert")
    public ResponseEntity<String> save(@ModelAttribute WrkModel wrkModel, @AuthenticationPrincipal AuthUser authUser) {

        try {
        	if (StringUtils.isBlank(wrkModel.getWrkId())) {
        		wrkModel.setWrkId(idUtil.getWrkId());
        		wrkModel.setRgstId(authUser.getMemberModel().getUserId());
        		wrkModel.setModiId(authUser.getMemberModel().getUserId());
        		String result = wrkService.save(wrkModel);
        		return new ResponseEntity<>(result, HttpStatus.OK);
        	} else {
        		wrkModel.setRgstId(authUser.getMemberModel().getUserId());
        		wrkModel.setModiId(authUser.getMemberModel().getUserId());
        		
        		String result = wrkService.save(wrkModel);
        		return new ResponseEntity<>(result, HttpStatus.OK);
        	}

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 업무카테고리 모델을 삭제한다.
     *
     * @param wrkModel
     * @return
     */
    @PostMapping("/wrkCat/delete")
    public ResponseEntity<String> delete(@ModelAttribute WrkModel wrkModel, @AuthenticationPrincipal AuthUser authUser) {

        try {
            wrkModel.setRgstId(authUser.getMemberModel().getUserId());
            wrkModel.setModiId(authUser.getMemberModel().getUserId());

            String result = wrkService.delete(wrkModel);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping("/wrkCat/detail/{wrkId}")
    @ResponseBody
    public WrkModel select(@PathVariable String wrkId) {
        WrkModel wrkModel = new WrkModel();
        wrkModel.setWrkId(wrkId);

        return wrkService.select(wrkModel);
    }
}

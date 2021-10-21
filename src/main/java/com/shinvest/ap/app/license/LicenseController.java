package com.shinvest.ap.app.license;

import com.shinvest.ap.app.license.model.LicenseModel;
import com.shinvest.ap.app.license.service.LicenseService;
import com.shinvest.ap.app.role.model.RoleModel;
import com.shinvest.ap.app.role.service.RoleService;
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
import javax.ws.rs.Path;
import java.security.Principal;

/**
 * 사용자관리/라이센스관리 컨트롤러
 *
 */
@RequestMapping("/system")
@Controller
public class LicenseController {

    @Resource
    private RoleService roleService;

    @Resource
    private LicenseService licenseService;

    /**
     * 라이센스관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/license")
    public String list(@ModelAttribute Criteria criteria, Model model) {

        model.addAttribute("roles", roleService.selectAllList());
        model.addAttribute("licenses", licenseService.selectList(criteria));
        criteria.setTotalCount(licenseService.selectListCount(criteria));
        model.addAttribute("pages", criteria);

        return "license/license";
    }

    /**
     * 라이센스 페이지로 이동한다. 페이징시에 파라미터 노출을 하지 않기 위해서 redirect하기 위한 기능
     * @param criteria      // 페이징 모델
     * @param attributes    // 권한 페이지로 모델을 전달하기 위한 속성
     * @return
     */
    @PostMapping("/license")
    public String list(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {

        attributes.addFlashAttribute("criteria", criteria);

        return "redirect:/license";
    }

    /**
     * 라이센스 모델을 저장한다. 라이센스모델이 존재하면 업데이트 없으면 신규생성한다.
     *
     * @param licenseModel
     * @return      Insert/Update/Fail을 페이지로 리턴하며 alert메시지 처리
     */
    @PostMapping("/license/insert")
    public ResponseEntity<String> insert(@ModelAttribute LicenseModel licenseModel, @AuthenticationPrincipal AuthUser authUser) {

        try {
            licenseModel.setRgstId(authUser.getMemberModel().getUserId());
            licenseModel.setModiId(authUser.getMemberModel().getUserId());
            String result = licenseService.save(licenseModel);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 라이센스 모델을 삭제한다.
     *
     * @param licenseModel
     * @return      Delete/Fail을 페이지로 리턴하며 alert메시지 처리
     */
    @PostMapping("/license/delete")
    public ResponseEntity<String> delete(@ModelAttribute LicenseModel licenseModel, Principal principal) {

        try {
            licenseModel.setRgstId(principal.getName());
            licenseModel.setModiId(principal.getName());
            String result = licenseService.delete(licenseModel);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/license/detail/{roleId}/{licenseId}")
    @ResponseBody
    public LicenseModel select(@PathVariable String roleId, @PathVariable String licenseId) {

        LicenseModel licenseModel = new LicenseModel();
        licenseModel.setAuthId(roleId);
        licenseModel.setLicenseId(licenseId);

        return licenseService.select(licenseModel);
    }
}

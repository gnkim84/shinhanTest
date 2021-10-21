package com.shinvest.ap.app.menuAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.dept.service.DeptService;
import com.shinvest.ap.app.member.model.MemberCriteria;
import com.shinvest.ap.app.member.service.MemberService;
import com.shinvest.ap.app.menu.model.MenuModel;
import com.shinvest.ap.app.menu.service.MenuService;
import com.shinvest.ap.app.role.service.RoleService;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

/**
 * 시스템관리 / 메뉴관리 컨트롤러
 *
 */
@RequestMapping("/system")
@Controller
public class MenuAuthController {

	@Resource
	private MenuService menuService;
	
	@Resource
	private RoleService roleService;

	@Resource
	private CodeService codeService;
	
	@Resource
	private DeptService deptService;
	
	@Resource
	private MemberService memberService;
	
	/**
	 * 메뉴권한관리 페이지로 이동한다.
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/menuAuth")
	public String menu(@ModelAttribute MemberCriteria Mcriteria,@ModelAttribute Criteria criteria ,Model model) {
		List<MenuModel> list = menuService.selectList();
    	String rootMenuId = null;
    	for (MenuModel menu : list) {
    		if (menu.getLv() == 0) {
    			rootMenuId = menu.getMenuId();
    			break;
    		}
    	}
    	model.addAttribute("roles", roleService.selectList(criteria));
        model.addAttribute("menus", list);
        model.addAttribute("rootMenuId", rootMenuId);
        
        // 화면 표시용 코드 셋팅
        // 회사구분 코드
        model.addAttribute("codeCompanyCdList", codeService.selectGroupIdAllList("COMPANY_CODE"));
        // 사용여부 코드
        model.addAttribute("codeUseYnList", codeService.selectGroupIdAllList("USE_YN"));        
        // 검색구분 코드
        model.addAttribute("codeMemSearchCdList", codeService.selectGroupIdAllList("USER_SEARCH_CODE"));

        // 권한코드 조회
        model.addAttribute("roles", roleService.selectAllList());
        // 부서 조회
        model.addAttribute("depts", deptService.selectDeptClList());

        model.addAttribute("members", memberService.selectMemberList(Mcriteria));
        criteria.setTotalCount(memberService.selectMemberListCount(Mcriteria));
        model.addAttribute("pages", Mcriteria);
        return "menu/menuAuth";
	}

	@PostMapping("/menuAuth")
	@ResponseBody
	public List<MenuModel> AuthSearch(Model model, MenuModel menuModel, String authId, HttpServletRequest request) {
		List<MenuModel> list = new ArrayList<>();
		menuModel.setUseYn("Y");
		return list;
	}
	
	/**
	 * 권한별 메뉴 조회
	 * @param request
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/menuAuth/{authId}/popup", method=RequestMethod.POST)
	public Map<String,Object> menuAuth(HttpServletRequest request, @AuthenticationPrincipal AuthUser authUser, @PathVariable String authId) {
		Map<String,Object> result = new HashMap<String, Object>();
		boolean res = false;
		
		List<MenuModel> menuAuths = menuService.selectMenuListWithAuth(authId);
		
		if(menuAuths != null) {
			res = true;
			result.put("menuAuths", menuAuths);
		}
		result.put("result", res);
		return result;
	}

	/**
	 * 권한별 메뉴 저장
	 * @param request
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/menuAuth/update/{authId}/popup", method=RequestMethod.POST)
	public Map<String,Object> updateMenuAuth(HttpServletRequest request, @AuthenticationPrincipal AuthUser authUser, @PathVariable String authId, @ModelAttribute MenuModel menuModel) {
		Map<String,Object> result = new HashMap<String, Object>();
		boolean res = false;
		
		JSONArray jsons = menuModel.getTreeJson();
		
		for(int i=0; i < jsons.length();i++) {
			JSONObject json = jsons.getJSONObject(i);
			MenuModel model = new MenuModel();
			model.setAuthId(authId);
			model.setMenuId(json.getString("menuId"));
			String authUseYn = "N";
			if("true".equals(json.getString("checked"))) {
				authUseYn = "Y";
			}
			model.setAuthUseYn(authUseYn);
			model.setModiId(authUser.getUsername());
			long menuAuths = menuService.updateMenuListWithAuth(model);
			if(menuAuths > 0) {
				res = true;
			}else {
				res = false;
			}
		}
		result.put("result", res);
		return result;
	}
}

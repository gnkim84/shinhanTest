package com.shinvest.ap.app.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartRequest;

import com.shinvest.ap.app.menu.model.MenuModel;
import com.shinvest.ap.app.menu.service.MenuService;
import com.shinvest.ap.config.security.AuthUser;

/**
 * 시스템관리 / 메뉴관리 컨트롤러
 *
 */
@RequestMapping("/system")
@Controller
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 메뉴관리 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/menu")
    public String menu(Model model) {
    	List<MenuModel> list = menuService.selectList();
    	String rootMenuId = null;
    	for (MenuModel menu : list) {
    		if (menu.getLv() == 0) {
    			rootMenuId = menu.getMenuId();
    			break;
    		}
    	}
        model.addAttribute("menus", list);
        model.addAttribute("rootMenuId", rootMenuId);
        return "menu/menu";
    }

    /**
     * 메뉴관리 모델을 저장한다.
     *
     * @param menuModel 메뉴모델
     * @param authUser
     * @return
     */
    @PostMapping("/menu/insert")
    public String save(@ModelAttribute MenuModel menuModel, @AuthenticationPrincipal AuthUser authUser) {
    	
    	
        try {
            menuModel.setRgstId(authUser.getMemberModel().getUserId());
            menuModel.setModiId(authUser.getMemberModel().getUserId());

            long count = menuService.save(menuModel);

            return "redirect:/system/menu";
        } catch (Exception e) {
            return "forward:/system/menu";
        }
    }

    /**
     * 메뉴모델을 삭제한다.
     *
     * @param menuModel
     * @param authUser
     * @return
     */
    @PostMapping("/menu/delete")
    public String delete(@ModelAttribute MenuModel menuModel, @AuthenticationPrincipal AuthUser authUser) {

        try {
            menuModel.setRgstId(authUser.getMemberModel().getUserId());
            menuModel.setModiId(authUser.getMemberModel().getUserId());

            long count = menuService.delete(menuModel);

            return "redirect:/menu";
        } catch (Exception e) {
            return "forward:/menu";
        }
    }

    /**
     * 메뉴검색 팝업으로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/menu/popup")
    public String menuPopup(Model model) {

        model.addAttribute("menus", menuService.selectList());

        return "popup/upperMenuPopup";
    }
}

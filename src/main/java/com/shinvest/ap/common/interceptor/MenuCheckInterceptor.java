package com.shinvest.ap.common.interceptor;

import java.util.List;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.binding.BindingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.shinvest.ap.app.menu.model.MenuModel;
import com.shinvest.ap.app.menu.service.MenuService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 호출되는 MenuURL을 기반으로 Submenu
 */
@Slf4j
public class MenuCheckInterceptor implements HandlerInterceptor {

	@Resource
	private MenuService menuService;

	/**
	 * 컨트롤러 호출후 호출된 URL을 기반으로 레프트메뉴에 대한 목록을 조회해서 페이지로 전달한다.
	 *
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AuthUser) {
					AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
					// detail, regist, modify : 화면
					// insert, update, delete : 동작
					String uri = request.getRequestURI();
					Matcher matcher = Constant.UrlPattern.MENU_CHECK_PATTERN.matcher(uri);
					if (matcher.find()) {
						uri = uri.substring(0, matcher.start());
					}
					
					try {
						String upperMenuId = menuService.selectUpperMenuIdForMenuUrl(uri);
						List<MenuModel> subMenuList = menuService.selectLeftMenuListWithAuth(upperMenuId,authUser.getMemberModel().getAuthId());
						modelAndView.addObject("subMenuList", subMenuList);
						modelAndView.addObject("myUri", request.getRequestURI());
					} catch (BindingException e) {
						log.warn("urlCheck Error {}", e.getMessage());
					}
				}
			}
		}
	}
}

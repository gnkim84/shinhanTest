package com.shinvest.ap.app.menu.mapper;

import com.shinvest.ap.app.menu.model.MenuModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

import java.util.List;
import java.util.Map;

/**
 * Mybatis 코드관리 매핑 Interface
 */
@ConnMapperFirst
public interface MenuMapper {

    /**
     * 메뉴모델 리스트를 조회한다.
     *
     * @return
     */
    List<MenuModel> selectList();

    /**
     * 메뉴모델을 조회한다.
     *
     * @param model
     * @return
     */
    MenuModel select(MenuModel model);

    /**
     * 메뉴모델을 삭제한다.
     * @param model
     * @return
     */
    long delete(MenuModel model);

    /**
     * 메뉴모델을 업데이트 한다.
     *
     * @param model
     * @return
     */
    long update(MenuModel model);

    /**
     * 메뉴모델을 생성한다.
     *
     * @param model
     * @return
     */
    long insert(MenuModel model);

    
    
    
    /**
     * 메뉴URL 조건의 상위메뉴ID를 조회한다. 메뉴URL의 하위메뉴를 조회하기 위해서 상위메뉴ID를 조회한다.
     *
     * @param model
     * @return
     */
    String selectUpperMenuIdForMenuUrl(MenuModel model);
    
    /**
     * 최상위 메뉴 조회
     */
    List<MenuModel> selectTopMenuListWithAuth(String authId);
    
    /**
     * 권한과 상위 메뉴 ID에 따른 메뉴 리스트를 조회
     */
    List<MenuModel> selectLeftMenuListWithAuth(Map<String,String> param);
    
    /**
     * 권한으로 접근 가능한 첫번째 메뉴 URL 조회
     */
    String selectFirstMenuUrl(Map<String,String> param);
    
    /**
     * 권한별 메뉴 리스트
     */
    List<MenuModel> selectMenuListWithAuth(String authId);
    
    /**
     * 권한별 메뉴를 업데이트 한다.
     *
     * @param model
     * @return
     */
    long updateMenuListWithAuth(MenuModel model);
}

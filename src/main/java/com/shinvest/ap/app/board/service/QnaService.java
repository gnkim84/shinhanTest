package com.shinvest.ap.app.board.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.shinvest.ap.app.board.mapper.QnaMapper;
import com.shinvest.ap.app.board.model.QnaModel;
import com.shinvest.ap.app.board.model.QnaModel;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.paging.Criteria;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 713034
 * Notice Service
 */
@Slf4j
@Service
public class QnaService {

    @Resource
    private QnaMapper qnaMapper;
    
	@Resource(name="idUtil")
	private IdUtil idUtil;

	/**
	 * Search All Notice List
	 * @param none
	 * @return List<Object>
	 */
	public List<QnaModel> selectQnaList(Criteria criteria) {
		return qnaMapper.selectQnaList(criteria);
	}
	
	public int selectQnaListCount(Criteria criteria) {
		return qnaMapper.selectQnaListCount(criteria);
	}
	
	
	public QnaModel selectQna(QnaModel model) {
		return qnaMapper.selectQna(model);
	}
	
	public long deleteQna(QnaModel model) {
		return qnaMapper.deleteQna(model);
	}

	public long updateQna(QnaModel model) {
		return qnaMapper.updateQna(model);
	}
	
	/**
	 * Get Qna Id
	 * @return
	 */
	public String getQnaId() {
		
		String QnaId = idUtil.getQnaId();
		return QnaId;
		
	}
	
	
	
//	/**
//	 * 쪼개기 테스트
//	 * Search List With Only Text
//	 * @param srchTxt
//	 * @return
//	 */
//	public Map<String, Object> searchListOnlyText(String srchTxt) {
//		
//		Map<String, Object> tmpParam = new HashMap<String, Object>();
//		Map<String, Object> tmpPagination = new HashMap<String, Object>();
//		
//		if (srchTxt != null) {			
//			tmpParam.put("stdCefcSearchText", srchTxt);
//		}
//		
//		tmpPagination.put("rowCountPerPage", "10");
//		tmpPagination.put("currentPageNo", "1");
//		tmpPagination.put("unitPageCount", "10");
//		
//		tmpParam.put("pagination", tmpPagination);
//		
//		Map<String, Object> result = searchList(tmpParam);
//		
//		return result;
//	}
//	
//	/**
//	 * Search metadata
//	 * @param userId2 
//	 * @param none
//	 * @return List<Object>
//	 */
//	public Map<String, Object> searchList(Map<String, Object> param) {
//		
//		Map<String, Object> result = new HashMap<String, Object>();
//		
//		if(param.get("stdCefcClsCds") != null) {
//			String[] stdCefcClsCds = param.get("stdCefcClsCds").toString().split(",");
//			List<String> list = Arrays.asList(stdCefcClsCds);
//			List<String> newList = new ArrayList<String>();
//			
//			JSONArray arr = util.searchClassify();
//			
//			if (arr != null && arr.length() != 0) {
//				
//				for(int j = 0 ; j < list.size() ; j++) {
//					for (int i = 0; i < arr.length(); i++) {
//						JSONObject tmp = arr.getJSONObject(i);
//						boolean check = true;
//						
//						// 하위 분류 가 있는지 확인
//						if(!tmp.get("stdCefcClsCd").toString().equals(list.get(j).toString()) && tmp.get("stdCategoryLevel").toString().equals("03")) {
//							if(tmp.get("stdCefcClsCd").toString().startsWith(list.get(j).toString()) && tmp.get("stdCefcClsCd").toString().length() > list.get(j).toString().length()) {
//								for(int k = 0 ; k < newList.size() ; k++) {
//									if(newList.get(k).toString().equals(tmp.get("stdCefcClsCd").toString())) {
//										check = false;
//										break;
//									}
//								}
//								if(check) {
//									newList.add(tmp.get("stdCefcClsCd").toString());
//								}
//							}
//						}
//						
//						// 소문류 일 경우 
//						if(tmp.get("stdCefcClsCd").toString().equals(list.get(j).toString()) && tmp.get("stdCategoryLevel").toString().equals("03")) {
//								
//							for(int k = 0 ; k < newList.size() ; k++) {
//								if(newList.get(k).toString().equals(tmp.get("stdCefcClsCd").toString())) {
//									check = false;
//									break;
//								}
//							}
//							if(check) {
//								newList.add(tmp.get("stdCefcClsCd").toString());
//							}
//						}
//					}
//				}
//			}
//			String codeList = "";
//			for(int j = 0 ; j < newList.size() ; j ++) {
//				if(newList.size() == j+1) {
//					codeList += newList.get(j).toString();
//				}else {
//					codeList += newList.get(j).toString() + "|";
//				}
//			}
//			
//			if(param.get("stdCefcSearchText").toString().startsWith("테스트0::")) {
//				param.put("stdCefcSearchText", param.get("stdCefcSearchText").toString().replace("테스트0::", ""));
//				param.put("stdCefcClsCds", "10_02_01");
//			}else if(param.get("stdCefcSearchText").toString().startsWith("테스트1::")) {
//				param.put("stdCefcSearchText", param.get("stdCefcSearchText").toString().replace("테스트1::", ""));
//				param.put("stdCefcClsCds", "10_02_01|10_03_03");
//			}else if(param.get("stdCefcSearchText").toString().startsWith("테스트2::")) {
//				param.put("stdCefcSearchText", param.get("stdCefcSearchText").toString().replace("테스트2::", ""));
//				param.put("stdCefcClsCds", "20_01_01");
//			}else if(param.get("stdCefcSearchText").toString().startsWith("테스트3::")) {
//				param.put("stdCefcSearchText", param.get("stdCefcSearchText").toString().replace("테스트3::", ""));
//				param.put("stdCefcClsCds", "30_01_01");
//			}else {
//				param.put("stdCefcClsCds", codeList);
//			}
//		}
//		
//		JSONObject jsonParam = new JSONObject(param);
//		
//		log.debug("metadataPARAM: {}", param);
//		
//		JSONObject obj = util.searchList(jsonParam);
//		
//		if (obj.has("bizMetaMsts") && obj.get("bizMetaMsts") != JSONObject.NULL) {
//			JSONArray arr = (JSONArray)obj.get("bizMetaMsts");
//			if (arr != null && arr.length() != 0) {
//				List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
//				for (int i = 0; i < arr.length(); i++) {
//					JSONObject tmp = arr.getJSONObject(i);
//					tmpList.add(tmp.toMap());
//				}
//				result.put("bizMetaMsts", tmpList);
//			}
//			JSONObject seachParamObj = obj.getJSONObject("seachParam");
//			log.debug("metadataPAGE: {}", seachParamObj);
//			result.put("seachParam", seachParamObj.toMap());
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * Search metadataDetail
//	 * @param userId2 
//	 * @param none
//	 * @return List<Object>
//	 */
//	public Map<String, Object> searchDetail(Map<String, Object> param) {
//	
//	   Map<String, Object> result = new HashMap<String, Object>();
//		
//		JSONObject jsonParam = new JSONObject(param);
//		
//		log.debug("metadataPARAM: {}", param);
//		
//		JSONObject obj = util.searchDetail(jsonParam);
//
//		log.debug("metadataOBJ: {}", obj);
//		
//		if (obj.has("bizMetaMst") && obj.get("bizMetaMst") != JSONObject.NULL) {
//			JSONObject metaObj = obj.getJSONObject("bizMetaMst");
//			result.put("bizMetaMst", metaObj.toMap());
//		}
//		
//		if (obj.has("bizMetaDtls") && obj.get("bizMetaDtls") != JSONObject.NULL) {
//			JSONArray arr = (JSONArray)obj.get("bizMetaDtls");
//			if (arr != null && arr.length() != 0) {
//				List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
//				for (int i = 0; i < arr.length(); i++) {
//					JSONObject tmp = arr.getJSONObject(i);
//					tmpList.add(tmp.toMap());
//				}
//				result.put("bizMetaDtls", tmpList);
//			}
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * Search category
//	 * @param none
//	 * @return List<Object>
//	 */
//	public List<Map<String, Object>> categorySearchList(Map<String, Object> param) {
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		log.debug("categorySearchList param: " + param);
//		JSONArray arr = util.searchClassify();
//		if (arr != null && arr.length() != 0) {
//			boolean firstNode = true;
//			for (int i = 0; i < arr.length(); i++) {
//				JSONObject tmp = arr.getJSONObject(i);
//				if(tmp.get("stdCefcClsCdName").toString().indexOf(param.get("stdCefcSearchText").toString()) > -1){
//					if(!param.get("classifyCodes").toString().equals("") && param.get("classifyCodes") != null) {
//						
//						log.debug("categorySearchList BIG in");
//						
//						if(tmp.get("stdCategoryLevel").toString().equals("02") &&
//								tmp.get("stdCefcClsCd").toString().startsWith(param.get("classifyCodes").toString()) && firstNode) {
//							for(int j = 0 ; j < arr.length() ; j++) {
//								JSONObject tmp2 = arr.getJSONObject(j);	
//								if(tmp.get("categoryUpId").toString().equals(tmp2.get("stdCefcClsCd").toString())){
//									firstNode = false;
//									result.add(tmp2.toMap());
//									break;
//								}
//							}
//						}else {
//							if(tmp.get("stdCategoryLevel").toString().equals("03")) {
//								for(int j = 0 ; j < arr.length() ; j++) {
//									JSONObject tmp2 = arr.getJSONObject(j);
//								}
//							}
//							if(tmp.get("stdCefcClsCd").toString().startsWith(param.get("classifyCodes").toString())) {
//								result.add(tmp.toMap());	
//							}
//						}
//						
//					}else {
//						if(tmp.get("stdCategoryLevel").toString().equals("02") && firstNode) {
//							for(int j = 0 ; j < arr.length() ; j++) {
//								JSONObject tmp2 = arr.getJSONObject(j);	
//								if(tmp.get("categoryUpId").toString().equals(tmp2.get("stdCefcClsCd").toString())){
//									firstNode = false;
//									result.add(tmp2.toMap());
//									break;
//								}
//								
//							}
//						}else {
//							result.add(tmp.toMap());
//						}
//					}
//				}
//			}
//		}
//		log.debug("categorySearchList result: "+ result.toString());
//		
//		return result;
//	}
	
}

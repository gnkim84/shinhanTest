package com.shinvest.ap.common.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.shinvest.ap.common.ApiRequestUtil;
import com.shinvest.ap.config.props.GoldwingProps;
import com.shinvest.ap.socket.EaiSocketClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Goldwing 결재 관련 서비스
 * 프로젝트, 보고서 등 결재가 필요한 화면에서 사용한다.
 */
@Slf4j
@Service
public class GoldwingService {

    @Resource(name="eaiSocketClient")
    private EaiSocketClient eaiSocketClient;
    
	@Resource(name = "goldwingProps")
	private GoldwingProps goldwingProps;
	
	@Resource(name="apiRequestUtil")
	private ApiRequestUtil apiRequestUtil;		
    
    public String getGoldwingUrl(String userId, String companyCode, String aprvId, String aprvType) {
    	JSONObject result = eaiSocketClient.requestToken(userId, companyCode);  
    	
    	String goldwingUrl = "";
    	if ("S".equals(result.get("result").toString())) {
    		String conndocUrl = goldwingProps.getConndocUrl();
    		String svcCode = goldwingProps.getSvcCode();
    		String token = result.get("token").toString();
    		String formId = goldwingProps.getFormId();
    		String eaiInterfaceId = goldwingProps.getEaiInterfaceId();
    		String eaiServiceCode = goldwingProps.getEaiServiceCode();

    		//step1 : connkeyParam가공 (connkeyParam은 URLEncoding하여 _connkey_=??? 이런식으로 넘겨준다. '키=밸류'의 구분자는 '|'으로 한다.)
    		String portalKey = StringUtils.joinWith("_", aprvId, aprvType);	//승인키 : aprvId (ex. ar21000000015), 승인타입 : aprvType (ex. project, reportrole) 
    		String connkey = StringUtils.joinWith("|",
    				//시스템 정보 구분 키 (※SvcCode 동일)
    				StringUtils.joinWith("=", "sys", svcCode),
    				//현업시스템 서비스 정보 (※EAI 또는 HTTP)
    				StringUtils.joinWith("=", "Method", "EAI"),
    				//EAI 인터페이스ID
    				StringUtils.joinWith("=", "EAIInterfaceID", eaiInterfaceId),
    				//EAI 서비스코드
    				StringUtils.joinWith("=", "EAIServiceCode", eaiServiceCode),        				
    				//현업시스템 트랜잭션 고유키
    				StringUtils.joinWith("=", "key",portalKey)
    		);
    		
    		//step2 : 실제 최종적으로 보내질 commonParam 가공
    		Map<String, String> commonParam = new HashMap<String, String>();
    		commonParam.put("SvcCode",		svcCode);
    		commonParam.put("Token",		token);
    		commonParam.put("Mode",			"DRAFT");	//※DRAFT:기안, VIEW:조회
    		commonParam.put("APRTYPE",		"COMPANY");	//※COMPANY:회사양식, COMMON:그룹사공통양식
    		commonParam.put("formURL",		formId);
    		commonParam.put("ComID",		companyCode);
    		commonParam.put("_connkey_",	connkey);
    		
    		//step3 : url 생성
    		goldwingUrl = apiRequestUtil.buildGetUrl(conndocUrl, commonParam);
    		
    	}
    	return goldwingUrl;
    }
}

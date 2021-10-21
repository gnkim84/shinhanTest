package com.shinvest.ap.app.test;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinvest.ap.app.test.service.AsyncService;
import com.shinvest.ap.app.test.service.TestService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.annotation.NoLogging;
import com.shinvest.ap.common.aws.AwsLambdaUtil;
import com.shinvest.ap.common.aws.AwsSageMakerUtil;
import com.shinvest.ap.common.service.GoldwingService;
import com.shinvest.ap.config.props.TableauProps;
import com.shinvest.ap.socket.EaiSocketClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Profile({Constant.Profile.DEV,Constant.Profile.LOCAL})
public class TestController {
	
	@Resource(name="testService")
	private TestService service;

    @Resource
    private AsyncService asyncService;
    
    @Resource(name="tableauProps")
	private TableauProps tableauProps;
     
    @Resource(name="awsLambdaUtil")
    private AwsLambdaUtil awsLambdaUtil;
    
    @Resource(name="awsSageMakerUtil")
    private AwsSageMakerUtil awsSageMakerUtil;
    
    @Resource(name="eaiSocketClient")
    private EaiSocketClient eaiSocketClient;
    
	@Resource(name="goldwingService")
	private GoldwingService goldwingService;    

    @NoLogging
    @GetMapping("/test")
    public String test() {
        log.debug("call test page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("authentication getName : ",auth.getName());
		Object obj = auth.getPrincipal();
		if (obj != null) {
			if (obj instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) obj;
				log.debug("username : ", userDetails.getUsername());
			} else {
				log.debug("user principal is not UserDetails - obj : {}",obj.toString());
			}
		} else {
			log.debug("user principal is null");
		}
        return "test";
    }
    
    @NoLogging
    @GetMapping("/test/async")
    public void testAsync() {
        for(int i = 0; i < 50; i++) {
            asyncService.asyncHello(i);
        }
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/props")
    public Map<String,String> testProps() {
    	System.setProperty("TEST", "TEST_PROPS");
    	log.debug("TEST PROPS : {}",System.getProperty("TEST"));
    	
    	Map<String,String> env = System.getenv();
    	for (String key : env.keySet()) {
    		log.debug("{} : {}",key,env.get(key));
    	}
    	
    	log.debug("################### tableauProps - userPw : {}",tableauProps.getUserpw());
    	
    	return env;
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/tableau")
    public String testTableau() {
    	log.debug("TEST Tableau");
    	
    	String test = service.testTableau();
    	
    	return test;
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/aws/lambda")
    public String testAwsLambda() {
    	log.debug("TEST Aws Lambda");
    	JSONObject result = awsLambdaUtil.invokeLambda("W-SHI-AN2-DAP-DEV-LM-SBX-LIST-USER-PROFILES", new JSONObject());
    	return result.toString();
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/aws/sageMaker")
    public String testAwsSageMaker() {
    	log.debug("TEST Aws SageMaker");
    	
    	JSONObject params = new JSONObject();
    	params.put("pageSize", 15);
    	JSONObject apps = awsSageMakerUtil.listApps(params);
    	JSONObject jobs = awsSageMakerUtil.listJobs(params);
    	JSONObject userProfiles = awsSageMakerUtil.listUserProfiles(params);
    	
    	params.put("apps",apps);
    	params.put("jobs",jobs);
    	params.put("userProfiles",userProfiles);
    	return params.toString();
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/socket/client/{userId}")
    public String testEaiSocketClient(@PathVariable String userId) {
    	log.debug("TEST EAI Socket Client");
    	JSONObject params = new JSONObject();
    	if (StringUtils.isBlank(userId)) {
    		params.put("userId", "713033");
    	} else {
    		params.put("userId", userId);
    	}
    	params.put("companyCode", "GS");
    	JSONObject result = eaiSocketClient.requestToken(params);
    	return result.toString();
    }
    
    @NoLogging
    @ResponseBody
    @GetMapping("/test/goldwing/url/{userId}/{aprvId}/{aprvTy}")
    public String testGoldwingUrl(@PathVariable String userId, @PathVariable String aprvId, @PathVariable String aprvTy) {
    	log.debug("TEST testGoldwingUrl");
    	// 승인 타입
    	// 보고서 게시 : aprvReportOpen
    	// 보고서 역할 : aprvReportRole
    	// 프로젝트 승인 : aprvProject
    	// 프로젝트 야간 사용 : aprvProjectNight
    	// 사용자 권한 : aprvAuthChange
    	// 데이터 권한 : aprvDataResource
    	// 모델 배포 승인 : aprvModelDeploy
    	String goldwingUrl = goldwingService.getGoldwingUrl(userId, "GS", aprvId, aprvTy);
    	
    	return goldwingUrl;
    }    
    
}

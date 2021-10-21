package com.shinvest.ap.app.report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shinvest.ap.app.audit.model.LogModel;
import com.shinvest.ap.app.audit.service.LogService;
import com.shinvest.ap.app.code.model.CodeModel;
import com.shinvest.ap.app.dept.service.DeptService;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.report.model.ReportModel;
import com.shinvest.ap.app.report.service.ReportService;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.annotation.NoLogging;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.common.tableauapi.model.StringUtil;
import com.shinvest.ap.common.tableauapi.service.TableauCommonService;
import com.shinvest.ap.config.security.AuthUser;
import com.sun.jersey.api.uri.UriComponent;
import com.shinvest.ap.config.props.TableauProps;
import com.shinvest.ap.common.ApiRequestUtil;
import com.shinvest.ap.common.Constant;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로젝트관리/보고서관리 컨트롤러
 */
@RequestMapping("/project")
@Controller
@Slf4j
public class ReportController {
	
	@Resource
	private TableauCommonService commonService;	
	
	@Resource(name="apiRequestUtil")
	private ApiRequestUtil apiRequestUtil;		
	
	@Resource
	IdUtil idUtil;
	
	@Resource
	ReportService reportService;

	@Resource
	LogService logService;

	@Resource
	DeptService deptService;
	
	@Resource(name="tableauProps")
	private TableauProps props;
	
	/**
	 * 보고서관리 페이지로 이동한다.
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/report")
	public String report(@ModelAttribute Criteria criteria, Model model) {
		model.addAttribute("reportList", reportService.selectReportList(criteria));
		criteria.setTotalCount(reportService.selectReportListCount(criteria));
		model.addAttribute("pages", criteria);
		return "report/report";
	}
	
	/**
	 * 보고서관리 페이지로 이동한다.
	 *
	 * @param model
	 * @return
	 */
	@PostMapping("/report")
	public String report(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {
		attributes.addFlashAttribute("criteria", criteria);
		return "redirect:/project/report";
	}

	/**
	 * 보고서 등록페이지로 이동한다.
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/report/regist")
	public String reportRegist(@AuthenticationPrincipal AuthUser authUser, Model model) {
		MemberModel user = authUser.getMemberModel();
		reportService.getVWProjectList(user, model);
		reportService.selectAllReportCodeList(model);
		
		model.addAttribute("user", user);
		
		//태블로 워크북 목록		
		model.addAttribute("tableauWorkbookList", reportService.selectTableauWorkbookList());
		
		return "report/reportRegist";
	}

	/**
	 * 보고서 변경페이지로 이동한다.
	 * @param reportId
	 * @param model
	 * @return
	 */
	@GetMapping("/report/modify/{reportId}")
	public String select(@PathVariable String reportId, @AuthenticationPrincipal AuthUser authUser, Model model) {
		ReportModel reportModel = new ReportModel();
		MemberModel user = authUser.getMemberModel();
		reportModel.setReportId(reportId);
		reportService.getVWProjectList(user, model);
		reportService.selectAllReportCodeList(model);
		
		ReportModel reportInfo = reportService.selectReport(reportModel);
		
		model.addAttribute("user", user);
		model.addAttribute("reportInfo", reportInfo);
		if(reportInfo.getReportAttr() != null) {
			model.addAttribute("reportAttrs", reportInfo.getReportAttr().optJSONArray("params"));
		}

		// 부서 조회
        model.addAttribute("depts", deptService.selectDeptClList());
		
		//태블로 워크북 목록
		//model.addAttribute("tableauWorkbookList", reportService.selectTableauWorkbookList());
		
		return "report/reportRegist";
	}
	
	/**
	 * 보고서 신규 생성
	 * @param reportInfo
	 * @param authUser
	 * @param model
	 * @return
	 */
	@PostMapping("/report/insert")
	public String insert(@ModelAttribute ReportModel reportInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
		if (reportInfo.getTableauWorkbookId() != null && !"".equals(reportInfo.getTableauWorkbookId().trim())) {
			MemberModel user = authUser.getMemberModel();
			reportInfo.setRgstId(user.getUserId());
			reportInfo.setModiId(user.getUserId());
			reportInfo.setReportId(idUtil.getReportId());
			model.addAttribute("reportInsertInfo", reportService.insertReport(reportInfo));

			//해당 보고서의 각 사용자별 태블로 권한부여
			//reportService.addTableauUserAuth(reportInfo);
			
			log.debug("PROJECT INSERT INFO: {}", model);
		} else {
			log.warn("{} 오류 발생", Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		return "redirect:/project/report";
	}
	
	
	/**
	 * Update Old Info
	 * @param reportInfo
	 * @param authUser
	 * @param model
	 * @return
	 */
	@PostMapping("/report/update")
	public String update(@ModelAttribute ReportModel reportInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
		if (reportInfo.getTableauWorkbookId() != null && !"".equals(reportInfo.getTableauWorkbookId().trim())) {
			MemberModel user = authUser.getMemberModel();
			reportInfo.setModiId(user.getUserId());
			
			if(reportInfo.getDeptMngYn() == null) {
				reportInfo.setDeptMngYn("N");
			}
			
			model.addAttribute("reportUpdateInfo", reportService.updateReport(reportInfo, user));
			
			//사용자, 관리자 권한 테이블 insert
			//model.addAttribute("reportUpdateUserInfo", reportService.updateReportUserRole(reportInfo, user));
			
			//해당 보고서의 각 사용자별 태블로 권한부여
			//reportService.addTableauUserAuth(reportInfo);		
			
			log.debug("PROJECT UPDATE INFO: {}", model);			
		} else {
			log.warn("{} 오류 발생", Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		return "redirect:/project/report";
	}
	
	/**
	 * Delete Old Info
	 * @param reportId
	 * @param model
	 * @return
	 */
	@PostMapping("/report/delete")
	public String delete(@ModelAttribute ReportModel reportInfo, Model model) {
		model.addAttribute("reportDeleteInfo", reportService.deleteReport(reportInfo));
		
		log.debug("PROJECT DELETE INFO: {}", model);
		
		return "redirect:/project/report";
	}
	
	//http://127.0.0.1:8081/project/report/RP2021_000000011/tableau/popup
	@GetMapping("/report/{reportId}/popup")
	public String reportView(@PathVariable String reportId, @AuthenticationPrincipal AuthUser authUser, Model model) {
		//보고서 정보 추출
		ReportModel tmpModel = new ReportModel();
		tmpModel.setReportId(reportId);
		ReportModel report = reportService.selectReport(tmpModel);
		
		//태블로 토큰 요청
    	Map<String, String> param = new HashMap<String, String>();
    	param.put("username", reportId);
    	String tableauToken = apiRequestUtil.requestPost(props.getTokenUrl(), param);
    	log.debug("###tableauToken : {}", tableauToken);
    	
    	StringBuffer urlSb = new StringBuffer(props.getTokenUrl());
    	urlSb.append(tableauToken);
    	urlSb.append("/#/user/local/"+reportId+"/content");
//    	urlSb.append("/user/local/"+reportId+"/content");
    	//urlSb.append("/views/");
    	//urlSb.append(report.getReportUrl());
    	//urlSb.append(report.getReportAttr().get("param"));
    	String reportViewUrl = urlSb.toString();
    	log.debug("###reportViewUrl : {}", urlSb.toString());
    	
		return "redirect:"+reportViewUrl;
	}
	
	/**
	 * Taleau 보고서 상세 PopUp
	 * @param reportId
	 * @param authUser
	 * @param model
	 * @return
	 */
	@GetMapping("/report/reportPreview/{reportId}/popup")
	public String reportPreview(@PathVariable String reportId, @AuthenticationPrincipal AuthUser authUser, Model model) {
		MemberModel user = authUser.getMemberModel();
		
		String urlparam = "";
		String username = props.getUsername();
		ReportModel m = new ReportModel();
		m.setReportId(reportId);
		ReportModel report = reportService.selectReport(m);
		
		//뷰 권한 체크
		m.setUserId(user.getUserId());
		m.setDeptCode(user.getDeptCode());
		ReportModel userRole = reportService.selectCheckReportUserRole(m);
		String authYn = "N";
		String mgrYn = "N";
		if(userRole != null) {
			authYn = userRole.getAuthYn();
		}
		
		//편집 권한 체크
		String deptCode = report.getDeptCode();
		m.setRefTy("1");
		m.setRoleSe("2");
		m.setUseYn("Y");
		m.setRefId(user.getUserId());
		List<ReportModel> roleUsers = reportService.selectReportUserRole(m);
		
		if(roleUsers.size() > 0) {
			mgrYn = "Y";
		}
		
		if(!"".equals(report.getDeptMngYn()) && "Y".equals(report.getDeptMngYn()) && deptCode.equals(user.getDeptCode())) {
			mgrYn = "Y";
		}
		
		if("Y".equals(mgrYn)) {
			authYn = "Y";
			urlparam = "&:toolbar=yes";
			username = authUser.getMemberModel().getUserId();
		}else {
			urlparam = "&:toolbar=no";
		}
		
		if(!"".equals(report.getReportAttr()) && report.getReportAttr() != null && report.getReportAttr().length() != 0) {
			JSONArray params = report.getReportAttr().getJSONArray("params");
			
			for(int i=0;i<params.length();i++) {
				JSONObject p = params.getJSONObject(i);
				if("99".equals(p.getString("paramType"))) {
					urlparam += "&" + p.getString("paramNm") + "=" + p.getString("paramValue");
				}else{
					urlparam += "&" + p.getString("paramNm") + "=" + paramType(p.getString("paramType"));
				}
			}
		}
		authYn = "Y";
		mgrYn = "Y";
		//태블로 토큰 요청
    	Map<String, String> param = new HashMap<String, String>();
    	param.put("username", username);
    	String tableauToken = apiRequestUtil.requestPost(props.getTokenUrl(), param);
    	log.debug("###tableauToken : {}", tableauToken);
    	
    	if(!"-1".equals(tableauToken) && StringUtils.isNotBlank(tableauToken)) {
    		//tableau view log insert
    		LogModel logModel = new LogModel();
    		logModel.setUserId(user.getUserId());
    		logModel.setReportId(reportId);
    		logModel.setVer(report.getVer());
    		logModel.setTableauWorkbookId(report.getTableauWorkbookId());
    		logModel.setTableauParam(report.getReportAttr());
    		logModel.setTableauUserId(user.getTableauUserId());
    		logModel.setUserNm(user.getUserNm());
    		logModel.setDeptCode(user.getDeptCode());
    		logModel.setDeptNm(user.getDeptNm());
    		logModel.setPstnCode(user.getPstnCode());
    		logModel.setPstnNm(user.getPstnNm());
    		logService.insertTableau(logModel);
    	}
    	
    	
    	StringBuffer urlSb = new StringBuffer(props.getTokenUrl());
    	urlSb.append(tableauToken);
    	urlSb.append("/views/");
    	urlSb.append(report.getReportUrl());
    	urlSb.append("?:embed=y&:device=desktop"+urlparam);
    	String reportViewUrl = urlSb.toString();
    	log.debug("###reportViewUrl : {}", urlSb.toString());
    	
    	model.addAttribute("reportInfo", report);
    	model.addAttribute("tableauToken", tableauToken);
    	model.addAttribute("reportViewUrl", reportViewUrl);
    	model.addAttribute("authYn", authYn);
    	
		return "pop/reportView";
	}		
	
	private String paramType(String type) {
		String result = "";
		
		SimpleDateFormat format;
		
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		/**
		 * 1 : 당일
		 * 2 : 전일
		 * 3 : 전전일
		 * 4 : 당월
		 * 5 : 전월
		 * 6 : 전전월
		 * 7 : 당해년도
		 * 8 : 전년도
		 * 9 : 전전년도
		 */
		switch (type) {
		case "1":
			format = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, 0);
			result = format.format(cal.getTime());
			break;
		case "2":
			format = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, -1);
			result = format.format(cal.getTime());
			break;
		case "3":
			format = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, -2);
			result = format.format(cal.getTime());
			break;
		case "4":
			format = new SimpleDateFormat("yyyy-MM");
			cal.add(Calendar.MONTH, 0);
			result = format.format(cal.getTime());
			break;
		case "5":
			format = new SimpleDateFormat("yyyy-MM");
			cal.add(Calendar.MONTH, -1);
			result = format.format(cal.getTime());
			break;
		case "6":
			format = new SimpleDateFormat("yyyy-MM");
			cal.add(Calendar.MONTH, -2);
			result = format.format(cal.getTime());
			break;
		case "7":
			format = new SimpleDateFormat("yyyy");
			cal.add(Calendar.YEAR, 0);
			result = format.format(cal.getTime());
			break;
		case "8":
			format = new SimpleDateFormat("yyyy");
			cal.add(Calendar.YEAR, -1);
			result = format.format(cal.getTime());
			break;
		case "9":
			format = new SimpleDateFormat("yyyy");
			cal.add(Calendar.YEAR, -1);
			result = format.format(cal.getTime());
			break;

		default:
			format = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DATE, 0);
			result = format.format(cal.getTime());
			break;
		}
		return result;
	}
	
	@NoLogging
	@ResponseBody
	@PostMapping("/report/detail/userRole/check")
    public String userRoleCheck(HttpServletRequest request, @AuthenticationPrincipal AuthUser authUser) {
		MemberModel user = authUser.getMemberModel();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		ReportModel m = new ReportModel();
		m.setReportId(request.getParameter("reportId"));
		ReportModel report = reportService.selectReport(m);
		String authYn = "N";
		
		if(report != null) {
			//뷰 권한 체크
			m.setUserId(user.getUserId());
			m.setDeptCode(user.getDeptCode());
			ReportModel userRole = reportService.selectCheckReportUserRole(m);
			if(userRole != null) {
				authYn = userRole.getAuthYn();
			}
			
			//편집 권한 체크
			String deptCode = report.getDeptCode();
			m.setRefTy("1");
			m.setRoleSe("2");
			m.setUseYn("Y");
			m.setRefId(user.getUserId());
			List<ReportModel> roleUsers = reportService.selectReportUserRole(m);
			
			
			if(roleUsers.size() > 0) {
				authYn = "Y";
			}
			
			if(!"".equals(report.getDeptMngYn()) && "Y".equals(report.getDeptMngYn()) && deptCode.equals(user.getDeptCode())) {
				authYn = "Y";
			}
			resultMap.put("result", authYn);
		}else {
			resultMap.put("result", authYn);
		}
        
        return resultMap.toString();
    }
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/syncuser/popup")
	@ResponseBody
	public String syncuser() throws Exception {
		Map<String, Object> resultMap = commonService.syncTableauUsers();
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/syncgroup/popup")
	@ResponseBody
	public String syncgroup() throws Exception {
		Map<String, Object> resultMap = commonService.syncTableauGroups();
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/syncproject/popup")
	@ResponseBody
	public String syncproject() throws Exception {
		Map<String, Object> resultMap = commonService.syncTableauProjects();
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/syncworkbook/popup")
	@ResponseBody
	public String syncworkbook() throws Exception {
		Map<String, Object> resultMap = commonService.syncTableauWorkbooks();
		return resultMap.toString();
	}	 

	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/syncworkbookView/popup")
	@ResponseBody
	public String syncworkbookView() throws Exception {
		Map<String, Object> resultMap = commonService.syncTableauWorkbookView();
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/adduser/popup")
	@ResponseBody
	public String adduser() throws Exception {
		//게스트로 하면 <summary>금지됨</summary><detail>게스트 역할의 사용자를 추가할 수 없습니다.</detail>
		//리드온리는 더이상 사용되지 않는다. 리드온리로 넣어도 'Unlicensed'로 바뀌어서 저장된다.
		//라이센스 없음도 상세화면 접근됨 (=> 원래 그런건가???) => 아예 승인까지 떨어져야 계정생성을 해야 함
//		Map<String, Object> resultMap = commonService.addTableauUser("713035","9");
		Map<String, Object> resultMap = commonService.addTableauUser("713054","3");
		//Map<String, Object> resultMap = commonService.addTableauUser("713032","9");
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/updateuser/popup")
	@ResponseBody
	public String updateuser() throws Exception {
		//9는 라이센스 없음, 4가 뷰어, 3이 익스플로러 (게스트나 리드온리는 없어졌거나 지원안한다고 에러남)		
		Map<String, Object> resultMap = commonService.updateTableauUser("713035","전상현","713035pw","4");
		//Map<String, Object> resultMap = commonService.updateTableauUser("713032","김정희","713032pw","4");
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/addgroup/popup")
	@ResponseBody
	public String addgroup() throws Exception {
		Map<String, Object> resultMap = commonService.addTableauGroup("포탈테스트그룹re");
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/updategroup/popup")
	@ResponseBody
	public String updategroup() throws Exception {
		Map<String, Object> resultMap = commonService.updateTableauGroup("포탈테스트그룹re","포탈테스트그룹rere");
		return resultMap.toString();
	}

	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/addproject/popup")
	@ResponseBody
	public String addproject() throws Exception {
		Map<String, Object> resultMap = commonService.addTableauProject(null, "테스트플젝2", "테스트플젝2 설명");
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/updateproject/popup")
	@ResponseBody
	public String updateproject() throws Exception {
		Map<String, Object> resultMap = commonService.updateTableauProject("테스트플젝2", null, "테스트플젝2re", "테스트플젝 설명2re");
		return resultMap.toString();
	}	
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/addworkbookpermission/popup")
	@ResponseBody
	public String addworkbookpermission() throws Exception {
/*
 *             &lt;enumeration value="AddComment"/>
 *             &lt;enumeration value="ChangeHierarchy"/>
 *             &lt;enumeration value="ChangePermissions"/>
 *             &lt;enumeration value="Connect"/>
 *             &lt;enumeration value="Delete"/>
 *             &lt;enumeration value="ExportData"/>
 *             &lt;enumeration value="ExportImage"/>
 *             &lt;enumeration value="ExportXml"/>
 *             &lt;enumeration value="Filter"/>
 *             &lt;enumeration value="ProjectLeader"/>
 *             &lt;enumeration value="Read"/>
 *             &lt;enumeration value="ShareView"/>
 *             &lt;enumeration value="ViewComments"/>
 *             &lt;enumeration value="ViewUnderlyingData"/>
 *             &lt;enumeration value="WebAuthoring"/>
 *             &lt;enumeration value="Write"/> 		
 */
		Map<String, Object> resultMap = commonService.addPermissionToWorkbook("e2dd4a37-e596-49c8-9919-2ca77362eef2", "509d71c9-122a-4858-a392-0636123bb67f", "2");
		return resultMap.toString();
	}	
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/addworkbookpermission/{auth}/popup")
	@ResponseBody
	public String addworkbookpermission(@PathVariable String auth) throws Exception {
		Map<String, Object> resultMap = commonService.addPermissionToWorkbook("e2dd4a37-e596-49c8-9919-2ca77362eef2", "509d71c9-122a-4858-a392-0636123bb67f", auth);
		return resultMap.toString();
	}		
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/deleteworkbookpermission/{auth}/popup")
	@ResponseBody
	public String deleteworkbookpermission(@PathVariable String auth) throws Exception {
		Map<String, Object> resultMap = commonService.deletePermissionToWorkbook("f49d51ab-9b61-4d35-b254-e83c7d03e2c5", "509d71c9-122a-4858-a392-0636123bb67f", auth);
		return resultMap.toString();
	}
	
	@NoLogging
	@Profile({Constant.Profile.LOCAL})
	@RequestMapping("/downloadPreimage/popup")
	@ResponseBody
	public String downloadPreimage() throws Exception {
		//String workbookId, String viewId, String workbookName, String path
		Map<String, Object> resultMap = commonService.downloadPreimage("509d71c9-122a-4858-a392-0636123bb67f", "", "509d71c9-122a-4858-a392-0636123bb67f", "C:/downimage");
		return resultMap.toString();
	}
	
}

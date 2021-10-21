package com.shinvest.ap.app.extrnl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import com.shinvest.ap.app.dept.service.DeptService;
import com.shinvest.ap.app.extrnl.model.ExtrnlDataCriteria;
import com.shinvest.ap.app.extrnl.model.ExtrnlDataModel;
import com.shinvest.ap.app.extrnl.service.ExtrnlDataService;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.config.security.AuthUser;

/**
 * 외부 자산 관리 > 외부 데이터 관리	/external/data
 */
@RequestMapping("/external")
@Controller
public class ExtrnlDataController {

	@Resource
    private IdUtil idUtil;
	
	@Resource
	private ExtrnlDataService extrnlDataService;
	
	@Resource
	DeptService deptService;
    
    @GetMapping("/data")
    public String data(@ModelAttribute ExtrnlDataCriteria criteria, Model model) {
        //외부데이터에서 사용하는 공통코드 모두 set 
    	extrnlDataService.selectAllExtrnlDataCodeList(model);
    	
        model.addAttribute("extrnlDataList", extrnlDataService.selectList(criteria));
        model.addAttribute("cycle", extrnlDataService.selectCycleList(criteria));
        model.addAttribute("deptInfo", extrnlDataService.selectDeptList(criteria));
        criteria.setTotalCount(extrnlDataService.selectListCount(criteria));
        model.addAttribute("pages", criteria);
        
        return "extrnl/extrnlData";
    }    
    
    @GetMapping("/data/regist")
    public String regist(Model model) {
        //외부데이터에서 사용하는 공통코드 모두 set 
    	extrnlDataService.selectAllExtrnlDataCodeList(model);    	
    	// 부서 조회
        model.addAttribute("depts", deptService.selectDeptClList());
        return "extrnl/extrnlDataRegist";
    }

	@PostMapping("/data/insert")
	public String save(@ModelAttribute ExtrnlDataModel extrnlDataModel, @AuthenticationPrincipal AuthUser authUser) {
		extrnlDataModel.setRgstId(authUser.getMemberModel().getUserId());
		extrnlDataModel.setModiId(authUser.getMemberModel().getUserId());
		if(extrnlDataModel.getDataId() == "" || extrnlDataModel.getDataId() == null) {
			extrnlDataModel.setDataId(idUtil.getExtrnlDataId());
		}
		extrnlDataModel.setRgstSe("화면");	//등록 구분[엑셀 / 화면]
		extrnlDataService.save(extrnlDataModel);
		
		return "redirect:/external/data";
	}
	
    @GetMapping("/data/modify/{dataId}")
    public String modify(Model model, @PathVariable String dataId, @ModelAttribute ExtrnlDataCriteria criteria ) {
        //외부데이터에서 사용하는 공통코드 모두 set 
    	extrnlDataService.selectAllExtrnlDataCodeList(model);    	
    	
    	//기존정보 추출
    	ExtrnlDataModel extrnlDataModel  = new ExtrnlDataModel ();
    	extrnlDataModel.setDataId(dataId);
    	model.addAttribute("extrnlDataInfo", extrnlDataService.select(extrnlDataModel));
    	criteria.setDataId(extrnlDataModel.getDataId());
        model.addAttribute("deptInfo", extrnlDataService.selectDeptList(criteria));

    	// 부서 조회
        model.addAttribute("depts", deptService.selectDeptClList());
    	
        return "extrnl/extrnlDataModify";
    }
	
    @RequestMapping("/data/detail/{dataId}")
    @ResponseBody
    public ExtrnlDataModel select(@PathVariable String dataId) {
        ExtrnlDataModel extrnlDataModel  = new ExtrnlDataModel();
        extrnlDataModel.setDataId(dataId);

        return extrnlDataService.select(extrnlDataModel);
    }      
    
	@PostMapping("/data/delete")
	public String delete(@ModelAttribute ExtrnlDataModel extrnlDataModel, @AuthenticationPrincipal AuthUser authUser) {
		extrnlDataModel.setRgstId(authUser.getMemberModel().getUserId());
		extrnlDataModel.setModiId(authUser.getMemberModel().getUserId());
		extrnlDataService.delete(extrnlDataModel);
		
		return "redirect:/external/data";
	}		
    
	
	
	
	
	@ResponseBody
	@RequestMapping(value="/data/excelUpload", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> excelUpload(HttpServletRequest request, MultipartRequest multipart, @AuthenticationPrincipal AuthUser authUser) {
		Map<String,Object> result = extrnlDataService.excelUpload(request, multipart, authUser);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/data/multiInsert", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> multiInsert(HttpServletRequest request, @AuthenticationPrincipal AuthUser authUser) {
		Map<String,Object> result = extrnlDataService.multiInsert(request, authUser);
		return result;
	}	
	
	
}

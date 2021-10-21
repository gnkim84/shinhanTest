package com.shinvest.ap.app.extrnl.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.extrnl.mapper.ExtrnlDataMapper;
import com.shinvest.ap.app.extrnl.model.ExtrnlDataModel;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.ParseUtil;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.props.ExternalDataProps;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExtrnlDataService {

	@Resource
    private IdUtil idUtil;	
	
	@Resource(name="commonUtil")
	private CommonUtil commonUtil;	
	
	@Resource(name="parseUtil")
	private ParseUtil parseUtil;	
	
	@Resource
	private CodeService codeService;	
	
	@Resource(name = "externalDataProps")
	private ExternalDataProps externalDataProps;
	
    @Resource
    private ExtrnlDataMapper extrnlDataMapper;
    
    public List<ExtrnlDataModel> selectList(Criteria criteria) {
        return extrnlDataMapper.selectList(criteria);
    }
    public List<ExtrnlDataModel> selectCycleList(Criteria criteria) {
        return extrnlDataMapper.selectCycleList(criteria);
    }
    public List<ExtrnlDataModel> selectDeptList(Criteria criteria) {
        return extrnlDataMapper.selectDeptList(criteria);
    }
    public int selectListCount(Criteria criteria) {
        return extrnlDataMapper.selectListCount(criteria);
    }

    public ExtrnlDataModel select(ExtrnlDataModel model) {
        return extrnlDataMapper.select(model);
    }

    @Transactional
    public String delete(ExtrnlDataModel model) {
        long count = extrnlDataMapper.delete(model);

        if(count > 0) {
            return Constant.DB.DELETE;
        } else {
            return Constant.DB.FAIL;
        }
    }

    @Transactional
    public String save(ExtrnlDataModel model) {
        ExtrnlDataModel existDataModel = select(model);

        if(existDataModel != null) {
            long count = extrnlDataMapper.update(model);
            if(count > 0) {
                return Constant.DB.UPDATE;
            } else {
                return Constant.DB.FAIL;
            }
        } else {
            long count = extrnlDataMapper.insert(model);
            if(count > 0) {
                return Constant.DB.INSERT;
            } else {
                return Constant.DB.FAIL;
            }
        }
    }
    
	public void selectAllExtrnlDataCodeList(Model model) {
    	model.addAttribute("srcSysCodeList", codeService.selectGroupIdAllList("SRC_SYS_CODE"));
    	model.addAttribute("dataTyList", codeService.selectGroupIdAllList("DATA_TY"));
    	model.addAttribute("dataTrnsMthdList", codeService.selectGroupIdAllList("DATA_TRNS_MTHD"));
    	model.addAttribute("dataTrnsCycleList", codeService.selectGroupIdAllList("DATA_TRNS_CYCLE"));
    	model.addAttribute("langList", codeService.selectGroupIdAllList("LANG"));
    }
	
	//엑셀파일 업로드
	public Map<String, Object> excelUpload(HttpServletRequest request, MultipartRequest multipart, AuthUser authUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		final Map<String, MultipartFile> files = multipart.getFileMap();
		String result = "success";
		MultipartFile file = null;
		String orgFileName = null;
		String fileExt = null;
		String newFileName = "";
		if (files.size() == 1) {
			//저장할 폴더 생성
			String excelPath = externalDataProps.getExcelPath();
			File f = new File(excelPath);
			if(!f.exists()) { f.mkdirs(); }
			
			//파일 정보 추출및 이동
			for (String key : files.keySet()) {
				file = files.get(key);
			}
			System.out.println("#getOriginalFilename = " + file.getOriginalFilename());
			orgFileName = file.getOriginalFilename();
			fileExt = orgFileName.substring(orgFileName.lastIndexOf(".")+1);	// 파일 확장자
			if ("xls".equals(fileExt) || "xlsx".equals(fileExt)) {
				newFileName = commonUtil.getDateString(Constant.DATE_FORMAT.DEFAULT_DATETIME) + "_ExtrnlDataExcel." + fileExt;
				try {
					file.transferTo(new File(excelPath  + File.separator + newFileName));
				} catch (IllegalStateException | IOException e) {
					log.warn("ExtrnlDataService excelUpload Exception");
				}
			}
		}
		
		resultMap.put("result", result);
		resultMap.put("newFileName", newFileName);
		return resultMap;
	}
	
	//업로드된 엑셀파일 DB INSERT
	public Map<String, Object> multiInsert(HttpServletRequest request, AuthUser authUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCnt = "0";
		StringBuilder message = new StringBuilder();
		
		String excelNewFileName = (request.getParameter("excelNewFileName") == null) ? "" : request.getParameter("excelNewFileName").toString();
		if (!"".equals(excelNewFileName)) {
			String[] headersKor = {
					"소스 시스템 코드", "데이터 타입", "데이터 명", "데이터 설명", "데이터 항목","데이터 위치", "비고", 
					"구매 부서 코드", "구매 담당자 ID", "관리 부서 코드", "관리 담당자 ID", "사용 부서 코드(,구분)", 
					"종료 일시(yyyy-MM-dd)", "소스 시스템 URL", "데이터 전송 방식", "데이터 전송 주기(,구분)", "언어"
				};
			String[] headers = {
				"srcSysCode", "dataTy", "dataNm", "dataDsc", "dataItem", "dataLc", "rmk", 
				"purchsDeptCode", "purchsPicrId", "mngDeptCode", "mngPicrId", "deptInfo", 
				"endDt", "srcSysUrl", "dataTrnsmisMthd", "dataTrnsmisCycle", "lang"
			};
			List<Map<String,String>> excelList = parseUtil.parseExcel(Paths.get(externalDataProps.getExcelPath(), excelNewFileName), headers, 0, 2);
			if (excelList.size() == 0) {
				message.append("no data");
			} else {
				ArrayList<ExtrnlDataModel> dataList = new ArrayList<ExtrnlDataModel>();
				String validationMsg;
				int loopIndex = 0;
				for (Map<String, String> map : excelList) {
					validationMsg = validateData(map, headersKor);
					if ("".equals(validationMsg)) {
						ExtrnlDataModel extrnlData = new ExtrnlDataModel();
						extrnlData.setDataId(idUtil.getExtrnlDataId());
						extrnlData.setSrcSysCode(getStringValue(map,"srcSysCode"));
						extrnlData.setDataTy(getStringValue(map,"dataTy"));
						extrnlData.setDataNm(getStringValue(map,"dataNm"));
						extrnlData.setDataDsc(getStringValue(map,"dataDsc"));
						extrnlData.setDataItem(getStringValue(map,"dataItem"));
						extrnlData.setDataLc(getStringValue(map,"dataLc"));
						extrnlData.setRmk(getStringValue(map,"rmk"));
						extrnlData.setPurchsDeptCode(getStringValue(map,"purchsDeptCode"));
						extrnlData.setPurchsPicrId(getStringValue(map,"purchsPicrId"));
						extrnlData.setMngDeptCode(getStringValue(map,"mngDeptCode"));
						extrnlData.setMngPicrId(getStringValue(map,"mngPicrId"));
						extrnlData.setDeptInfo(getJsonArrayValue(map, "deptInfo"));						//["713", "511", "804"]
						extrnlData.setEndDt(getLocalDateValue(map, "endDt"));							//yyyy-MM-dd
						extrnlData.setSrcSysUrl(getStringValue(map,"srcSysUrl"));
						extrnlData.setDataTrnsmisMthd(getStringValue(map,"dataTrnsmisMthd"));
						extrnlData.setDataTrnsmisCycle(getJsonArrayValue(map, "dataTrnsmisCycle"));		//["day", "week", "month"]
						extrnlData.setLang(getStringValue(map,"lang"));
						extrnlData.setRgstSe("엑셀");
						extrnlData.setUseYn("Y");
						extrnlData.setRgstId(authUser.getMemberModel().getUserId());
						extrnlData.setModiId(authUser.getMemberModel().getUserId());						
						
						dataList.add(extrnlData);						
					} else {
						message.append(StringUtils.joinWith("", "#row ", loopIndex, "\t => ", validationMsg, "\r\n"));
					}
					loopIndex++;
				}
				if (dataList.size() > 0 && excelList.size()==dataList.size()) {
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("dataList", dataList);
					long insertRowCnt = extrnlDataMapper.insertMulti(paramMap);	
					resultCnt = String.valueOf(insertRowCnt);
					message.append("success");
				}
			}

		}
		
		resultMap.put("resultCnt", resultCnt);
		resultMap.put("message", message.toString());
		return resultMap;
	}
	
	//엑셀의 각 row데이터 유효성 검증
	public String validateData(Map<String, String> map, String[] headersKor) {
		StringBuilder sb = new StringBuilder();
		if (getStringValue(map,"srcSysCode") == null) 				{ sb.append(StringUtils.joinWith(":", headersKor[0], "필수입력")); 			sb.append(" / ");}
		if (getStringValue(map,"srcSysCode").length() > 16) 		{ sb.append(StringUtils.joinWith(":", headersKor[0], "길이제한(16)")); 		sb.append(" / ");}
		if (getStringValue(map,"dataTy") == null) 					{ sb.append(StringUtils.joinWith(":", headersKor[1], "필수입력")); 			sb.append(" / ");}
		if (getStringValue(map,"dataTy").length() > 16) 			{ sb.append(StringUtils.joinWith(":", headersKor[1], "길이제한(16)")); 		sb.append(" / ");}
		if (getStringValue(map,"dataNm") == null) 					{ sb.append(StringUtils.joinWith(":", headersKor[2], "필수입력")); 			sb.append(" / ");}
		if (getStringValue(map,"dataNm").length() > 100) 			{ sb.append(StringUtils.joinWith(":", headersKor[2], "길이제한(100)")); 	sb.append(" / ");}
		if (getStringValue(map,"dataDsc").length() > 1000) 			{ sb.append(StringUtils.joinWith(":", headersKor[3], "길이제한(1000)")); 	sb.append(" / ");}
		if (getStringValue(map,"dataItem").length() > 1000) 		{ sb.append(StringUtils.joinWith(":", headersKor[4], "길이제한(1000)")); 	sb.append(" / ");}
		if (getStringValue(map,"dataLc").length() > 1000) 			{ sb.append(StringUtils.joinWith(":", headersKor[5], "길이제한(1000)")); 	sb.append(" / ");}
		if (getStringValue(map,"rmk").length() > 1000) 				{ sb.append(StringUtils.joinWith(":", headersKor[6], "길이제한(1000)")); 	sb.append(" / ");}
		if (getStringValue(map,"purchsDeptCode").length() > 16) 	{ sb.append(StringUtils.joinWith(":", headersKor[7], "길이제한(16)")); 		sb.append(" / ");}
		if (getStringValue(map,"purchsPicrId").length() > 32) 		{ sb.append(StringUtils.joinWith(":", headersKor[8], "길이제한(32)")); 		sb.append(" / ");}
		if (getStringValue(map,"mngDeptCode").length() > 16) 		{ sb.append(StringUtils.joinWith(":", headersKor[9], "길이제한(16)")); 		sb.append(" / ");}
		if (getStringValue(map,"mngPicrId").length() > 32) 			{ sb.append(StringUtils.joinWith(":", headersKor[10], "길이제한(32)")); 	sb.append(" / ");}
		if (isDateFormat(getStringValue(map,"mngPicrId"))) 			{ sb.append(StringUtils.joinWith(":", headersKor[12], "비유효날짜")); 		sb.append(" / ");}
		if (getStringValue(map,"srcSysUrl").length() > 256) 		{ sb.append(StringUtils.joinWith(":", headersKor[13], "길이제한(256)")); 	sb.append(" / ");}
		if (getStringValue(map,"dataTrnsmisMthd").length() > 16) 	{ sb.append(StringUtils.joinWith(":", headersKor[14], "길이제한(16)")); 	sb.append(" / ");}
		if (getStringValue(map,"lang").length() > 16) 				{ sb.append(StringUtils.joinWith(":", headersKor[16], "길이제한(16)")); 	sb.append(" / ");}
		return sb.toString();
	}
	//yyyy-MM-dd 형식인지 확인
	public boolean isDateFormat(String date) {
		if (date == null) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("^((19|20)\\d\\d)?([-/.])?(0[1-9]|1[012])([-/.])?(0[1-9]|[12][0-9]|3[01])$");
			Matcher matcher = pattern.matcher(date);
			return matcher.find();
		}
	}
	public String getStringValue(Map<String, String> map, String key) {
		String stringValue = null;
		stringValue = map.getOrDefault(key, "");
		if ("".equals(stringValue)) {
			return null;
		}
		return stringValue.trim();
	}
	public LocalDate getLocalDateValue(Map<String, String> map, String key) {
		LocalDate localDateValue = null;
		if (getStringValue(map, key) != null) {
			localDateValue = LocalDate.parse(getStringValue(map, key), DateTimeFormatter.ISO_DATE);
		}
		return localDateValue;
	}
	public JSONArray getJsonArrayValue(Map<String, String> map, String key) {
		JSONArray jsonArrayValue = null;
		if (getStringValue(map, key) != null) {
			String tmpArr[] = getStringValue(map, key).split(",");
			if (tmpArr.length > 1) {
				jsonArrayValue = new JSONArray();
				for (String string : tmpArr) {
					jsonArrayValue.put(string.trim());
				}
			}			
		}
		return jsonArrayValue;
	}
	
	
	
}

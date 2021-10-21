package com.shinvest.ap.app.board;

import java.util.List;

import javax.annotation.Resource;

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

import com.shinvest.ap.app.board.model.FreeModel;
import com.shinvest.ap.app.board.service.FreeService;
import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.app.file.service.FileService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

/**
 * 자유 컨트롤러
 */
@RequestMapping("/board")
@Controller
public class FreeController {

    @Resource
    private CodeService codeService;	
    
	@Resource
	private FreeService service;

	@Resource
	private FileService fileService;

    /**
     * 자유 게시판 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/free")
    public String free(@ModelAttribute Criteria criteria, Model model) {

    	return "board/free/free";
    }
    
    /**
     * 자유 게시판 관리자 답변 등록 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/free/modify/{freeId}")
    public String freeRegist(@PathVariable String freeId, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	FreeModel freeModel = new FreeModel();
    	freeModel.setFreeId(freeId);
    	FreeModel qModel = service.selectFree(freeModel);
    	
    	model.addAttribute("freeInfo", qModel);
    	
    	//자유유형 구분 코드
        model.addAttribute("codeFreeCateCdList", codeService.selectGroupIdAllList("자유_CAT"));
        
    	//자유상태 구분 코드
        model.addAttribute("codeFreeStatCdList", codeService.selectGroupIdAllList("자유_STAT_CODE"));
        
    	//공개여부 구분 코드
        model.addAttribute("openYnCdList", codeService.selectGroupIdAllList("OPEN_YN"));
        
    	//공개여부 구분 코드
        model.addAttribute("codeUseYnList", codeService.selectGroupIdAllList("USE_YN"));        
        
        //파일 조회
    	FileModel f = new FileModel();
    	f.setRefId(freeId);
    	f.setFileCl(Constant.File.BOARDQ);
    	List<FileModel> qFileList = fileService.selectFileList(f);
    	JSONArray qfiles = new JSONArray();
    	for(FileModel fm : qFileList) {
    		JSONObject obj = new JSONObject();
    		obj.put("fileId", fm.getFileId());
    		obj.put("storageSe", fm.getStorageSe());
    		obj.put("savePath", fm.getSavePath());
    		obj.put("saveFileNm", fm.getSaveFileNm());
    		obj.put("fileNm", fm.getFileNm());
    		obj.put("fileExtsn", fm.getFileExtsn());
    		obj.put("fileSize", fm.getFileSize());
    		obj.put("useYn", fm.getUseYn());
    		obj.put("rgstId", fm.getRgstId());
    		obj.put("rgstDt", fm.getRgstDt());
    		obj.put("modiId", fm.getModiId());
    		obj.put("modiDt", fm.getModiDt());
    		obj.put("bucketNm", fm.getBucketNm());
    		obj.put("fileUrl", fm.getFileUrl());
    		obj.put("fileCl", fm.getFileCl());
    		obj.put("saveFileVer", fm.getSaveFileVer());
    		obj.put("inputStream", fm.getInputStream());
    		obj.put("bytes", fm.getBytes());
    		obj.put("modiByUserYn", fm.getModiByUserYn());
    		obj.put("refId", fm.getRefId());
    		obj.put("refVer", fm.getRefVer());
    		qfiles.put(obj);
    	}
    	
    	model.addAttribute("qFileList", qFileList);
    	model.addAttribute("qFileJsonList", qfiles);

    	//파일 조회
    	f.setFileCl(Constant.File.BOARDA);
    	List<FileModel> aFileList = fileService.selectFileList(f);
    	JSONArray afiles = new JSONArray();
    	for(FileModel fm : aFileList) {
    		JSONObject obj = new JSONObject();
    		obj.put("fileId", fm.getFileId());
    		obj.put("storageSe", fm.getStorageSe());
    		obj.put("savePath", fm.getSavePath());
    		obj.put("saveFileNm", fm.getSaveFileNm());
    		obj.put("fileNm", fm.getFileNm());
    		obj.put("fileExtsn", fm.getFileExtsn());
    		obj.put("fileSize", fm.getFileSize());
    		obj.put("useYn", fm.getUseYn());
    		obj.put("rgstId", fm.getRgstId());
    		obj.put("rgstDt", fm.getRgstDt());
    		obj.put("modiId", fm.getModiId());
    		obj.put("modiDt", fm.getModiDt());
    		obj.put("bucketNm", fm.getBucketNm());
    		obj.put("fileUrl", fm.getFileUrl());
    		obj.put("fileCl", fm.getFileCl());
    		obj.put("saveFileVer", fm.getSaveFileVer());
    		obj.put("inputStream", fm.getInputStream());
    		obj.put("bytes", fm.getBytes());
    		obj.put("modiByUserYn", fm.getModiByUserYn());
    		obj.put("refId", fm.getRefId());
    		obj.put("refVer", fm.getRefVer());
    		afiles.put(obj);
    	}
    	
    	model.addAttribute("aFileList", aFileList);
    	model.addAttribute("aFileJsonList", afiles);
        
    	return "board/free/freeRegist";
    }
    
    /**
     * 자유 게시판 관리자 답변 등록한다.
     *
     * @param FreeModel freeInfo
     * @return
     */
    @PostMapping("/free/update")
    public String freeUpdate(@ModelAttribute FreeModel freeInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	freeInfo.setModiId(authUser.getMemberModel().getUserId());
    	freeInfo.setAnswRgstId(authUser.getMemberModel().getUserId());
    	model.addAttribute("freeUpdateInfo", service.updateFree(freeInfo));
    	
    	if(freeInfo.getFileIds() != null) {
    		for(String fileId : freeInfo.getFileIds()) {
    			FileModel file = new FileModel();
    			file.setFileId(fileId);
    			file.setRefId(freeInfo.getFreeId());
    			file.setModiId(authUser.getMemberModel().getUserId());
    			fileService.updateFile(file);
    		}
    	}
    	
    	return "redirect:/board/free";
    }
    
    /**
     *  자유 게시판 관리자 답변 삭제한다
     * @param noticeId
     * @param model
     * @return
     */
    @PostMapping("/free/delete")
    public String delete(@ModelAttribute FreeModel freeInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	freeInfo.setModiId(authUser.getMemberModel().getUserId());
    	model.addAttribute("freeDeleteInfo", service.deleteFree(freeInfo));
    	return "redirect:/board/free";
    }
}

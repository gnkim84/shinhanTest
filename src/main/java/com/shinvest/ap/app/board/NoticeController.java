package com.shinvest.ap.app.board;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shinvest.ap.app.board.model.NoticeModel;
import com.shinvest.ap.app.board.service.NoticeService;
import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.app.file.service.FileService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.annotation.NoLogging;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import javax.annotation.Resource;

@RequestMapping("/board")
@Controller
@Slf4j
public class NoticeController {

	@Resource
    private IdUtil idUtil;
	
	@Resource
	private CodeService codeService;

	@Resource
	private NoticeService noticeService;

	@Resource
	private FileService fileService;
	
	/**
	 * Search All Info
	 * @param criteria
	 * @param model
	 * @return
	 */
    @GetMapping("/notice")
    public String notice(@ModelAttribute Criteria criteria, Model model) {
    	model.addAttribute("noticeList", noticeService.selectNoticeList(criteria));
    	criteria.setTotalCount(noticeService.selectNoticeListCount(criteria));
    	model.addAttribute("pages", criteria);
    	model.addAttribute("codeUseYnList", codeService.selectGroupIdAllList("USE_YN"));
    	model.addAttribute("codeImportantYnList", codeService.selectGroupIdAllList("IMPORTANT_YN"));
        return "board/notice/notice";
    }
    
    /**
     * Search Specific Info
     * @param criteria
     * @param attributes
     * @return
     */
    @PostMapping("/notice")
    public String notice(@ModelAttribute Criteria criteria, RedirectAttributes attributes) {
        attributes.addFlashAttribute("criteria", criteria);
        return "redirect:/board/notice";
    }
    
    /**
     * Get New Info
     * @param model
     * @return
     */
    @GetMapping("/notice/regist")
    public String select(Model model) {
        //공지사항에서 사용하는 공통코드 모두 set 
    	noticeService.selectAllNoticeCodeList(model);    	
    	
        return "board/notice/noticeRegist";
    }
    
    /**
     * Get Old Info
     * @param noticeId
     * @param model
     * @return
     */
    @GetMapping("/notice/modify/{noticeId}")
    public String select(@PathVariable String noticeId, Model model) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setNoticeId(noticeId);
        model.addAttribute("noticeInfo", noticeService.selectNotice(noticeModel));
        
        //공지사항에서 사용하는 공통코드 모두 set 
    	noticeService.selectAllNoticeCodeList(model);
    	
    	//파일 조회
    	FileModel f = new FileModel();
    	f.setRefId(noticeId);    	
    	List<FileModel> fileList = fileService.selectFileList(f);
    	JSONArray files = new JSONArray();
    	for(FileModel fm : fileList) {
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
    		files.put(obj);
    	}
    	
    	model.addAttribute("fileList", fileList);
    	model.addAttribute("fileJsonList", files);
    	
        return "board/notice/noticeRegist";
    }
    
    /**
     * Insert New Info
     * @param noticeInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/notice/insert")
    public String insert(@ModelAttribute NoticeModel noticeInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	noticeInfo.setRgstId(authUser.getMemberModel().getUserId());
    	noticeInfo.setModiId(authUser.getMemberModel().getUserId());
    	noticeInfo.setNoticeId(idUtil.getNoticeId());
    	model.addAttribute("noticeInsertInfo", noticeService.insertNotice(noticeInfo));
    	
    	if(noticeInfo.getFileIds() != null) {
	    	for(String fileId : noticeInfo.getFileIds()) {
	    		FileModel file = new FileModel();
	    		file.setFileId(fileId);
	    		file.setRefId(noticeInfo.getNoticeId());
	    		file.setModiId(authUser.getMemberModel().getUserId());
	    		fileService.updateFile(file);
	    	}
    	}
    	log.debug("NOTICE UPSERT MODEL: {}", model);
    	
    	return "redirect:/board/notice";
    }
    
    /**
     * Update Old Info
     * @param noticeInfo
     * @param authUser
     * @param model
     * @return
     */
    @PostMapping("/notice/update")
    public String update(@ModelAttribute NoticeModel noticeInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	noticeInfo.setModiId(authUser.getMemberModel().getUserId());
    	model.addAttribute("noticeUpdateInfo", noticeService.updateNotice(noticeInfo));
    	
    	if(noticeInfo.getFileIds() != null) {
    		for(String fileId : noticeInfo.getFileIds()) {
    			FileModel file = new FileModel();
    			file.setFileId(fileId);
    			file.setRefId(noticeInfo.getNoticeId());
    			file.setModiId(authUser.getMemberModel().getUserId());
    			fileService.updateFile(file);
    		}
    	}
    	log.debug("NOTICE UPSERT MODEL: {}", model);
    	
    	return "redirect:/board/notice";
    }
    
    /**
     * Delete Old Info
     * @param noticeId
     * @param model
     * @return
     */
    @PostMapping("/notice/delete")
    public String delete(@ModelAttribute NoticeModel noticeInfo,@ModelAttribute FileModel fileModel,@AuthenticationPrincipal AuthUser authUser, Model model) {
    	noticeInfo.setModiId(authUser.getMemberModel().getUserId());
    	model.addAttribute("noticeDeleteInfo", noticeService.deleteNotice(noticeInfo));
    	/*
    	fileModel.setRefId(noticeInfo.getNoticeId());
    	fileModel.setModiId(authUser.getMemberModel().getUserId());
    	fileService.deleteFileParent(fileModel);
    	*/
    	log.debug("NOTICE UPSERT MODEL: {}", model);
    	
    	return "redirect:/board/notice";
    }
    
    /**
	 * Search All Info
	 * @param criteria
	 * @param model
	 * @return
	 */
    @NoLogging
    @Profile({Constant.Profile.LOCAL})
    @GetMapping("/notice/detail/upload/{noticeId}")
    public String noticeUpload(@PathVariable String noticeId, Model model) {
    	NoticeModel noticeModel = new NoticeModel();
        noticeModel.setNoticeId(noticeId);
        model.addAttribute("noticeInfo", noticeService.selectNotice(noticeModel));
    	model.addAttribute("codeUseYnList", codeService.selectGroupIdAllList("USE_YN"));
    	model.addAttribute("codeImportantYnList", codeService.selectGroupIdAllList("IMPORTANT_YN"));
    	
    	FileModel f = new FileModel();
    	f.setRefId(noticeId);    	
    	List<FileModel> fileList = fileService.selectFileList(f);
    	JSONArray files = new JSONArray();
    	for(FileModel fm : fileList) {
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
    		files.put(obj);
    	}
    	
    	model.addAttribute("fileList", fileList);
    	model.addAttribute("fileJsonList", files);
        return "board/notice/noticeUpload";
    }
	
}

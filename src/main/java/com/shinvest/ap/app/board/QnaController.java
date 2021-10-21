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

import com.shinvest.ap.app.board.model.QnaModel;
import com.shinvest.ap.app.board.service.QnaService;
import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.app.file.service.FileService;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.config.security.AuthUser;

/**
 * QnA 컨트롤러
 */
@RequestMapping("/board")
@Controller
public class QnaController {

    @Resource
    private CodeService codeService;	
    
	@Resource
	private QnaService service;

	@Resource
	private FileService fileService;

    /**
     * QnA 게시판 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/qna")
    public String qna(@ModelAttribute Criteria criteria, Model model) {

    	model.addAttribute("searchqnaList", service.selectQnaList(criteria));
    	criteria.setTotalCount(service.selectQnaListCount(criteria));
    	model.addAttribute("pages", criteria);    	

    	return "board/qna/qna";
    }
    
    /**
     * QnA 게시판 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @PostMapping("/qna")
    public String qnaPost(@ModelAttribute Criteria criteria, Model model) {

    	model.addAttribute("searchQnaList", service.selectQnaList(criteria));
    	criteria.setTotalCount(service.selectQnaListCount(criteria));
    	model.addAttribute("pages", criteria);    
    	
    	return "board/qna/qna";
    }

    /**
     * QnA 게시판 관리자 답변 등록 페이지로 이동한다.
     *
     * @param model
     * @return
     */
    @GetMapping("/qna/modify/{qnaId}")
    public String qnaRegist(@PathVariable String qnaId, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	QnaModel qnaModel = new QnaModel();
    	qnaModel.setQnaId(qnaId);
    	QnaModel qModel = service.selectQna(qnaModel);
    	
    	model.addAttribute("qnaInfo", qModel);
    	
    	//QNA유형 구분 코드
        model.addAttribute("codeQnaCateCdList", codeService.selectGroupIdAllList("QNA_CAT"));
        
    	//QNA상태 구분 코드
        model.addAttribute("codeQnaStatCdList", codeService.selectGroupIdAllList("QNA_STAT_CODE"));
        
    	//공개여부 구분 코드
        model.addAttribute("openYnCdList", codeService.selectGroupIdAllList("OPEN_YN"));
        
    	//공개여부 구분 코드
        model.addAttribute("codeUseYnList", codeService.selectGroupIdAllList("USE_YN"));        
        
        //파일 조회
    	FileModel f = new FileModel();
    	f.setRefId(qnaId);
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
        
    	return "board/qna/qnaRegist";
    }
    
    /**
     * QnA 게시판 관리자 답변 등록한다.
     *
     * @param QnaModel qnaInfo
     * @return
     */
    @PostMapping("/qna/update")
    public String qnaUpdate(@ModelAttribute QnaModel qnaInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	qnaInfo.setModiId(authUser.getMemberModel().getUserId());
    	qnaInfo.setAnswRgstId(authUser.getMemberModel().getUserId());
    	model.addAttribute("qnaUpdateInfo", service.updateQna(qnaInfo));
    	
    	if(qnaInfo.getFileIds() != null) {
    		for(String fileId : qnaInfo.getFileIds()) {
    			FileModel file = new FileModel();
    			file.setFileId(fileId);
    			file.setRefId(qnaInfo.getQnaId());
    			file.setModiId(authUser.getMemberModel().getUserId());
    			fileService.updateFile(file);
    		}
    	}
    	
    	return "redirect:/board/qna";
    }
    
    /**
     *  QnA 게시판 관리자 답변 삭제한다
     * @param noticeId
     * @param model
     * @return
     */
    @PostMapping("/qna/delete")
    public String delete(@ModelAttribute QnaModel qnaInfo, @AuthenticationPrincipal AuthUser authUser, Model model) {
    	qnaInfo.setModiId(authUser.getMemberModel().getUserId());
    	model.addAttribute("qnaDeleteInfo", service.deleteQna(qnaInfo));
    	return "redirect:/board/qna";
    }
}

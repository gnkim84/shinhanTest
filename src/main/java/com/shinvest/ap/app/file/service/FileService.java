package com.shinvest.ap.app.file.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.shinvest.ap.app.file.mapper.FileMapper;
import com.shinvest.ap.app.file.model.FileModel;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.IdUtil;
import com.shinvest.ap.common.aws.AwsS3Util;
import com.shinvest.ap.config.props.HrProps;
import com.shinvest.ap.config.props.TableauProps;
import com.shinvest.ap.config.security.AuthUser;

import lombok.extern.slf4j.Slf4j;

/**
 * File Upload and Download Service
 */
@Slf4j
@Service
public class FileService {

	// 사용자 사진 기본 이미지
	@Value(Constant.DEFAULT_USER_PHOTO)
	private ClassPathResource defaultUserPhoto;

	// 태블로 프리뷰 기본 이미지
	@Value(Constant.DEFAULT_TABLEAU_PREVIEW)
	private ClassPathResource defaultPreview;

	@Resource(name = "hrProps")
	private HrProps hrProps;

	@Resource(name = "tableauProps")
	private TableauProps tableauProps;

	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;
	
	@Resource(name="idUtil")
	private IdUtil idUtil;

	@Resource(name = "fileMapper")
	private FileMapper mapper;

	@Resource(name = "awsS3Util")
	private AwsS3Util s3Util;

	@Resource(name = "fileService")
	private FileService fileService;

	/**
	 * 파일 다운로드 헤더
	 */
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Pragma", "no-cache");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		return headers;
	}

	/**
	 * 파일 다운로드 헤더
	 */
	public HttpHeaders getHeaders(String fileNm) {
		HttpHeaders headers = getHeaders();
		if (StringUtils.isNotBlank(fileNm)) {
			String downFileNm = "";
			try {
				downFileNm = URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20");
			} catch (UnsupportedEncodingException e) {
				log.warn("File download name URLEncoder encode Exception");
				downFileNm = "download";
			}
			headers.add("Content-Disposition", StringUtils.joinWith(null, "attachment;filename=\"", downFileNm,"\""));
		}
		return headers;
	}
	
	/**
	 * 파일을 ByteArrayResource 로 변환
	 * 
	 * @param model
	 * @return
	 */
	public ByteArrayResource getByteArrayResource(FileModel model) {
		ByteArrayResource resource = null;
		try {
			if (StringUtils.equals(Constant.File.AWS_S3, model.getStorageSe())) {
				// S3
				if (s3Util.download(model)) {
					resource = new ByteArrayResource(IOUtils.toByteArray(model.getInputStream()));
				}
			}
		} catch (IOException e) {
			log.warn("file 변환 처리 중 오류");
			log.warn(e.getMessage());
		}
		return resource;
	}

	/**
	 * 사용자 기본 사진
	 * 
	 * @return
	 */
	public ByteArrayResource getDefaultPhoto() {
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(IOUtils.toByteArray(defaultUserPhoto.getInputStream()));
		} catch (IOException e) {
			log.warn("사용자 기본 사진 처리 중 오류");
			log.warn(e.getMessage());
		}
		return resource;
	}

	/**
	 * 태블로 기본 프리뷰
	 */
	public ByteArrayResource getDefaultPreview() {
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(IOUtils.toByteArray(defaultPreview.getInputStream()));
		} catch (IOException e) {
			log.warn("사용자 기본 사진 처리 중 오류");
			log.warn(e.getMessage());
		}
		return resource;
	}

	/**
	 * File Upload
	 * 
	 * @param request
	 * @param multipart
	 * @return
	 * @throws IOException 
	 */
	public Map<String, Object> upload(HttpServletRequest request, MultipartRequest multipart, AuthUser authUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		final Map<String, MultipartFile> files = multipart.getFileMap();
		ArrayList<String> fileIds = new ArrayList<String>();
		String fileId = "";
		String fileUrl = "";
		String result = "success";
		String resultMessage = "성공";
		String fileCl = request.getParameter("fileCl") == null ? "" : request.getParameter("fileCl");
		MultipartFile file = null;
		for (String key : files.keySet()) {
			 file = files.get(key);
			 
			 FileModel f = new FileModel();
			 fileId = idUtil.getFileId();
			 fileIds.add(fileId);
			 f.setFileId(fileId);
			 //s3 기본 처리
			 //f.setStorageSe("");
			 //f.setSavePath("");
			 //f.setBucketNm("");
			 //f.setSaveFileVer("");
			 //f.setSaveFileNm("");
			 
			 //s3 기본 처리
			 f.setFileNm(file.getOriginalFilename());
			 f.setFileExtsn(FilenameUtils.getExtension(file.getOriginalFilename()));
			 f.setFileSize(file.getSize());
			 f.setUseYn("Y");
			 f.setRgstId(authUser.getMemberModel().getUserId());
			 f.setModiId(authUser.getMemberModel().getUserId());
			 
			 f.setFileUrl("");//특정 생성값이 필요하면 채운다.
			 if("".equals(fileCl)) {
				 f.setFileCl(Constant.File.BOARD);
			 }else if("Q".equals(fileCl)) {
				 f.setFileCl(Constant.File.BOARDQ);
			 }else if("A".equals(fileCl)) {
				 f.setFileCl(Constant.File.BOARDA);
			 }else if("B".equals(fileCl)) {
				 f.setFileCl(Constant.File.EDITOR);
			 }
			 
			 f.setRefId("");
			 //f.setRefVer("");
			 try {
				f.setInputStream(file.getResource().getInputStream());
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				 result = "fail";
				 resultMessage = "실패";
			 }
			 //파일 생성
			 if(!"fail".equals(result)) {
				 insertFile(f);
				 if (s3Util.upload(f)) {
					 log.debug("s3 upload success!");
					 updateFile(f);
				 }else {
					 result = "fail";
					 resultMessage = "실패";
				 }
				 //운영에 배포시 삭제
				 //updateFile(f);
				 //result = "success";
				 //resultMessage = "성공";
			 }
			 fileUrl = f.getFileUrl();
		}
		
		resultMap.put("result", result);
		resultMap.put("resultMessage", resultMessage);
		resultMap.put("fileIds", fileIds);
		resultMap.put("fileId", fileId);
		resultMap.put("fileUrl", fileUrl);
		return resultMap;
	}

	/**
	 * File Download
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> download(String uuid) {

		ByteArrayResource resource = null;

		long fileSize = 0;
		String fileNm = "";
		// request의 파일 정보 파라메터로 파일을 조회 후 다운로드
		FileModel f = new FileModel();
		f.setFileUrl(uuid);
		FileModel f1 = fileService.selectFile(f);
		if(f1 != null) {
			resource = getByteArrayResource(f1);
			if (resource != null) {
				fileSize = resource.contentLength();
			}
			fileNm = f1.getFileNm();
		}
		
		return ResponseEntity.ok().headers(getHeaders(fileNm)).contentLength(fileSize).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	/**
	 * File delete
	 * 
	 * @param request
	 * @param multipart
	 * @return
	 */
	public Map<String, Object> delete(HttpServletRequest request, FileModel file, AuthUser authUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
//		s3Util.delete(f);
		result.put("result", "success");
		result.put("resultMessage", "성공");
		return result;
	}
	
	/**
	 * Tableau preview Download
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> getPreview(HttpServletRequest request, String uuid) {
		// DB 조회 해서 이미지 가져오기
		FileModel model = null;
		String fileNm = null;
		ByteArrayResource resource = null;
		if (StringUtils.isNotBlank(uuid)) {
			model = mapper.selectPreviewFile(uuid);
			if (model != null) {
				resource = getByteArrayResource(model);
				fileNm = model.getFileNm();
			}
		}

		if (resource == null) {
			resource = getDefaultPreview();
			fileNm = "img_noimg.png";
		}
		long fileSize = resource.contentLength();

		return ResponseEntity.ok().headers(getHeaders(fileNm)).contentLength(fileSize).contentType(MediaType.IMAGE_PNG)
				.body(resource);
	}

	/**
	 * Tableau view Download
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> getView(HttpServletRequest request, String uuid) {
		// DB 조회 해서 이미지 가져오기
		FileModel model = null;
		String fileNm = null;
		ByteArrayResource resource = null;
		if (StringUtils.isNotBlank(uuid)) {
			model = mapper.selectViewFile(uuid);
			if (model != null) {
				resource = getByteArrayResource(model);
				fileNm = model.getFileNm();
			}
		}

		if (resource == null) {
			resource = getDefaultPreview();
			fileNm = "img_noimg.png";
		}
		long fileSize = resource.contentLength();

		return ResponseEntity.ok().headers(getHeaders(fileNm)).contentLength(fileSize).contentType(MediaType.IMAGE_PNG)
				.body(resource);
	}
	
	/**
	 * 사용자 사진 가져오기 - 특정 사용자
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> getUserPhoto(HttpServletRequest request, String uuid) {

		// DB 조회 해서 이미지 가져오기
		FileModel model = null;
		String fileNm = null;
		ByteArrayResource resource = null;
		if (StringUtils.isNotBlank(uuid)) {
			model = mapper.selectPhotoFile(uuid);
			if (model != null) {
				resource = getByteArrayResource(model);
				fileNm = model.getFileNm();
			}
		}

		if (resource == null) {
			resource = getDefaultPhoto();
			fileNm = "icon_top_user.png";
		}
		long fileSize = resource.contentLength();

		return ResponseEntity.ok().headers(getHeaders(fileNm)).contentLength(fileSize).contentType(MediaType.IMAGE_JPEG)
				.body(resource);
	}

	/**
	 * 사용자 사진 가져오기 - 로그인 사용자
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> getUserPhoto(HttpServletRequest request, AuthUser authUser) {

		String fileUrl = authUser.getMemberModel().getFileUrl();
		// DB 조회 해서 이미지 가져오기
		FileModel model = null;
		String fileNm = null;
		ByteArrayResource resource = null;
		if (StringUtils.isNotBlank(fileUrl)) {
			model = mapper.selectPhotoFile(fileUrl);
			if (model != null) {
				resource = getByteArrayResource(model);
				fileNm = model.getFileNm();
			}
		}

		if (resource == null) {
			resource = getDefaultPhoto();
			fileNm = "icon_top_user.png";
		}
		long fileSize = resource.contentLength();

		return ResponseEntity.ok().headers(getHeaders(fileNm)).contentLength(fileSize).contentType(MediaType.IMAGE_JPEG)
				.body(resource);
	}
	
	/**
	 * 에디터 이미지 가져오기
	 * 
	 * @param request
	 * @return
	 */
	public ResponseEntity<ByteArrayResource> getEditorImage(HttpServletRequest request, String uuid) {

		// DB 조회 해서 이미지 가져오기
		FileModel model = null;
		String fileNm = null;
		ByteArrayResource resource = null;
		if (StringUtils.isNotBlank(uuid)) {
			model = mapper.selectEditorImageFile(uuid);
			if (model != null) {
				resource = getByteArrayResource(model);
				fileNm = model.getFileNm();
			}
		}

		if (resource == null) {
			resource = getDefaultPreview();
			fileNm = "icon_top_user.png";
		}
		long fileSize = resource.contentLength();
		
		return ResponseEntity.ok().headers(getHeaders()).contentLength(fileSize).contentType(MediaType.IMAGE_JPEG)
				.body(resource);		
	}
	
	/**
	 * 파일 조회(list)
	 * @param model
	 * @return
	 */
	public List<FileModel> selectFileList(FileModel model) {
		return mapper.selectFileList(model);
	}
	
	/**
	 * 파일 조회(단건)
	 * @param model
	 * @return
	 */
	public FileModel selectFile(FileModel model) {
		return mapper.selectFile(model);
	}
	
	/**
	 * 파일 신규 생성
	 * @param model
	 * @return
	 */
	public long insertFile(FileModel model) {
		return mapper.insertFile(model);
	}

	/**
	 * 파일 수정
	 * @param model
	 * @return
	 */
	public long updateFile(FileModel model) {
		return mapper.updateFile(model);
	}

	/**
	 * 파일 삭제
	 * @param model
	 * @return
	 */
	public long deleteFile(FileModel model) {
		return mapper.deleteFile(model);
	}

	/**
	 * 파일 삭제(부모 아이디로)
	 * @param model
	 * @return
	 */
	public long deleteFileParent(FileModel model) {
		return mapper.deleteFileParent(model);
	}

}

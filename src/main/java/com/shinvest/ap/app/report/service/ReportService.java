package com.shinvest.ap.app.report.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.member.model.MemberModel;
import com.shinvest.ap.app.member.service.MemberService;
import com.shinvest.ap.app.project.model.ProjectCriteria;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.app.report.ReportController;
import com.shinvest.ap.app.report.mapper.ReportMapper;
import com.shinvest.ap.app.report.model.ReportModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;
import com.shinvest.ap.common.tableauapi.service.TableauCommonService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

	@Resource
	private ReportMapper reportMapper;

	@Resource
	CodeService codeService;

	@Resource
	TableauCommonService tableauCommonService;
	
	@Resource
	MemberService memberService;
	
	public List<ReportModel> selectReportList(Criteria criteria) {
		return reportMapper.selectReportList(criteria);
	}
	public int selectReportListCount(Criteria criteria) {
		return reportMapper.selectReportListCount(criteria);
	}
	public ReportModel selectReport(ReportModel model) {
		return reportMapper.selectReport(model);
	}
	
	@Transactional
	public String deleteReport(ReportModel model) {
		
		long count = reportMapper.deleteReport(model);
		reportMapper.insertReportHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
		
	}
	
	@Transactional
	public String insertReport(ReportModel model) {

		model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);

		long count = reportMapper.insertReport(model);
		reportMapper.insertReportHst(model);
		
		//Tableau 사용자 권한 부여
		addTableauUserAuth(model);
		//Tableau 사용자 권한 회수
		//deleteTableauUserAuth(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String updateReport(ReportModel model, MemberModel user) {
		
		/**
		 *  메이저 버전 업데이트/ 마이너 버전 업데이트 여부에 따른 JAVA 혹은 DB 상의 값 셋팅
		 * 올릴 버전 값을 세팅해주고 쿼리상에서 연산
		 */
		
		model.setMinorVer(new BigDecimal("0.1").toString());									// 마이너
		model.setUseYn(Constant.YES);
		
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("0.1")).toString());		// 마이너
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("1")).toString());		// 메이저

		long count = reportMapper.updateReport(model);
		reportMapper.insertReportHst(model);
		
		//보고서 사용권한
		updateReportUserRole(model, user);
		
		//Tableau 사용자 권한 부여
		//addTableauUserAuth(model);
		//Tableau 사용자 권한 회수
		//deleteTableauUserAuth(model);		
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String updateReportUserRole(ReportModel model, MemberModel user) {
		String result = "";
		//사용자 롤 정보 hist로 옮김 
		insertSelectReportUserRoleHist(model);
		
		deleteReportUserRole(model);
		
		//참여자
		JSONArray pcptUsers = model.getPcptInfo().optJSONObject("list").optJSONArray("userList");
		for(int i=0;i < pcptUsers.length();i++) {
			JSONObject pcpt = pcptUsers.optJSONObject(i);
			ReportModel ref = new ReportModel();
			ref.setReportId(model.getReportId());
			ref.setVer(model.getVer());
			ref.setMinorVer(model.getMinorVer());
			ref.setRefId(pcpt.getString("refId"));
			ref.setRefTy("1");//1:사용자, 2:부서, 3:그룹
			ref.setRoleSe("2");//1:관리자, 2:참여자
			ref.setRgstId(user.getUserId());
			ref.setModiId(user.getUserId());
			result = insertReportUserRole(ref);
		}

		JSONArray pcptDepts = model.getPcptInfo().optJSONObject("list").optJSONArray("deptList");
		for(int i=0;i < pcptDepts.length();i++) {
			JSONObject pcpt = pcptDepts.optJSONObject(i);
			ReportModel ref = new ReportModel();
			ref.setReportId(model.getReportId());
			ref.setVer(model.getVer());
			ref.setMinorVer(model.getMinorVer());
			ref.setRefId(pcpt.getString("refId"));
			ref.setRefTy("2");//1:사용자, 2:부서, 3:그룹
			ref.setRoleSe("2");//1:관리자, 2:참여자
			ref.setRgstId(user.getUserId());
			ref.setModiId(user.getUserId());
			result = insertReportUserRole(ref);
		}
		
		//편집권한
		JSONArray mgrUsers = model.getMgrInfo().optJSONObject("list").optJSONArray("userList");
		for(int i=0;i < mgrUsers.length();i++) {
			JSONObject mgr = mgrUsers.optJSONObject(i);
			ReportModel ref = new ReportModel();
			ref.setReportId(model.getReportId());
			ref.setVer(model.getVer());
			ref.setMinorVer(model.getMinorVer());
			ref.setRefId(mgr.getString("refId"));
			ref.setRefTy("1");//1:사용자, 2:부서, 3:그룹
			ref.setRoleSe("1");//1:관리자, 2:참여자
			ref.setRgstId(user.getUserId());
			ref.setModiId(user.getUserId());
			result = insertReportUserRole(ref);
		}

		JSONArray mgrDepts = model.getMgrInfo().optJSONObject("list").optJSONArray("deptList");
		for(int i=0;i < mgrDepts.length();i++) {
			JSONObject mgr = mgrDepts.optJSONObject(i);
			ReportModel ref = new ReportModel();
			ref.setReportId(model.getReportId());
			ref.setVer(model.getVer());
			ref.setMinorVer(model.getMinorVer());
			ref.setRefId(mgr.getString("refId"));
			ref.setRefTy("2");//1:사용자, 2:부서, 3:그룹
			ref.setRoleSe("1");//1:관리자, 2:참여자
			ref.setRgstId(user.getUserId());
			ref.setModiId(user.getUserId());
			result = insertReportUserRole(ref);
		}
		
        return result;
	}
	
	public List<ProjectModel> selectProjectList(ProjectCriteria criteria) {
		return reportMapper.selectProjectList(criteria);
	}
    
	/**
	 * 자신이 참여자로 존재하는 시각화 프로젝트 리스트 가져오기
	 * @param user
	 * @param model
	 */
    public void getVWProjectList(MemberModel user, Model model) {
    	ProjectCriteria criteria = new ProjectCriteria();
    	JSONObject pcptInfo = new JSONObject();
    	pcptInfo.put(Constant.KEY.USER_ID, user.getUserId());
    	criteria.setPcptInfo(pcptInfo);
    	criteria.setProjectTy(Constant.PROJECT.TYPE_VW);
        //프로젝트 리스트의 Id 와 Nm 가져오기
    	model.addAttribute("projectList", selectProjectList(criteria));
    }
	
    /**
     * 모든 보고서 코드 가져오기
     * @param model
     */
	public void selectAllReportCodeList(Model model) {

		model.addAttribute("codeRptTyList", codeService.selectGroupIdAllList("RPT_TY"));
    	model.addAttribute("codeActiveYnList", codeService.selectGroupIdAllList("ACTIVE_YN"));
    	model.addAttribute("codeRptAtmcAprvYnList", codeService.selectGroupIdAllList("RPT_ATMC_APRV_YN"));
    	
    }
	
	public void addTableauUserAuth(ReportModel model) {
		
		//사용자 권한 관리
		JSONArray pcpts = model.getPcptInfo().optJSONObject("list").optJSONArray("userList");
		JSONArray oldPcpts = model.getOldPcptInfo().optJSONObject("list").optJSONArray("userList");
		
		JSONArray oldPcptUserIds = model.getOldPcptInfo().optJSONArray("ids");
		
		for(int i=0;i < pcpts.length();i++) {
			JSONObject pcpt = pcpts.getJSONObject(i);
			String tableauUserId = pcpt.optString("tableauUserId");
			String pcptUserId = pcpt.optString("refId");
			String workbookId = model.getTableauWorkbookId();
			String auth = "2";
			try {
				if(oldPcptUserIds == null) {
					tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
				}else if(!oldPcptUserIds.toList().contains(pcptUserId)) {	
					log.info("#addPermissionToWorkbook pcptId = " + pcptUserId);
					tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//편집권한
		JSONArray mgrs = model.getMgrInfo().optJSONObject("list").optJSONArray("userList");
		JSONArray oldMgrs = model.getOldMgrInfo().optJSONObject("list").optJSONArray("userList");
		
		JSONArray oldMgrUserIds = model.getOldMgrInfo().optJSONArray("ids");
		
		for(int i=0;i < mgrs.length();i++) {
			JSONObject mgr = mgrs.getJSONObject(i);
			String tableauUserId = mgr.optString("tableauUserId");
			String mgrUserId = mgr.optString("refId");
			String workbookId = model.getTableauWorkbookId();
			String auth = "1";
			try {
				if(oldMgrUserIds == null) {
					tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
				}else if(!oldMgrUserIds.toList().contains(mgrUserId)) {
					log.info("#addPermissionToWorkbook mgrId = " + mgrUserId);
					tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addTableauDeptAuth(ReportModel model) {
		
		//사용자 권한 관리
		JSONArray pcpts = model.getPcptInfo().optJSONObject("list").optJSONArray("deptList");
		JSONArray oldPcpts = model.getOldPcptInfo().optJSONObject("list").optJSONArray("deptList");
		
		JSONArray oldPcptUserIds = model.getOldPcptInfo().optJSONArray("ids");
		
		for(int i=0;i < pcpts.length();i++) {
			JSONObject pcpt = pcpts.getJSONObject(i);
			String deptCode = pcpt.optString("refId");
			String workbookId = model.getTableauWorkbookId();
			String auth = "2";
			//조직코드로 사용자 조회하여 loop
			if(oldPcptUserIds == null || !oldPcptUserIds.toList().contains(deptCode)) {
				Criteria criteria = new Criteria();
				criteria.setSearchKey("deptCode");
				criteria.setSearchValue(deptCode);
				List<MemberModel> list = memberService.selectMemberAllList(criteria);
				for(MemberModel member : list) {
					String tableauUserId = member.getTableauUserId();
					try {
							log.info("#addPermissionToWorkbook pcptId = " + deptCode);
							tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		//편집권한
		JSONArray mgrs = model.getMgrInfo().optJSONObject("list").optJSONArray("deptList");
		JSONArray oldMgrs = model.getOldMgrInfo().optJSONObject("list").optJSONArray("deptList");
		
		JSONArray oldMgrUserIds = model.getOldMgrInfo().optJSONArray("ids");
		
		for(int i=0;i < mgrs.length();i++) {
			JSONObject mgr = mgrs.getJSONObject(i);
			String deptCode = mgr.optString("refId");
			String workbookId = model.getTableauWorkbookId();
			String auth = "1";
			//조직코드로 사용자 조회하여 loop
			if(oldPcptUserIds == null || !oldMgrUserIds.toList().contains(deptCode)) {
				Criteria criteria = new Criteria();
				criteria.setSearchKey("deptCode");
				criteria.setSearchValue(deptCode);
				List<MemberModel> list = memberService.selectMemberAllList(criteria);
				for(MemberModel member : list) {
					String tableauUserId = member.getTableauUserId();
					try {
							log.info("#addPermissionToWorkbook pcptId = " + deptCode);
							tableauCommonService.addPermissionToWorkbook(tableauUserId,workbookId, auth);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	private void deleteTableauUserAuth(ReportModel model) {
		//사용자 권한 관리
		JSONArray pcpts = model.getPcptInfo().optJSONObject("list").optJSONArray("userList");
		JSONArray oldPcpts = model.getOldPcptInfo().optJSONObject("list").optJSONArray("userList");
		
		JSONArray pcptIds = model.getPcptInfo().optJSONArray("ids");

		if(oldPcpts != null) {
			for(int i=0;i < oldPcpts.length();i++) {
				JSONObject oldPcpt = oldPcpts.optJSONObject(i);
				String deptCode = oldPcpt.optString("refId");
				String workbookId = model.getTableauWorkbookId();
				String auth = "2";
				if(!pcptIds.toList().contains(deptCode)) {
					Criteria criteria = new Criteria();
					criteria.setSearchKey("deptCode");
					criteria.setSearchValue(deptCode);
					List<MemberModel> list = memberService.selectMemberAllList(criteria);
					for(MemberModel member : list) {
						String tableauUserId = member.getTableauUserId();
						try {
							log.info("#deleteTableauUserAuth pcptId = " + deptCode);
							tableauCommonService.deletePermissionToWorkbook(tableauUserId, workbookId, auth);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
			}
		}

		//편집권한
		JSONArray mgrs = model.getMgrInfo().optJSONObject("list").optJSONArray("userList");
		JSONArray oldMgrs = model.getOldMgrInfo().optJSONObject("list").optJSONArray("userList");
		
		JSONArray mgrIds = model.getMgrInfo().optJSONArray("ids");
		if(oldMgrs != null) {
			for(int i=0;i < oldMgrs.length();i++) {
				JSONObject oldMgr = oldMgrs.optJSONObject(i);
				String deptCode = oldMgr.optString("refId");
				String workbookId = model.getTableauWorkbookId();
				String auth = "1";
				
				if(!mgrIds.toList().contains(deptCode)) {
					Criteria criteria = new Criteria();
					criteria.setSearchKey("deptCode");
					criteria.setSearchValue(deptCode);
					List<MemberModel> list = memberService.selectMemberAllList(criteria);
					for(MemberModel member : list) {
						String tableauUserId = member.getTableauUserId();
						try {
								log.info("#deleteTableauUserAuth mgrId = " + deptCode);
								tableauCommonService.deletePermissionToWorkbook(tableauUserId, workbookId, auth);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
			}
		}
	}

	private void deleteTableauDeptAuth(ReportModel model) {
		//사용자 권한 관리
		JSONArray pcpts = model.getPcptInfo().optJSONObject("list").optJSONArray("deptList");
		JSONArray oldPcpts = model.getOldPcptInfo().optJSONObject("list").optJSONArray("deptList");
		
		JSONArray pcptIds = model.getPcptInfo().optJSONArray("ids");
		
		if(oldPcpts != null) {
			for(int i=0;i < oldPcpts.length();i++) {
				JSONObject oldPcpt = oldPcpts.optJSONObject(i);
				String tableauUserId = oldPcpt.optString("tableauUserId");
				String oldPcptId = oldPcpt.optString("refId");
				String workbookId = model.getTableauWorkbookId();
				String auth = "2";
				try {
					if(!pcptIds.toList().contains(oldPcptId)) {
						log.info("#deleteTableauUserAuth pcptId = " + oldPcptId);
						tableauCommonService.deletePermissionToWorkbook(tableauUserId, workbookId, auth);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//편집권한
		JSONArray mgrs = model.getMgrInfo().optJSONObject("list").optJSONArray("deptList");
		JSONArray oldMgrs = model.getOldMgrInfo().optJSONObject("list").optJSONArray("deptList");
		
		JSONArray mgrIds = model.getMgrInfo().optJSONArray("ids");
		if(oldMgrs != null) {
			for(int i=0;i < oldMgrs.length();i++) {
				JSONObject oldMgr = oldMgrs.optJSONObject(i);
				String tableauUserId = oldMgr.optString("tableauUserId");
				String oldMgrId = oldMgr.optString("refId");
				String workbookId = model.getTableauWorkbookId();
				String auth = "1";
				try {
					if(!mgrIds.toList().contains(oldMgrId)) {
						log.info("#deleteTableauUserAuth mgrId = " + oldMgrId);
						tableauCommonService.deletePermissionToWorkbook(tableauUserId, workbookId, auth);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public List<Map<String, String>> selectTableauWorkbookList() {
		return reportMapper.selectTableauWorkbookList();
	}	
	
	@Transactional
	public String insertReportUserRole(ReportModel model) {

		//model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);
		
		long count = reportMapper.insertReportUserRole(model);
		//reportMapper.insertReportHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}

	@Transactional
	public String updateReportUserRole(ReportModel model) {
		
		//model.setVer(new BigDecimal("0.1").toString()); 
		model.setUseYn(Constant.YES);
		
		long count = reportMapper.updateReportUserRole(model);
		//reportMapper.insertReportHst(model);
		
		if(count > 0) {
			return Constant.DB.INSERT;
		} else {
			return Constant.DB.FAIL;
		}
	}

	@Transactional
	public String deleteReportUserRole(ReportModel model) {
		
		//model.setVer(new BigDecimal("0.1").toString()); 
		model.setUseYn(Constant.YES);
		
		long count = reportMapper.deleteReportUserRole(model);
		//reportMapper.insertReportHst(model);
		
		if(count > 0) {
			return Constant.DB.INSERT;
		} else {
			return Constant.DB.FAIL;
		}
	}
	
	@Transactional
	public String insertReportUserRoleHist(ReportModel model) {

		//model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);
		
		long count = reportMapper.insertReportUserRoleHist(model);
		//reportMapper.insertReportHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String insertSelectReportUserRoleHist(ReportModel model) {
		
		//model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);
		
		long count = reportMapper.insertSelectReportUserRoleHist(model);
		//reportMapper.insertReportHst(model);
		
		if(count > 0) {
			return Constant.DB.INSERT;
		} else {
			return Constant.DB.FAIL;
		}
	}
	
	public ReportModel selectCheckReportUserRole(ReportModel model) {
		return reportMapper.selectCheckReportUserRole(model);
	}

	public List<ReportModel> selectReportUserRole(ReportModel model) {
		return reportMapper.selectReportUserRole(model);
	}
}

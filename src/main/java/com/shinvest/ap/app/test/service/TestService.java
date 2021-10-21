package com.shinvest.ap.app.test.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.shinvest.ap.app.test.mapper.TestMapper;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.tableauapi.model.ProjectListType;
import com.shinvest.ap.common.tableauapi.model.TableauCredentialsType;
import com.shinvest.ap.common.tableauapi.util.TableauApiUtils;
import com.shinvest.ap.config.props.TableauProps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile({Constant.Profile.DEV,Constant.Profile.LOCAL})
public class TestService {
	
	@Resource(name="testingService")
	private TestingService service;

	@Resource(name="testMapper")
	private TestMapper mapper;
	
	@Resource(name="tableauApiUtils")
	private TableauApiUtils tableauApiUtils;
	
	@Resource(name="tableauProps")
	private TableauProps tableauProps;
	
	public void testException() {
		try {
			log.debug("test service try-catch start");
			service.testExceptoin();
		} catch(Exception e) {
			log.warn("test catch exception ##############################");
			log.warn(e.getMessage());
		}
	}
	
	public String testTableau() {
		String result = StringUtils.EMPTY;
		
		//sing in
		log.debug("Tableau sing in");
		TableauCredentialsType tableauCredentials = tableauApiUtils.invokeSignIn(tableauProps.getUsername(), "admin", StringUtils.EMPTY);
		log.debug("TableauCredentialsType : ",tableauCredentials.toString());
		ProjectListType projectList = tableauApiUtils.invokeQueryProjectsAll(tableauCredentials, tableauCredentials.getSite().getId());
		log.debug("ProjectListType : ",projectList.toString());
		//sing out
		tableauApiUtils.invokeSignOut(tableauCredentials);
		log.debug("Tableau sing out");
		
		result = projectList.toString();
		
		return result;
	}
}

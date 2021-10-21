package com.shinvest.ap.config.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.shic.eai.EaiCertEmp;
import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.config.props.UdbProps;
import com.shinvest.ap.config.security.mapper.SecurityMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthProvider extends DaoAuthenticationProvider {

	@Resource(name = "udbProps")
	private UdbProps props;

	@Resource(name = "commonUtil")
	private CommonUtil util;

	@Resource(name = "securityMapper")
	private SecurityMapper mapper;
	
	@Autowired
	private HttpServletRequest request;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// DB 조회 정보
		AuthUser authUser = (AuthUser) userDetails;

		// 사용자 입력 ID, PW
		// ID : authentication.getName()
		// PW : authentication.getCredentials().toString()
		
		if (util.isProd()) {
			// 운영
			if (authUser.isSso()) {
				if (authUser.getAuthorities().size() > 0) {
					// SSO 인증 authUser
				} else {
					// 미인증 authUser
					throw new CredentialsExpiredException(mapper.selectLoginMessage(Constant.LoginMessage.SSO_LOGIN_FAIL));
				}
			} else {
				// UDB 로그인
				if (checkUDB(authentication.getName(), authentication.getCredentials().toString())) {
					//UDB 인증 성공
				} else {
					//UDB 인증 실패
					throw new LockedException(mapper.selectLoginMessage(Constant.LoginMessage.UDB_LOGIN_FAIL));
				}
			}
		} else {
			// 개발 및 로컬
			// 외주 개발자 테스트 인 경우 MODI_SE = "R"
			if (StringUtils.equals("R", authUser.getMemberModel().getModiSe())) {
				if (!StringUtils.equals(authentication.getCredentials().toString(), "test")) {
					throw new BadCredentialsException(mapper.selectLoginMessage(Constant.LoginMessage.LOGIN_FAIL));
				}
			} else {
				if (authUser.isSso()) {
					// SSO 로그인
					if (authUser.getAuthorities().size() > 0) {
						// SSO 인증 authUser
					} else {
						// 미인증 authUser
						throw new CredentialsExpiredException(mapper.selectLoginMessage(Constant.LoginMessage.SSO_LOGIN_FAIL));
					}
				} else {
					// UDB 로그인
					if (checkUDB(authentication.getName(), authentication.getCredentials().toString())) {
						//UDB 인증 성공
					} else {
						//UDB 인증 실패
						throw new LockedException(mapper.selectLoginMessage(Constant.LoginMessage.UDB_LOGIN_FAIL));
					}
				}
			}
		}
	}

	// UDB 사용자 인증
	private boolean checkUDB(String userName, String password) {
		boolean result = false;
		String msg = null;
		int check = 5;
		EaiCertEmp cc = new EaiCertEmp(userName, password, util.getClientIP(request), props.getChannelType(), props.getHostName(), props.getProdCheck());
		try {
			// UDB 사용자 로그인
			// 0 : 정상 로그인
			// 1 : 비밀번호 오류
			// 2 : 비밀번호 입력 5회 오류
			// 3 : 비밀번호 변경 필요 - 골드넷
			// 4 : 사용자 없음
			check = cc.check();
			// 메시지 offset 275byte, length 80byte
			msg = new String(cc.getRecvTr().getBytes("EUC-KR"), 275, 80, "EUC-KR").trim();
			
		} catch (Exception e) {
			log.warn("사용자 로그인 UDB 연동 중 오류 발생");
			log.warn(e.getMessage());
			if (StringUtils.isBlank(msg)) {
				msg = mapper.selectLoginMessage(Constant.LoginMessage.UDB_CONNECT_FAIL);
			}
		}
		
		if (check == 0) {
			//UDB 인증 성공
			result = true;
		} else {
			//UDB 인증 실패
			throw new LockedException(msg);
		}
		return result;
	}
}

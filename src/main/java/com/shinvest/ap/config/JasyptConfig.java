package com.shinvest.ap.config;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shinvest.ap.common.CommonUtil;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.aws.AwsSecretsUtil;

@Configuration
public class JasyptConfig {
	
	@Resource(name="commonUtil")
	private CommonUtil commonUtil;
	
	@Resource(name="awsSecretsUtil")
	private AwsSecretsUtil awsSecretsUtil;

	/**
	 * Jasypt 암호화
	 * @return
	 */
	@Bean
	public StringEncryptor jasyptStringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		// AWS Secret 에서 Jasypt Key 가져올 경우
//		if (StringUtils.equals(Constant.Profile.PROD, commonUtil.getProfile())) {
//			config.setPassword(awsSecretsUtil.getValue(Constant.AWS.JASYPT_KEY_NAME_PRD));
//		} else {
//			config.setPassword(awsSecretsUtil.getValue(Constant.AWS.JASYPT_KEY_NAME_DEV));
//		}
		
		config.setPassword(Constant.Jasypt.KEY);
		
		config.setAlgorithm(Constant.Jasypt.ALGORITHM);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");
		
		encryptor.setConfig(config);
		return encryptor;
	}
}

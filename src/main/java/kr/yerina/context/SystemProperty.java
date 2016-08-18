package kr.yerina.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import kr.yerina.constant.SystemConstants;


/**
 * The type System property.
 */
public class SystemProperty {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SystemProperty.class);

	/**
	 * Init.
	 *
	 * @throws Exception the exception
	 */
	public static void init() throws Exception {
		
		if(StringUtils.isEmpty(System.getProperty(SystemConstants.SERVER_TYPE))){
			
			logger.info(">>>>>> 시스템 환경변수에 server.type 값을 설정하세요! <<<<<<");
			logger.info("주의!> default 값인 " + SystemConstants.SERVER_TYPE_DEV + " 로 설정합니다.");
			
			System.setProperty(SystemConstants.SERVER_TYPE, SystemConstants.SERVER_TYPE_DEV);
			
		}
		
		logger.info(">>>>>> server.type : " + System.getProperty(SystemConstants.SERVER_TYPE) + " <<<<<<");
		
	}
	
}

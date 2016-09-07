package kr.yerina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.yerina.context.RootContext;
import kr.yerina.context.SystemProperty;

/**
 *
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        (new Application()).init();
    }

    private void init() {
        try {
            // System Property Setting
            SystemProperty.init();
            if(RootContext.applicationContext == null){
                logger.error("Spring applicationContext 설정 오류");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}

package kr.yerina.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.yerina.inf.BaseMessage;

/**
 * Created by philip on 2016-08-19.
 */

@Component
public class TelegramProcess {

    private final Logger logger  = LoggerFactory.getLogger(TelegramProcess.class);

    public void procWeatherInformation(BaseMessage msg) {
        logger.debug("procWeatherInformation");
    }

}

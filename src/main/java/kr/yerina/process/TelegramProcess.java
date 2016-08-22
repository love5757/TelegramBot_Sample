package kr.yerina.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.yerina.inf.BaseBroker;
import kr.yerina.inf.BaseMessage;

/**
 * Created by philip on 2016-08-19.
 */

@Component
public class TelegramProcess extends AbstractProcess{

    private final Logger logger  = LoggerFactory.getLogger(TelegramProcess.class);

    @Autowired
    public BaseBroker baseBroker;

    public void procWeatherInformation(BaseMessage msg) {
        logger.debug("procWeatherInformation");
    }

    public void procStartCommand(BaseMessage msg) {
        baseBroker.sendMessage(msg);
        logger.debug("procStartCommand");

    }

}

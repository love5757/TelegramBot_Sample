package kr.yerina.broker;

import kr.yerina.context.TelegramApplicationContext;
import kr.yerina.inf.BaseBroker;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.BaseMessageResolver;
import kr.yerina.inf.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by philip on 2016-08-19.
 */

@Component
public class MessageBroker implements BaseBroker{

    private static final Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    @Autowired
    private TelegramApplicationContext telegramApplicationContext;

    @Autowired
    private CommandExecutor queueCommandExecutor;


    public void receiveMessage(BaseMessage msg) {

        logger.debug(msg.getMessageCommand());

        try {

            queueCommandExecutor.execute(msg);

        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void sendMessage(BaseMessage msg) {
        BaseMessageResolver resolver = getResolver(msg);
        resolver.resolveMessage(msg);
    }

    private BaseMessageResolver getResolver(BaseMessage msg){

        BaseMessageResolver rtnResolver = null;

        for (BaseMessageResolver baseResolver : telegramApplicationContext.getMessageResolverList()) {
            if(baseResolver.canResolve(msg)){
                rtnResolver = baseResolver;
                break;
            }
        }

        return rtnResolver;

    }

}

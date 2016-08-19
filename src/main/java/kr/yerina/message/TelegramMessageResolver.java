package kr.yerina.message;

import kr.yerina.constant.BotConfig;
import kr.yerina.context.RootContext;
import kr.yerina.inf.BaseBroker;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.BaseMessageResolver;
import kr.yerina.message.parser.TelegramParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

/**
 * Created by philip on 2016-08-19.
 */
public class TelegramMessageResolver extends TelegramLongPollingCommandBot implements BaseMessageResolver {

    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageResolver.class);

    public void init() {

        try {
            TelegramBotsApi aa = new TelegramBotsApi();
            aa.registerBot(new TelegramMessageResolver());
            logger.debug("봇 등록 완료됨!!!!");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        BaseMessage msg = RootContext.applicationContext.getBean(TelegramParser.class).parseContents(update.getMessage());
        RootContext.applicationContext.getBean(BaseBroker.class).receiveMessage(msg);

    }

    @Override
    public String getBotUsername() { return BotConfig.CHAT_USER; }

    @Override
    public String getBotToken() { return BotConfig.CHAT_TOKEN; }

    @Override
    public boolean canResolve(BaseMessage msg) {
        //TODO 무조건 true 나중에 작성 해야함.
        return true;
    }

    @Override
    public void resolveMessage(BaseMessage msg) {
        logger.info("[{}]",msg);
    }
}

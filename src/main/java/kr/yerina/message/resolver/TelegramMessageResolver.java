package kr.yerina.message.resolver;

import kr.yerina.command.LottoCommand;
import kr.yerina.constant.BotConfig;
import kr.yerina.context.RootContext;
import kr.yerina.inf.BaseBroker;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.BaseMessageResolver;
import kr.yerina.message.parser.TelegramParser;
import kr.yerina.service.inf.BaseService;
import org.glassfish.grizzly.utils.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by philip on 2016-08-19.
 */
public class TelegramMessageResolver extends TelegramLongPollingCommandBot implements BaseMessageResolver {

    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageResolver.class);

    public static ConcurrentHashMap<Integer, Pair<Message,BaseMessage>> replyToMessageState = new ConcurrentHashMap<>();

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

        logger.info("[update][{}]", update);
        logger.info("[replyToMessageState][{}]", replyToMessageState);

        final Message message = update.getMessage();
        final int selfCheckId = message.getText().indexOf("@" + BotConfig.CHAT_USER);

        //명령어 parsing
        if( (message.getChat().isGroupChat() && message.getText().startsWith("/") && selfCheckId != -1) || (!message.getChat().isGroupChat() && message.getText().startsWith("/")) ){
            parseContentsAndReceiveMessage(message);
        }else{
            try {
                final Pair<Message, BaseMessage> baseMessageMessagePair = replyToMessageState.get(message.getFrom().getId());
                //로또 응답
                if(message.isReply() &&
                   baseMessageMessagePair.getSecond().getMessageCommand().equals(LottoCommand.messageCommand) &&
                   baseMessageMessagePair.getFirst().getMessageId().equals(message.getReplyToMessage().getMessageId()) ) {
                    sendMessage(RootContext.applicationContext.getBean(BaseService.class).lottoInformation(message));
                }else{
                    //일반 대화
                    sendMessage(RootContext.applicationContext.getBean(BaseService.class).simsimiConversation(message, null));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseContentsAndReceiveMessage(Message message) {
        BaseMessage msg = RootContext.applicationContext.getBean(TelegramParser.class).parseContents(message);
        msg.setMessage(message);
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

        final SendMessage sendMessage = RootContext.applicationContext.getBean(TelegramParser.class).makeContents(msg);
        logger.info("[sendMessage][{}]",sendMessage);

        try {
            sendMessageAsync(sendMessage, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> method, JSONObject jsonObject) {
                    Message sentMessage = method.deserializeResponse(jsonObject);
                    logger.info("[sentMessage][{}]",sentMessage);
                    if (sentMessage != null && msg.getMessageCommand().equals("/lotto")) {
                        //ReplyToMessageId이 있으면 message ID를 저장해 놓는다.
                        replyToMessageState.put(msg.getMessage().getFrom().getId(), new Pair(sentMessage, msg));
                    }
                }
                @Override
                public void onError(BotApiMethod<Message> botApiMethod, JSONObject jsonObject) {
                }
                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                }
            });

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

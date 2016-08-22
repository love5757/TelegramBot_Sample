package kr.yerina.command;

import kr.yerina.annotation.MessageMapping;
import kr.yerina.domain.message.TelegramMessage;
import kr.yerina.inf.BaseCommand;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.TelegramContentsMakable;
import kr.yerina.inf.TelegramContentsParsable;
import kr.yerina.process.TelegramProcess;
import kr.yerina.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;

/**
 * Created by philip on 2016-08-22.
 */
@MessageMapping(messageCommand = "/lotto")
public class LottoCommand implements BaseCommand, TelegramContentsParsable, TelegramContentsMakable {

    static final Logger logger = LoggerFactory.getLogger(LottoCommand.class);

    public static String messageCommand = "/lotto";

    @Autowired
    private TelegramProcess telegramProcess;

    @Override
    public void excuete(BaseMessage msg) {
        telegramProcess.procLottoCommand(msg);
    }

    @Override
    public BaseMessage parseContents(Message message) {
        TelegramMessage msg = new TelegramMessage();
        msg.setMessageCommand(CommonUtil.removeContainBotIdByCommandMessage(message));
        msg.setMessage(message);
        return msg;
    }

    @Override
    public SendMessage makeContents(BaseMessage msg) {
        TelegramMessage telegramMessage = (TelegramMessage) msg;

        SendMessage sendMessage = new SendMessage();
        StringBuilder messageBuilder = new StringBuilder();
        //응답 메세지 ID 셋팅
        sendMessage.setChatId(telegramMessage.getChatId());
        sendMessage.setReplyToMessageId(telegramMessage.getMessage().getMessageId());
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        sendMessage.setReplyMarkup(forceReplyKeyboard);

        messageBuilder.append("로또 1등 번호가 필요하시죠?").append("\n").append("갖고 싶은 선물을 입력하세요.");
        sendMessage.setChatId(telegramMessage.getMessage().getChatId().toString());
        sendMessage.setText(messageBuilder.toString());

        return sendMessage;
    }

}

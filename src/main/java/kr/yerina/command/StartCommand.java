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


@MessageMapping(messageCommand = "/start")
public class StartCommand implements BaseCommand, TelegramContentsParsable, TelegramContentsMakable {

    static final Logger logger = LoggerFactory.getLogger(StartCommand.class);

    @Autowired
    private TelegramProcess telegramProcess;

    @Override
    public void excuete(BaseMessage msg) {
        telegramProcess.procStartCommand(msg);
    }


    @Override
    public BaseMessage parseContents(Message message) {

        TelegramMessage msg = new TelegramMessage();
        msg.setMessageCommand(CommonUtil.removeContainBotIdByCommandMessage(message));
        return msg;
    }

    @Override
    public SendMessage makeContents(BaseMessage msg) {
        TelegramMessage telegramMessage = (TelegramMessage) msg;

        SendMessage sendMessage = new SendMessage();
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Welcome ").append(telegramMessage.getMessage().getFrom().getUserName()).append("\n");
        messageBuilder.append("this bot will demonstrate you the command feature of the Java TelegramBots API!");
        sendMessage.setChatId(telegramMessage.getMessage().getChatId().toString());
        sendMessage.setText(messageBuilder.toString());

        return sendMessage;
    }
}
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
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;

@MessageMapping(messageCommand = "/hello")
public class HelloCommand implements BaseCommand, TelegramContentsParsable, TelegramContentsMakable {

    static final Logger logger = LoggerFactory.getLogger(HelloCommand.class);


    @Autowired
    private TelegramProcess telegramProcess;

    @Override
    public void excuete(BaseMessage msg) {
        telegramProcess.procHelloCommand(msg);
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

        final Chat chat = msg.getMessage().getChat();
        String userName = chat.getUserName();
        if (userName == null || userName.isEmpty()) {
            userName = chat.getFirstName() + " " + chat.getLastName();
        }

        StringBuilder messageTextBuilder = new StringBuilder("Hello ").append(userName);

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageTextBuilder.toString());

        return answer;

    }

}
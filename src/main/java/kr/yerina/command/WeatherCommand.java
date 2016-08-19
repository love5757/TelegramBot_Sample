package kr.yerina.command;

import kr.yerina.domain.message.TelegramMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Message;

import kr.yerina.annotation.MessageMapping;
import kr.yerina.inf.BaseCommand;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.TelegramContentsParsable;
import kr.yerina.process.TelegramProcess;

/**
 * Created by philip on 2016-08-19.
 */
@MessageMapping(messageCommand = "/weather")
public class WeatherCommand implements BaseCommand, TelegramContentsParsable{

    private static final Logger logger = LoggerFactory.getLogger(WeatherCommand.class);

    @Autowired
    private TelegramProcess telegramProcess;

    @Override
    public void excuete(BaseMessage msg) {
        telegramProcess.procWeatherInformation(msg);
    }

    @Override
    public BaseMessage parseContents(Message message) {
        TelegramMessage msg = new TelegramMessage();
        msg.setMessageCommand(message.getText());
        return msg;
    }
}

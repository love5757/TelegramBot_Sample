package kr.yerina.domain.message;

import kr.yerina.inf.BaseMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Created by philip on 2016-08-19.
 */
public class TelegramMessage implements BaseMessage {

    private String messageCommand;

    private String chatId;

    private String type;

    private Message message;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessageCommand(String messageCommand) {
        this.messageCommand = messageCommand;
    }

    public String getMessageCommand() {
        return messageCommand;
    }

    public String getType() {
        return type;
    }

}

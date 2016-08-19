package kr.yerina.domain.message;

import kr.yerina.inf.BaseMessage;

/**
 * Created by philip on 2016-08-19.
 */
public class TelegramMessage implements BaseMessage {

    private String messageCommand;
    private String type;

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

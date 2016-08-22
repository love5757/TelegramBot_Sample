package kr.yerina.inf;

import org.telegram.telegrambots.api.objects.Message;

/**
 * The interface Base message.
 */
public interface BaseMessage {

	public Message getMessage();

    public void setMessage(Message message);

	public String getMessageCommand();

	public String getType();

}

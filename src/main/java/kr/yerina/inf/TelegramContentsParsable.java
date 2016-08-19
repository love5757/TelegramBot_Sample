package kr.yerina.inf;


import org.telegram.telegrambots.api.objects.Message;

public interface TelegramContentsParsable {

	BaseMessage parseContents(Message message);
}

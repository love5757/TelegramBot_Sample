package kr.yerina.inf;


import org.telegram.telegrambots.api.methods.send.SendMessage;


public interface TelegramContentsMakable {

	SendMessage makeContents(BaseMessage msg);
}

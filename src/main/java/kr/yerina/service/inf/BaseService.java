package kr.yerina.service.inf;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Created by philip on 2016-08-19.
 */

public interface BaseService {

    SendMessage simsimiConversation(Message message, ReplyKeyboardMarkup replyKeyboardMarkup);

    SendMessage sendErrorMessage(Message message, String errorText);

}

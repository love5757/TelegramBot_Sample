package kr.yerina.message.parser;


import kr.yerina.factory.TelegramCommandFactory;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.TelegramContentsMakable;
import kr.yerina.inf.TelegramContentsParsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;


/**
 * The type Mqtt parser.
 */
@Component
public class TelegramParser {

	@Autowired
	private TelegramCommandFactory factory;


	public BaseMessage parseContents(Message message) {

		TelegramContentsParsable parser = (TelegramContentsParsable) factory.getCommandByMessageCommand(message.getText());
		//해당 커맨드를 찾아서 parseContents
		BaseMessage msg = parser.parseContents(message);
		
		return msg;
	
	}

	/**
	 * Make contents mqtt.
	 *
	 * @param msg the msg
	 *
	 * @return the mqtt
	 */
	public SendMessage makeContents(BaseMessage msg){
		
		TelegramContentsMakable parser = (TelegramContentsMakable) factory.getCommandByMessageCommand(msg.getMessageCommand());

		return parser.makeContents(msg);
	
	}
	
}

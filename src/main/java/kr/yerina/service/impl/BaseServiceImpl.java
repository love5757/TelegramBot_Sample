package kr.yerina.service.impl;

import kr.yerina.constant.BotConfig;
import kr.yerina.domain.respones.SimsimiRespones;
import kr.yerina.service.inf.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by philip on 2016-08-19.
 */
@Service
public class BaseServiceImpl implements BaseService{

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    private static final String HELP_TEXT = "Send me the channel username where you added me as admin.";
    private static final String CANCEL_COMMAND = "/stop";
    private static final String AFTER_CHANNEL_TEXT = "A message to provided channel will be sent if the bot was added to it as admin.";
    private static final String WRONG_CHANNEL_TEXT = "Wrong username, please remember to add *@* before the username and send only the username.";
    private static final String CHANNEL_MESSAGE_TEXT = "This message was sent by *@updateschannelbot*. Enjoy!";
    private static final String ERROR_MESSAGE_TEXT = "There was an error sending the message to channel *%s*, the error was: ```%s```";


    public SendMessage simsimiConversation(Message message, ReplyKeyboardMarkup replyKeyboardMarkup){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String baseUrl = "http://sandbox.api.simsimi.com/request.p?key="+ BotConfig.SIMSIMI_API_KEY+"&lc=ko&ft=1.0&text=";
        String encodeText = null;
        try {
            String text = message.getText();
            if(message.getChat().isGroupChat()){
                text.replace("/","");
            }
            logger.debug("채팅방 type[{}]",message.getChat().isGroupChat());
            logger.debug("심심이 보내는 메세지 [{}]",text);

            encodeText = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(baseUrl+ encodeText);

        final SimsimiRespones simsimiResult = restTemplate.getForObject(uri, SimsimiRespones.class);

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());


        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        if(StringUtils.isEmpty(simsimiResult.getResponse())){
            sendMessage.setText(simsimiResult.getMsg());
        }else{
            sendMessage.setText(simsimiResult.getResponse());
        }

        return sendMessage;
    }

    public SendMessage sendErrorMessage(Message message, String errorText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(String.format(ERROR_MESSAGE_TEXT, message.getText().trim(), errorText.replace("\"", "\\\"")));
        sendMessage.enableMarkdown(true);

        return sendMessage;
    }

}

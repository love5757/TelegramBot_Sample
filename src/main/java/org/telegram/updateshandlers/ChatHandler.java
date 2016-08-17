package org.telegram.updateshandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.telegram.commands.HelloCommand;
import org.telegram.commands.HelpCommand;
import org.telegram.commands.StartCommand;
import org.telegram.commands.StopCommand;
import org.telegram.constant.BotConfig;
import org.telegram.domain.respones.SimsimiRespones;
import org.telegram.services.Emoji;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by philip on 2016-08-17.
 */
public class ChatHandler extends TelegramLongPollingCommandBot {

    static final Logger logger = LoggerFactory.getLogger(ChatHandler.class);

    private static final String HELP_TEXT = "Send me the channel username where you added me as admin.";
    private static final String CANCEL_COMMAND = "/stop";
    private static final String AFTER_CHANNEL_TEXT = "A message to provided channel will be sent if the bot was added to it as admin.";
    private static final String WRONG_CHANNEL_TEXT = "Wrong username, please remember to add *@* before the username and send only the username.";
    private static final String CHANNEL_MESSAGE_TEXT = "This message was sent by *@updateschannelbot*. Enjoy!";
    private static final String ERROR_MESSAGE_TEXT = "There was an error sending the message to channel *%s*, the error was: ```%s```";

    private final ConcurrentHashMap<Integer, Integer> userState = new ConcurrentHashMap<>();


    public ChatHandler(){
        register(new HelloCommand());
        register(new StartCommand());
        register(new StopCommand());
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE);
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                logger.error("{}",e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });
    }


    @Override
    public void processNonCommandUpdate(Update update) {
        logger.debug("{}",update);
        if (update.hasMessage()) {
            Message message = update.getMessage();

            try {
                message = update.getMessage();
                if (message != null && message.hasText()) {
                    try {
                        //메세지 핸들링
                        handleIncomingMessage(message);
                    } catch (InvalidObjectException e) {
                        logger.error("{}",e);
                    }
                }
            } catch (Exception e) {
                logger.error("{}",e);
            }
        }
    }

    @Override
    public String getBotUsername() { return BotConfig.CHAT_USER; }

    @Override
    public String getBotToken() {
        return BotConfig.CHAT_TOKEN;
    }


    private void handleIncomingMessage(Message message) throws InvalidObjectException {

        try {
            simsimiConversation(message, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sendErrorMessage(message,e.getMessage());
        }

    }

    private void onWaitingChannelMessage(Message message) throws InvalidObjectException {
        try {
            if (message.getText().equals(CANCEL_COMMAND)) {
                userState.remove(message.getFrom().getId());
                sendHelpMessage(message.getChatId().toString(), message.getMessageId(), null);
            } else {
                logger.info("{}",message.getText());
                if (message.getText().startsWith("@") && !message.getText().trim().contains(" ")) {
                    sendMessage(getMessageToChannelSent(message));
                    sendMessageToChannel(message.getText(), message);
                    userState.remove(message.getFrom().getId());
                } else {
                    sendMessage(getWrongUsernameMessage(message));
                }
            }
        } catch (TelegramApiException e) {
            logger.error("{}",e);
        }
    }

    private void sendMessageToChannel(String username, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(username.trim());

        sendMessage.setText(CHANNEL_MESSAGE_TEXT);
        sendMessage.enableMarkdown(true);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            sendErrorMessage(message, e.getMessage());
        }
    }

    private void sendErrorMessage(Message message, String errorText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(String.format(ERROR_MESSAGE_TEXT, message.getText().trim(), errorText.replace("\"", "\\\"")));
        sendMessage.enableMarkdown(true);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("{}",e);
        }
    }

    private static SendMessage getWrongUsernameMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        sendMessage.setReplyMarkup(forceReplyKeyboard);

        sendMessage.setText(WRONG_CHANNEL_TEXT);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    private static SendMessage getMessageToChannelSent(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(AFTER_CHANNEL_TEXT);
        return sendMessage;
    }

    private void sendHelpMessage(String chatId, Integer messageId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);

        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        sendMessage.setText(HELP_TEXT);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("{}",e);
        }
    }


    private void simsimiConversation( Message message, ReplyKeyboardMarkup replyKeyboardMarkup) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String baseUrl = "http://sandbox.api.simsimi.com/request.p?key="+BotConfig.SIMSIMI_API_KEY+"&lc=ko&ft=1.0&text=";
        final String encodeText = URLEncoder.encode(message.getText(), "UTF-8");
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

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            sendErrorMessage(message,"심심이 대화하다 알수 없는 에러 발생!!!!");
            logger.error("{}",e);
        }
    }

}

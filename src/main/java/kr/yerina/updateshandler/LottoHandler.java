package kr.yerina.updateshandler;

import kr.yerina.constant.BotConfig;
import kr.yerina.constant.Commands;
import kr.yerina.domain.respones.LottoRespones;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by philip on 2016-08-18.
 */
@Component
public class LottoHandler extends TelegramLongPollingBot{

    static final Logger logger = LoggerFactory.getLogger(LottoHandler.class);

    private final ConcurrentHashMap<Integer, Integer> lottoState = new ConcurrentHashMap<>();


    public void init() {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(new LottoHandler());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            handleDirections(update);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() { return BotConfig.CHAT_USER; }

    @Override
    public String getBotToken() { return BotConfig.CHAT_TOKEN;

    }


    private void handleDirections(Update update) throws InvalidObjectException {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {

                if (message.getText().startsWith(Commands.LOTTO_COMMAND)) {
                    onLottoCommand(message);
                } else if (!message.getText().startsWith("/")) {
                    if (message.isReply() && lottoState.get(message.getFrom().getId()).equals(message.getReplyToMessage().getMessageId()) ) {
                        onLottoReceived(message);
                    } else if (!message.isReply()) {
                        if (message.getFrom().getId() == -1) {
                            sendHelpMessage(message);
                        } else {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setText("회차 번호를 입력하세요.");
                            sendMessageRequest.setChatId(message.getChatId().toString());
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException e) {
                                logger.error(e.getMessage());
                            }
                        }
                    }
                }
            }

    }



    private void onLottoCommand(Message message) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId().toString());
        sendMessageRequest.setReplyToMessageId(message.getMessageId());
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        sendMessageRequest.setReplyMarkup(forceReplyKeyboard);
        sendMessageRequest.setText("조회 하고 싶은 회차를 입력하세요.숫자만 입력!!");

        try {
            sendMessageAsync(sendMessageRequest, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> method, JSONObject jsonObject) {
                    Message sentMessage = method.deserializeResponse(jsonObject);
                    if (sentMessage != null) {
                        lottoState.put(message.getFrom().getId(), sentMessage.getMessageId());
                    }
                }
                @Override
                public void onError(BotApiMethod<Message> botApiMethod, JSONObject jsonObject) {
                }

                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                }
            });
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    private void onLottoReceived(Message message)  {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(message.getChatId().toString());
        sendMessageRequest.setText("아직 개발 중임~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //TODO 로또 사이트 접속 불가로 해당 내용 완료 할 수 없음.
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String baseUrl = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";
        String encodeText = null;
        try {
            encodeText = URLEncoder.encode(message.getText(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(baseUrl+ encodeText);

        final LottoRespones result = restTemplate.getForObject(uri, LottoRespones.class);
        logger.debug("[{}]",result);

        try {
            sendMessageAsync(sendMessageRequest, new SentCallback<Message>() {
                @Override
                public void onResult(BotApiMethod<Message> method, JSONObject jsonObject) {
                    Message sentMessage = method.deserializeResponse(jsonObject);
                }

                @Override
                public void onError(BotApiMethod<Message> botApiMethod, JSONObject jsonObject) {
                }

                @Override
                public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
                }
            });

        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }

    }

    private void sendHelpMessage(Message message) throws InvalidObjectException {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setText("help typ.........");
        sendMessageRequest.setChatId(message.getChatId().toString());
        try {
            sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

}

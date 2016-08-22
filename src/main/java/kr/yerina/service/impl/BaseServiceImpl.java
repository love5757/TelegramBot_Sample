package kr.yerina.service.impl;

import jersey.repackaged.com.google.common.base.Joiner;
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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

            encodeText = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(baseUrl+ encodeText);

        final SimsimiRespones simsimiResult = restTemplate.getForObject(uri, SimsimiRespones.class);

        logger.debug("[simsimiResult][{}]",simsimiResult);

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


    public SendMessage lottoInformation(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());

        /*RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String baseUrl = "http://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo=";
        String encodeText = null;
        try {
            encodeText = URLEncoder.encode(message.getText(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(baseUrl+ encodeText);

        final LottoRespones result = restTemplate.getForObject(uri, LottoRespones.class);*/

        //TODO 로또 사이트 접속 불가(11번가 네트워크)로 해당 내용 완료 할 수 없음.

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(message.getFrom().getUserName()).append("님의 ");
        List<Integer> lottoList = IntStream.of(lottoNumGenerator()).boxed().collect(Collectors.toList());
        messageBuilder.append("추천번호는 [ ").append(Joiner.on(" "+'|'+" ").join(lottoList)).append(" ]");
        sendMessage.setText(messageBuilder.toString());

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


    private int[] lottoNumGenerator(){

        //6개의 숫자 변수를 저장할 배열 생성
        int lottoNumbers[] = new int[6];

        //Random을 활용하여 1~45의 난수 생성 & 배열에 저장
        Random random = new Random();

        for(int i =0;i<lottoNumbers.length;i++){
            lottoNumbers[i]=random.nextInt(45)+1;
        }

        //배열 안 난수의 중복 체크 & 값 재설정
        boolean duplication = true;

        for(int i =0;i<lottoNumbers.length;i++){
            for(int j=0;j<lottoNumbers.length;j++){
                if(i!=j&&lottoNumbers[i]==lottoNumbers[j]){
                    duplication=true;
                    lottoNumbers[i]=random.nextInt(45)+1;
                    break;
                }else{
                    duplication=false;
                }
            }
            //중복안되는 수로 변할때까지 무한 재설정되도록 i--
            if(duplication){
                i--;
            }
        }
        return lottoNumbers;
    }


}

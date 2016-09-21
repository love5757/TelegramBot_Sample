package kr.yerina.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philip on 2016-09-07.
 */
@Component
public class KiccRecruitInformation {

    private static final Logger logger = LoggerFactory.getLogger(KiccRecruitInformation.class);


    private static final String URL = "https://www.kicc.co.kr/kr/company/employ/employ/status.jsp";
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String ACCEPT_ENCODING = "gzip, deflate, br";


    @PostConstruct
    public void init(){
        logger.debug("PostConstruct");
        Runnable task   = () ->  System.out.println("TestTestTestTestTest");
        new Thread(task).start();



        try {
            sendPost();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    // HTTP POST request
    private void sendPost() throws Exception {

        RestTemplate restTemplate=new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        final String url= URL;
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("Accept", ACCEPT);
        requestHeaders.add("Content-type", CONTENT_TYPE);
        requestHeaders.add("Accept-Encoding", ACCEPT_ENCODING);

        Map<String, Object> params = new HashMap<>();
        params.put("notice_id", "");
        params.put("re_name", "");
        params.put("password", "");
        params.put("hp_no1", "");
        params.put("hp_no2", "");
        params.put("hp_no3", "");
        params.put("s_menu", "");
        params.put("s_menu2", "");


        String body = null;

        body = objectMapper.writeValueAsString(params);

        HttpEntity entity = new HttpEntity(body, requestHeaders);

        final ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(
                URL,
                entity,
                String.class
        );


        logger.debug("{}",stringResponseEntity.getBody());
        logger.debug("[status code][{}]",stringResponseEntity.getStatusCode());
        logger.debug("{}",stringResponseEntity.getHeaders());

        logger.debug("[requestHeaders][{}]", requestHeaders);
    }







}

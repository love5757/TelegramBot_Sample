package kr.yerina.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by philip on 2016-09-07.
 */
@Component
public class KiccRecruitInformation {

    private static final Logger logger = LoggerFactory.getLogger(KiccRecruitInformation.class);

    @PostConstruct
    public void init(){
        logger.debug("PostConstruct");


        Runnable task   = () ->  System.out.println("TestTestTestTestTest");

        new Thread(task).start();

    }





}

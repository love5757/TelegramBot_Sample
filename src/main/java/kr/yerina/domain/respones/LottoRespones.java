package kr.yerina.domain.respones;

import lombok.Data;

import java.util.Date;

/**
 * Created by philip on 2016-08-18.
 */
@Data
public class LottoRespones {

    private Integer bnusNo;
    private Integer firstWinamnt;       //1등 당첨 금액
    private Integer totSellamnt;        //총 판매 금액
    private String returnValue;         //ex. success
    private Integer drwtNo1;
    private Integer drwtNo2;
    private Integer drwtNo3;
    private Integer drwtNo4;
    private Integer drwtNo5;
    private Integer drwtNo6;
    private Date drwNoDate;             //날짜
    private Integer drwNo;              //회차
    private Integer firstPrzwnerCo;     //1등 당첨자 수



}

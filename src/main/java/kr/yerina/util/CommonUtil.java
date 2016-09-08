package kr.yerina.util;

import kr.yerina.constant.BotConfig;
import kr.yerina.domain.Param;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.api.objects.Message;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * The type Common util.
 */
public class CommonUtil {

	public final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * The constant TX_FORMAT.
	 */
	public static String TX_FORMAT = "yyyyMMddHHmmssSSS";

	/**
	 * 현재날짜 format 으로 구하기
	 *
	 * @param format the format
	 *
	 * @return today
	 */
	public static String getToday(String format) {

		Calendar calendar = Calendar.getInstance();
		String today  = new SimpleDateFormat(format, Locale.KOREA).format(calendar.getTime());

		return today;
	}

	/**
	 * Padding left str string.
	 *
	 * @param paddingChar the padding char
	 * @param strLength   the str length
	 * @param str         the str
	 *
	 * @return the string
	 */
	public static String paddingLeftStr(char paddingChar, int strLength, String str){
		int maxCnt = strLength - str.length();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < maxCnt; i++) {
			sb.append(paddingChar);			
		}
		str= sb.toString()+str;
		
		return str;
	}

	/**
	 * Object 를 Map 으로 반환(Object의 String형 변환)
	 *
	 * @param obj the obj
	 *
	 * @return list
	 *
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException   the illegal access exception
	 */
	public static List<Param> converObjectToList(Object obj)
			throws IllegalArgumentException, IllegalAccessException {

		List<Param> paramList = new ArrayList<Param>();
		Param param = null;
		
		for (Field field : obj.getClass().getDeclaredFields()) {

			field.setAccessible(true);

			if (!StringUtils.isEmpty(field.get(obj)) && (field.get(obj) instanceof String)) {
				
				param = new Param();
				param.setKey(field.getName());
				param.setValue(field.get(obj));
				paramList.add(param);
			}
			
		}

		return paramList;

	}

	public static Date stringToDate(String stringDate){
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return transFormat.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static KeyParameter getMySqlAESPasswdKey(String passwd, int keyLength) {
		byte[] pword = passwd.getBytes();
		byte[] rawKey = new byte[keyLength/8];
		int j = 0;
		for (int i = 0; i < pword.length; i++, j++) {

			if(j==rawKey.length) {
				j = 0;
			}
			rawKey[j] = pword[i];
		}

		return new KeyParameter(rawKey);
	}


	public static Object mysqlAesPasswdEncrypt (byte [] toEncrypt, KeyParameter key) {
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESFastEngine());

			cipher.init(true, key);
			byte[] result = new byte[cipher.getOutputSize(toEncrypt.length)];
			int len = cipher.processBytes(toEncrypt, 0, toEncrypt.length, result, 0);
			cipher.doFinal(result, len);
			return  Hex.encodeHexString(result);
		} catch (InvalidCipherTextException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}


	public static Object mysqlAesPasswdDecrypt (byte[] toDecrypt, KeyParameter key) {
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESFastEngine());

			cipher.init(false, key);
			byte[] result = new byte[cipher.getOutputSize(toDecrypt.length)];
			int len = cipher.processBytes(toDecrypt, 0, toDecrypt.length, result, 0);
			cipher.doFinal(result, len);
			return new String(stripTrailingZeros(result));
		} catch (InvalidCipherTextException e) {
			logger.debug(e.getMessage());
		}
		return null;
	}


	public static byte[] stripTrailingZeros(byte[] data) {
		int lastData = data.length-1;
		for (int i = data.length-1; i >= 0; i--) {
			if(data[i]!=(byte)0) {
				lastData = i;
				break;
			}
		}

		byte[] data2 = new byte[lastData+1];
		System.arraycopy(data, 0, data2, 0, lastData+1);
		return data2;
	}

	/**
	 * 메세지에 아이디가 포함되어 있으면 해당 아이디를 삭제 하고 내용만 Return
	 * @param message
	 * @return
	 */
	public static String removeContainBotIdByCommandMessage(Message message){
		final int selfCheckId = message.getText().indexOf("@" + BotConfig.CHAT_USER);
		String whitoutBotIdMessageResult = message.getText();
		if(selfCheckId != -1){ whitoutBotIdMessageResult = message.getText().split("@")[0]; }
		return whitoutBotIdMessageResult;
	}


	public static void main(String[] args) {
		try {
			//인코딩 확인용
			String word = "무궁화 꽃이 피었습니다.";
			System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
			System.out.println("utf-8 -> ksc5601       : " + new String(word.getBytes("utf-8"), "ksc5601"));
			System.out.println("utf-8 -> x-windows-949 : " + new String(word.getBytes("utf-8"), "x-windows-949"));
			System.out.println("utf-8 -> iso-8859-1    : " + new String(word.getBytes("utf-8"), "iso-8859-1"));
			System.out.println("iso-8859-1 -> euc-kr        : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
			System.out.println("iso-8859-1 -> ksc5601       : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
			System.out.println("iso-8859-1 -> x-windows-949 : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
			System.out.println("iso-8859-1 -> utf-8         : " + new String(word.getBytes("iso-8859-1"), "utf-8"));
			System.out.println("euc-kr -> utf-8         : " + new String(word.getBytes("euc-kr"), "utf-8"));
			System.out.println("euc-kr -> ksc5601       : " + new String(word.getBytes("euc-kr"), "ksc5601"));
			System.out.println("euc-kr -> x-windows-949 : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
			System.out.println("euc-kr -> iso-8859-1    : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));
			System.out.println("ksc5601 -> euc-kr        : " + new String(word.getBytes("ksc5601"), "euc-kr"));
			System.out.println("ksc5601 -> utf-8         : " + new String(word.getBytes("ksc5601"), "utf-8"));
			System.out.println("ksc5601 -> x-windows-949 : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
			System.out.println("ksc5601 -> iso-8859-1    : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));
			System.out.println("x-windows-949 -> euc-kr     : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
			System.out.println("x-windows-949 -> utf-8      : " + new String(word.getBytes("x-windows-949"), "utf-8"));
			System.out.println("x-windows-949 -> ksc5601    : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
			System.out.println("x-windows-949 -> iso-8859-1 : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}

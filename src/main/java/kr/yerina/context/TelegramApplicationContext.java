package kr.yerina.context;

import kr.yerina.inf.BaseMessageResolver;

import java.util.List;




public class TelegramApplicationContext {

	private List<BaseMessageResolver> messageResolverList;


	public void setMessageResolverList(List<BaseMessageResolver> messageResolverList) {
		this.messageResolverList = messageResolverList;
	}

	public List<BaseMessageResolver> getMessageResolverList() {
		return messageResolverList;
	}
		
}

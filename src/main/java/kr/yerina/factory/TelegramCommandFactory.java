package kr.yerina.factory;

import kr.yerina.annotation.MessageMapping;
import kr.yerina.context.RootContext;
import kr.yerina.inf.BaseCommand;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.CommandFactory;
import kr.yerina.util.ProxyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("factory")
public class TelegramCommandFactory implements CommandFactory {

	public static final Logger logger = LoggerFactory.getLogger(TelegramCommandFactory.class);

	public static Map<String, BaseCommand> mappingCmdMap = new ConcurrentHashMap<String, BaseCommand>();

	private synchronized void init(){
		
		if(mappingCmdMap.isEmpty()){
			
			String messageId = null;
			Map<String, Object> cmdMap = RootContext.applicationContext.getBeansWithAnnotation(MessageMapping.class);
			
			for (Object cmd : cmdMap.values()) {
				
				Object obj = null;
				
				try {
					
					obj = ProxyConverter.getTargetObject(cmd);
					
					MessageMapping messageMapping = obj.getClass().getAnnotation(MessageMapping.class);
					messageId =  messageMapping.messageCommand();
					mappingCmdMap.put(messageId, (BaseCommand) cmd);
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}
			
		}
		
	}

	/**
	 * Gets mapping cmd map.
	 *
	 * @return the mapping cmd map
	 */
	private Map<String, BaseCommand> getMappingCmdMap() {
		
		if(mappingCmdMap.isEmpty()){
			init();
		}
	
		return mappingCmdMap;
	}

	/**
	 * Create command base command.
	 *
	 * @param msg the msg
	 *
	 * @return the base command
	 */
	public BaseCommand createCommand(BaseMessage msg) {
		
		BaseCommand cmd = null;
		cmd = getMappingCmdMap().get(msg.getMessageCommand());

		// 커맨드 객체 생성
		cmd = (BaseCommand)RootContext.applicationContext.getAutowireCapableBeanFactory().createBean(cmd.getClass(), AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
		return cmd;
	
	}

	public BaseCommand getCommand(BaseMessage msg){
		return getMappingCmdMap().get(msg.getMessageCommand());
	}

	@Override
	public BaseCommand getCommandByMessageCommand(String messageCommand) {
		return getMappingCmdMap().get(messageCommand);
	}

	
}

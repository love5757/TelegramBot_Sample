package kr.yerina.executor;

import kr.yerina.inf.*;
import kr.yerina.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * The type Queue command executor.
 */
@Component
public class QueueCommandExecutor implements CommandExecutor, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(QueueCommandExecutor.class);

	@Autowired
	private CommandFactory factory;


	private BlockingQueue<BaseMessage> queue;

	private ExecutorService customerTaskPool;

	private List<BaseInterceptor> interceptorList = new ArrayList<BaseInterceptor>();

	public List<BaseInterceptor> getInterceptorList() {
		return interceptorList;
	}

	public void setInterceptorList(List<BaseInterceptor> interceptorList) {
		this.interceptorList = interceptorList;
	}

	public void afterPropertiesSet() throws Exception {

		String queueSize = PropertiesUtil.getProperty("queueSize");
		String poolSize = PropertiesUtil.getProperty("poolSize");
		
		queue = new ArrayBlockingQueue<BaseMessage>(Integer.parseInt(queueSize));
		customerTaskPool = Executors.newFixedThreadPool(Integer.parseInt(poolSize));
		
		for (int i = 0; i < Integer.parseInt(poolSize); i++) {
			customerTaskPool.submit(new MessageConsumer());
		}
		
	}

	public void execute(BaseMessage msg) throws InterruptedException {
		queue.put(msg);
	}

	class MessageConsumer implements Runnable {


		BaseMessage msg;

		BaseCommand cmd;

		public void run() {
			while(true) {
				try {
					msg = queue.take();

					cmd = factory.getCommand(msg);
					boolean resultFlag = true;
					for (BaseInterceptor baseInterceptor : interceptorList) {
						resultFlag = baseInterceptor.preHandle(msg, cmd);
						if(!resultFlag) break; // false 리턴시 다음 interceptor 실행 안함
					}
					// false 리턴시 Command 실행 처리 안함
					if(!resultFlag) continue;
					
					cmd.excuete(msg);
					
					for (BaseInterceptor baseInterceptor : interceptorList) {
						baseInterceptor.postHandle(msg, cmd);
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
 
	}
	
}

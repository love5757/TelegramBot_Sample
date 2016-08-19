package kr.yerina.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.yerina.inf.BaseCommand;
import kr.yerina.inf.BaseMessage;
import kr.yerina.inf.CommandExecutor;
import kr.yerina.inf.CommandFactory;

/**
 * The type Single command executor.
 */
@Component
public class SingleCommandExecutor implements CommandExecutor {

	/**
	 * The Factory.
	 */
	@Autowired
	private CommandFactory factory;

	/**
	 * Execute.
	 *
	 * @param msg the msg
	 *
	 * @throws Exception the exception
	 */
	public void execute(BaseMessage msg) throws Exception {
		BaseCommand cmd = factory.getCommand(msg);
		cmd.excuete(msg);
	}

	
}

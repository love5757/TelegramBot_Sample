package kr.yerina.inf;


/**
 * The interface Command executor.
 */
public interface CommandExecutor {
	/**
	 * Execute.
	 *
	 * @param msg the msg
	 *
	 * @throws Exception the exception
	 */
	public void execute(BaseMessage msg) throws Exception;
}

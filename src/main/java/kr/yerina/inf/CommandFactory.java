package kr.yerina.inf;

/**
 * The interface Command factory.
 */
public interface CommandFactory {
	/**
	 * Create command base command.
	 *
	 * @param msg the msg
	 *
	 * @return the base command
	 */
	BaseCommand createCommand(BaseMessage msg);

	/**
	 * Gets command.
	 *
	 * @param msg the msg
	 *
	 * @return the command
	 */
	BaseCommand getCommand(BaseMessage msg);

	/**
	 * Gets command by message comand.
	 *
	 * @param messageCommand the message command
	 *
	 * @return the command by message command
	 */
	BaseCommand getCommandByMessageCommand(String messageCommand);
}

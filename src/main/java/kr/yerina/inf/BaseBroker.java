package kr.yerina.inf;

/**
 * The interface Base broker.
 */
public interface BaseBroker {

	/**
	 * Receive message.
	 *
	 * @param msg the msg
	 */
	public void receiveMessage(BaseMessage msg);

	/**
	 * Send message.
	 *
	 * @param msg the msg
	 */
	public void sendMessage(BaseMessage msg);
}

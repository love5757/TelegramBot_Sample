package kr.yerina.inf;

/**
 * The interface Base interceptor.
 */
public interface BaseInterceptor {
	/**
	 * Pre handle boolean.
	 *
	 * @param msg the msg
	 * @param cmd the cmd
	 *
	 * @return the boolean
	 *
	 * @throws Exception the exception
	 */
	public boolean preHandle(BaseMessage msg, BaseCommand cmd) throws Exception;

	/**
	 * Post handle.
	 *
	 * @param msg the msg
	 * @param cmd the cmd
	 *
	 * @throws Exception the exception
	 */
	public void postHandle(BaseMessage msg, BaseCommand cmd) throws Exception;
}

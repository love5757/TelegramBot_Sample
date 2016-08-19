package kr.yerina.inf;

/**
 * The interface Base message resolver.
 */
public interface BaseMessageResolver {

	public boolean canResolve(BaseMessage msg);


	public void resolveMessage(BaseMessage msg);
}

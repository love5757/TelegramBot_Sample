package kr.yerina.util;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * The type Proxy converter.
 */
public class ProxyConverter {

	/**
	 * Gets target object.
	 *
	 * @param <T>   the type parameter
	 * @param proxy the proxy
	 *
	 * @return the target object
	 *
	 * @throws Exception the exception
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T getTargetObject(Object proxy) throws Exception {
	  if (AopUtils.isJdkDynamicProxy(proxy)) {
	    return (T) ((Advised)proxy).getTargetSource().getTarget();
	  } else {
	    return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
	  }
	} 
}

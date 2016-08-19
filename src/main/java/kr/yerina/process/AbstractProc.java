package kr.yerina.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The type Abstract proc.
 */
@Component
public abstract class AbstractProc {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

}

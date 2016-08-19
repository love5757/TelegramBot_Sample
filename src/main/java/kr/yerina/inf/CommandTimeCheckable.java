package kr.yerina.inf;

import java.util.Map;

/**
 * The interface Command time checkable.
 */
public interface CommandTimeCheckable {
	/**
	 * Sets start time.
	 *
	 * @param startTime the start time
	 */
	public void setStartTime(long startTime);

	/**
	 * Gets wait command id map.
	 *
	 * @return the wait command id map
	 */
	public Map<String, Integer> getWaitCommandIdMap();
}

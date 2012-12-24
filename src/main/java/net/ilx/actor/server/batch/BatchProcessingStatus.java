package net.ilx.actor.server.batch;

public enum BatchProcessingStatus {
	/**
	 * When we successfully process batch entries we're successfull.
	 */
	SUCCESSFUL,
	/**
	 * When all processing is done and there is no more batch entries to process we are finished.
	 */
	FINISHED,
	FAILED,
	UNKNOWN
	;

}

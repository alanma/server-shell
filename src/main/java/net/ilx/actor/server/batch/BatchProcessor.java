package net.ilx.actor.server.batch;



public interface BatchProcessor<T> {
	/**
	 * Identifier.
	 * @return
	 */
	String getId();

	/**
	 * Current batch processing status.
	 * @return current batch processing status
	 */
	BatchProcessingStatus getStatus();

	/**
	 * Process batches incrementally, one by one, by taking care of batch processing status.
	 */
	BatchProcessingStatus processIncrementally();

	long totalRecords();

	long processedRecords();

}

package net.ilx.actor.server.batch;


public interface BatchProducer<T> {
	String getId();

	Batch<T> getBatch();

	void loadNextBatch();

	long totalRecords();

	void skipUntilBatchThatContainsTx(long transactionNumber);
}

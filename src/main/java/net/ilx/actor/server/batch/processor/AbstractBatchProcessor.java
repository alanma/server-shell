package net.ilx.actor.server.batch.processor;

import java.util.UUID;

import net.ilx.actor.server.batch.Batch;
import net.ilx.actor.server.batch.BatchProcessingStatus;
import net.ilx.actor.server.batch.BatchProcessor;
import net.ilx.actor.server.batch.BatchProducer;

import org.jboss.logging.Logger;

public abstract class AbstractBatchProcessor<T> implements BatchProcessor<T> {
	private UUID uuid = UUID.randomUUID();
	protected final Logger LOG = Logger.getLogger(this.getClass());

	private BatchProcessingStatus currentStatus = BatchProcessingStatus.SUCCESSFUL;
	private BatchProducer<T> batchProducer;

	long processedRecords = 0;

	public AbstractBatchProcessor(final BatchProducer<T> batchProducer) {
		this.batchProducer = batchProducer;
	}

	@Override
	public BatchProcessingStatus getStatus() {
		return currentStatus;
	}

	@Override
	public BatchProcessingStatus processIncrementally() {
		BatchProcessingStatus processingStatus = this.getStatus();
		switch (processingStatus) {
		case SUCCESSFUL: // process next batch
			// continue normally
			this.prepareBatch();
			this.processBatch();
			break;
		case FAILED: // REPEAT processing of the same batch
			this.processBatch();
			break;
		case FINISHED: // nothing to do
			break;
		default:
			// UNKNOWN
			break;
		}
		BatchProcessingStatus status = this.getStatus();

		return status;
	}

	protected void prepareBatch() {
		batchProducer.loadNextBatch();
	}

	protected BatchProcessingStatus processBatch() {
		BatchProcessingStatus batchResult = BatchProcessingStatus.UNKNOWN;

		Batch<T> batch = batchProducer.getBatch();
		if (batch.size() > 0) {
			batchResult = processBatch(batch);
			if (BatchProcessingStatus.SUCCESSFUL == batchResult) {
				processedRecords += batch.size();
			}
		} else {
			// no more records
			batchResult = BatchProcessingStatus.FINISHED;
		}

		currentStatus = batchResult;
		return batchResult;
	}

	protected abstract BatchProcessingStatus processBatch(final Batch<T> batch);

	@Override
	public String getId() {
		return uuid.toString() + " : " + this.batchProducer.getId();
	}

	@Override
	public long totalRecords() {
		return batchProducer.totalRecords();
	}


	@Override
	public long processedRecords() {
		return processedRecords;
	}

}

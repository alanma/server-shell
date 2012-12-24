package net.ilx.actor.server.batch;

import java.util.List;

public interface Batch<T> {
	String getId();

	List<T> getContents();

	/**
	 * Size of the current batch.
	 */
	long size();

	/**
	 * Batch number starts with 1 (0 is initial value, but batch 0 does NOT EXIST).
	 *
	 * @return current batch sequence number
	 */
	long getBatchNumber();

	/**
	 * Maximal batch size.
	 * @return
	 */
	long getMaxBatchSize();
}

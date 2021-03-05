
package io.cuillgln.toys.infrastructure.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BatchQueue<T> {

	private final BlockingQueue<T> queue;
	private final ReentrantLock lock;
	private final Condition full;

	public BatchQueue(int captity) {
		queue = new ArrayBlockingQueue<T>(captity);
		this.lock = new ReentrantLock();
		this.full = this.lock.newCondition();
	}

	public List<T> poll(int millis) throws InterruptedException {
		long nanos = TimeUnit.MILLISECONDS.toNanos(millis);
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			while (queue.remainingCapacity() > 0) {
				if (nanos <= 0L) {
					break;
				}
				nanos = full.awaitNanos(nanos);
			}
		} finally {
			lock.unlock();
		}
		List<T> batch = new ArrayList<T>();
		queue.drainTo(batch);
		return batch;
	}

	public void add(T data) throws InterruptedException {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			if (queue.remainingCapacity() <= 0) {
				full.signal();
			}
		} finally {
			lock.unlock();
		}
		queue.put(data);
	}
}

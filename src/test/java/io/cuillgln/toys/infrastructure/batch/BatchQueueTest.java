
package io.cuillgln.toys.infrastructure.batch;

import java.util.List;

public class BatchQueueTest {

	private BatchQueue<Integer> batchQueue = new BatchQueue<Integer>(4);

	public BatchQueueTest() {
		new Thread(new Runner()).start();
	}

	public void add(int i) {
		try {
			batchQueue.add(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		BatchQueueTest test = new BatchQueueTest();
		for (int i = 0; i < 10000; i++) {
			test.add(i);
			Thread.sleep(900);
		}
	}

	class Runner implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("=================");
					List<Integer> batch = batchQueue.poll(2000);
					System.out.println(batch.toString());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

	// A doesn't happen-before B, so use CountDownLatch
	private static final CountDownLatch startLatch = new CountDownLatch(1);

	private static final int MAX_PRIME_THREAD_COUNT = 10;
	private static final CountDownLatch endLatch = new CountDownLatch(
			MAX_PRIME_THREAD_COUNT);
	private static final BlockingQueue<BigInteger> blockingQueue = new LinkedBlockingQueue<>();
	private static final PriorityBlockingQueue<BigInteger> priorityQueue = new PriorityBlockingQueue<>();

	public static void main(String[] args) {
		// 1. Generate fibonacci numbers - 1 thread
		// 2. Check primeness of a number - 10 threads
		// 3. Output fib numbers that're prime

		ExecutorService threadPool = Executors
				.newFixedThreadPool(MAX_PRIME_THREAD_COUNT + 1);

		Runnable fibRunnable = new FibRunnable(blockingQueue, startLatch);
		threadPool.execute(fibRunnable);

		Runnable[] primeRunnable = new Runnable[MAX_PRIME_THREAD_COUNT];
		for (int i = 0; i < primeRunnable.length; i++) {
			primeRunnable[i] = new PrimeRunnable(blockingQueue, priorityQueue,
					startLatch, endLatch);
			B: threadPool.execute(primeRunnable[i]);
		}

		try {
			endLatch.await();

			System.out.println("Prime numbers for the first "
					+ FibRunnable.FIB_COUNT + " fibonacci numbers");

			while (priorityQueue.size() > 0) {
				System.out.println(priorityQueue.take());
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			Thread.currentThread().interrupt();
		}

		threadPool.shutdown();
	}
}

class FibRunnable implements Runnable {

	final static int FIB_COUNT = 300;

	private final BlockingQueue<BigInteger> queue;
	private final CountDownLatch latch;

	public FibRunnable(BlockingQueue<BigInteger> queue, CountDownLatch latch) {
		this.queue = queue;
		this.latch = latch;
	}

	@Override
	public void run() {
		//@formatter:off
		A: ;
		//@formatter:on

		BigInteger previous = new BigInteger("0");
		BigInteger current = new BigInteger("1");

		queue.offer(previous);
		latch.countDown();

		for (int i = 0; i < FIB_COUNT; i++) {
			queue.offer(previous.add(current));
			BigInteger temp = current;
			current = previous.add(current);
			previous = temp;
		}

	}
}

class PrimeRunnable implements Runnable {

	private final BlockingQueue<BigInteger> queue;
	private final CountDownLatch latch;
	private final PriorityBlockingQueue<BigInteger> priorityqueue;
	private final CountDownLatch endlatch;

	public PrimeRunnable(BlockingQueue<BigInteger> queue,
			PriorityBlockingQueue<BigInteger> priorityqueue,
			CountDownLatch latch, CountDownLatch endlatch) {
		this.queue = queue;
		this.priorityqueue = priorityqueue;
		this.latch = latch;
		this.endlatch = endlatch;
	}

	@Override
	public void run() {
		try {
			latch.await();
			while (queue.size() > 0) {
				BigInteger number = queue.take();
				if (number.isProbablePrime(100)) {
					priorityqueue.add(number);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			endlatch.countDown();
		}
	}

}
package networking;

import java.util.concurrent.Semaphore;

public class Lock {
	Semaphore sm;
	int limit = 1;
	public Lock(int a) {
		sm = new Semaphore(a);
	}
	public void release() {
		if (!(sm.availablePermits() > limit)) {
			sm.release();
		}
	}
	public void check() throws InterruptedException {
		sm.acquire();
		release();
	}
	public void drop() {
		sm.drainPermits();
	}
}
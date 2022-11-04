package earstone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountingTest {

    @DisplayName("두개의 쓰레드에서 동시에 증감해서 예상했던 값이 아니다.")
    @Test
    void testCounting() throws InterruptedException {
        final Counter counter = new Counter();

        class CountingThread extends Thread {
            public void run() {
                for (int i = 0; i < 10000; ++i) counter.inc();
            }
        }

        CountingThread t1 = new CountingThread();
        CountingThread t2 = new CountingThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter.getCount());

        assertThat(counter.getCount()).isNotEqualTo(20000);
    }

    public static class Counter {
        private int count = 0;
        public void inc() {
            ++count;
        }

        public int getCount() {
            return count;
        }
    }

    @DisplayName("두개의 쓰레드에서 동시에 증감해도 동기화 되어 예상했던 값이 나온다.")
    @Test
    void testSyncCounting() throws InterruptedException {
        final SyncCounter counter = new SyncCounter();

        class CountingThread extends Thread {
            public void run() {
                for (int i = 0; i < 10000; ++i) counter.inc();
            }
        }

        CountingThread t1 = new CountingThread();
        CountingThread t2 = new CountingThread();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter.getCount());

        assertThat(counter.getCount()).isEqualTo(20000);
    }

    public static class SyncCounter {
        private int count = 0;
        public synchronized void inc() {
            ++count;
        }

        public int getCount() {
            return count;
        }
    }

}
/**
 * 
 */
package org.duffqiu.patterndeomtest;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.duffqiu.patterndemo.LazySingleton;
import org.junit.Test;

/**
 * @author macbook
 * 
 *         2014年2月4日
 */
public class LazySingletonTest {

    @Test
    public void testEquals() {

	LazySingleton a = LazySingleton.getInstance();
	LazySingleton b = LazySingleton.getInstance();

	assertEquals(a, b);

    }

    @Test
    public void testMultithread() throws InterruptedException {

	int threadNum = Runtime.getRuntime().availableProcessors() + 1;

	final List<LazySingleton> litSingleton = new Vector<>();

	ExecutorService manager = Executors.newScheduledThreadPool(threadNum);

	Collection<Callable<Object>> tasks = new Vector<Callable<Object>>();

	for (int i = 0; i < threadNum; i++) {
	    tasks.add(new Callable<Object>() {

		@Override
		public Object call() throws Exception {
		    LazySingleton a = LazySingleton.getInstance();
		    litSingleton.add(a);
		    return null;
		}

	    });
	}

	manager.invokeAll(tasks);

	manager.awaitTermination(2, TimeUnit.SECONDS);

	LazySingleton b = litSingleton.get(0);

	for (Object o : litSingleton.toArray()) {
	    assertEquals(b, o);
	}

    }
}

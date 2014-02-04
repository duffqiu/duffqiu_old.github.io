/**
 * 
 */
package org.duffqiu.patterndemo;

/**
 * @author macbook
 * 
 *         2014年2月4日
 */
public class LazySingleton {

    private LazySingleton() {

    }

    private static class LazyHolder {
	private static final LazySingleton instance = new LazySingleton();
    }

    public static LazySingleton getInstance() {
	return LazyHolder.instance;
    }
}

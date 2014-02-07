/**
 * The file show the lazy holder
 */
package org.duffqiu.patterndemo;

/**
 * @author macbook
 * 
 *         2014年2月4日
 */
public final class LazySingleton {

    private LazySingleton() {

    }

    private static class LazyHolder {
	private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static LazySingleton getInstance() {
	return LazyHolder.INSTANCE;
    }
}

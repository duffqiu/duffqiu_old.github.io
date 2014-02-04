/**
 * 
 */
package org.duffqiu.patterndeomtest;

import static org.junit.Assert.assertEquals;

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

}

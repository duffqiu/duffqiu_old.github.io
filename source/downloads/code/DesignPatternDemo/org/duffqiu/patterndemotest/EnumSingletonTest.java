/**
 * 
 */
package org.duffqiu.patterndeomtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.duffqiu.patterndemo.EnumSingleton;
import org.junit.Test;

/**
 * @author macbook
 * 
 *         2014年2月5日
 */
public class EnumSingletonTest {

    @Test
    public void test() {
	EnumSingleton a = EnumSingleton.INSTANCE;

	byte[] bytes = SerializationUtils.serialize(a);
	EnumSingleton b = SerializationUtils.deserialize(bytes);

	assertThat(a).isSameAs(b);
    }

}

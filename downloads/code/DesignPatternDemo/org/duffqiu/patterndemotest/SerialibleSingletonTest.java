/**
 * 
 */
package org.duffqiu.patterndeomtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.SerializationUtils;
import org.duffqiu.patterndemo.SerialibleSingleton;
import org.junit.Test;

/**
 * @author macbook
 * 
 *         2014年2月5日
 */
public class SerialibleSingletonTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {

	SerialibleSingleton a = SerialibleSingleton.getInstance();

	//serialization
	byte[] bytes = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(baos);
	oos.writeObject(a);
	bytes = baos.toByteArray();

	//de-serialization

	InputStream is = new ByteArrayInputStream(bytes);
	ObjectInputStream ois = new ObjectInputStream(is);
	SerialibleSingleton b = (SerialibleSingleton) ois.readObject();

	assertThat(a).isSameAs(b);

    }

    @Test
    public void testWithCommLang() {
	SerialibleSingleton a = SerialibleSingleton.getInstance();

	byte[] bytes = SerializationUtils.serialize(a);
	SerialibleSingleton b = SerializationUtils.deserialize(bytes);

	assertThat(a).isSameAs(b);

    }
}

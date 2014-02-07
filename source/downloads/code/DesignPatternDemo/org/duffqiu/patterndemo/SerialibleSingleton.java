/**
 * 
 */
package org.duffqiu.patterndemo;

import java.io.Serializable;

/**
 * @author macbook
 * 
 *         2014年2月5日
 */
public final class SerialibleSingleton implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7646684293730736310L;

    private static final int DEFAULT_AGO = 36;

    private String name = "duff qiu";
    private int age = DEFAULT_AGO;

    private SerialibleSingleton() {

    }

    private static class LazyHolder {
	private static final SerialibleSingleton INSTANCE = new SerialibleSingleton();
    }

    public static SerialibleSingleton getInstance() {
	return LazyHolder.INSTANCE;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + age;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SerialibleSingleton other = (SerialibleSingleton) obj;
	if (age != other.age)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    private Object readResolve() {
	//make sure the instance is the same as the local system after de-serialization 
	return LazyHolder.INSTANCE;
    }
}

/**
 * test git polling
 */
package org.duffqiu.patterndemo;

/**
 * @author macbook
 * 
 *         2014年2月5日
 */
public enum EnumSingleton {
    INSTANCE;

    private static final int DEFAULT_AGO = 36;

    private String name = "duff qiu";
    private int age = DEFAULT_AGO;

    /**
     * @return the name
     */
    public synchronized String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public synchronized void setName(String name) {
	this.name = name;
    }

    /**
     * @return the age
     */
    public synchronized int getAge() {
	return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public synchronized void setAge(int age) {
	this.age = age;
    }

}

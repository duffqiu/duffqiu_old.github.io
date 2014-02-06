/**
 * 
 */
package org.duffqiu.patterndemo;

/**
 * @author macbook
 * 
 *         2014年2月5日
 */
public enum EnumSingleton {
    INSTANCE;

    private String name = "duff qiu";
    private int age = 36;

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
	return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
	this.age = age;
    }

}

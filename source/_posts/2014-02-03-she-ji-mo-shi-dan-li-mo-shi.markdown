---
layout: post
title: "设计模式: 单例模式"
date: 2014-02-03 17:47:12 +0800
comments: true
categories: [Java, Design Pattern]
---

###单例模式原理

1. 模式名称：单例模式(Singleton Pattern)
2. 问题：需要确保系统中的某个类只能有一个实例存在。现实中如序号ID分配器，确保ID在系统中必须是唯一的。(当然也有很多方式生成唯一ID，这里只是个例子)
3. 解决方案：限制类被new出来，只能通过唯一的访问点（static方法）获得一个全局的自身的实例。这个访问点确保实例不会被多次创建
4. 结论：通过唯一访问点有效的控制实例的生成

###简单的Singleton

    {% codeblock lang:java %}
    
    public class Singleton {
    	private static Singleton instance = new Singleton();
    	//public staic Singleton INSTANCE = new Singleton(); //not suggest
    	
    
        //must make the default create menthod private
        private Singleton() {
        }
    
        public staic Singleton getInstance() {
        }
    
    //....
    
    }

    {% endcodeblock %}
    
这里有几点注意：

1. 默认的构造函数要设置为private，避免类被错误的new出来
2. 尽量用方法的方式获取类的实例    
3. 如果构造过程表负责，则需要用static\{...\}方式阔起来
4. 成员变量和单例控制函数都应该是static

###延迟初始化

根据[Effective Java]()中的第55条，除非真的有必要，不然不用延迟初始化  
延迟初始化一定要考虑多线程安全的问题

使用**Initialization on demand holder**方式来控制延迟初始化

在单例中，变量是static的，使用Lazy Holder的方式根据需要创建这个静态变量，同时由JVM来保证线程并发问题。
    
{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemo/LazySingleton.java %}

小提示：对于非单例的类的延迟初始化要用double check的方式，具体可以参见Effective Java 第71条

提供两个JUnit test，一个是简单比对两次生成的singleton是否相同，一个是多线程（线程数比CPU大就可）的方式同时去获取singleton，然后比对第一个和其它的是否相同

{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemotest/LazySingletonTest.java %}

###单例的序列化
如果单例实现了Serializable接口，则需要注意，默认情况下，反序列化都会生成一个新的对象。根据Effective Java 第74条，谨慎实现序列化接口，除非真的有必要。因为一旦发布这个类出去，再改动这个类时使用端需要重新编译的，从而降低了类的灵活性

解决办法，利用反序列化完成前的readResolve函数重新返回本地的单例，确保系统的单例唯一性  
代码演示如下。如果将readResolve方法注释掉，则单元测试将报错

{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemo/SerialibleSingleton.java %}

测试代码如下，一个自己实现的序列化和反序列化，一个是用apache common lang实现的。

{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemotest/SerialibleSingletonTest.java %}

###Enum实现singleton
这个参见Effective Java 第77条，好处是不用特别处理readResolve，也不用实现Serializable接口。缺点是无法延迟初始化，以及看起来不是太好看

代码如下：

{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemo/EnumSingleton.java %}

测试代码如下：

{% include_code lang:java DesignPatternDemo/org/duffqiu/patterndemotest/EnumSingletonTest.java %}

###小插曲
测试的判断，我这里用的是assertj的库，比较简单好用，参见[assertj](http://joel-costigliola.github.io/assertj/index.html)

从上面的测试序列化和反序列化的测试例子看出来，序列化和反序列化是挺麻烦的事情，还好有开源的工具帮组我们做到简单化。可以使用[apache common lang](http://commons.apache.org/proper/commons-lang/)的SerializationUtils。
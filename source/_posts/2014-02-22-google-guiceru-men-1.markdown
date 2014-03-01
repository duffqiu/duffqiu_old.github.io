---
layout: post
title: "Google Guice入门1"
date: 2014-02-22 20:26:34 +0800
comments: true
categories: [Java, Guice]
---

###原由
---
在一个系统中，多个模块总是通过各种关系关联在一起的。但是一般的编程方式总是讲如何组装各种模块与处理业务逻辑混合在一块，造成代码的维护比较困看，也容易造成代码的行数很大。


###解决办法
---

在前面我们已经介绍过[IoC]({%post_url 2014-02-19-she-ji-mo-shi-:ioche-yi-lai-zhu-ru %})。可以通过自己创建单独的模块来维护类间的依赖。但是现在已经有成熟的框架帮助我们完成这个。主要有Spring和[Google Guice](https://code.google.com/p/google-guice/)。这里主要讲一下如何使用Google Guice，这是一个基本的入门使用，后面如果用到高级的地方再讲。

###Guice的使用

####简介

Google Guice是通过代码的方式来简单维护类间的依赖关系，不象Spring通过XML或annotation然后通过autoscan的方式来维护依赖。个人感觉好的地方是，对于程序员而言，代码方式更加贴近，也更容易理解。

####使用方式(基于maven)
- 在maven项目中增加Guice的jar包。注意，这里我们没有用AOP，所以特地讲AOP去掉，从而减少了对AOP的依赖

``` sh
    <dependency>
    	<groupId>com.google.inject</groupId>
    	<artifactId>guice</artifactId>
    	<version>3.0</version>
    	<classifier>no_aop</classifier>
    </dependency>
```

- 在依赖的顶层被使用的类上指明要注入的类

  注入方式有：  
  
  - 属性注入：在对应的属性上使用`@inject`的annotation，前提是要有默认的public的无參构造函数，建议这种方式
  
  - 构造函数注入：在对应的构造函数上使用`@inject`的annotation，前提是该构造函数带有需要被注入的参数。

``` java
public class SendMessageImpl implements ISendMessage {

    @Inject
    private SmppSendMsg smpp;

    @Inject
    private IDescription desc;
    ...
}    
```
    
- 生成绑定关系的类，该类必须是继承于AbstractModule，然后在重载`configure`方法，在该方法中指定绑定关系。Best Practice：该类的命名后缀建议为BindingModule。如果注入的为具体的类，则无需在`configure`方法中指定绑定关系，如果为接口，则需要绑定到具体的类如：`bind(<Interface>.class).to(<ConcreteClass>.class)`。注意就算`configure`方法为空，都要生成一个Module类。Best Practise：尽量使用接口而不是具体的类，以便于以后更改类的实现而不用更改业务类的代码，因为注入都是在这个Module类完成。

- 如果注入的类必须使用带参数的构造函数，则必须手工完成类的实例的生成。但是都必须在module类中指定。

  主要有两种方式手工完成类的实例的生成：
  
  - 使用`@provider` annotation指定类的生成方法，该方法必须是返回一个该类的实例，但是这种方式无法让Guice自动完成Singleton。
  
  - 给需要手工生成实例的类创建一个provider类，该类实现 `Provider<T>`接口，然后在`configure`方法中使用`bind(xxx.class).toProvider(yyy.class)`指定手工生成实例的类。如果需要Singleton则在后面增加指定如`bind(xxx.class).toProvider(yyy.class).in(
	        Singleton.class)`。Best Practice：该类的命名后缀建议为Provider。

- 客户端的使用方式 

   使用生成的BindingModule获取一个Inject实例，然后使用该实例创建需要的对象。如： 

``` java
Injector injector = Guice.createInjector(new SendMessageBindingModule());
ISendMessage sender = injector.getInstance(SendMessageImpl.class);
sender.xxx();
...
```
 
###小插曲
这是在写Decorator设计模式的时候分心出来写的，代码可以参考[DesignPatternDemo的Decorator部分](https://github.com/duffqiu/DesignPatternDemo)



---
layout: post
title: "Google Guice入门2"
date: 2014-02-23 14:26:45 +0800
comments: true
categories: [Java, Guice]
---

###原由
---
在使用Guice做依赖注入的时候，很可能会发生在一个依赖模块（Module）中有多个同类型的接口需要注入，但是又需要根据不同的使用类注入不同的具体对象。如果只是简单的申明为接口注入对象，则会使用同一个对象。如：`bind(IDescription.class).to(ConnectionDescription.class);`


###解决办法
---
使用`@Named("<xxxx>")`来命名需要注入的接口，然后在注入的时候指定名称来特定注入，如： 

顶层使用类中：

``` java
@Inject
@Named("SendMsgWithAuditDesc")
private IDescription desc;
```


Module configure方法中

``` java

//为什么的注入
bind(IDescription.class).to(ConnectionDescription.class);

//指定名称的注入
bind(IDescription.class).annotatedWith(
	        Names.named("SendMsgWithAuditDesc")).toProvider(
	        SendMessageWithAuditDescriptionProvider.class);
```
 
这里用了prvoider是因为需要使用参数的构造函数。Provider类如下：

``` java
public class SendMessageWithAuditDescriptionProvider implements
        Provider<ConnectionDescription> {

    @Override
    public final ConnectionDescription get() {

	return new ConnectionDescription("Audit with SMPP",
	        "Send Msg with audit via SMPP");
    }

}
```

代码可以参考[DesignPatternDemo的Decorator部分](https://github.com/duffqiu/DesignPatternDemo)

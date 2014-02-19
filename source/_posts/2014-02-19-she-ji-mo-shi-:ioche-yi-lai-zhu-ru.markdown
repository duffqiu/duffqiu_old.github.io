---
layout: post
title: "设计模式：IoC和依赖注入"
date: 2014-02-19 20:40:33 +0800
comments: true
categories: [Java, Design Pattern]
---

###原由
在生成对象的时候，我们通常会用到工厂方法模式，但是这会造成factory类过多不好维护的问题，也不好测试的问题。


###解决办法
为了减少factory类，现在流行的做法是使用IoC(控制反转)。这个严格上不是个模式。IoC的实现方式通常有两种：

1. Service Locator，服务定位器
2. Dependency Injection，依赖注入

因为服务定位器的做法侵入代码比较严重，同时比较难测试，一般很少用。

依赖注入比较流行，主要是因为开源框架的支持。有两大框架，一个是Spring，一个是[Google Guice](https://code.google.com/p/google-guice/)。因为Spring还有别的功能（如template的方式我比较喜欢，很多东西都简化很多），不能整体对比，只能对比DI的功能。网上有不少的对比，但是对与一般的开发而言没有太大的参考价值，主要习惯性问题。我个人觉得需要考虑只是整体上除了DI还有没有别的需要，如果有就用Spring，没有就用Guice，毕竟Guice小而且依赖少（就两个Jar包），据说Guice速度块。（但是由速度决定开发包的选择的需求基本很少）。后面抽时间我打算好好看一遍Guice真么使用。

注入的方式分为一下几种：
1. Setter
2. Constructor
3. Annotation
4. Interface
5. Parameter
6. Propagating (传播)
7. 其它

对于注入个人觉得需要仔细分辨具体是否需要，不要什么都通过注入完成。一些简单的对象如log对象，完全没有必要注入。

###小插曲
今天和同事谈到这个注入框架的问题，顺便提到了[google guava开发库](https://code.google.com/p/guava-libraries/wiki/GuavaExplained)，这个值得研究研究细节。尽然Google都在用，那么日常中需要封装的一些工具、通用结构等都应该涵盖了。这个开发的年代，自己再创造工具已经没有太大必要了。





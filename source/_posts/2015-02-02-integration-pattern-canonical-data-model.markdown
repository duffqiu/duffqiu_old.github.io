---
layout: post
title: "系统集成模式：规范化数据流模式"
date: 2015-02-02 18:18:16 +0800
comments: true
categories: 
---

###原由
---
[Normalizer模式]({% post_url 2015-02-02-integration-pattern-normalizer %})中，我们提到到如果多个系统一起集成将造成Endpoint成几何级数的增长。


###解决办法
---

使用Canonical Data Model(规范化数据模型)来解决这个问题
其特点如下：
- 每一个EndPoint一端和集成系统衔接，接收或发送特定系统的消息
- 每一个EndPoint的另外一段则是接收或发送Common的消息给另外的EndPoint

如图1：
![图一](/images/cdm-1.png)

- 所有的在EndPoint间流动的消息都是基于Common的接口，这样当引入一个新的系统的时候这需要实现一个EndPoint就可以了。

如图二：
![图二](/images/cdm-2.png)

当我们需要考入如何实现Endpoint的时候，则可以考虑[Apache Camel Framework](http://camel.apache.org/)


---
layout: post
title: "设计模式：原型模式"
date: 2014-02-17 23:23:31 +0800
comments: true
categories: [Java, Design Pattern]
---

###原型模式原理
---
1. 模式名称：原型模式 (Prototype Pattern)
2. 问题：在创建大的聚合对象的时候，如果从每个层次的自对象开始创建，则比较麻烦，同时带来中间过程的错误风险。而往往需要的新的对象只是某个现有对象的基础上改变一小部分的内容
3. 解决方案：利用Java对象的clone的方式，根据实际情况对已有对象进行软复制或者硬复制（通过序列化的方式）
4. 结论：以一个已有的对象做原型，通过最小的操作得到需要的新的对象


###UML图例
---

{% plantuml %}

title Prototype Pattern Design

interface Cloneable{
}

class Object{
+ Object clone()
}

class Product{
- float price
- String name

+ Object clone()
+ Product cloneProduct(String newName, float newPrice)
}

class Client

Product..|>Cloneable
Product--|>Object
Client -->Product

note right of Product : clone current product\n and reset name and price 

{% endplantuml %}

###模式讲解

1. Java的最顶的类Object本身就提供clone方法，但是是protected的。
2. 如果子类需要实现clone方法，还不许继承cloneable接口，然后重载这个方法，根据需要将该方法改为public的。
3. Object的clone方法的实现是采用逐字节的方式从内存中复制数据。如果类中引用了别的对象，则复制的是对象的应用（即对象地址），而不是复制应用对象的内容，所以这种方式是浅复制。
4. 要实现深复制，则可以利用序列化反序列化这个工程等到一个值copy的对象。但是前提是类以及它应用的对象都必须实现serializable接口，可被序列化。
5. Apache的common lang提供了SerializationUtils工具类，改类的clone方法完成通过序列化的clone

###代码以及测试参考
参见我的github代码：[DesignPatternDemo](https://github.com/duffqiu/DesignPatternDemo)，注意改代码是通过gitflow的管理的，请参考develop branch

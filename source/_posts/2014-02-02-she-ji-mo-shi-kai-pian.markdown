---
layout: post
title: "设计模式开篇"
date: 2014-02-02 15:31:04 +0800
comments: true
categories: [Java, Design Pattern]
---

###参考
从这里开始，主要是参考[漫谈设计模式](http://www.amazon.cn/漫谈设计模式-从面向对象开始-刘济华/dp/B006QQZE4Y/ref=sr_1_1?ie=UTF8&qid=1391326331&sr=8-1&keywords=漫谈设计模式)

要去理解设计模式，必须是有以下基础

1. 语言基础(Java, C++, Object-C...)，这里是以Java语言做介绍，如果需要初步学Java则可以看看[Udemy](https://www.udemy.com/java-tutorial/?couponCode=FREE)上面的免费入门视频
2. 懂得OO的设计理念/原则，后面再抽个章节讲讲这个[OO的设计原则]({% post_url 2014-02-02-oode-[?]-xie-she-ji-yuan-ze %})
3. 懂得UML的基础，入门可以先看看这本简单的[系统分析师UML项目实战](http://www.amazon.cn/系统分析师UML项目实战-邱郁惠/dp/B00DSQZ9IQ/ref=sr_1_1?ie=UTF8&qid=1391326582&sr=8-1&keywords=系统分析师UML项目实战)，个人也不太建议UML在工作做过于教条化。之前曾经有1年多都是在画UML图，然后开发依据UML图去做开发，结果效果并不是很好。但是一些基础的比较实际的用法还是值得去做的。要看明白设计模式的类图，请先看看[UML类图]({% post_url 2014-02-02-umllei-tu %})

### 经典的GoF 23个设计模式，这只是个基础
GoF的23个设计模式只是一个经典的总结，但是不代表只有23个，其实在工作中很多问题的共性解决方案都有可能编程设计模式，只要复合以下4个基本的设计模式要素

1. 模式名称（Pattern Name）
2. 问题描述（Problem）
3. 解决方案（Solution）
4. 效果／结果（consequence）

### 模式的套用
不是说能套上设计模式就是好的设计，好的设计需要多个方面的积累，AOP、BDD、TDD。。。  
模式更像是独孤九剑，最后是忘记模式而自然而然的使用模式(好像有些玄乎？)  
设计模式的基础还是OO，只有用好OO才能更好的理解和用好设计模式
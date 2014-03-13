---
layout: post
title: "设计模式：代理模式"
date: 2014-02-23 17:05:45 +0800
comments: true
categories: [Java, Design Pattern]
---

###原由
---

在编程中我们往往会使用到大对象，而对这些大对象的访问总是比较耗时，但是大对象又不是每次都需要使用到。例如从数据库读取出来的对象，或者从远程服务读取的对象。

###解决办法
---
使用一个代理对象，实现原有对象的接口，并由这个代理对象来负责大对象的创建、缓存等优化操作。
最常用的例子是数据的储存，我们希望将一个大对象很快的存到数据库中，但是又不希望等待以及做很多异常的处理，以为对于使用端而言并不关系存储方式和异常处理。所以可以通过Proxy对象来隔离这些特殊的处理，并通过多线程等异步手段提高性能。

###UML图

{% plantuml %}

@startuml

title Proxy Design Pattern

interface Subject  <<Interface>>{

+doSomething()

}

class RealSubject {

+doSomething()

}


class ProxySubject {

-RealSubject realSubject

+doSomething()

}

class Client

Client-->Subject

RealSubject..|>Subject
ProxySubject..|>Subject

ProxySubject*-->RealSubject

note right of ProxySubject
doSomething() \{
  ...
  realSubject.doSomething();
  ...
\}
end note

@enduml

{% endplantuml %}

###Proxy与Decorate的区别

－ Decorate的目的是给已有的类增加业务功能，而Proxy就像名义一样目的是为了某种而外目的给已有的类增加非业务功能

- 从UML图可以看出，Proxy是图钉针对某个某个固定对象的，而Decorate是针对接口的，并且Proxy是负责对应的对象的生命周期的，让Decorate只是使用对应的对象。同时Proxy和对应的对象的数量关系并不确定，可以是1-*，也可以是1-1。而Decorate只能是1-1

- Decorate可以通过递归的方式叠加使用，而Proxy不作此用途

- 当然既然类似，就可能可以混用。没有必要教条的说一定要明确Proxy或Decorate。






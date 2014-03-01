---
layout: post
title: "设计模式：装饰器模式"
date: 2014-02-22 15:09:29 +0800
comments: true
categories: [Java, Design Pattern]
---

###装饰器模式原理
---
1. 模式名称：装饰器模式 (Decorator Pattern)，另外一个别名是包装器模式(Wrapper)
2. 问题：在开发过程，当一个比较大的类实现并测试后，将逐渐被稳定下来。但是这个时候有了新的需求，需要增强原由的功能，一般的做法是直接修改原由的类，但是会影响到原有类的稳定性，所有的测试都要回归，并且新增代码会对已有功能的稳定性带来影响，而且违背OO的开放－封闭原则。另外一个做法是继承原有的类然后扩展功，但是会造成代码重复，而且扩展性不高，同时如果类已经是final的则无法通过这种方式达到目的。
3. 解决方案：提供一个与原来相同接口的类，但是原来的实现累作为新累的一个部分。这样对于客户端而言并没有改变，但是实际行为变成了除原有的功能外，新增了功能。同时可以由多个新增功能的类叠加来处理。简单讲就是`Interface i = new A();`变成了`Interface i = new B(new A)`
4. 结论：无需静态继承，可以灵活扩展。可以作为AOP的简单实现。缺点是产生很多类似的类，容易引起歧异。

###案例说明
---
这里提供一个简单的业务需求来完成这个例子。
需求的简单说明如下 ：系统一个模块给系统的其它部分用以发送用户通知。原本已经实现好了SMPP发送的功能，但是现在为了统计方便，需要在每次发送时记录到内存中/文件中，以便审计，但是这个对于客户端使用必须是透明的


####UML图

{% plantuml %}

title Decorator Design Pattern

class SendMessageWithAuditImpl {
	-ISendMessage sender
	+ int sendMessage()
}

class SendMessageWithLogImpl {
    -ISendMessage sender
	+ int sendMessage()
}

interface ISendMessage {
	+ int sendMessage()
}

class SendMessageImpl {
	+ int sendMessage()
}

ISendMessage <|.. SendMessageWithAuditImpl : <<realize>>
ISendMessage <|.. SendMessageImpl
ISendMessage <|.. SendMessageWithLogImpl

SendMessageWithAuditImpl "1" o--> "  1"  ISendMessage : <<aggregate>>

{% endplantuml %}

###模式要点

一个类A实现接口I，同时拥有一个同样实现接口I的对象。客户端调用的时候，可以实例化多个实现接口I的对象，然后叠加在一起后给客户端使用。客户端只是看到I提供的功能描述，并不知道具体的实现叠加了多少。
这个模式的最大目的就是能够动态的给类增加功能。

使用中可以通过I i = new A(new B(new C))的方式使用，可以通过Guice等通过Inject的方式组装。在[DesignPatternDemo的Decorator部分](https://github.com/duffqiu/DesignPatternDemo)例子中同时提供了这两种方式的代码。
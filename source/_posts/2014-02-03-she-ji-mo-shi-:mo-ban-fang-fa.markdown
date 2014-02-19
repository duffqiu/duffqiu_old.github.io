---
layout: post
title: "设计模式：模版方法"
date: 2014-02-03 14:53:40 +0800
comments: true
categories: [Java, Design Pattern]
---

###模版方法模式原理

1. 名称：模版方法(Template Method)模式
2. 问题：在很多时候多个对象有很多个共性，只是某个／某些行为不同，如果单独实现这些对象，则需要很多的copy - paste
3. 解决方案：将共性的行为抽取到抽象类，将特定的行为由具体的对象来实现／或者这个具体的行为本身就是一个对象(主要由继承方式和回调方式两种实现手段)
4. 结果：减少了相同的代码，增强了维护性

###继承方式实现模版方法模式

<!--![继承方式的模版方法](/images/TemplatePattern1.png)-->

{% plantuml %}
title Template Method (Extends Abstract Class) Design Pattern

abstract class AbstractParentClass <|-- class ChildClass1
abstract class AbstractParentClass <|-- class ChildClass2
abstract class AbstractParentClass <|-- class ChildClass3

abstract class AbstractParentClass {
+ void operation()
+ abstract void abstractOperation()
}

ChildClass1 : + void abstractOperation()
ChildClass2 : + void abstractOperation()
ChildClass3 : + void abstractOperation()
{% endplantuml %}

这里注意点：
1. 父类是抽象类
2. 抽象方法是个个子类需要实现的各自的具体行为
3. 父类只是实现一个算法框架
4. Java为了避免父类的的方法被覆盖／重写，可以将其申明为final或private

###回调函数实现模版方法模式

<!--![回调函数的模版方法](/images/TemplatePattern2.png)-->

{% plantuml %}
title Template method (Callback) design pattern

class MainClass ..|> interface MainInterface
interface MainInterface ..> interface CallbackInterface
class CallbackClass ..|> interface CallbackInterface

MainInterface : + void operation(callback : CallbackInterface)
CallbackInterface : void callbackMethod()

CallbackClass : void callbackMethod()
MainClass : + void operation(callback : CallbackInterface)
{% endplantuml %}

这里注意点：
1. 主类对应的接口依赖与回调的接口定义，不是直接依赖回调对象本身
2. 主类无需被继承，提定行为由实现回调接口的对象实现
3. 最为常用的例子是多线程处理中，回调对象都实现了Runable接口实现特性的逻辑，控制线程的主逻辑类如ExecutorService依赖于Runable接口

个人比较建议使用回调函数实现模版方法模式。（之前用C++的时候计较多的是用继承的方式）

####Vi小技巧
1. yy 用来copy当前行
2. 数字n＋yy  用来copy从单前行开始的n行
3. p 在当前光标出插入copy的内容
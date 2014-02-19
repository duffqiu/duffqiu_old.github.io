---
layout: post
title: "设计模式：工厂方法模式"
date: 2014-02-15 22:15:00 +0800
comments: true
categories: [Design Pattern, Java]
---

###工厂方法模式原理
---
1. 模式名称：工厂方法模式 (Facotory Method)
2. 问题：
   
   - 对象的创建比较复杂，需要关联其它对象／资源（如配置文件，数据库等）
   - 需要写一堆if-else的方式来决定使用那个对象的具体类
   - 希望隐藏创建过程的细节，降低偶和度
   - 优化性能，在创建过程中缓存对象等 
   - 使用者只关心接口，而不关心具体的类，因为用new的方式总是要和具体的类偶和 
    
3. 解决方案：使用一个类来帮助创建需要的对象，而该类有外部传入使用方，使用方只是对创建类的接口依赖（写到这里的时候，发现漫谈设计模式的书的UML的图我的理解不同，应该是client依赖与工厂接口，而不是关联工厂对象）
4. 结论：提高对象的封装，减少了使用方的依赖



###UML图例
---

{% plantuml %}
title Factory method design pattern

'interface definition
interface Factory{
+ Product createProduct()
}
interface Product

'class definition
class ConcreteFactory {
+ Product createProduct()
}
class ConcreteProduct
class Client{
+ void Client(Factory factory)
+ void doSomething()
}

'relationship
Factory<|.. ConcreteFactory
Product<|..ConcreteProduct
ConcreteFactory..>ConcreteProduct
Client..>Factory

{% endplantuml %}


###示例代码
---
这个比较简单就不用代码举例了，看看上面的UML图就能写出代码了。关键是在合适的场景下要想起来用到它

###静态工厂方法
原来就是避免了过多的产生工厂类，而将自身设计为工厂，并提供静态工厂方法来创建对象。劣势是失去类面向对象的特征，无法继承、动态多态等。注意：静态方法是无法被覆盖的。

{% plantuml %}

title Static Factory Method design pattern

interface Product

class ConcreteProduct{
+ {static} Product createProduct()
}

class Client{
+ void doSomething()
}

Product<|..ConcreteProduct
ConcreteProduct..>Product

Client..>ConcreteProduct

{% endplantuml %}

###书籍纠正
1. 在《漫谈设计模式》的P50页的setCurrentConnection方法没有必要提供
2. 在《漫谈设计模式》的P51页的closeConnect方法，没有必要提供函数参数

###小插曲：ThreadLocal的并发性
在工厂方法模式中，可以利用ThreadLocal来做对象的缓冲池。但是里面有个比较可爹的问题，如果在线程中被加入到ThreadLocal中，但是在线程结束前不将这个对象手动的移除出ThreadLocal，则ThreadLocal会被另外一个线程使用到。具体可以看我的Github上的[DesignPatternDemo的代码](https://github.com/duffqiu/DesignPatternDemo)

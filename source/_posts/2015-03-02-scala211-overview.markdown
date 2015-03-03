---
layout: post
title: "scala211特性简介"
date: 2015-03-02 12:02:16 +0800
comments: true
categories: Scala
---

###原由
---
Scala语言本生的变更还是比较快的，现在最通用是2.10和2.11版本
那么是否需要用2.11版本呢？
我们来看一看2.11的一些特性。参考于[scala2.11overview](http://docs.scala-lang.org/scala/2.11/)


###Scala 2.11特性简介
---

- 更小

    - Actor包被废弃，如果的需要直接用Akka的包，已经被包含在了Scala的版准语言包了
    - XML包被移除出scala-library.jar，编程了一个独立的包，如需要可以加入如下依赖 `libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.3"`

- 更快: 提高了性能
- 更强
    
    - 语言：支持case class的参数多于22个参数(参见：[1](https://issues.scala-lang.org/browse/SI-7296))
    - 语言：增强类型推断。(不过好像这个问题又被打开了, 参见[2](https://issues.scala-lang.org/browse/SI-1786))
    - REPL: 这个增强了很多项， 不一一举例了，下面是一些我个人觉得有用的。

```
scala> :settings <+/-> <flag>
//这个主要是针对在REPL中提示有warning的时候，需要使用某些启动参数。原来的方式是需要退出REPL然后再用指定参数启动。现在可以直接通过这个名利来激活(+)/去除(-)某个特性(flag)，如显示deprecation，则用":settings + deprecation"
```

    - REPL: 支持script引擎 (我个人没有用过scala下的script引擎，不知道。后面再写个如何在[代码中动态执行Scala]({% post_url 2015-03-03-scala-script %}))




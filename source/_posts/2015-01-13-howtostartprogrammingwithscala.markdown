---
layout: post
title: "如何开始用scala编程"
date: 2015-01-13 17:19:11 +0800
comments: true
categories: Scala
---

###原由
在经历这么多年的OO的编程概念后，这一两年有兴起了FP(Functional Programming)的编程模式
作为多年的Java程序员如何能更好的接受FP并应用到开发中呢？主要有两个选择

- [Scala](http://scala-lang.org/)
- [Clojure](http://clojure.org/)

他俩最主要的区别是Clojure是比较纯粹的JVM下List语言的变种。没有OO的概念
Scala比较好的融合了OO和FP，比较合适Java平滑过渡到FP的编程范式。从Java8的新特性看
Java和Scala已经越来越接近。但是个人觉得Scala更好看写。或许是因为Java有过多的历史负担吧

如果是想纯粹学一下List，也可以看看[Haskell](https://www.haskell.org)

### Coursera的视频课程
开始一门新的语言，大家总是期望有好的视频来讲解，最出名的是这个了，但是建议大家还是看完入门的书籍对scala有一定的了解后再来看这个
[Functional Programming Principles in Scala](https://www.coursera.org/course/progfun)

### 入门书籍推荐
现在关于Scala的书籍已经很多了，要达到熟悉的程度要看不下10本书。但是对于基础入门，以下这本我建议大家买个纸质版    
![快学Scala](/images/kuaixuescala.jpg)    

这本有些旧了，某些章节可以不用看，具体如下：
- 第19章: 解析，这个太难，也不容易应用，可以忽略
- 第20章: Actor，这个已经被Akka代替，不用再看，后面将写些如何应用Akka
- 第22章: 定界延续，这个也太难，可以暂时忽略

### 一下有趣的例子
最近同学们在谈论list/FP的时候提到的一个例子:
有一个农场，鸡的数量是鸭的4倍，但是鸭比猪少9只，鸭和猪的数量是67只，求所有动物的脚加起来总共有多少只？
这个是个推论性的问题，在scala中用for{}的方式来解答(这里的for不是做循环，scala语言的用法叫for comprehension)

``` java

val d = (1 to 67) toStream

val feet = for {
  d1 <- d 
  c1 = d1 * 4
  p1 == d1 +9 if(d1 + p1 == 67)
} yield {
  (c1+d1)*2 + p1*4
}
println(feet.take(1).force)

```

#### 小提示
Linux下查找文件中的某些字符串
`grep -rnw '<path>' -e "<string value>"`


---
layout: post
title: "Scala中函数式编程还是命令式编程"
date: 2015-02-05 11:44:46 +0800
comments: true
categories: Scala, FP
---

###原由
---
Scala是即支持传统的命令式编程也支持函数式编程(FP)，我们既可以像java那样写代码，也可以像Lisp那样写。
但是那种好呢？


###解决办法
---
个人的理解是，主要取决于对问题的解决方式的思路。
如果已经习惯了命令式编程的思路，那就用命令式编程好了。
不过既然FP已经逐渐成为潮流，那换换思路也未尝不可。
从Scala语言本身看两者没有冲突

###一个例子来对比两种思路
---

例子：有一个Map的数组/列表，要将不同Map中相同key的value值相加，最后得到一个Map

``` java
Vector(Map("test" -> 1, "hello" -> 2, "wwww" -> 9), Map("test" -> 3, "hello" -> 5, "http" -> 33))
```
期望得到的结果

``` java
Map(test -> 4, hello -> 7, wwww -> 9, http -> 33)
```

- 命令式编程方式
  思路：循环Vector中的每个Map，然后循环Map中的每个Key-Value，将相同的Key对应的Value加起来后存入到一个临时的Map，最后返回这个Map

``` java
def merge(input: Vector[Map[String, Int]]) = {
  
  var mergeRes = input.head
  val temp = input.tail
  
  for(itr <- temp) {
  	for((word, count) <- itr) {
  		mergeRes = mergeRes + (word -> (mergeRes.get(word).getOrElse(0) + count))
  	}
  }
  
  mergeRes
}
```

- 函数式编程方式
  思路：对于Vector作折叠操作(reduceLeft)，因为每此计算都要以上一次计算作为输入。而计算中需要做的操作由使用者控制。同时对于每个Map都需要操作每个key-value并返回新的key-value所以用到map函数

``` java
  def mergeFP(input: Vector[Map[String, Int]]) = {
    input.reduceLeft {
      (el, acc) =>
        el.map {
          case (word, count) =>
            acc.get(word).map(accCount => (word -> (accCount + count))).getOrElse(word -> count)
        } ++ (acc -- el.keys)
    }
  }
```

从两者的对比俩看，如果不是熟悉FP的思路的化，似乎FP的代码更难看些。这也是为什么有人认为FP不利于代码沟通的原因吧。
但是从FP的实现看，中间没有任何可变变量（其实Scala对于reduceLeft用了中间可变变量，只是对于函数的使用者不需要了）

**注** 改FP的例子载自Akka in Action，但是它的是foldLeft不是reduceLeft




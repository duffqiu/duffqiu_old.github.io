---
layout: post
title: "初学sbt"
date: 2015-02-10 15:22:32 +0800
comments: true
categories: Scala, SBT
---

###原由
---
初学一种语言最烦的就是搭建开发环境了，每次都要装一堆的plugin，有时候又因为不同的plugin的版本等问题冲突
前面介绍如何使用Maven来搭建开发环境，总体而言还算比较复杂。特别是在不同的机器上竟然有些plugin不可用，也不知道问题在那里。
所以既然是以开发Scala为主，那还是用[SBT](http://scala-sbt.org)吧


###主要参考
---
直接参考[SBT官网的Getting Started](http://www.scala-sbt.org/0.13/tutorial/index.html)并结合自己的一些实践做了下浓缩
官网已经提供中文版本了(开来中国是用Scala增长比较快的地区，另一个是美国硅谷，有兴趣可以看看google trend上关于Scala)

该Getting Started有几章可以暂时不用看的，到真的有需要再看
- Scope
- Multi-project builds
- Custom settings and tasks
- .scala build definition

另外在Github上有个[giter8，简称g8](https://github.com/n8han/giter8)的工具，可以帮助获取scala, sbt的模版，不过自从有了[typesafe的activator](http://typesafe.com/get-started)后，提供g8模版已经不多了，原有的模版也没怎么更新了

#### 安装SBT
可以根据SBT官网的Getting Started，对于不同的平台采用不同的安装方式
安装完后，基本上就是一个sbt的命令脚本和sbt-launch.jar
安装完后，通过命令行运行一次`sbt`，然后sbt会将对应的依赖包下载下来。
sbt会在用户的home目录下创建.sbt和.ivy2的目录，依赖包都放在了ivy2的目录里。因为sbt实际是通过ivy2来管理包依赖。不过大家不用去看ivy2，这些都是sbt自己管理的。
如果是公司有代理的环境中，则需要设置java代理的参数才可以使用，不然sbt总是在尝试下载依赖包。
一般设置java代理的参数是在shell的文件中设置JAVA_OPTS,但是好像不其作用。所以最好的方式是直接修改sbt的命令脚本增加代理配置参数

``` sh
JAVA_OPTS="$JAVA_OPTS -Dhttp.proxyHost=<proxy host> -Dhttp.proxyPort=<proxy port> -Dhttps.proxyHost=<proxy host> -Dhttps.proxyPort=<proxy port>"
```

#### 建立一个scala/java工程
建议不要从参考SBT官网的Getting Started的hello world，因为它将源文件建在了工程的根目录上了
很奇怪，到目前位置都没有个一个像maven那样可以直接在Eclipse上创建一个sbt的工程，而必须先手工创建一些文件

具体步骤为：





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

- 创建一个工程目录如： sbt-sample
- 在工程目录下再创建一个子目录，必须名为project
- 在工程目录下创建名为build.sbt的定义文件，并写入基本的信息，需要注意的是每个配置都要空一行。以后打开别人的工程先看看这个文件的基本定义，特比是scala的版本

``` sh
name := "sbt-sample"

version := "1.0"

scalaVersion := "2.11.4"

organization := "org.duff"
```
- 使用[sbteclpise](https://github.com/typesafehub/sbteclipse)插件， 在project子目录下创建一个plugins.sbt文件，然后加入一下内容，然后在sbt的命令行下执行`eclipse`就可以生成对应的eclipse的工程了。如果改动了sbt的文件则最后重新执行一次`eclipse`获得最新的包依赖等

``` sh
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")
```

- 打开Eclipse，写个测试的App

``` java
package org.duff.sbtsample

object Main extends App{
  
  println("this is a sbt sample")

}

```

- 在sbt命令行下执行`compile`，然后`run`就可以看到对应的输入了

- 设置包依赖仓库（repository）。对于Maven是设置的Maven的conf文件中，但是对于SBT是每次定义在项目的build.sbt文件中
     
   - 方法一： 通过URL定义如： `resolvers += "<rep name>" at "<rep url>"`

``` sh
resolvers += 
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

   - 方法二： 使用[预定义的Rep](http://www.scala-sbt.org/0.13/docs/Resolvers.html)，主要有：
   
       - DefaultMavenRepository at  https://repo1.maven.org/maven2/，这个会被默认使用，无需指定
       - JavaNet1Repository  at http://download.java.net/maven/1/
       - Resolver.sonatypeRepo("public") (or “snapshots”, “releases”) at https://oss.sonatype.org/content/repositories/public
       - Resolver.typesafeRepo("releases") (or “snapshots”) at https://repo.typesafe.com/typesafe/releases
       - Resolver.typesafeIvyRepo("releases") (or “snapshots”) at https://repo.typesafe.com/typesafe/ivy-releases
       - Resolver.sbtPluginRepo("releases") (or “snapshots”) at https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases
       - Resolver.bintrayRepo("owner", "repo") at https://dl.bintray.com/[owner]/[repo]/
       - Resolver.jcenterRepo at https://jcenter.bintray.com/

``` sh
resolvers += JavaNet1Repository
```

   - 方法三：同时指定多个rep，使用`resolvers ++= Seq(<rep1>, <rep2>)`, rep1/rep2可以使用方法一或方法二

``` sh
resolvers ++= Seq(Resolver.sonatypeRepo("public"),
    Resolver.typesafeRepo("releases"))
```

- 指定工程依赖包，和resolver类似，可以一个一个指定(+=)，也可以同时指定(++= Seq())，另外如果需要对多个包使用同一个变量指定如scala版本等，则可以使用如下例子的方式(即Scala的闭包)

```
libraryDependencies ++= {
  val akkaVersion       = "2.3.9"
  val sprayVersion      = "1.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion withSources() withJavadoc(),
    "ch.qos.logback"    %  "logback-classic" % "1.1.2",
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test",
    "org.scalatest"     %% "scalatest"       % "2.2.0"       % "test" withSources() withJavadoc()
  )
}
```
   - 每个lib的定义格式为 `<groupID> % <artifactID> % <revision> % configuration`。如上面的 ch.qos.logback，默认情况下都是"compile"的配置，而对于scalatest是指定在"test"的时候才使用
   - 如果artifactID是根据scala的版本有不同的版本则可以简化的使用%%的方式省去指定xxx_2.11.4 (使用scala 2.11.4)而是直接写xxx
   - 需要同时下载javadoc和source，则可以在最后用withSources() withJavadoc()

- 更改完后，如果还在sbt命令行下，则可以运行`reload`来更新相应的配置

- 打包程序，如果使用的不同的framework/toolkit，则有不同的打包方式，一般的打包方式是打成大的jar包

   - 配置打包plugin，在project子目录中增加一个名为assembly.sbt的文件，增加以下内容

```
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")
```
   - 然后就可以在sbt命令行下执行`assembly`，则可以打包出jar文件了。可以在子目录target/scala-2.11/下找到这个jar
   - 通过执行`java -jar <xxx.jar>`来执行程序
   - 可以通过在build.sbt中增加以下配置更改打包jar

       - `ssemblyJarName in assembly := "<xxxx.jar>"` 更改打包的jar文件名
       - `mainClass in assembly := Some("<com.example.Main>")` 更改main函数的入库
       - 如果像用工程的版本号来合成打包文件，则可以这样写 `assemblyJarName in assembly := "<name>" + version.value +".jar"`


到此一个就本的sbt的scala开发环境就建成了。
后续不同的项目的变化基本上就集中在增加新的依赖包，增加不同的plugin来执行不同的任务
个人使用看来，对比maven相对而言要简单一些。只是SBT使用场景基本只局限于java和scala，同时sbt的写法是scala语法




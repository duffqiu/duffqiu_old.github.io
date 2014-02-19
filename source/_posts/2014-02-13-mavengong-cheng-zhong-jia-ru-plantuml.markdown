---
layout: post
title: "Maven工程中加入PlantUML"
date: 2014-02-13 23:37:47 +0800
comments: true
categories: [Maven, PlantUML]
---

###原由
设计于编码的最好实践是设计文档与代码文档放在最近的地方，便于同时修改和更新。最好都放入到一个代码工程中

###解决办法
PlantUML的Mavin Plugin是最好的解决办法。在Maven工程中建一个目录存放PlantUML的设计文件，txt文件就可以。如：`src/main/plantuml`

1. 然后确保已经安装好了graphvizDot。

2. 然后就可以写plantuml的文本

3. 修改maven的工程的pom.xml文件，增加[PlantUML的Maven Plugin](https://github.com/duffqiu/maven-plantuml-plugin)，注意用的版本，我用时maven plugin最新的版本为7954，plantuml版本是7991，代码如下

``` sh

    <plugin>
      <groupId>com.github.jeluard</groupId>
      <artifactId>plantuml-maven-plugin</artifactId>
      <version>7954</version>
      <configuration>
        <graphvizDot>/opt/local/bin/dot</graphvizDot>
        <sourceFiles>
          <directory>${basedir}</directory>
          <includes>
            <include>src/main/plantuml/*.txt</include>
          </includes>
        </sourceFiles>
      </configuration>
      <dependencies>
        <dependency>
          <groupId>net.sourceforge.plantuml</groupId>
          <artifactId>plantuml</artifactId>
          <version>7991</version>
        </dependency>
      </dependencies>
    </plugin>

```

因为用port安装的graphvizDot目录不在`/usr/bin`下，所以需要特定指定改目录

然后用maven的build的target：`com.github.jeluard:plantuml-maven-plugin:generate`就可以将plantuml下的文件转成UML图形了。图形文件放在`target/plantuml`目录下

如果用Eclipse，则可以安装对应的[eclipse插件，安装地址 ](http://plantuml.sourceforge.net/updatesitejuno/)，这样在写plantuml文本的时候就可以同时看到图形了。



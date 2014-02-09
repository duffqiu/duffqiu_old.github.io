---
layout: post
title: "在Jenkins中使用Findbugs"
date: 2014-02-08 13:02:06 +0800
comments: true
categories: [Jenkins, Findbugs]
---

###原由
看了看Sonar的介绍，的确很专业，但是是否每个功能都用的上？而且Sonar也是集成了PMD, Findbugs, Checkstyle等插件的功能来帮助完成相关的任务的。  
但是Jenkins也同样直接支持这些插件，是否只用Jenkins加上这些插件就可以了？

前面介绍Jenkins的时候已经用了不少插件，这里再加一个Findbugs

###配置

- 在对应的maven项目中加入maven的Findbugs插件，配置示例如下：

``` sh
	<plugin>
	  <groupId>org.codehaus.mojo</groupId>
	  <artifactId>findbugs-maven-plugin</artifactId>
	  <version>2.5.4-SNAPSHOT</version>
	  <configuration>
	    <xmlOutput>true</xmlOutput>
	  </configuration>
	</plugin>
```  
- 在Jenkins中安装对应的Findbugs插件“FindBugs Plug-in”  
- 然后在Jenkins的项目配置中激活Findbugs，也就是打个勾。  
- 给Maven的运行加入新的goald：“findbugs:findbugs”  
- 重新构建项目就可以得到了

####小插曲：
在Eclpse中使用的时候没有发现问题，但是到了Jenkins后，从后台的build输出确发现了一个异常的warning: “Failed to notify spy hudson.maven.Maven3Builder$JenkinsEventSpy: Failed to load edu.umd.cs.findbugs.detect.TestASM”，从而没能在Jenkins上看到Findbugs的报告。后来查了半天，尽然是Jenkins的Bug，临时的解决办法尽然是将Maven换成`3.0.5`的版本，不要使用3.1或3.1.1
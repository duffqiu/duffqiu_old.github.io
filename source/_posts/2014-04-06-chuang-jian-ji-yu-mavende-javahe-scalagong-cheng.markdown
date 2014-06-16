---
layout: post
title: "创建基于maven的java和scala工程"
date: 2014-04-06 09:20:56 +0800
comments: true
categories: [Java, Scala, Maven]
---

###原由
---
最近在看Scala和ScalaTest，但是要搭建一个同时运行Java和Scala的项目工程，还是遇到了些问题。因为我习惯了搭建出来的工程是Maven工程，同时即要能在Eclipse上运行，也能用Maven命令的方式运行以便后续用Jenkins。经过一段摸索终于试验出来。但是很奇怪，搜索出来的结果没有一个直接好使的。


###解决办法
---

####Eclipse配置

- 安装Scala的IDE，参见[Scala-IDE官网](http://scala-ide.org)，注意选择Scala孵化器中的ScalaTest插件
- 安装Maven在Eclipse上的插件，通过`Preference ->Maven->Discovery`安装m2e的对应插件。在后续的POM文件中使用到的插件最好都安装对应的m2e插件。(包括：build-helper，pmd, checkstyle, findbugs, compiler...)
- 创建Maven工程中，添加如下插件


    - build-helper插件，添加scala的source和test source。因为`<sourceDirectory>`只能指定一个目录，所以需要这个插件来帮助添加scala的文件目录

```
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>build-helper-maven-plugin</artifactId>
				<version>1.8</version>
				<executions>
					<execution>
						<id>add-source</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>add-source</goal>
						</goals>
						<configuration>
							<sources>
								<source>src/main/scala</source>
							</sources>
						</configuration>
					</execution>
					<execution>
						<id>add-test-source</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>add-test-source</goal>
						</goals>
						<configuration>
							<sources>
								<source>src/test/scala</source>
							</sources>
						</configuration>
					</execution>
				</executions>
			</plugin>

```
    
   - Scala插件，协助编译scala，同时指定scala版本
   
```
			<plugin>
				<groupId>net.alchim31.maven</groupId>
				<artifactId>scala-maven-plugin</artifactId>
				<version>3.1.6</version>
				<executions>
					<execution>
						<goals>
							<goal>compile</goal>
							<goal>testCompile</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<scalaVersion>2.10.3</scalaVersion>
				</configuration>
			</plugin>
```   

   - ScalaTest插件
   
```
			<plugin>
				<groupId>org.scalatest</groupId>
				<artifactId>scalatest-maven-plugin</artifactId>
				<version>1.0-RC2</version>
				<configuration>
					<reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
					<junitxml>.</junitxml>
					<filereports>WDF TestSuite.txt</filereports>
				</configuration>
				<executions>
					<execution>
						<id>test</id>
						<goals>
							<goal>test</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```   

   - 指定Junit插件，如果需要同时使用JUnit测试java代码。注意，新版本的插件已经可以支持多线程并发，大大提高测试的速度。强烈建议使用最新的版本

```
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>2.17</version>
				<configuration>
					<parallel>methods</parallel>
					<threadCount>10</threadCount>
				</configuration>
			</plugin>
```

   - 安装m2e的scala maven plugin的connector插件，不然在eclipse上直接读取pom文件会解释出错。 [update地址](http://alchim31.free.fr/m2e-scala/update-site/ )

###小插曲
不知道什么原因，ScalaTest的官网被屏蔽了。一个纯开源的技术网站有这个必要性么？还是躺枪了？




---
layout: post
title: "用Jenkins搭建基于Github和JUnit的自动化测试"
date: 2014-02-06 20:23:53 +0800
comments: true
categories: [Auto Test, Jenkins, JUnit]
---

###原由 
在用Github管理代码，同时用JUnit做单元测试，则是否有一种方式／工具帮助我们自动从Github提取最新的代码然后运行JUnit测试并报告结果呢？  
答案是：[Jenkins](http://jenkins-ci.org/)

###安装及启动配置
安装配置都是在Mac OSX下

1. 下载OSX的安装包，直接安装就可以了。安装完后，程序被放在了/Applications/Jenkins/jenkins.war
2. 启动是通过系统服务的方式启动的，启动配置文件在/Library/LaunchDaemons/org.jenkins-ci.plist，可以通过launchctl命令的方式启动／卸载服务，launchctl使用可以参见[1]({% post_url 2014-01-30-mac-tips-guan-li-hou-tai-zi-dong-yun-xing-de-fu-wu %})
3. 如果需要卸载这个Jenkins，可以运行
       
        /Library/Application Support/Jenkins/Uninstall.command
4. 安装完成后，Jenkins会自动启动，打开本地8080端口，可以通过更改/Library/Application Support/Jenkins/jenkins-runner.sh启动脚本增加启动参数的方式更改，具体参数可以参考[2](https://wiki.jenkins-ci.org/display/JENKINS/Starting+and+Accessing+Jenkins)。但是在Mac上可以用defaults工具来更改端口。具体命令如下
       
        >sudo defaults write  /Library/Preferences/org.jenkins-ci httpPort 9080       
   
   然后用launchctl命令重新启动Jenkins就可以了。Java的配置参数也可以通过这个方式更改
   
   小提示，可以用defaults命令查看已经配置的参数
       
       defaults read <file>
       
5. 默认安装后，Jenkins是用jenkins用户来运行的       
       
###系统运行配置

####前置安装
1. 安装[Maven](http://maven.apache.org/download.cgi)
2. 安装[Git](https://code.google.com/p/git-osx-installer/)
3. 安装Java1.7

####安装Jenkins插件

1. 安装Git插件
   Jenkins启动完成后，通过浏览器打开，默认是不用认证的。俺后点击“系统管理”->“管理插件”->“可选插件”  
   然后在“Filter:”里输入“git”，然后选择安装“Jenkins GIT client plugin”和“Jenkins GIT plugin”
   
2. 安装Junit插件
   同理搜索出Junit相关插件，然后安装“JUnit Attachments Plugin”和“multi-module-tests-publisher” 
   
3. 安装performance插件
   同理搜索出performance相关插件，然后安装“Performance plugin”  
   
4. 安装代码覆盖率报告检查插件
   同理搜索出cover相关插件，然后安装“Jenkins Cobertura Plugin”   
   
5. 安装代码检查报告插件
   同理搜索出“pmd”，然后安装“PMD Plug-in”     
   同理搜索出“checkstyple”，然后安装“Checkstyle Plug-in” 
   
记得重启Jenkins激活插件     

小提示，插件中有Github的插件，但是个人觉得不是太好用，直接用Git插件就可以了

####系统配置
Jenkins已经做了不少汉化的工作，界面还算友好
然后点击“系统管理”->“系统设置”，然后配置Git，Maven，Java以及Mail Notification就可以。（很奇怪，用QQ的STMP不行，用Google的STMP就可以。不过记得到Gmail去激活SMTP/POP3）

      
####启动安全
最好还是激活安全配置
点击“系统管理”->“Configure Global Security”  
然后选择勾上“启用安全”，“Unix用户/组数据库”，“登录用户可以做任何事”  
这样只有Mac用户可以登录并使用了

###配置第一个自动测试例子
系统配置好，我们就可以来配置一个测试例子了

####前置条件
为了简单方便，Java工程要变成Maven工程，如果不是熟悉Maven的，可以先看看[Maven实战](http://www.amazon.cn/Maven实战-许晓斌/dp/B004CLZ7BA/ref=sr_1_1?ie=UTF8&qid=1391746077&sr=8-1&keywords=maven)

如果需要cobertura覆盖率报告，  
必须要在maven工程中的build plugins中使用cobertura-maven-plugin，配置例子如下：
    
	  <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.6</version>
        <configuration>
          <instrumentation>
            <excludes>
              <exclude>*/*Test.class</exclude>
            </excludes>
          </instrumentation>
          <format>xml</format>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>clean</goal>
            </goals>
          </execution>
        </executions>
      </plugin>   

如果需要检查代码的写法则需要在maven中引入maven-jxr-plugin，maven-pmd-plugin，maven-checkstyle-plugin，具体配置例子如下：

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
        <version>2.4</version>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.0.1</version>
        <configuration>
          <format>xml</format>
        </configuration>
      </plugin>   

		<plugin>
		  <groupId>org.apache.maven.plugins</groupId>
		  <artifactId>maven-checkstyle-plugin</artifactId>
		  <version>2.11</version>
	      <configuration>
	        <configLocation>config/checkstyle.xml</configLocation>
	        <encoding>UTF-8</encoding>
	        <outputFileFormat>xml</outputFileFormat>
	        <consoleOutput>true</consoleOutput>
	        <failsOnError>false</failsOnError>
	        <linkXRef>true</linkXRef>
	      </configuration>
		</plugin>
      
####新建一个自动测试任务
点击“新建”选择“构建一个maven2/3项目”      

1. 配置项目基本信息，描述等   
2. 配置Gihub的项目信息  
   - 指定github repository地址如我的：https://github.com/duffqiu/DesignPatternDemo.git
   - 指定认证用户，我用ssh的方式连接
   - 指定代码分支，如我用“*/develop”来测试develop分支
3. 构建触发器，选择“Build whenever a SNAPSHOT dependency is built”，和“Poll SCM”（内容指定为“H/5 * * * *”，即5分钟查询一次github）
4. 指定Maven Goals，我用“clean cobertura:cobertura”，这样可以生成覆盖率的报告
5. 增加“构建后操作”
   - 配置覆盖率报告位置，默认为：“**/target/site/cobertura/coverage.xml” 
   - 配置junit性能测试报告位置，默认为：“\*\*/TEST-\*.xml”  
6. 使用pmd和checkstyle报告
   - 勾上“Publish Checkstyle analysis results”
   - 勾上“Publish PMD analysis results”   
   - 同时还要修改Maven Goals为：“clean cobertura:cobertura jxr:jxr checkstyle:checkstyle pmd:pmd”  

提示jxr:jxr是用来绑定报告中的代码行号   
   
然后保存就可以用“立即构建”来测试了  
可以通过“查克·诺里斯说：控制台输出”来查看运行过程的log输出   

###小插曲
我在安装完配置后，运行一个测试总是包无法找到Maven的错误，可是路径都是对的。后来发现Jenkins是用jenkins用户来运行的，而我将Maven安装在当前的用户的home路径下，造成了jenkins无法访问到。所以应该考虑将Maven安装到/usr/local下，或者copy一份到/Users/Shared/Jenkins/下（记得更改用户权限为jenkins用户）
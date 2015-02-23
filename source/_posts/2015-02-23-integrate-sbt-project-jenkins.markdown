---
layout: post
title: "集成SBT到Jenkins中"
date: 2015-02-23 14:56:24 +0800
comments: true
categories: SBT,Jenkins
---

###原由
---

之前有些过关于Jenkins上搭建maven工程[1]({%post_url yong-jenkinsda-jian-ji-yu-githubhe-junitde-zi-dong-hua-ce-shi%})，但是对于Scala的工程最好还是用SBT，那么如何在Jenkins用SBT呢？


###解决办法
---

Jenkins已经提供了SBT的插件，类似Maven的插件

具体步骤如下：

- 在Jenkins上的系统管理->管理Jenkins->管理插件中选择可选插件，然后输入sbt过滤条件
- 选择安装sbt plugin
- 重启Jenkins
- 配置sbt：系统管理->系统设置，设置"sbt lauch jars"，注意这里是指具体的jar文件，不是配置路径。如`/opt/local/share/sbt/sbt-launch.jar`  
- 新建项目（因为不像Maven有特定的选项），所以选择"构建一个自由风格的软件项目"  
- 然后在构建步骤中选择"Build using SBT"，然后配置对应的参数。这里需要注意的是JVM Flags，如果不配置很可能PerGem不够造成OutofMemoryError。但是该配置什么呢？如果不是太清楚，则可以直接拷贝sbt的启动脚本中的JVM Flags参数，如我机器的：`-XX:+CMSClassUnloadingEnabled -Xms1536m -Xmx1536m -XX:MaxPermSize=384m -XX:ReservedCodeCacheSize=192m -Dfile.encoding=UTF8`
- 如果Git repo下是涵盖了多个项目，则可以制定sub-directory path来运行特定个项目


#### Linux/Unix下跟踪或查看文件的命令

1. 输入文件到console: `cat <filename>`
2. 逐行显示文件: `less <filename>`
3. 显示文件最后的一屏: `tail <filename>`
4. 如果需要跟踪文件的变化则: `tail -f <filename>`










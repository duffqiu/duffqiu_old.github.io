---
layout: post
title: "Octopress在github上建blog的准备工作"
date: 2014-01-25 23:03:42 +0800
comments: true
categories: Octopress
---

###需要一堆的软件
我都是在Mac上安装的，windows真的没有兴趣再试多一次了

####Mac系统的准备工作
这个部分或许和安装[Octopress][]没什么关系，但是用Mac系统的话最好都装上，通常都用的上

[Octopress]: http://octopress.org "Octopress官网"

* 安装[MacPorts][]，官网有比较详细的介绍，可以下pkg安装；也可以源文件安装，但是前提是必须安装xcode的command line工具，运行一下命令安装。

       sudo xcode-select --install
      
	之前也是用pkg方式安装的，但是用
  	  
  	  sudo port -v selfupgrad
  	  
  的时候出现了错误，所以还是安装上xcode的command line工具就好了，好像是OSX Mavericks版本才有？这个问题是通过stackover上的帖子[cant-update-macports-with-mac-os-x-mavericks][1]的解答获得的。port工具可以帮助安装所需要软件以及其依赖的软件，省去了很多找软件的麻烦，另外一个和macports差不多的软件管理工具是[HomeBrew](http://brew.sh "Brew官网")

* 安装[Git](http://git-scm.com/)工具，如何使用Git还是看官方文档吧，这里就不讲了
* 安装ruby，OSX已经自带了

* 用MacPorts安装rbenv

  	  sudo port install rbenv
安装octopress需要，但是具体什么用没有细研究

* 配置zsh shell，建议用zsh shell，具体如何配置可以参考Mactalk，如果后面讲书的内容写到blog里，是否有侵权？

[MacPorts]: http://www.macports.org/index.php
[1]: http://stackoverflow.com/questions/19622337/cant-update-macports-with-mac-os-x-mavericks

####获取Octopress并安装相应的包
1. 在机器上建立一个本地的git repo，并进入这个目录
2. 获取Octopress源码
         
       git clone git://github.com/imathis/octopress.git octopress
3. 安装依赖工具
          
       cd octopress
       gem install bundler
       rbenv rehash #还是不太明白这个具体做什么用
       bundle install
       
4. 安装Octopress默认的Theme

       rake install

####配置Github
生成一个名为<your github name>.github.io的repository，注意这里<your github name>是你的github的账户名，要用.io不是.com

####初始化octopress并部署到Github
1. 关联octopress和github

       rake setup_github_pages
   根据提示输入<your github name>.github.io的repository对应的地址
2. 部署octpress到Github

       rake generate #生成静态文件
       rake deploy #部署静态文件到github
3. Push原文件到Github的source branch上，如果不做这步，github将只有生成好的文件

       git add .
       git commit -m <'your message'>
       git push origin source
      
到这一步，基本上就将Octopress基本配置好了，后面将讲讲如何写blogger和配置Octopress
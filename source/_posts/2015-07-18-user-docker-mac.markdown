---
layout: post
title: "在Mac下使用docker"
date: 2015-07-18 18:29:43 +0800
comments: true
categories: [MAC,Docker]
---

###原由
---
Docker是基于Linux Kernel的容器管理器，但是如果想在Mac上使用就必须安装Linux的虚拟机。当然如果是自己安装一个Linux到virtualbox或者vmare也是没有问题的。我自己就是这样用CoreOS的。但是如果只是想试用或学习docker，这样的方式有些重了，因为每次都要启动虚拟机，登录虚拟机再使用。为了解决这个docker有了[boot2docker](http://docs.docker.com/installation/mac/)。但是boot2docker还是需要比较繁琐的安装。所以最好的方式是直接使用[kitematic](https://kitematic.com)，kitematic还同时支持windows。但是切记不要将kitematic用在生产环境。


###解决办法
---

- 直接下载kitematic，然后得到一个kitematic.app
- 将它放到application中，直接运行就好
- 在菜单中将docker命令行安装到系统，需要管理员权限，这样就可以通过命令行使用docker了(不过命令行启动需要在kitematic点击`DOCKER CLI`)


---
layout: post
title: "CoreOS的安装"
date: 2015-03-29 11:10:44 +0800
comments: true
categories: CoreOS
---

###原由
---
想玩Docker的人肯定应该听说过[CoreOS](https://coreos.com)。它是一个紧密结合Docker为大集群服务器而设计的Linux系统。它本身不允许安装软件包。所有提供的功能和扩展都是通过Docker容器来提供。安装CoreOS可以有多种方式，网管上都有介绍。这里主要说一下两种方式，一种是单机版，一种是服务器集群。这两种方式都是基于VirtualBox的安装.


### 单机安装CoreOS
---

- 配置Virtualbox，创建一个linux的虚拟主机出来，内存512就够了
- 下载CoreOS的ISO文件。（注意：国内对于CoreOS的下载服务器做了屏蔽，也不知道是为什么）
- 设置CoreOS的ISO的文件作为虚拟光驱，然后启动CoreOS的虚拟机
- CoreOS的安装程序没有图形界面，光驱载入后只是给了个命令行的console
- 在命令行中输入`sudo coreos-install -d /dev/sda`，然后这个脚本将下载CoreOS的安装包并开始安装 (注意：同样国内服务器屏蔽了下载服务器)
- 安装成功后关闭CoreOS的虚拟机，`sudo systemctrl poweroff`
- 然后调整CoreOS的虚拟机，将启动顺序改为硬盘为先
- 启动CoreOS的虚拟机，但是需要先进入GUN GRUB设置启动脚本，因为默认情况CoreOS是用证书连接的。没有用户名和密码。所以直接启动是无法登录系统的。
- 启动CoreOS的虚拟机后用上下键选择启动default项，在最后一行的末尾加上`console=tty0 console=ttyS0 coreos.autologin=tty1 coreos.autologin=ttyS0`，然后F10保存后启动，这个时候将不需要密码进入系统了(这个改动只会一次有效，以后需要还要重新更改)
- CoreOS默认安装后有用户core，这个时候可以用`sudo passwd core`来修改密码
- 修改密码成功后，以后登录就可以用core用户和新设置的密码了
- CoreOS的虚拟机启动后默认已经启动了Docker，可以用`docker info`来查看docker的状态
- 单机版的CoreOS只是拿来练习docker用的，或者是做开发环境，不可用作生产环境



---
layout: post
title: "CoreOS安装和使用Keepalived"
date: 2015-07-05 15:43:47 +0800
comments: true
categories: [CoreOS, Keepalived]
---

###原由
---
在之前的文章[Openstack用Keealived来实现VIP]({% post_url 2015-07-05-openstack-vip %})介绍了如何使用Keepalived来实现VIP，但是如果使用的vm是CoreOS，所有的程序又必须运行在Docker中的话，又该如何办呢？


###解决办法
---

- 将Keepalived做成一个Docker的image，如果需要可以自己下载keepalived的源码编译出可执行文件。因为只是用到VIP特性，可以在编译的时候将IPVS去掉（该功能可以使用haproxy代替）。一个比较偷懒的办法是先用yum的方式安装keepalived(基于CentOS)`yum install keepalived`，然后用`rpm -e --nodeps keepalived`去掉安装的可执行文件而是用自己编译的可执行文件。不然的话就需要一个一个指定的方式来安装keepalived需要的依赖包了
- 查看CoreOS的内核是否已经启动ip_vs模块`lsmod`。似乎从681.2开始，默认都已经启动了，之前的版本没有默认启动
- 如果没有启动ip_vs，则可以通过`sudo modprobe -a ip_vs`来加载
- 使用`--priviliedged`和`--net=host`的方式来启动keepalived
- 具体的docker file可以参考我的github上的[repo](https://github.com/duffqiu/keepalived)



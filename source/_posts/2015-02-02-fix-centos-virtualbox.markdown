---
layout: post
title: "安装附属包失败后修复virtualbox上的centos"
date: 2015-02-02 16:45:25 +0800
comments: true
categories: CentOS, VirtualBox
---

###原由
---
在virtualbox安装好centos7后，共享windows下的目录以及在host机器作拷贝张贴都不行，总是提示virtualbox的附属包没有安装，所以重新进行了安装。
但是重装时发现virtualboxguess module（vboxguest）没有安装成功，提示需要安装kernel-develp包，然后再重装这个附属包。
按照提示用`sudo yum install kernel-develop`, 然后再重装这个附属包
重新启动后竟然网卡无法使用了，同时也无法mount cdrom来卸载这个virtualbox的附属包。

网卡提示的错误信息是：

```
11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain network[2920]: RTNETLINK answers: File exists

11月 05 15:30:10 localhost.localdomain systemd[1]: network.service: control process exited, code=exited status=1

11月 05 15:30:10 localhost.localdomain systemd[1]: Failed to start LSB: Bring up/down networking.

11月 05 15:30:10 localhost.localdomain systemd[1]: Unit network.service entered failed state.
```

Google了半天都没有办法，唯一能定位的地方是virtualbox修改了kernel的module造成

###解决办法
---

kernel的module一般都要通过原码的方式在本地编译后安装/加载，所以才需要kernel-develop的rpm包，难道问题在这里？
通过命令`rpm -qa|grep kernel`查看安装了的包，竟然发现有两个不同的版本的kernel-develop
那么可能的问题就出在了版本上
那就需要将不对的kernel-develop rpm包卸载掉并将对应的module删除后重新编印virtualbox的module
方法如下：

- 删除对应的编译需要的lib库： `sudo rm /lib/modules/3.10.0-123.el7.x86_64/misc/vbox*`
- 重建modules.dep和map文件: `sudo depmod -a`
- 重新加载module: `sudo modprobe module`
- reboot
- 这个时候应该cdroom和网卡都回来了
- 重新安装virtualbox的附属包

###总结

对于需要影响到kernel的安装的时候， 最好先备份一次VM，避免因为kernel被破坏后难以恢复的问题
对于需要编译kernel module的时候，先查一下已经安装的kernel的开发包有没有多个名称相同但是版本不同的包，如果有则需要删除重复的 
从过程来看，修复是很快的，但是找到问题的根源是复杂的。google也不是万能的。

特定申明：该版权是[Junjun](mailto: sean.gong@gmail.com)的 -:)



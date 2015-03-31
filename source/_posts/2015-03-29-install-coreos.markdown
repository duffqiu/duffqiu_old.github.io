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
- 注意：有可能不同的虚拟机启动的时候的cosole不是tty0或者ttyS0,则可以试一下tty1
- CoreOS默认安装后有用户core，这个时候可以用`sudo passwd core`来修改密码
- 修改密码成功后，以后登录就可以用core用户和新设置的密码了
- CoreOS的虚拟机启动后默认已经启动了Docker，可以用`docker info`来查看docker的状态
- 单机版的CoreOS只是拿来练习docker用的，或者是做开发环境，不可用作生产环境
- 在Virutalbox上的虚拟主机上的网络配置端口映射（如SSH的22端口映射到主机的2222端口，这样就可以通过连接本机的2222端口到虚拟机了）

### Vagrant安装单一虚拟机的CoreoS集群
---

- 主要参照官网的文档说明[Running CoreOS on Vagrant](https://coreos.com/docs/running-coreos/platforms/vagrant/)
- [Vagrant](https://coreos.com/docs/running-coreos/platforms/vagrant/)主要是作为开发环境配置管理工具，需要1.6.3及以上版本
- 根据Virtualbox以及Vagrant的说明安装好这些工具
- 用Git克隆对应的vagrant安装CoreOS的工程

``` sh
git clone https://github.com/coreos/coreos-vagrant.git
cd coreos-vagrant
```
- 从这个项目中的example文件copy得到一份`user-data`和`config.rb`。`user-data`是CoreOS的云配置文件，是[fleet](https://github.com/coreos/fleet)需要用到的配置内容。`config.rb`是Vagrant用到的配置选项。
- 修改`user-data`文件，配置[etcd](https://github.com/coreos/fleet)用到的这个CoreOS cluster服务发现所要的etcd的服务。是不是有些绕？etcd需要ectd?后面再写个如何建etcd群的文章给CoreOS cluster用。

   - 使用免费的公开的etcd的服务获得一个群的token: 调用`https://discovery.etcd.io/new`得到一个token值
   - 将这个token替换discovery配置项中的<token>就可。
   - 需要注意的是，每次`vagrant destroy`后要重新更新这个token值
   - 每次更新这个文件后，需要用`vagrant reload --provision`的来更新VM

- 修改`config.rb`配置文件

   - 设置群中服务器个的个数 `$num_instances=3`
   - 设置VM的版本，如使用最新的稳定版 `$update_channel='stable'`。如果需要指定版本，则可以修改`Vagrantfile`中的config.vm.box_version配置

- 用启动CoreOS cluster

   - `vagrant up` 启动，如果之前没有box，则会自动下载对应的box。注意如果有代理，则需要在环境变量中设置http_proxy以及https_proxy(windows下也要) 
   - 如果只想启动一台server则可以：`vagrant up <name>`，默认<name>可以是core-01, core-02, core-03...
   - 检查启动状态：`vagrant status`
   - 连接进server: `vagrant ssh <name>`，这样将通过证书来认证默认用户core。如果使用Putty这样的ssh client则可以参考[用ssh登录vagrant创建的vm](https://github.com/Varying-Vagrant-Vagrants/VVV/wiki/Connect-to-Your-Vagrant-Virtual-Machine-with-PuTTY)
   - 如果想共享主机目录到CoreOS则可以去掉`Vagrantfile`中关于`config.vm.synced_folder`的注释符

- 验证CoreOS是否成功建立集群

   - 登录其中一台CoreOS
   - 使用命令：`fleetctl list-machines`看是否成功列出集群中所有的CoreOS服务器，如果有错误，一般是配置`user-data`文件的discovery不正确


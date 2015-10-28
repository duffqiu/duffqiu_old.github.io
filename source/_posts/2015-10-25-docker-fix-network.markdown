---
layout: post
title: "配置Docker的容器网络与主机网络相同"
date: 2015-10-25 19:31:58 +0800
comments: true
categories: Docker
---

###原由
---
原生的Docker安装后，容器使用的网络是一个通过Bridge方式的NAT内部网络，但是多个主机中的容器是无法通信的。如果需要将多个主机中的容器构成同一个网络，则需要另外独立于Docker来预先配置网络


###解决办法
---

目前可行的方式有多种，有用OVS的Overlay Network的等SDN方式。但是鉴于SDN方式依然不是很成熟，所以还是期望将容器的网络构建于与主机相同的网络或使用独立网卡在不同的网络中。这种方式下，从网络的角度，容器就是一台独立的机器。

具体方式如下(以CentOS 7为主机系统):

- 安装bridge工具：`yum install bridge-utilis`
- 安装docker-selinux: `yum install docker-selinux`，尽管是1.7.1版本，但是可以用在1.8.1的引擎上。不过因为linux权限控制原因，这个只能使用devicemapper的方式，对于overlay方式的存储，必须将selinux关闭。可以通过`sestatus查看selinux状态`
- 先卸载原有的Docker，目前CentOS yum repo的版本为1.7.1，但是该版本的网络功能还是有问题，具体可以参见Github上Docker的Release Note。
- 参照Docker官方的方式配置Docker yum repo[docker centos install guide](https://docs.docker.com/installation/centos/), 然后`yum install docker-engine` (目前需要1.8.3版本)
- 如果需要其它用户使用docker而非用root，则需要创建docker用户和用户组，然后将其它用户加入到docker用户组中。并且将`/var/lib/docker`目录的拥有者改为`docker.docker`
- 创建一个linux bridge，为了方便，将名字也命名为docker0，这样就不用给docker engine增加-b启动参数. 
   - `/etc/sysconfig/network-scripts`，目录下创建一个文件ifcfg-docker0，然后增加一下内容
   
```
DEVICE=docker0
TYPE=bridge
BOOTPROTO=static
IPADDR=192.168.1.26
NETMASK=255.255.255.0
GATEWAY=192.168.1.1
DNS1=144.114.114.114
ONBOOT=yes
DELAY=0
```   

- 修改对应的网卡配置， `/etc/sysconfig/network-scripts`，目录下ifcfg-enp0s3（名字可能有所不同），将自身的IP对应的设置注释掉，同时增加一个行：`BRIDGE=docker0`
- 重启机器，然后通过`ip a`可以参看到多了个虚拟网卡docker0，并且ip被配置在这个虚拟网卡上，原来的网卡如enp0s3已经没有了ip地址，并且通过`brctl show`可以看到docker0被指向了enp0s3的接口
- 修改docker引擎启动参数，在文件`/usr/lib/systemd/system/docker.service`增加`--fixed-cidr=192.168.1.96/27 --default-gateway=192.168.1.1` ，然后重启引擎

- 这里有个坑：如果enp0s3启动比docker0慢的话，则docker0启动失败，变通的办法是在`/etc/rc.local`文件中增加一行`ifup docker0` （似乎是用virtualbox会这样），并确保改文件是可执行的，通过安装NetworkManager可以解决这个问题 `yum insall NetworkManager`

- 另外一个坑：如果是MAC OSX下使用Virtualbox起的主机的话，则容器无法联通到外面，不知道是MAC限制的原因，还是说我的wifi路由器无法支持的原因。在多次试验后终于找到了问题，是我使用VM的bridge方式连通外部的时候是通过WIFI的网卡，这个会照成无法连接的问题，主要体现是容器发出的ARP到了Mac后没有响应回来。使用了有线网卡就OK了。不过要记住在VM的bridge属性promiscuous上选择ALL方式
- Docker无法启动报devicemapper的动态链接包没有装载的问题，这需要更新devicemapper库 `yum install device-mapper-libs`，不过最好的方式是装docker钱先更新一下系统 `yum update`

注意：如果容器也想使用host主机网络上的DHCP，则需要借助pipework的方式（或者启动容器时--net=none）来设置网络，因为docker会自动使用它用到的bridge来分配ip，而不是从外部dhcp分配ip。但是pipework对centos支持不好，使用dhcp后，docker0的bridge没有起来，需要仿照它源码的内容在容器里面操作。（容器必须具备网络权限）





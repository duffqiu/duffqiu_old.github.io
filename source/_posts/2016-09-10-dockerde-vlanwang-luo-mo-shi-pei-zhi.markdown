---
layout: post
title: "Docker的VLAN网络模式配置"
date: 2016-09-10 11:08:36 +0800
comments: true
categories: Docker
---

###Docker网络模式选择
---

目前已有不少文章介绍了Docker的网络模型，但是在实际应用中还是有不少坑和需要注意的点
在Docker应用到生产环境的时候，网络模型的选择主要有以下几种

- 原生Bridge NAT模式
- Linux Bridge VLAN模式
- 利用第三方的网络方案


###原生的Bridge NAT模式
---

这是Docker原生的网络模式，每台主机的容器都在一个独立的子网中，外部访问必须通过主机端口映射的方式。同时不同主机间的容器间访问也必须通过这种主机端口映射的方式。换句话说，一台主机上的容器其实是不知道另外一台主机的容器的。这种方式是否可以用于生产，一开始我是忧郁的，同时之前有写文章写NAT的性能损耗比较大，在没有资源做完整测试的情况下，我们最初的方案就没敢用这个网络方案。但是最近测试自己测试的结果看，NAT的性能是可以接受的（QPS和Latency和VLAN的模式都比较接近），只要有合适的方案将不同主机的容器联通就可。如用Mesos+Marathon+Bamboo+HAProxy的方式就可以的。


###Linux Bridge VLAN模式
---

这是我考虑Docker网络模型一开始就想决定好的，主要有几方面的原因：

- NAT的方式一开始不敢用
- 其它第三饭的工具还不太成熟
- 一开始容器的数量不可能太多（因为VLAN的模式受限于VLAN的整体数量，只能4096个容器），如果按照一个主机10-16个容器算，可以支持到256台主机，这个还是可以接受的
- 每个主机需要一个独立IP，并且可以互联互通
- 运维管理要简单，毕竟我们的运维体系还是物理机体系的
- 可以做到主机网络和VLAN网络的隔离

我们用的主机是CentosOS 7.X系列， 主机的网络配置如下：两个1G的网卡，通过Bond的方式绑在一起，然后配置主机一个虚拟网卡在VALN 1上，容器的Docker0的Bridge在另外一个VLAN 1上

网卡配置:
这里需要注意的是必须安装了bridge-utils, NetworkManager包

步骤
1. 配置两个网卡，不要配置IP/GATEWAY等网络参数，增加MASTER=bond0以及SLAVE=yes
完整例子如：

```
TYPE=Ethernet
BOOTPROTO=none
DEFROUTE=yes
PEERDNS=yes
PEERROUTES=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=no
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_PEERDNS=yes
IPV6_PEERROUTES=yes
IPV6_FAILURE_FATAL=no
NAME=enp2s0f0
UUID=7f6fa8e9-0177-46a8-b8ea-55c2187bea11
DEVICE=enp2s0f0
ONBOOT=yes
MASTER=bond0
SLAVE=yes
```

2. 增加bond0网络配置/etc/sysconfig/network-scripts/ifcfg-bond0，并根据使用情况选择 mode值，内容如下。因为要在bond0上再配置VLAN所以没有配置IP等相关参数。如果配置了IP等参数则可以认为bond0就是一个普通网卡了。

```
DEVICE=bond0
NAME=bond0
TYPE=Bond
BONDING_MASTER=yes
ONBOOT=yes
BOOTPROTO=none
BONDING_OPTS="miimon=100 mode=0"
```

可以通过`cat /proc/net/bonding/bond0`看配置的情况

3. 在bond0上配置vlan，这里的vlan号为136，所以文件名为/etc/sysconfig/network-scripts/ifcfg-bond0.136，内容为：

```
VLAN=yes
TYPE=Ethernet
DEVICE=bond0.136
NAME=bond0.136
PHYSDEV=bond0
ONBOOT=yes
BOOTPROTO=static
BRIDGE=docker0
```
 因为后续需要将docker的bridge(docker0)挂在这个vlan上，所以TYPE必须是Ethernet，而且多了项配置BRIDGE=docker0

4. 配置docker的bridge( /etc/sysconfig/network-scripts/ifcfg-docker0)，名字也可以用别的，为了减少docker engine需要增加-b启动参数，所以用了默认的名字。同时没有给docker0配置IP，因为有另外一个vlan的ip来管理主机

```
TYPE=bridge
VLAN=yes
DEVICE=docker0
SLAVE=bond0.136
NAME=docker0
ONBOOT=yes
BOOTPROTO=none
```

5. Docker的配置

```
按机器用途类型，修改 Docker 启动文件 /usr/lib/systemd/system/docker.service，内容如下：
      在 ExecStart=/usr/bin/docker daemon -H fd:// 这行后面加上：
      --fixed-cidr=172.20.56.16/28 --default-gateway=172.20.56.1 --registry-mirror=http://registry.xxxx.com:5000 --insecure-registry=docker.xxx.com:5000 --storage-driver=overlay --ip-forward=false --iptables=false --log-driver=journald
      （注释："--fixed-cidr="后面填写的是该机器容器IP子网段；"--default-gateway="后面填写的是容器IP子网网关；"--registry-mirror="后面填写的是生产环境的docker registry域名。）
```

##### 这里有几个坑：

- 对于Docker的存储，不要使用默认的方式，而是要使用overlay，同时是配合CentOS 7.X
- 因为开始安装机器的时候网络配置不是Bond的模式，需要配置好bond后需要清理下原有的配置
   
   - 修改 /etc/sysctl.conf 文件，把 "net.ipv4.ip_forward" 的值修改为1
   - 不需要在/etc/sysconfig/network文件上配置gateway
   - 创建文件 /etc/modules-load.d/bonding.conf，内容如下：bonding

- 默认情况，安装好docker后，并没有docker用户组和docker用户，必须使用root之行，如果不用root则需要:

   - 创建docker用户组：sudo groupadd docker
   - 把当前用户加入docker用户组，例如当前使用的是apps用户：sudo usermod -aG docker apps
   - 创建docker用户并加入docker用户组：sudo useradd docker -g docker
   - 修改“/var/lib/docker”目录及其子目录的owner和group：sudo chown -R docker:docker 
   - 退出并重新登录，刷新当前用户的权限。
   
 - 最大的坑是我们在某些主机上安装一些平台服务如Zookeeper，如果容器想访问这些配置为容器的主机上的服务是访问不通，，这个弄好了好久都没有办法，找网络的同事，从交换机配置等来看都没有问题，然后在老罗的指导下做以下尝试：
 
    － 在主机上启动tcpdump，然后在主机上抓取来自于容器的ping包，可以发现主机收到了容器发来的ICMP包，但是容器没有收到任何响应，ping总是超时，难道是主机抛弃了ICMP包？
    -  打开火星文检测看看是否有包进来: `sudo sysctl net.ipv4.conf.bond0/51.log_martians=1` (bond0/51为对应bond0.51的网卡)可以看到有包进来说明有来源不明的数据包。
    - 从以上两点可以想到网络上我们配置了两个不同的网卡，不同的网断，原理上他们应该是各自隔离的，及访问网段1的数据要从网段1的网卡进来，如果从网段2进来，Linux认为是非法包
    - 据其原因，是因为Linux的RP(Reverse Path)过滤的问题，在这种情况下，需要将这台主机的RP关闭就可
    
```
sudo sysctl net.ipv4.conf.bond0/51.rp_filter=0
sudo sysctl net.ipv4.conf.bond0/52.rp_filter=0
sudo sysctl net.ipv4.conf.all.rp_filter=0
sudo sysctl net.ipv4.conf.bond0.rp_filter=0
```    







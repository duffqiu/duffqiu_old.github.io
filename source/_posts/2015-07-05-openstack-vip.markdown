---
layout: post
title: "Openstack用Keealived来实现VIP"
date: 2015-07-05 15:34:52 +0800
comments: true
categories: [Openstack, Keepalived]
---

###原由
---
在Openstack创建VM的时候一般都是使用DHCP的方式来分配虚拟机的IP地址。这样就会出现一个问题，每次重建VM的时候，IP地址都会被改变，从而影响外部的访问。同时如果期望有两个虚拟机同时服务一个IP接入的话，则需要用到VIP的方式来实现。


###解决办法
---

VIP的实现方式通常使用VRRP（Virtual Router Redundancy Protocol）协议的方式。目前开源比较通用的软件是[Keepalived](http://keepalived.org/)，但是在Openstack的环境中，并不是在两台虚拟机安装了Keepalived就可以的。而是需要做某些配置，下面具体讲讲

- 在OpenStack的一个内部网络上创建一个Port，这个Port将得到一个内网的IP `neutron port-create --name <port name> <internal network name>` 。如果不知道openstack上有哪些网络可用，则可以用`neutron net-list`来查看
- 创建一个外网可访问的floating ip。`neutron floatingip-create <external network name>`
- 将这个floating ip关联到这个新建的port上。 `neutron floatingip-associate <floating ip uuid> <port uuid>`。这样就可以通过外部网络访问到这个port口了。
- 启动两台VM，然后各自配置keepalived，并且用这个内网的ip作为VIP给keepalvied使用。配置好后，似乎都没有问题了。但是始终无法访问，原因是openstack对于使用VIP有安全限制，必须认为的将这个VIP与VM关联才可以访问
- 配置VM的allowed-address-pair。首先通过`nova list |grep <vm identity>`来找到自己创建的VM，然后通过`neutron port-list|grep <vm ip>`来找到VM port对应的uuid。通过`neutron port-show <vm port uuid>`来查看是否配置了allowed-address-pair。如果没有则需要更新port来支持allowed-address-pair。命令是`neutron port-update <vm port uuid> --allowed-address-pairs type=dict list=true ip_address=<VIP>`

###如何调试

- 如果一切都配置好了，但是就是无法通，则可以在VM或者是Openstack对应的Computer Node上用`tcpdump -i <net card name> icmp`的方式开跟踪是否消息包是通的
- `ip neigh |grep <vip>` 来查看<vip>对应的mac地址。然后在作为master的keepalived的机器上看看是否是这个mac地址




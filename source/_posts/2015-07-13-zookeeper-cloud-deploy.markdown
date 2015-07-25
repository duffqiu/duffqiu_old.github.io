---
layout: post
title: "zookeeper云部署方案设计"
date: 2015-07-13 16:50:40 +0800
comments: true
categories: [Zookeeper,Cloud]
---

###原由
---
[Zookeeper](http://zookeeper.apache.org/)从Hadoop开始就被Apache多个项目使用。其作为分布式的Key-Value的配置存储，master-election，分布式锁以及service discovery等功能被广泛使用。尽管后很多后起之秀如：[Etcd](https://coreos.com/etcd/), [Consul](https://www.consul.io/)，但是因为不同框架间的依赖关系（如Mesos, Storm, Kafka等都依赖zookeeper），Zookeeper仍然是无可代替的。当然也有一种趋势是，新的一些框架／应用开始同时支持多种类似于zookeeper的框架。在选择上用哪种框架来做为配置存储等，各有各的忧虑了。而作为一个互联网的后台平台，很可能需要同时用到多种类似于zookeeper的框架。而对于如何将zookeeper部署到云上，支持scale-in, scale-out, fault tolerance, high avaiabilty等特性则很少有文章提起。本文则是通过在CoreOS上，利用docker以及CoreOS的etcd, fleet等设计如何将zookeeper部署到云平台上，并达到上面提到各种特性。


###方案设计
---

#### 总体设计思路


![zookeeper cloud deploy](/images/zookeepercloud.jpg)


#### 设计前提

- 设计上将zookeeper部署到CoreOS的主机上，并通过Docker Container的方式运行。当然这个不是必须的。也可以直接部署到linux/windows的主机上，只是管理和运维的方式略有不同，这个例子可以作为参考
- 因为最新的zookeeper3.4.6有个bug，如果设置了zoo.cfg中使用域名的方式来作为集群中的主机名，则当如果域名对应的ip被改变后，zookeeper将无法识别。 参见[issue 1506](https://issues.apache.org/jira/browse/ZOOKEEPER-1506)，所以我下载了源码自己编译了一个版本
- 据说zookeeper在后续的版本会加入自身的service discovery功能，则云化部署需要重新调整
- 一个zookeeper的quorum中主机数n的容错率为ceil(n/2)，即如果机器为3台则必须有2台存活；如果为5台则必须有3台存活。

- zookeeper的集群是在配置的时候通过zoo.cfg的配置文件中的主机列表决定一个quorum，如以下配置。利用CoreOS的fleet做到在某个主机意外down掉后，fleet会在另外一个可用的主机上重新启动服务，所以这里不能使用固定ip，而是使用了域名。

```
server.1=zookeeper-1:2888:3888
server.2=zookeeper-2:2888:3888
server.3=zookeeper-3:2888:3888
```

- 因为要使用到域名，不得不使用自己的域名服务器，我使用了skydns2作为我的域名解析，它是利用etcd作为配置数据存储。（因为用了CoreOS，etcd自然就已经在了，不过在etcd切换到2.0版本的时候发生了写问题，不过那是后话了）。具体可以参见我的github上如何打包配置skeydns: [duffqiu/skydns](https://github.com/duffqiu/skydns2)
- 因为在zookeeper集群配置中需要特定指定集群的数量和明确其ip或域名，这将带来以下问题

   - 如何能动态的扩展zookeeper来支持更多的客户端，同时又不用去重启哪些已经运行的zookeeper，避免因为zookeeper重启而造成的应用的重联
   - 如何避免因为扩充了zookeeper的主机数量而造成zookeeper自身的master的选举的效率的问题
   - 如何到达在不需要的时候可以动态的减少zookeeper的主机数而不造成影响
   
   
-  根据以上问题分析，我们需要用到zookeeper的observer特性  


#### 思路要点

- 在一个数据中心，配置可靠的zookeeper集群核心，主机数量为3/5/7个，具体看使用情况定。有文章说不要将核心的群集中的主机分布到不同的数据中心，因为多数据中心的网络延迟和不可靠性将极大影响zookeeper集群的可用性。如本图中的core1, core2, core3。这和核心的主机配置都是通过域名的方式，以fleet的服务方式部署，当一个主机意外down后，也能通过fleet来恢复，从而达到高可用性。因为是固定的配置，这个核心是不会scale-in, scale-out的。具体可以参考[duffqiu/zk-fleet](https://github.com/duffqiu/zk-fleet)
- 利用zookeeper的observer特性，来作为应用的接入的边缘节点，该类observer主机不参与zookeeper的master选举，而不会造成选举性能问题。因为核心的zookeeper集群可以不用配置这些边缘节点，所以这些边缘节点的scale-out, scale-in不会影响到核心集群。边缘节点的配置可以参考[duffqiu/zk-observer](https://github.com/duffqiu/zk-observer)
- 为了屏蔽zookeeper的伸缩性对于应用的影响，则对于一组边缘节点通用使用一个反向代理作为起接入点，如zk-1会接入到zk-observer-1-1...zk-observer-1-n。这个配置我还没做，后续补上


#### Tips

zookeeper的默认配置对于日志文件没有限定，这样会造成磁盘的无尽消耗，需要将配置增加如下

```
autopurge.snapRetainCount=10
autopurge.purgeInterval=1
```
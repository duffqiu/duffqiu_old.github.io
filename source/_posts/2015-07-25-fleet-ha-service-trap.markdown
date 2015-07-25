---
layout: post
title: "fleet部署高可用性服务的坑"
date: 2015-07-25 17:39:16 +0800
comments: true
categories: [CoreOS, Fleet]
---

###原由
---
在一个云集群环境中，如何部署服务使其达到高可用性是运维中重要的事情。如果选用CoreOS和Docker作为基础，那么Fleet将是一个很好的服务调度工具。不过改调度工具是比较适用于低层的服务，如果想要灵活的更小粒度的调度应用服务，则需要参考Apache Mesos或者是Google的Kubernetes。至于用Mesos或者是Kubernetes，我后面试完后再分享。回到Fleet，我使用它主要是因为需要集群中重要部署某些特定的服务给应用服务使用，如zookeeper。所以参见fleet的文档“[如使用fleet何部署容器](https://coreos.com/fleet/docs/latest/launching-containers-fleet.html)”，但是对于高可用性章节，有些注意点文档倒是没有提，给使用过程造成了一定的麻烦。这个问题也在issue列表中[#969](https://github.com/coreos/fleet/issues/969)，在本文时改问题依然没有修复。不过如何恢复错误倒是有办法的


###解决办法
---

问题的来源是这样的，如果需要部署高可用性服务，那么systemd服务的命名方式是xxxx@.service。注意，文件一定要以`.service`最为后缀命名。然后通过fleet命令来部署它，本例子是部署3个服务实例

```
fleetctl submit xxxx@.service
fleetctl load xxxx@{1..3}.service
fleetctl start xxxx@{1..3}.service
```

但是在敲键盘的时候，输入`load`指令时讲要运行的服务的实例的个数给漏了，写成和 submit相同的形式，因为我是调出历史命令改的

```
fleetctl load xxxx@.service
```

这个时候fleet将出问题了，fleetctl会一直挂在哪里不动了。通过`ctl+c`取消后，使用正确的命令也不会去部署服务了（某台错误的机器）

通过查看fleet的服务日志`journalctl -u fleet`可以看到其一直在输出错误：

```
ul 25 09:21:59 core-02 fleetd[550]: ERROR reconcile.go:79: Unable to determine agent's current state: failed fetching unit states from UnitManager: Unit name xxxxr@.service is not valid.
Jul 25 09:21:59 core-02 fleetd[550]: ERROR generator.go:65: Failed fetching current unit states: Unit name xxxx@.service is not valid.
Jul 25 09:22:00 core-02 fleetd[550]: ERROR generator.go:65: Failed fetching current unit states: Unit name xxxx@.service is not valid.
Jul 25 09:22:01 core-02 fleetd[550]: ERROR generator.go:65: Failed fetching current unit states: Unit name xxxx@.service is not valid.
Jul 25 09:22:02 core-02 fleetd[550]: ERROR generator.go:65: Failed fetching current unit states: Unit name zookeeper@.service is not valid.
Jul 25 09:22:03 core-02 fleetd[550]: ERROR generator.go:65: Failed fetching current unit states: Unit name xxxx@.service is not valid.
Jul 25 09:22:04 core-02 fleetd[550]: ERROR reconcile.go:79: Unable to determine agent's current state: failed fetching unit states from UnitManager: Unit name xxxx@.service is not valid
```

解决步骤如下

- 找到出问题的机器。因为fleet是随机部署服务的，所以需要确定是哪台机才可以定位问题。通过`fleetctl list-unit-files`看看`xxxx@.service`被load到哪台机上
- 通过ssh登陆到错误的机器，查找xxxx@.service文件所在的地方，然后将其删除。文件主要放在两个地方：`/run/fleet/units/`和`/run/systemd/system`。需要sudo权限
- 运行`systemctl daemon-reload`刷新systemd的服务
- 重新启动fleet: `systemctl restart fleet`
- 需要注意的是如果重启fleet将会造成所以依赖fleet管理的服务都会被重新启动。如果服务是通过docker容器运行的，而服务描述文件中有写了`docker pull`将有可能造成服务恢复需要很长时间





---
layout: post
title: "Coreos fleet使用陷阱几例"
date: 2015-07-05 18:57:28 +0800
comments: true
categories: [CoreOS,Fleet]
---

###原由
---
CoreOS带了一个基于Systemd的集群服务管理工具，可以便于管理在多个CoreOS实例中部署和管理服务，虽然它的力度比较粗一点，无法针对资源的情况来管理，不像Mesos，但是因为其基于Systemd，其服务依赖等做的比较好，比较适用于基础服务的部署。
但是在使用过程中还是遇到了不少坑和麻烦，现在一一列举出来


###Fleet的坑
---

- Fleet的Service Unit的文件的写法。在Unit文件中使用环境变量只能用在`[Service]`域中，其它域是无法使用的。参见[issue 1246](https://github.com/coreos/fleet/issues/1246)
- Fleet的Service Unit的文件的[X-Fleet]域的Conflicts不要像systemd那样一行写多个服务的名字。如果有多个冲突项，则需要写多行的Conflicts。参见[issue 1245](https://github.com/coreos/fleet/issues/1245)
- Fleetctl要能够连接集群中的其它机器，则需要使用ssh-agent

```
eval `ssh-agent`
ssh-add <ssh的私钥，如果是用openstack，则是用openstack生成的私钥>
```






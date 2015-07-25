---
layout: post
title: "CoreOS Etcd2"
date: 2015-07-05 16:13:19 +0800
comments: true
categories: [CoreOS, ETCD2]
---

###原由
---
从CoreOS 682.1开始，ETCD2已经被默认安装了，通过Clould Init的方式在Openstack上启动CoreOS时发生了一些错误，总结如下


###解决办法
---

- ETCD2总是启动不成功，通过`journalctl _EXE=/usr/bin/coreos-cloudinit`查看发现其提示没有发现本地的文件有相关的member信息。究其原因是我在cloudinit中设置了etcd2的文件路径，而改文件目录默认是root的权限，但是etcd2运行的时候是以etcd用户运行的，所以造成无法访问改文件目录而启动失败
- 解决的办法是通过在cloudinit中写一个oneshot的systemd的服务，该服务将这个目录的权限和所有权更改为etcd，可参照我github上的[cloudinit的例子](https://raw.githubusercontent.com/duffqiu/coreos-openstack/master/etcd2/cloud-config-front1.yaml)
- 另外需要注意的是，etcd2通过discovery指定的cluster的大小。如果没有满足这个cluster的大小的节点联入则etcd2的集群是无法启动的
- etcd2的cluster，在接入节点满足cluster指定的大小后，后续的接入节点将自动降格为proxy模式
- etcdctl可以用`--debug`开查看发出的CURL命令是什么


#### Docker tips

- 从Docker 1.6开始，docker可以支持查看container的运行时的CPU和Memery的消耗，以下命令为列出本机所有containter的运行状况

{% raw  %}
docker stats $(docker inspect --format="{{ .Name }}" $(docker ps -q))
{% endraw %}

- 从Docker 1.6开始，docker支持直接使用命令进入到container中进行交互，而不用向以前那么麻烦的使用nsenter的方式了，具体用法是

``` sh
docker exec -it <containter name> <command in container>
```

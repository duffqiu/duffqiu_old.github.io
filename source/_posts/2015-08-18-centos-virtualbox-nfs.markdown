---
layout: post
title: "Linux下的Virtualbox使用NFS"
date: 2015-08-18 10:08:43 +0800
comments: true
categories: [Virtualbox, Centos, NFS]
---

###原由
---
在使用vagrant安装配置CoreOS的时候，无法共享主机目录到CoreOS中


###解决办法
---
需要先在主机中安装相应的NFS服务，具体方法：

- 安装nfs相应的服务

```
sudo yum install nfs-utils rpcbind
```

- 启动相应的服务并设置linux启动时同时启动

```
sudo systemctl start nfs-server
sudo systemctl start rpcbind
sudo systemctl enable nfs-server
```

- 似乎到这里就可以了，但是在启动CoreOS的时候还是不能用，后来发现时linux firewall的问题，需要将nfs的服务在firewall中打开（如果只是打开端口好像不行）

```
sudo firewall-cmd --permanent --zone=public --add-service=nfs 
sudo firewall-cmd –reload 
```
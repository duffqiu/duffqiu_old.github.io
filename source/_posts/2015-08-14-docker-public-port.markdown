---
layout: post
title: "如何获取docker动态分配的port"
date: 2015-08-14 14:31:12 +0800
comments: true
categories: Docker
---

###原由
---
当在一个集群环境中，我们需要用到docker启动一个服务的时候，如果总是指定固定的公开端口给docker运行的服务，那么将极大的限制了服务部署的灵活性和可维护行。那么有没有办法在服务启动后去容易获得docker动态分配的端口呢？


###解决办法
---

Docker提供了port的子命令，具体使用如下：

```
docker port <container-name> |cut -d':' -f2
```


####后续问题

目前这种方式只适合外服务外做服务注册的场景。
如果服务注册是在服务程序中完成的（如上报到zookeeper等），那么目前是没有办法的。有个[issue3778](https://github.com/docker/docker/issues/3778)在跟，不知道docker什么时候提供



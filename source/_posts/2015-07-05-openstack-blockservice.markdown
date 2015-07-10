---
layout: post
title: "Openstack如何更好的使用block service作为磁盘使用"
date: 2015-07-05 17:15:59 +0800
comments: true
categories: [Openstack, Block Storage]
---

###原由
---
在使用Openstack过程中，VM总是要运行某些程序，而这些程序的数据是记录在磁盘中的。如何保证在VM被删除或者重建后这些数据依然能够存在呢？这就要用到Openstack的Block Service了。但是这里面有几个问题：

- block service创建出来的卷是是个无文件格式的盘，无法被直接读写
- 通常我们希望在给一个VM初始化时挂在一个盘时，这个盘已经包含了某些必须的文件内容 


###解决办法
---

思路是做出一个带有文件格式已经需要的文件内容／数据的block service的image，这样就不用去格式化这个卷，同时也包含所需的内容，具体方法如下：

- 创建一个本地image，指定大小。 `dd if=/dev/null of=example.img bs=1M seek=1024`。这里创建了一个1G大小的镜像文件example.img，对于不同的使用目的，这里的bs需要调优
- 格式化成ext4。 `mkfs.ext4 -F example.img`
- 创建本地的挂载目录 `mkdir <path>`
- 挂载这个image到指定的目录 `mount -t ext4 -o loop example.img <path>`
- 将需要的资料放入到这个目录中
- 卸载该image `umount <path>`
- 将image转成qcow2格式 `qemu-img convert -f raw -O qcow2 example.img example.qcow2`
- 上传到openstack

```
glance image-create --name <image name> \
--container-format bare \
--disk-format qcow2 \
--file exmaple.qcow2 \
--is-public True
```

- 创建block storage的卷

```
cinder create --image-id <image uuid> --display-name <disk name> <size x G>
```
- 创建完成就可以将其挂载到VM中使用了
- 可以在创建VM时在nova命令中生成这个卷


#### Openstack tips

- `vgdisplay`获取openstack的磁盘空间信息

#### Bash tips

- `echo $?` 获取最近命令的status code


---
layout: post
title: "CentOS下使用NTFS格式的U盘或移动硬盘"
date: 2015-08-10 09:14:06 +0800
comments: true
categories: CentOS
---

###原由
---
CentOS7没有携带NTFS的驱动，当使用NTFS的硬盘的时候就会出现无法识别的问题


###解决办法
---
安装ntfs-3g的驱动

```
wget https://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-5.noarch.rpm
sudo rpm -ivUh epel-release-7-5.noarch.rpm
sudo yum install ntfs-3g
```

注意：需要讲U盘退出后再插入才可以生效





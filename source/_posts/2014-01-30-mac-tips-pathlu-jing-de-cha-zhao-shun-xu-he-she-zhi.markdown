---
layout: post
title: "mac tips: path路径的查找顺序和设置"
date: 2014-01-30 13:14:17 +0800
comments: true
categories: [OSX, MAC]
---

###OSX系统的PATH设置

1. 系统读取path配置的顺序为  
   1). /etc/paths wenjian   
   2). /etc/paths.d/目录下的所有设置文件  
   3). /etc/profile 文件 
   4). 用户目录下的shell配置文件，如zsh的.zshrc文件
    
    
2. 获取当前path的值的方式  
   1). echo $PATH  
   2). /usr/libexec/path_helper  
   
3. 用户设置path的建议  
   1). 只针对用户自己的设置配知道shell的配置文件中，如.zshrc
   2). 针对系统的文件，可以在/etc/paths.d/路径中生成一个对应的配置文件

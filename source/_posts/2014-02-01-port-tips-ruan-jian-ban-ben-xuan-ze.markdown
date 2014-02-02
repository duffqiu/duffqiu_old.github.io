---
layout: post
title: "Port tips: 软件版本选择"
date: 2014-02-01 23:30:20 +0800
comments: true
categories: Port
---

### port查看本地已安装的文件
    
    >port installed
    
### port查看同一个软件的不同版本
    
    >port select --list python    #这里查看的已经安装了的python的版本

我的机器情况是：    

![image](/images/Snip20140201_1.png)    


显示为none指的是系统自带的版本

### 激活某个版本 

    >sudo port select --set python python2.7
    
    
**注意：** 只有那些有对应的select软件包的软件才可以设置版本    
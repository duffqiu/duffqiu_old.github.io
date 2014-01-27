---
layout: post
title: "octopress插入代码块的问题"
date: 2014-01-27 18:20:21 +0800
comments: true
categories: Octopress
---

#### 在blog中插入Octopress代码块的问题

Octopress的说明文档中有多种插入代码块的方式，但是当初入markdown代码的时候就出现问题，尝试了多种方式后无果，最后查看别人的blog的源码发现使用

``` sh   
{% raw %}   
   # 必须有空行
   #代码块  
   # 必须有空行
{% endraw %}  
```

的方式将代码块再扩起来解决的。

如果有序列(如1., 2...)，则需要缩进代码快和raw..endraw指令

出个小题，这里是怎么写出raw...endraw的?
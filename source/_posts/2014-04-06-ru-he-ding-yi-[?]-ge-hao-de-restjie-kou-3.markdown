---
layout: post
title: "如何定义一个好的REST接口3"
date: 2014-04-06 10:49:35 +0800
comments: true
categories: [REST]
---

###原由
---
在定义REST接口的时候，如果采用resource的接口形式，则如何去定义接口的URL呢？


###解决办法
---

- 首先要解决的问题是在系统中如何去定位resource，然后才考虑如何设计URL。这样你必须有一个resource tree，如我在项目的设计样式：

![resource tree](http://duffqiu.github.io/images/Rest_Resource_Tree.png "resouce tree")
   
   这里首先确定要使用的资源入口是User，然后通过userid确定唯一的资源，然后再获取该资源下的子资源Calendar和Meeting

- 有了这个resource tree，那么URL就好定义了，如获取用户id为300222的所有Calendar的URL为： `http://xxxx.com/<webcontext>/p/<component_name>/v1/user/300222/calendar`



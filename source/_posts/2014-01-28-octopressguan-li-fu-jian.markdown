---
layout: post
title: "octopress管理附件"
date: 2014-01-28 12:10:51 +0800
comments: true
categories: Octopress
---

####如何在Octopress使用附件

1. 在octopress目录中，将对应的附件放到/source/assets目录里面
2. 然后重新生成和部署

       rake gengerate
       rake deploy
3. 然后在对应的blog中，使用http的链接方式指定文件就可如
   
       [附件](/assets/<附件名>)